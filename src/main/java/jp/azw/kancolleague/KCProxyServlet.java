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
import org.json.JSONObject;
import org.json.JSONTokener;

import jp.azw.kancolleague.util.KCJsonType;

class KCProxyServlet extends ProxyServlet {

	private static final long serialVersionUID = -5197951173756399184L;
	public static final String RESPONSE = "response";
	private KCDataReceiver dataReceiver;

	private boolean allAccess = false;

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
			byte[] buffer, int offset, int length) throws IOException {
		ByteArrayOutputStream outputStream = (ByteArrayOutputStream) request.getAttribute(RESPONSE);
		if (outputStream == null) {
			outputStream = new ByteArrayOutputStream();
			request.setAttribute(RESPONSE, outputStream);
		}
		outputStream.write(buffer, offset, length);

		super.onResponseContent(request, response, proxyResponse, buffer, offset, length);
	}

	@Override
	protected void onResponseSuccess(HttpServletRequest request, HttpServletResponse response,
			Response proxyResponse) {
		// この proxy 部分は最小にして、あまり直接触れないように
		dataReceiver.onReceive(request, response, proxyResponse);

		super.onResponseSuccess(request, response, proxyResponse);
	}

	public KCProxyServlet setAllAccess(boolean isAllowed) {
		allAccess = isAllowed;
		return this;
	}

	public boolean getAllAccess() {
		return allAccess;
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
