package jp.azw.kancolleague.kcapi;


import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ShipGraph {
	@SerializedName("api_id")
	private int id; // api_id 管理用 id

	@SerializedName("api_sortno")
	private int sortNo; // api_sort_no 図鑑番号

	@SerializedName("api_filename")
	private String fileName;
	
	@SerializedName("api_version")
	private List<String> version;
	
//	public ShipGraph(JsonObject apiMstShipgraph) {
//		id = apiMstShipgraph.get("api_id").getAsInt();
//		sortNo = apiMstShipgraph.get("api_sortno").getAsInt();
//		fileName = apiMstShipgraph.get("api_filename").getAsString();
//		version = apiMstShipgraph.get("api_version").getAsString();
//	}

	public int getId() {
		return id;
	}

	public int getSortNo() {
		return sortNo;
	}

	public String getFileName() {
		return fileName;
	}

	public List<String> getVersion() {
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

	public void setVersion(List<String> version) {
		this.version = version;
	}
}
