package jp.azw.kancolleague.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.JsonObject;

import jp.azw.kancolleague.LoadJson;
import jp.azw.kancolleague.data.BasicShipData;
import jp.azw.kancolleague.data.ShipGraph;

@RunWith(JUnit4.class)
public class DataUtilTest {
	JsonObject apiStart2;

	@Before
	public void setup() {
		apiStart2 = LoadJson.loadJson("api_start2");
	}
	@Test
	public void test_loadbuilder() {
		Map<Integer, Pair<Optional<BasicShipData>, Optional<ShipGraph>>> map = DataUtil.joinShipGraph(BasicShipData.buildList(apiStart2), ShipGraph.buildList(apiStart2));
		map.forEach((i, pair) -> {
			pair.getLeft().ifPresent(data -> assertThat(i, is(data.getId())));
			pair.getRight().ifPresent(data -> assertThat(i, is(data.getId())));
		});
	}
}
