package jp.azw.kancolleague;

import java.util.List;
import java.util.Map;

import jp.azw.kancolleague.kcapi.ApiBasic;
import jp.azw.kancolleague.kcapi.BasicShipData;
import jp.azw.kancolleague.kcapi.ShipGraph;

public interface DataChangedHandler {
	default public void apiBasic(ApiBasic apiBasic) {}

	default public void basicShipData(List<BasicShipData> basicShipDatas) {}
	
	default public void shipGraph(List<ShipGraph> buildList) {} 
	
	default public void unknown(String uri, Map<String, String[]> parameters) {}
	
	public static DataChangedHandler createEmptyHandler() {
		return new DataChangedHandler() {};
	}
}
