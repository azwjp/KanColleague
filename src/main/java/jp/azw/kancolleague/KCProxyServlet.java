package jp.azw.kancolleague;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ProxyConfiguration;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.proxy.ProxyServlet;

/**
 * 艦これとの通信をキャプチャするためのプロキシ。
 * とはいってもここの部分には艦これおよび KanColleague 特有の機能は入れない。
 * 
 * @author sayama
 * @see {@link KCDataReceiver}
 */
class KCProxyServlet extends ProxyServlet {

	private static final long serialVersionUID = -5197951173756399184L;
	public static final String RESPONSE = "response";
	private KCDataReceiver dataReceiver;

	private Optional<ProxyConfiguration> proxyConfiguration = Optional.empty();

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
	
	/* Proxy */
	/**
	 * インターネット側 (艦これ側) のプロキシを設定する。
	 * 
	 * @return
	 */
	public KCProxyServlet setProxy(Optional<ProxyConfiguration> proxyConfiguration) {
		this.proxyConfiguration = proxyConfiguration;
		return this;
	}
	
	@Override
	protected HttpClient createHttpClient() throws ServletException {
		HttpClient client = super.createHttpClient();
		proxyConfiguration.ifPresent(conf -> client.setProxyConfiguration(conf));
		return client;
	};
	
	

	public KCProxyServlet setAllAccess(boolean isAllowed) {
		allAccess = isAllowed;
		return this;
	}

	public boolean getAllAccess() {
		return allAccess;
	}

	public KCProxyServlet setDataReceiver(KCDataReceiver dataReceiver) {
		this.dataReceiver = dataReceiver;
		return this;
	}

	public static KCProxyServlet instance(KCDataReceiver dataReceiver) {
		return new KCProxyServlet().setDataReceiver(dataReceiver);
	}
}
