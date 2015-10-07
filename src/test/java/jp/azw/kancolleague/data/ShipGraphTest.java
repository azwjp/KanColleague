package jp.azw.kancolleague.data;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import jp.azw.kancolleague.LoadJson;

public class ShipGraphTest {
	JSONObject apiStart2;
	int length;

	@Before
	public void setup() {
		apiStart2 = LoadJson.loadJson("api_start2");
		length = apiStart2.getJSONObject("api_data").getJSONArray("api_mst_shipgraph").length();
	}

	@Test
	public void test_buildList() {
		List<ShipGraph> list = ShipGraph.buildList(apiStart2);
		assertThat(list.size(), is(length));
		
		// ID の重複がないか
		list.parallelStream().forEach(ship -> {
			long count = list.parallelStream().filter(ship2 -> ship.getId() == ship2.getId()).count();
			assertThat(String.valueOf(count), count == 1);
		});
	}
	
	@Test
	public void test_buildMap() {
		Map<Integer, ShipGraph> map = ShipGraph.buildMap(apiStart2);
		assertThat(map.size(), is(length));
		map.entrySet().parallelStream().forEach(entry -> assertThat(entry.getValue().getId(), is(entry.getKey())));
	}
}
