package jp.azw.kancolleague.kcdata;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import jp.azw.kancolleague.kcapi.Root;

public abstract class RootTest<T extends Root> {
	protected T json;
	
	@SuppressWarnings("serial")
	protected Map<String, String[]> param = new HashMap<String, String[]>(){
		{
			put("api_token", new String[] { "test" });
			put("api_verno", new String[] { "1" });
		}
	};
	
	@Test
	public void test_Root_Param() {
		assertThat(json.getToken(), is("test"));
		assertThat(json.getVerNo(), is("1"));
	}
}
