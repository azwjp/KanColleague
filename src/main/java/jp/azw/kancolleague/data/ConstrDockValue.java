package jp.azw.kancolleague.data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

import com.google.gson.JsonObject;

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

	private JsonObject apiData;

	public ConstrDockValue(JsonObject apiData) {
		this.apiData = apiData;
	}

	/**
	 * ユーザの ID。ユーザ情報がはいった basic の JSON に入っている ID と同一。当然すべてのドックで同じ。
	 * ここにある必要性が感じられない。
	 * 
	 * @return api_member_id
	 */
	public int getMemberId() {
		return apiData.get("api_member_id").getAsInt();
	}

	/**
	 * ドックの番号。上から何番目か。
	 * 
	 * @return api_id
	 */
	public int getId() {
		return apiData.get("api_id").getAsInt();
	}

	/**
	 * 建造状態を元々の数字で返す
	 * 
	 * @return
	 */
	public int getStateNum() {
		return apiData.get("api_created_ship_id").getAsInt();
	}
	
	/**
	 * 建造状態を {@link State} で返す。
	 * 
	 * @return
	 */
	public State getState() {
		return State.getState(apiData.get("api_created_ship_id").getAsInt());
	}

	/**
	 * 建造中の艦の ID。
	 * 
	 * @return
	 */
	public int getShipId() {
		return apiData.get("api_created_ship_id").getAsInt();
	}

	public ZonedDateTime completeTime() {
		long time = completeTimeLong();
		return time == 0 ? null
				: ZonedDateTime.of(LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.ofHours(9)),
						ZoneId.of("Asia/Tokyo"));
	}

	public long completeTimeLong() {
		return apiData.get("api_complete_time").getAsLong();
	}

	public String completeTimeStr() {
		return apiData.get("api_complete_time_str").getAsString();
	}

	public int getUsedFuel() {
		return apiData.get("api_item1").getAsInt();
	}

	public int getUsedAmmo() {
		return apiData.get("api_item2").getAsInt();
	}

	public int getUsedSteel() {
		return apiData.get("api_item3").getAsInt();
	}

	public int getUsedBauxite() {
		return apiData.get("api_item4").getAsInt();
	}

	public int getUsedMaterial() {
		return apiData.get("api_item5").getAsInt();
	}
	
	public static List<ConstrDockValue> getConstrDock(JsonObject kDock) {
		List<ConstrDockValue> list = new ArrayList<>();
		StreamSupport.stream(kDock.get("api_data").getAsJsonArray().spliterator(), false).map(apiData -> new ConstrDockValue(apiData.getAsJsonObject())).forEach(e -> list.add(e));
		return list;
	}
}
