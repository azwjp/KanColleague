package jp.azw.kancolleague.kcdata;

import java.util.HashMap;
import java.util.Map;

public class RootTest {
	@SuppressWarnings("serial")
	protected Map<String, String[]> param = new HashMap<String, String[]>(){
		{
			put("api_token", new String[] { "test" });
			put("api_verno", new String[] { "1" });
			put("api_kind", new String[] { "1" });
			put("api_id_items", new String[] { "9094,7570,8777,8968" });
			put("api_onslot", new String[] { "1" });
		}
	};
}
