package jp.azw.kancolleague.kcapi;

import java.util.Map;

import com.google.gson.JsonObject;

public class Root {
	private String token;
	private int verNo;
	
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
}
