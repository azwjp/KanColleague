package jp.azw.kancolleague.kcapi;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jp.azw.kancolleague.util.JsonUtil;

public class ApiStart2 extends Root {
	private List<BasicShipData> basicShipDatas;
	private List<ShipGraph> shipGraphs;

	public ApiStart2(JsonObject apiStart2, Map<String, String[]> requestParams) {
		basicShipDatas = JsonUtil.fromJsonArray(apiStart2.get("api_data").getAsJsonObject().get("api_mst_ship"))
				.map(JsonElement::getAsJsonObject)
				.map(BasicShipData::new)
				.collect(Collectors.toList());
		shipGraphs = JsonUtil.fromJsonArray(apiStart2.get("api_data").getAsJsonObject().get("api_mst_shipgraph"))
				.map(JsonElement::getAsJsonObject)
				.map(ShipGraph::new)
				.collect(Collectors.toList());
	}

	public Map<Integer, BasicShipData> getBasicShipDataMap () {
		return basicShipDatas.parallelStream().collect(Collectors.toMap(BasicShipData::getId, b -> b));
	}
	
	public Map<Integer, ShipGraph> getShipGraphMap () {
		return shipGraphs.parallelStream().collect(Collectors.toMap(ShipGraph::getId, b -> b));
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
		basicShipDatas.parallelStream()
				.map(b -> Pair.of(b.getId(),
						Pair.of(Optional.of(b),
								shipGraphs.parallelStream()
										.filter(graph -> b.getId() == graph.getId())
										.findAny())))
				.collect(Collectors.toConcurrentMap(Pair::getLeft, Pair::getRight));
		
		// 右結合
		// 左結合で回収できなかったもののみ
		shipGraphs.parallelStream().forEach(g -> map.putIfAbsent(g.getId(), Pair.of(Optional.empty(), Optional.of(g))));
		
		return map;
	}

	public List<BasicShipData> getBasicShipDatas() {
		return basicShipDatas;
	}

	public List<ShipGraph> getShipGraphs() {
		return shipGraphs;
	}
}
