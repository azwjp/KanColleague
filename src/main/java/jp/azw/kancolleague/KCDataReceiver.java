package jp.azw.kancolleague;

import org.json.JSONObject;

import jp.azw.kancolleague.data.BasicShipData;
import jp.azw.kancolleague.data.ShipGraph;
import jp.azw.kancolleague.util.KCJsonType;

public class KCDataReceiver {
	private DataChangedHandler dataHandler;
	private JsonEventHandler jsonHandler;

	private KCDataReceiver() {
	}

	public void setDataHandler(DataChangedHandler dataHandler) {
		this.dataHandler = dataHandler != null ? dataHandler : DataChangedHandler.createEmptyHandler();
	}

	public void setJsonHandler(JsonEventHandler jsonHandler) {
		this.jsonHandler = jsonHandler != null ? jsonHandler : JsonEventHandler.createEmptyHandler();
	}

	public void onReceive(KCJsonType type, JSONObject json) {
		switch (type) {
		case API_START2:
			jsonHandler.apiStart2(json);
			dataHandler.basicShipData(BasicShipData.buildList(json));
			dataHandler.shipGraph(ShipGraph.buildList(json));
			break;
		default: // KCJsonType.UNKNOWN
			break;
		}
	}

	public KCDataReceiver reset() {
		dataHandler = DataChangedHandler.createEmptyHandler();
		jsonHandler = JsonEventHandler.createEmptyHandler();
		return this;
	}

	public static KCDataReceiver instance() {
		return new KCDataReceiver().reset();
	}
}
