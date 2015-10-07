package jp.azw.kancolleague.data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

public class ConstrDockValue {

	public enum State {
		/**
		 * ドック未解放
		 */
		UNRELEASED(-1),
		/**
		 * ドック未使用
		 */
		EMPTY(0),
		/**
		 * 建造中
		 */
		CONSTRUTING(2),
		/**
		 * 建造完了
		 */
		FINISHED(3),
		/**
		 * 知らない数字が送られてきた場合
		 */
		UNKNOWN(0x8000_0000);
		int value;

		private State(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static State getState(int value) {
			return Arrays.stream(values()).parallel().filter(state -> state.getValue() == value ).findAny().orElse(UNKNOWN);
		}
	}

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
	 * 建造状態を元々の数字で返す
	 * 
	 * @return
	 */
	public int getStateNum() {
		return apiData.getInt("api_created_ship_id");
	}
	
	/**
	 * 建造状態を {@link State} で返す。
	 * 
	 * @return
	 */
	public State getState() {
		return State.getState(apiData.getInt("api_created_ship_id"));
	}

	/**
	 * 建造中の艦の ID。
	 * 
	 * @return
	 */
	public int getShipId() {
		return apiData.getInt("api_created_ship_id");
	}

	public ZonedDateTime completeTime() {
		long time = completeTimeLong();
		return time == 0 ? null
				: ZonedDateTime.of(LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.ofHours(9)),
						ZoneId.of("Asia/Tokyo"));
	}

	public long completeTimeLong() {
		return apiData.getLong("api_complete_time");
	}

	public String completeTimeStr() {
		return apiData.getString("api_complete_time_str");
	}

	public int getUsedFuel() {
		return apiData.getInt("api_item1");
	}

	public int getUsedAmmo() {
		return apiData.getInt("api_item2");
	}

	public int getUsedSteel() {
		return apiData.getInt("api_item3");
	}

	public int getUsedBauxite() {
		return apiData.getInt("api_item4");
	}

	public int getUsedMaterial() {
		return apiData.getInt("api_item5");
	}
	
	public static List<ConstrDockValue> getConstrDock(JSONObject kDock) {
		List<ConstrDockValue> list = new ArrayList<>();
		JSONArray apiData = kDock.getJSONArray("api_data");
		IntStream.range(0, apiData.length()).forEach(i -> list.add(new ConstrDockValue(apiData.getJSONObject(i))));
		return list;
	}
}
