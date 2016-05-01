package jp.azw.kancolleague.kcapi;


import com.google.gson.JsonObject;

public class ShipGraph {
	private int id; // api_id 管理用 id
	private int sortNo; // api_sort_no 図鑑番号
	private String fileName;
	private String version;
	
	public ShipGraph(JsonObject apiMstShipgraph) {
		id = apiMstShipgraph.get("api_id").getAsInt();
		sortNo = apiMstShipgraph.get("api_sortno").getAsInt();
		fileName = apiMstShipgraph.get("api_filename").getAsString();
		version = apiMstShipgraph.get("api_version").getAsString();
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
}
