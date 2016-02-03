package jp.azw.kancolleague;

import org.json.JSONObject;

public interface JsonEventHandler {
	default public void apiStart2 (JSONObject json){}

	default public void unknown(String uri, JSONObject json) {}
	
	public static JsonEventHandler createEmptyHandler() {
		return new JsonEventHandler() {};
	}
}
