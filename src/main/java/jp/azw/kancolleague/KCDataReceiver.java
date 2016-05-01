package jp.azw.kancolleague;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.proxy.ProxyServlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jp.azw.kancolleague.kcapi.ApiStart2;
import jp.azw.kancolleague.kcapi.Root;
import jp.azw.kancolleague.kcapi.expedition.ExpeditionResult;
import jp.azw.kancolleague.kcapi.portaction.Charge;
import jp.azw.kancolleague.util.KCJsonType;

/**
 * この <code>KCDataReceiver</code> は Context 内で {@link JsonEventHandler} および {@link JsonEventHandler} をセットする。
 * 
 * @author sayama
 *
 */
public class KCDataReceiver {
	private static final String KC_RESOURCE_SERVER = "125.6.188.25";
	private DataChangedHandler dataHandler;
	private JsonEventHandler jsonHandler;
	private ResourceHandler resourceHandler;
	private Optional<String> currentServer = Optional.empty();
	ExecutorService ex = Executors.newWorkStealingPool();
	private boolean isParallel = false;

	private KCDataReceiver() {
	}

	public KCDataReceiver setHandler(DataChangedHandler dataHandler) {
		this.dataHandler = dataHandler != null ? dataHandler : DataChangedHandler.createEmptyHandler();
		return this;
	}

	public KCDataReceiver setHandler(JsonEventHandler jsonHandler) {
		this.jsonHandler = jsonHandler != null ? jsonHandler : JsonEventHandler.createEmptyHandler();
		return this;
	}

	public KCDataReceiver setHandler(ResourceHandler resourceHandler) {
		this.resourceHandler = resourceHandler != null ? resourceHandler : ResourceHandler.createEmptyHandler();
		return this;
	}

	
	/**
	 * <p>
	 * ここの部分では {@link KCProxyServlet#onResponseSuccess} の onResponseSuccess でやろうとする処理のうち、
	 * 艦これ及び KanColleague 特有の部分の処理を行う。
	 * 並列化しない部分の処理はここに記述するが、並列化してもよい・したい部分は {@link #parallel} に分けて記述する。
	 * </p>
	 * <p>
	 * 必要ならば、 {@link ProxyServlet} を継承した別のクラスがオーバーライドした {@link ProxyServlet#onResponseSuccess} から利用してもよい。
	 * </p>
	 * 
	 * @param request {@link ProxyServlet#onResponseSuccess} が引数として受け取ったものをそのまま
	 * @param response {@link ProxyServlet#onResponseSuccess} が引数として受け取ったものをそのまま
	 * @param proxyResponse {@link ProxyServlet#onResponseSuccess} が引数として受け取ったものをそのまま
	 */
	public void onReceive(HttpServletRequest request, HttpServletResponse response,
			Response proxyResponse) {
		if (isKcData(request, response)) {
			try(ByteArrayOutputStream outputStream = (ByteArrayOutputStream) request.getAttribute(KCProxyServlet.RESPONSE)){
				if (outputStream != null && response.getContentType() != null) {
					byte[] responseBody = outputStream.toByteArray();
					switch (response.getContentType()) {
					case "text/plain": // JSON
						parallel(() -> handleJson(request.getRequestURI(),
								responseBody,
								request.getParameterMap(),
								request.getSession().getCreationTime()));
						break;
					case "application/x-shockwave-flash": // flash
						resourceHandler.flash(request.getRequestURI(), new ByteArrayInputStream(responseBody), request.getParameterMap());
						break;
					case "audio/mpeg": // 音声
						resourceHandler.audio(request.getRequestURI(), new ByteArrayInputStream(responseBody), request.getParameterMap());
						break;
					case "image/png":
						resourceHandler.png(request.getRequestURI(), new ByteArrayInputStream(responseBody), request.getParameterMap());
						break;
					default:
						resourceHandler.other(request.getRequestURI(), new ByteArrayInputStream(responseBody), request.getParameterMap());
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void parallel(Runnable task) {
		if(isParallel) {
			ex.submit(task);
		} else {
			task.run();
		}
	}
	
	/**
	 * 並立化してもいい処理をここにまとめる。
	 * スレッドセーフでない場合は <code>isParallel</code> を <code>false</code> にする。
	 * 
	 * @param uri /kcsapi/ から始まる URI
	 * @param responseBody svdata={ から始まる JSON のデータ
	 * @param params HTTP の request で送信したパラメータ
	 * @param requestCreationTime Request の作成時刻
	 */
	protected void handleJson(String uri, byte[] responseBody, Map<String, String[]> params, long requestCreationTime) {
		KCJsonType type = KCJsonType.detect(uri);
		try {
			// 頭の svdata= を削除
			InputStream is = new ByteArrayInputStream(responseBody);
			is.mark(1);
			if (is.read() == 's'){ // svdata= を削除
				for (int c = is.read(); c != -1 && c != '='; c = is.read());
			} else {
				is.reset();
			}
			InputStreamReader isr = new InputStreamReader(is);
			JsonObject json = new Gson().fromJson(isr, JsonObject.class);
			try {
				isr.close();
				is.close();
			} catch (IOException e) {				
			}
			isr = null;
			is = null;
			
			jsonHandler.allEvent(uri, json, params);
			
			switch (type) {
			case API_START2:
				jsonHandler.apiStart2(json);
				dataHandler.apiStart2(commonHandlingAction(new ApiStart2(json, params), requestCreationTime));
				break;
			case API_REQ_HOKYU__CHARGE:
				jsonHandler.apiReqHokyu_charge(json);
				dataHandler.portactionCharge(commonHandlingAction(new Charge(json, params), requestCreationTime));
				break;
			case EXPEDITION_RESULT:
				jsonHandler.apiReqMission_result(json);
				dataHandler.expeditionResult(commonHandlingAction(new ExpeditionResult(json, params), requestCreationTime));
				break;
			case UNKNOWN: // KCJsonType.UNKNOWN
				jsonHandler.unknown(uri, json, params);
				dataHandler.unknown(uri, params);
				break;
			default:
				break;
			}
		} catch (IOException e) {
		}
	}

	private <T extends Root> T commonHandlingAction(T root, long requestCreationTime) {
		root.setTime(requestCreationTime);
		dataHandler.allEvent(root);
		return root;
	}

	public KCDataReceiver reset() {
		dataHandler = DataChangedHandler.createEmptyHandler();
		jsonHandler = JsonEventHandler.createEmptyHandler();
		resourceHandler = ResourceHandler.createEmptyHandler();
		return this;
	}

	public static KCDataReceiver instance() {
		return new KCDataReceiver().reset();
	}
	
	/**
	 * どのデータかをアドレスから特定する。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean isKcData(HttpServletRequest request, HttpServletResponse response) {
		if (request.getServerName().equals(KC_RESOURCE_SERVER)) {
			// 艦これリソースサーバだった場合
			return true;
		} else if (!currentServer.isPresent()) {
			// 自分の鎮守府サーバがまだわからない場合
			KCJsonType type = KCJsonType.detect(request.getRequestURI());
			if (type != KCJsonType.UNKNOWN) {
				currentServer = Optional.of(request.getServerName());
			}
			return true;
		} else if (request.getServerName().equals(currentServer)) {
			// 自分の鎮守府サーバがわかっている場合
			return KCJsonType.detect(request.getRequestURI()) != KCJsonType.NON_KC;
		} else {
			return false;
		}
	}

	public boolean isParallel() {
		return isParallel;
	}

	public void setParallel(boolean isParallel) {
		this.isParallel = isParallel;
	}
}
