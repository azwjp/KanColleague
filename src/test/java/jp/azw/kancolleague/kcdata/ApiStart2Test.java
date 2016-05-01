package jp.azw.kancolleague.kcdata;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.JsonObject;

import jp.azw.kancolleague.LoadJson;
import jp.azw.kancolleague.kcapi.ApiStart2;
import jp.azw.kancolleague.kcapi.BasicShipData;
import jp.azw.kancolleague.kcapi.ShipGraph;

@RunWith(JUnit4.class)
public class ApiStart2Test extends RootTest<ApiStart2> {
	JsonObject apiStart2Json;
	int apiMstShipLength;
	int shipGraphLength;

	@Before
	public void setUp() throws Exception {
		apiStart2Json = LoadJson.loadJson("api_start2");
		json = ApiStart2.instance(apiStart2Json, param);
		apiMstShipLength = apiStart2Json.get("api_data").getAsJsonObject().get("api_mst_ship").getAsJsonArray().size();
		shipGraphLength = apiStart2Json.get("api_data").getAsJsonObject().get("api_mst_shipgraph").getAsJsonArray().size();
	}

	@Test
	public void test_getBasicShipDatas() {
		int length = apiStart2Json.get("api_data").getAsJsonObject().get("api_mst_ship").getAsJsonArray().size();
		
		List<BasicShipData> list = json.getBasicShipDatas();
		assertThat(list.size(), is(length));
		
		// ID の重複がないか
		list.parallelStream().forEach(ship -> {
			long count = list.parallelStream().filter(ship2 -> ship.getId() == ship2.getId()).count();
			assertThat(String.valueOf(count), count == 1);
		});
	}

	@Test
	public void test_getBasicShipDataMap() {
		Map<Integer, BasicShipData> map = json.getBasicShipDataMap();
		assertThat(map.size(), is(apiMstShipLength));
		map.entrySet().parallelStream().forEach(entry -> assertThat(entry.getValue().getId(), is(entry.getKey())));
	}


	@Test
	public void test_getShipGraphs() {
		List<ShipGraph> list = json.getShipGraphs();
		assertThat(list.size(), is(shipGraphLength));
		
		// ID の重複がないか
		list.parallelStream().forEach(ship -> {
			long count = list.parallelStream().filter(ship2 -> ship.getId() == ship2.getId()).count();
			assertThat(String.valueOf(count), count == 1);
		});
	}
	
	@Test
	public void test_getShipGraphMap() {
		Map<Integer, ShipGraph> map = json.getShipGraphMap();
		assertThat(map.size(), is(shipGraphLength));
		map.entrySet().parallelStream().forEach(entry -> assertThat(entry.getValue().getId(), is(entry.getKey())));
	}
	
	@Test
	public void test_joinedShipGraph() {
		Map<Integer, Pair<Optional<BasicShipData>, Optional<ShipGraph>>> map = json.joinedShipGraph();
		map.forEach((i, pair) -> {
			pair.getLeft().ifPresent(data -> assertThat(i, is(data.getId())));
			pair.getRight().ifPresent(data -> assertThat(i, is(data.getId())));
		});
	}
}
