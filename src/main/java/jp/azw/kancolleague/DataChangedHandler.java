package jp.azw.kancolleague;

import java.util.Map;

import jp.azw.kancolleague.kcapi.ApiStart2;
import jp.azw.kancolleague.kcapi.expedition.ExpeditionResult;
import jp.azw.kancolleague.kcapi.portaction.Charge;

public interface DataChangedHandler {
	default public void allEvent(Object object) {}
	
	/* api_start2 */
	default public void apiStart2(ApiStart2 apiStart2) {}
	
	/* others */
	default public void portactionCharge (Charge charge) {}
	
	default public void expeditionResult (ExpeditionResult result) {}
	
	default public void unknown(String uri, Map<String, String[]> parameters) {}
	
	public static DataChangedHandler createEmptyHandler() {
		return new DataChangedHandler() {};
	}
}
