package jp.azw.kancolleague;

import java.util.List;

import jp.azw.kancolleague.data.ApiBasic;
import jp.azw.kancolleague.data.BasicShipData;
import jp.azw.kancolleague.data.ShipGraph;

public interface DataChangedHandler {
	public void apiBasic(ApiBasic apiBasic);

	public void basicShipData(List<BasicShipData> basicShipDatas);
	
	public void shipGraph(List<ShipGraph> buildList);

	public static class EmptyHandler implements DataChangedHandler {

		@Override
		public void apiBasic(ApiBasic apiBasic) {
		}

		@Override
		public void basicShipData(List<BasicShipData> basicShipDatas) {
		}

		@Override
		public void shipGraph(List<ShipGraph> buildList) {
		}

	}

}
