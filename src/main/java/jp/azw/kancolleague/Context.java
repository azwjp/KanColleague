package jp.azw.kancolleague;

import java.util.Optional;

import org.eclipse.jetty.client.api.ProxyConfiguration;
import org.eclipse.jetty.proxy.ConnectHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Context{
	private Server server;
	private int port;
	private KCProxyServlet servlet;
	private ServerConnector connector;
	private KCDataReceiver dataReceiver = KCDataReceiver.instance();
	private Optional<ProxyConfiguration> proxyConfiguration = Optional.empty();
	private boolean allAccess = false;
	
	private Context () {
		
	}
 
	private Context create() {
		connector = new ServerConnector(server = new Server());
		connector.setPort(port);
		server.addConnector(connector);

		HandlerCollection handlers = new HandlerCollection();
		server.setHandler(handlers);

		// proxy servlet
		ServletContextHandler context = new ServletContextHandler(handlers, "/", ServletContextHandler.SESSIONS);
		context.addServlet(new ServletHolder(servlet = KCProxyServlet.instance(dataReceiver)), "/*");

		// proxy handler (CONNECT method)
		// for HTTPS
		handlers.addHandler(new ConnectHandler());

		return this;
	}

	public Context start() throws Exception {
		if (server == null) {
			create();
		}

		server.start();

		return this;
	}

	public Context stop() throws Exception {
		if (server != null) {
			server.stop();
			server.join();
			server = null;
		}

		return this;
	}
	
	/**
	 * インターネット側 (艦これ側) のプロキシを設定する。解除するときは  {@link #unsetProxy()} で。
	 * {@link start()} またち {@link restart()} するまで変更は適用されない。
	 * 
	 * @param host null か 空 ("") の場合は "localhost"
	 * @param port
	 * @return このインスタンス自身
	 */
	public Context setProxy(String host, int port) {
		if (host == null || host.isEmpty()) {
			host = "localhost";
		}
		proxyConfiguration = Optional.of(new ProxyConfiguration(host, port));
		return refleshServletSetting();
	}
	
	/**
	 * インターネット側 (艦これ側) のプロキシを解除する。
	 * {@link start()} またち {@link restart()} するまで変更は適用されない。
	 * 
	 * @return
	 */
	public Context unsetProxy() {
		proxyConfiguration = Optional.empty();
		return refleshServletSetting();
	}

	/**
	 * 受信側 (ローカル側、ブラウザ側) のポート番号をセットする。
	 * {@link start()} またち {@link restart()} するまで変更は適用されない。
	 * 
	 * @return このインスタンス自身
	 * @return
	 */
	public Context setPort(int port) {
		this.port = port;
		if (connector != null) {
			connector.setPort(port);
		}
		return this;
	}
	
	/**
	 * {@link #stop()} してから {@link start()} する。
	 * 
	 * @return このインスタンス自身
	 * @throws Exception
	 */
	public Context restart() throws Exception {
		return stop().start();
	}

	public int getPort() {
		return port;
	}

	public KCDataReceiver getDataReceiver() {
		return dataReceiver;
	}
	
	public static Context instance(int port) {
		return new Context().setPort(port).create();
	}
	
	private Context refleshServletSetting() {
		if (servlet != null) {
			servlet.setDataReceiver(dataReceiver).setProxy(proxyConfiguration).setAllAccess(allAccess);
			try {
				stop().create().start();
			} catch (Exception e) {
			}
		}
		return this;
	}
}
