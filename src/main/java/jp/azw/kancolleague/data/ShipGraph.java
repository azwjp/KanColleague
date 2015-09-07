package jp.azw.kancolleague.data;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class ShipGraph {
	private int id; // api_id 管理用 id
	private int sortNo; // api_sort_no 図鑑番号
	private String fileName;
	private String version;
	
	public ShipGraph(JsonObject apiStart2, int index) {
		JsonObject apiMstShipgraph = apiStart2.getJsonObject("api_data").getJsonArray("api_mst_shipgraph").getJsonObject(index);
		id = apiMstShipgraph.getInt("api_id");
		sortNo = apiMstShipgraph.getInt("api_sortno");
		fileName = apiMstShipgraph.getString("api_filename");
		version = apiMstShipgraph.getString("api_version");
	}

	public int getId() {
		return id;
	}

	public int getSortNo() {
		return sortNo;
	}

	public String getFileName() {
		return fileName;
	}

	public String getVersion() {
		return version;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public static List<ShipGraph> buildList(JsonObject apiStart2) {
		JsonArray apiMstShipgraphs = apiStart2.getJsonObject("api_data").getJsonArray("api_mst_shipgraph");
		List<ShipGraph> list = new LinkedList<>();
		IntStream.range(0, apiMstShipgraphs.size()).parallel().forEach(i -> list.add(new ShipGraph(apiStart2, i)));
		return list;
	}
	
	public static Map<Integer, ShipGraph> buildMap(JsonObject apiStart2) {
		JsonArray apiMstShipgraphs = apiStart2.getJsonObject("api_data").getJsonArray("api_mst_shipgraph");
		Map<Integer, ShipGraph> map = new HashMap<>();
		IntStream.range(0, apiMstShipgraphs.size()).parallel().forEach(i -> {
			ShipGraph graph = new ShipGraph(apiStart2, i);
			map.put(graph.getId(), graph);
		});
		return map;
	}
}
