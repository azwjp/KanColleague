package jp.azw.kancolleague.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;


public class ConstrDock extends ArrayList<ConstrDock.ConstrDockValue> {
	public enum State {
		EMPTY(0), UNKNOWN(-1);
		int value;

		private State(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static State getState(int value) {
			return Arrays.stream(values()).filter(state -> {
				return state.getValue() == value;
			}).findAny().orElse(UNKNOWN);
		}
	}

	public ConstrDock(JSONObject kDock) {
		super();
		JSONArray apiData = kDock.getJSONArray("api_data");
		IntStream.range(0, apiData.length()).forEach(i -> this.add(new ConstrDockValue(apiData.getJSONObject(i))));

	}

	public class ConstrDockValue {
		private JSONObject apiData;

		public ConstrDockValue(JSONObject apiData) {
			this.apiData = apiData;
		}

		/**
		 * ユーザの ID。ユーザ情報がはいった basic の JSON に入っている ID と同一。当然すべてのドックで同じ。
		 * ここにある必要性が感じられない。
		 * 
		 * @return api_member_id
		 */
		public int getMemberId() {
			return apiData.getInt("api_member_id");
		}

		/**
		 * ドックの番号。上から何番目か。
		 * 
		 * @return api_id
		 */
		public int getId() {
			return apiData.getInt("api_id");
		}

		/**
		 * 建造状態
		 * 
		 * @return
		 */
		public int getStateNum() {
			return apiData.getInt("api_created_ship_id");
		}

		/**
		 * 建造中の艦の ID。
		 * 
		 * @return
		 */
		public int getShipId() {
			return apiData.getInt("api_created_ship_id");
		}
	}
}
