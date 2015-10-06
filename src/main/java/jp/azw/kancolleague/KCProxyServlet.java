package jp.azw.kancolleague;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.util.Callback;
import org.json.JSONObject;
import org.json.JSONTokener;

import jp.azw.kancolleague.util.KCJsonType;

class KCProxyServlet extends ProxyServlet {

	private static final long serialVersionUID = -5197951173756399184L;
	public static final String RESPONSE = "response";
	private KCDataReceiver dataReceiver;

	private boolean allAccess = false;

	private Optional<String> currentServer = Optional.empty();

	private KCProxyServlet() {
		super();
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (allAccess || InetAddress.getByName(request.getRemoteAddr()).isLoopbackAddress()) {
			super.service(request, response);
		} else {
			// localhost ではない
			response.setStatus(HttpStatus.FORBIDDEN_403);
		}
	}

	@Override
	protected void onResponseContent(HttpServletRequest request, HttpServletResponse response, Response proxyResponse,
			byte[] buffer, int offset, int length, Callback callback) {
		ByteArrayOutputStream outputStream = (ByteArrayOutputStream) request.getAttribute(RESPONSE);
		if (outputStream == null) {
			outputStream = new ByteArrayOutputStream();
			request.setAttribute(RESPONSE, outputStream);
		}
		outputStream.write(buffer, offset, length);

		super.onResponseContent(request, response, proxyResponse, buffer, offset, length, callback);
	}

	@Override
	protected void onProxyResponseSuccess(HttpServletRequest request, HttpServletResponse response,
			Response proxyResponse) {
		// この proxy 部分は最小にして、あまり直接触れないように

		if (isKcData(request, response) != KCJsonType.UNKNOWN) {
			ByteArrayOutputStream outputStream = (ByteArrayOutputStream) request.getAttribute(RESPONSE);
			if (outputStream != null) {
				// hasGotten = true;
				byte[] responseBody = outputStream.toByteArray();

				try (InputStream is = new ByteArrayInputStream(responseBody)) {

					// 頭の svdata= を削除
					for (int c = is.read(); c != -1 && c != '='; c = is.read());

					JSONObject json = new JSONObject(new JSONTokener(is));
					dataReceiver.onReceive(KCJsonType.detect(request.getRequestURI()), json);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}

		super.onProxyResponseSuccess(request, response, proxyResponse);
	}

	public KCProxyServlet setAllAccess(boolean isAllowed) {
		allAccess = isAllowed;
		return this;
	}

	public boolean getAllAccess() {
		return allAccess;
	}

	/**
	 * どのデータかをアドレスから特定する。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	private KCJsonType isKcData(HttpServletRequest request, HttpServletResponse response) {
		/*
		 * とりあえず速度を優先したいので、KCJsonType::detect(String) の実行回数は出来るだけ少なくする。 そのため
		 * content-type とサーバで絞り込む。 最も回数が多く、プレイ中で速度が必要な「サーバ特定済み」の時点での動作速度を優先する。
		 * 後で contentType の判定は後半にもって行きたい。
		 */
		if (response.getContentType().equals("text/plain")) {
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
		return KCJsonType.UNKNOWN;
	}

	public KCDataReceiver getDataReceiver() {
		return dataReceiver;
	}

	private KCProxyServlet setDataReceiver(KCDataReceiver dataReceiver) {
		this.dataReceiver = dataReceiver;
		return this;
	}

	public static KCProxyServlet instance() {
		return new KCProxyServlet().setDataReceiver(KCDataReceiver.instance());
	}
}
