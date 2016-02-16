package jp.azw.kancolleague.kcdata;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jp.azw.kancolleague.LoadJson;
import jp.azw.kancolleague.kcapi.ApiStart2;
import jp.azw.kancolleague.kcapi.BasicShipData;

@RunWith(JUnit4.class)
public class BasicShipDataTest extends RootTest{
	JsonObject apiStart2Json;
	ApiStart2 apiStart2;
	int length;

	@Before
	public void setup() {
		apiStart2Json = LoadJson.loadJson("api_start2");
		apiStart2 = new ApiStart2(apiStart2Json, param);
		length = apiStart2Json.get("api_data").getAsJsonObject().get("api_mst_ship").getAsJsonArray().size();
	}

	@Test
	public void test_loadjson() {
		BasicShipData ams;
		JsonArray apiMstShip = apiStart2Json.get("api_data").getAsJsonObject().get("api_mst_ship").getAsJsonArray();
		for (int i = 0; i < length; i++) {
			ams = new BasicShipData(apiMstShip.get(i).getAsJsonObject());
			assertThat(ams.isFriend(), is(ams.getTimeToBuild().isPresent()));
		}
	}
}
