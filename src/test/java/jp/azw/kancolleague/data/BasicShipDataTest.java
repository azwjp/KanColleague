package jp.azw.kancolleague.data;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import jp.azw.kancolleague.LoadJson;

@RunWith(JUnit4.class)
public class BasicShipDataTest {
	JSONObject apiStart2;

	@Before
	public void setup() {
		apiStart2 = LoadJson.loadJson("api_start2");
	}

	@Test
	public void test_loadjson() {
		BasicShipData ams = new BasicShipData(apiStart2, 0);
		for (int i = 0; i < apiStart2.getJSONObject("api_data").getJSONArray("api_mst_ship").length(); i++) {
//			System.out.println(i);
			ams = new BasicShipData(apiStart2, i);
			assertThat(ams.isFriend(), is(ams.getTimeToBuild().isPresent()));
		}
	}


}
