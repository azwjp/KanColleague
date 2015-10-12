package jp.azw.kancolleague;

import java.util.List;

import jp.azw.kancolleague.data.ApiBasic;
import jp.azw.kancolleague.data.BasicShipData;
import jp.azw.kancolleague.data.ShipGraph;

public interface DataChangedHandler {
	default public void apiBasic(ApiBasic apiBasic) {}

	default public void basicShipData(List<BasicShipData> basicShipDatas) {};
	
	default public void shipGraph(List<ShipGraph> buildList) {};
	
	public static DataChangedHandler createEmptyHandler() {
		return new DataChangedHandler() {};
	}
}
