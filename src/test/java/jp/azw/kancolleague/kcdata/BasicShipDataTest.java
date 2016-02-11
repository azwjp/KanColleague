package jp.azw.kancolleague.kcdata;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jp.azw.kancolleague.LoadJson;
import jp.azw.kancolleague.kcapi.BasicShipData;

@RunWith(JUnit4.class)
public class BasicShipDataTest {
	JsonObject apiStart2;
	int length;

	@Before
	public void setup() {
		apiStart2 = LoadJson.loadJson("api_start2");
		length = apiStart2.get("api_data").getAsJsonObject().get("api_mst_ship").getAsJsonArray().size();
	}

	@Test
	public void test_loadjson() {
		BasicShipData ams;
		JsonArray apiMstShip = apiStart2.get("api_data").getAsJsonObject().get("api_mst_ship").getAsJsonArray();
		for (int i = 0; i < length; i++) {
			ams = new BasicShipData(apiMstShip.get(i).getAsJsonObject());
			assertThat(ams.isFriend(), is(ams.getTimeToBuild().isPresent()));
		}
	}

	@Test
	public void test_buildList() {
		List<BasicShipData> list = BasicShipData.buildList(apiStart2);
		assertThat(list.size(), is(length));
		
		// ID の重複がないか
		list.parallelStream().forEach(ship -> {
			long count = list.parallelStream().filter(ship2 -> ship.getId() == ship2.getId()).count();
			assertThat(String.valueOf(count), count == 1);
		});
	}

	@Test
	public void test_buildMap() {
		Map<Integer, BasicShipData> map = BasicShipData.buildMap(apiStart2);
		assertThat(map.size(), is(length));
		map.entrySet().parallelStream().forEach(entry -> assertThat(entry.getValue().getId(), is(entry.getKey())));
	}
}
