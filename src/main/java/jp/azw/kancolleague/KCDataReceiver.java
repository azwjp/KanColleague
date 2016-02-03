package jp.azw.kancolleague;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.api.Response;
import org.json.JSONObject;
import org.json.JSONTokener;

import jp.azw.kancolleague.data.BasicShipData;
import jp.azw.kancolleague.data.ShipGraph;
import jp.azw.kancolleague.util.KCJsonType;

public class KCDataReceiver {
	private DataChangedHandler dataHandler;
	private JsonEventHandler jsonHandler;
	private Optional<String> currentServer = Optional.empty();

	private KCDataReceiver() {
	}

	public void setDataHandler(DataChangedHandler dataHandler) {
		this.dataHandler = dataHandler != null ? dataHandler : DataChangedHandler.createEmptyHandler();
	}

	public void setJsonHandler(JsonEventHandler jsonHandler) {
		this.jsonHandler = jsonHandler != null ? jsonHandler : JsonEventHandler.createEmptyHandler();
	}

	public void onReceive(HttpServletRequest request, HttpServletResponse response,
			Response proxyResponse) {
		if (isKcData(request, response) != KCJsonType.NON_KC) {
			try(ByteArrayOutputStream outputStream = (ByteArrayOutputStream) request.getAttribute(KCProxyServlet.RESPONSE)){
				if (outputStream != null) {
					// hasGotten = true;
					byte[] responseBody = outputStream.toByteArray();
					try (InputStream is = new ByteArrayInputStream(responseBody)) {

						// 頭の svdata= を削除
						for (int c = is.read(); c != -1 && c != '='; c = is.read());

						JSONObject json = new JSONObject(new JSONTokener(is));
						handleJson(request.getRequestURI(), KCJsonType.detect(request.getRequestURI()), json);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void handleJson(String uri, KCJsonType type, JSONObject json) {
		switch (type) {
		case API_START2:
			jsonHandler.apiStart2(json);
			dataHandler.basicShipData(BasicShipData.buildList(json));
			dataHandler.shipGraph(ShipGraph.buildList(json));
			break;
		case UNKNOWN: // KCJsonType.UNKNOWN
			jsonHandler.unknown(uri, json);
			dataHandler.unknown(uri);
			break;
		default:
			break;
		}
	}

	public KCDataReceiver reset() {
		dataHandler = DataChangedHandler.createEmptyHandler();
		jsonHandler = JsonEventHandler.createEmptyHandler();
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
	public KCJsonType isKcData(HttpServletRequest request, HttpServletResponse response) {
		/*
		 * とりあえず速度を優先したいので、KCJsonType::detect(String) の実行回数は出来るだけ少なくする。 そのため
		 * content-type とサーバで絞り込む。 最も回数が多く、プレイ中で速度が必要な「サーバ特定済み」の時点での動作速度を優先する。
		 * 後で contentType の判定は後半にもって行きたい。
		 */
		if (response.getContentType() != null && response.getContentType().equals("text/plain")) {
			if (!currentServer.isPresent()){
				KCJsonType type = KCJsonType.detect(request.getRequestURI());
				if (type != KCJsonType.UNKNOWN) {
					currentServer = Optional.of(request.getServerName());
				}
				return type;
			} else {
				if (request.getServerName().equals(currentServer)) {
					return KCJsonType.detect(request.getRequestURI());
				}
			}
		}
		return KCJsonType.NON_KC;
	}
}
