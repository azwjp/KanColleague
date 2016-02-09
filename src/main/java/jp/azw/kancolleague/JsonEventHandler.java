package jp.azw.kancolleague;

import com.google.gson.JsonObject;

public interface JsonEventHandler {
	default public void apiStart2 (JsonObject json){}

	default public void unknown(String uri, JsonObject json) {}
	
	public static JsonEventHandler createEmptyHandler() {
		return new JsonEventHandler() {};
	}
}
