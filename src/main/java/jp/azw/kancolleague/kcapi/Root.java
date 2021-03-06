package jp.azw.kancolleague.kcapi;

import java.util.Map;

import com.google.gson.JsonObject;

public class Root {
	private String token;
	private String verNo;
	private long time;
	
	protected Root() {
		
	}
	
	protected Root(JsonObject json, Map<String, String[]> requestParams) {
		init(json, requestParams);
	}
	
	protected void init(JsonObject json, Map<String, String[]> parameters) {
		token = parameters.getOrDefault("api_token", new String[]{""})[0];
		verNo = parameters.getOrDefault("api_verno", new String[]{""})[0];
	}

	public String getToken() {
		return token;
	}

	public String getVerNo() {
		return verNo;
	}

	/**
	 * このライブラリでは、このオブジェクト作成時に HTTP request の作成時刻を入れてある。
	 * 
	 * @return 時刻の数値表記。デフォルトでは HTTP request の作成時刻。
	 */
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
