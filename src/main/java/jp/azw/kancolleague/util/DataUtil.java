package jp.azw.kancolleague.util;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.tuple.Pair;

import jp.azw.kancolleague.data.BasicShipData;
import jp.azw.kancolleague.data.ShipGraph;

public class DataUtil {

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
	 * @param apiStart2
	 * @return ConcurrentHashMap
	 */
	public static Map<Integer, Pair<Optional<BasicShipData>, Optional<ShipGraph>>> joinShipGraph(List<BasicShipData> ships, List<ShipGraph> graphs){
		// concurrent に
		Map<Integer, Pair<Optional<BasicShipData>, Optional<ShipGraph>>> map = new ConcurrentHashMap<>();

		// 左結合
		ships.parallelStream().forEach(
				data -> map.put(
						data.getId(),
						Pair.of(
								Optional.of(data),
								graphs.parallelStream().filter(
										graph -> data.getId() == graph.getId()
								).findAny()
						)
				)
		);
		
		// 右結合
		// 左結合で回収できなかったもののみ
		graphs.parallelStream().filter(graph -> !map.containsKey(graph.getId())).forEach(
				graph -> map.put(
						graph.getId(),
						Pair.of(
								ships.parallelStream().filter(
										ship -> ship.getId() == graph.getId()
								).findAny(),
								Optional.of(graph)
						)
				)
		);
		
		return map;
	}
}
