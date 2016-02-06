package jp.azw.kancolleague;

import org.eclipse.jetty.proxy.ConnectHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Context {
	private Server server;
	private int port;
	private KCProxyServlet servlet;
	private ServerConnector connector;
	
	private Context () {
		
	}
 
	public Context create() {
		connector = new ServerConnector(server = new Server());
		connector.setPort(port);
		server.addConnector(connector);

		HandlerCollection handlers = new HandlerCollection();
		server.setHandler(handlers);

		// proxy servlet
		ServletContextHandler context = new ServletContextHandler(handlers, "/", ServletContextHandler.SESSIONS);
		servlet = KCProxyServlet.instance();
		context.addServlet(new ServletHolder(servlet), "/*");

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
		// server.join();

		return this;
	}

	public Context stop() throws Exception {
		if (server != null) {
			server.stop();
			server.join();
		}

		return this;
	}

	/**
	 * ポート番号をセットする。
	 * 
	 * @param port
	 * @return
	 */
	public Context setPort(int port) {
		// TODO ServerConnector の setPort でちゃんと変わるか確かめる
		// {@link #create()} するまで変わらないのかどうか。
		this.port = port;
		if (connector != null) {
			connector.setPort(port);
		}
		return this;
	}

	public int getPort() {
		return port;
	}

	public KCDataReceiver getDataReceiver() {
		return servlet.getDataReceiver();
	}
	
	public static Context instance(int port) {
		return new Context().setPort(port).create();
	}
}
