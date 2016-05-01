package jp.azw.kancolleague.kcapi;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jp.azw.kancolleague.util.JsonUtil;

public class ApiStart2 extends Root {
	private Map<Integer, BasicShipData> basicShipDatasMap;
	private Map<Integer, ShipGraph> shipGraphsMap;

	public Map<Integer, BasicShipData> getBasicShipDataMap () {
		return basicShipDatasMap;
	}
	
	public Map<Integer, ShipGraph> getShipGraphMap () {
		return shipGraphsMap;
	}
	

	/**
	 * Map<br />
	 * - Integer<br />
	 * - Pair<br />
	 *   - Optional<br />
	 *     - BasicShipData<br />
	 *   - Optional<br />
	 *     - ShipGraph<br />
	 * 
	 * @param ships
	 * @param graphs
	 * @return
	 */
	/**
	 * api_start2 のデータを利用して Map を作成します。 Map のキーは api_id のデータです。 ShipGraph
	 * のデータも含みます。
	 * 
	 * @param apiStart2Json
	 * @return ConcurrentHashMap
	 */
	public Map<Integer, Pair<Optional<BasicShipData>, Optional<ShipGraph>>> joinedShipGraph(){
		Map<Integer, Pair<Optional<BasicShipData>, Optional<ShipGraph>>> map =
		// 左結合
		getBasicShipDatas().parallelStream()
				.map(b -> Pair.of(b.getId(),
						Pair.of(Optional.of(b),
								getShipGraphs().parallelStream()
										.filter(graph -> b.getId() == graph.getId())
										.findAny())))
				.collect(Collectors.toConcurrentMap(Pair::getLeft, Pair::getRight));
		
		// 右結合
		// 左結合で回収できなかったもののみ
		getShipGraphs().parallelStream().forEach(g -> map.putIfAbsent(g.getId(), Pair.of(Optional.empty(), Optional.of(g))));
		
		return map;
	}

	public List<BasicShipData> getBasicShipDatas() {
		List<BasicShipData> list = new LinkedList<>();
		list.addAll(basicShipDatasMap.values());
		return list;
	}

	public List<ShipGraph> getShipGraphs() {
		List<ShipGraph> list = new LinkedList<>();
		list.addAll(shipGraphsMap.values());
		return list;
	}
	
	public static ApiStart2 instance(JsonObject apiStart2, Map<String, String[]> requestParams) {
		ApiStart2 a = new ApiStart2();
		a.init(apiStart2, requestParams);
		a.basicShipDatasMap = JsonUtil.fromJsonArray(apiStart2.get("api_data").getAsJsonObject().get("api_mst_ship"))
				.map(JsonElement::getAsJsonObject)
				.map(BasicShipData::new)
				.collect(Collectors.toMap(BasicShipData::getId, b -> b));
		a.shipGraphsMap = JsonUtil.fromJsonArray(apiStart2.get("api_data").getAsJsonObject().get("api_mst_shipgraph"))
				.map(element -> new Gson().fromJson(element, ShipGraph.class))
				.collect(Collectors.toMap(ShipGraph::getId, b -> b));
		
		return a;
	}
}
