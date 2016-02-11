package jp.azw.kancolleague;

import java.util.Map;

import com.google.gson.JsonObject;

public interface JsonEventHandler {
	default public void apiStart2 (JsonObject json){}

	default public void unknown(String uri, JsonObject json, Map<String, String[]> parameters) {}
	
	public static JsonEventHandler createEmptyHandler() {
		return new JsonEventHandler() {};
	}
}
