package jp.azw.kancolleague.data;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

public class ShipGraph {
	private int id; // api_id 管理用 id
	private int sortNo; // api_sort_no 図鑑番号
	private String fileName;
	private String version;
	
	public ShipGraph(JSONObject apiStart2, int index) {
		JSONObject apiMstShipgraph = apiStart2.getJSONObject("api_data").getJSONArray("api_mst_shipgraph").getJSONObject(index);
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
	
	public static List<ShipGraph> buildList(JSONObject apiStart2) {
		JSONArray apiMstShipgraphs = apiStart2.getJSONObject("api_data").getJSONArray("api_mst_shipgraph");
		List<ShipGraph> list = Collections.synchronizedList(new LinkedList<>());
		IntStream.range(0, apiMstShipgraphs.length()).parallel().forEach(i -> 
		{list.add(new ShipGraph(apiStart2, i));}
		);
		return list;
	}
	
	public static Map<Integer, ShipGraph> buildMap(JSONObject apiStart2) {
		JSONArray apiMstShipgraphs = apiStart2.getJSONObject("api_data").getJSONArray("api_mst_shipgraph");
		Map<Integer, ShipGraph> map = new HashMap<>();
		IntStream.range(0, apiMstShipgraphs.length()).parallel().forEach(i -> {
			ShipGraph graph = new ShipGraph(apiStart2, i);
			map.put(graph.getId(), graph);
		});
		return map;
	}
}
