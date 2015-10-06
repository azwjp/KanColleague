package jp.azw.kancolleague;

import org.json.JSONObject;

public interface JsonEventHandler {
	public void apiStart2 (JSONObject json);
	
	static public class EmptyHandler implements JsonEventHandler {

		@Override
		public void apiStart2(JSONObject json) {
		}
		
	}
}
