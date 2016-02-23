package jp.azw.kancolleague.kcapi;

import java.util.Map;

import com.google.gson.JsonObject;

public class Root {
	private String token;
	private int verNo;
	private long time;
	
	protected Root() {
		
	}
	
	protected Root(JsonObject json, Map<String, String[]> parameters) {
		token = parameters.get("api_token")[0];
		verNo = Integer.valueOf(parameters.get("api_verno")[0]);
	}

	public String getToken() {
		return token;
	}

	public int getVerNo() {
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
