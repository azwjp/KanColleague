package jp.azw.kancolleague.kcapi.expedition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jp.azw.kancolleague.kcapi.Root;
import jp.azw.kancolleague.util.Resource;
import jp.azw.kancolleague.util.アイテム入手;
import jp.azw.kancolleague.util.提督経験値入手;
import jp.azw.kancolleague.util.提督経験値情報;
import jp.azw.kancolleague.util.艦娘経験値入手;
import jp.azw.kancolleague.util.艦娘経験値情報;

/**
 * verno1
 * 
 * @author sayama
 *
 */
public class ExpeditionResult extends Root implements 提督経験値情報, 提督経験値入手, 艦娘経験値入手, 艦娘経験値情報{
	public enum Result {
		/**
		 * 成功
		 */
		SUCCESS(1),
		/**
		 * 知らない数字が送られてきた場合
		 */
		UNKNOWN(0x8000_0000);
		
		int value;
		private Result(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static Result getResult(int value) {
			return Arrays.stream(values()).parallel().filter(state -> state.getValue() == value ).findAny().orElse(UNKNOWN);
		}
	}
	
	/**
	 * api_deck_id
	 */
	private int deckId;
	/**
	 * api_clear_result
	 */
	private Result clearResult;
	/**
	 * api_maparea_name
	 */
	private String mapareaName;
	/**
	 * api_questlevel
	 */
	private int questLevel;
	/**
	 * api_questname
	 */
	private String questName;
	/**
	 * api_detail
	 * 遠征の説明文
	 */
	private String detail;
	/**
	 * api_get_exp
	 */
	private int getExp;
	/**
	 * get_exp_lvup
	 * <current, to next>
	 */
	private List<Pair<Integer, Integer>> currentExp;
	/**
	 * api_get_material
	 */
	private Resource gettingResource;
	/**
	 * api_get_ship_exp
	 */
	private List<Integer> gettingExp;
	/**
	 * api_ship_id
	 */
	private List<Integer> shipIds;
	/**
	 * api_member_exp
	 */
	private int 提督経験値;
	/**
	 * api_member_lv
	 */
	private int 司令部Level;
	
	private List<Integer> gettingItemFlag;
	
	private List<GettingItem> gettingItem = new ArrayList<>();

	public ExpeditionResult(JsonObject json, Map<String, String[]> requestParam) {
		super(json, requestParam);
		JsonObject apiData = json.get("api_data").getAsJsonObject();
		deckId = Integer.valueOf(requestParam.get("api_deck_id")[0]);
		clearResult = Result.getResult(apiData.get("api_clear_result").getAsInt());
		mapareaName = apiData.get("api_maparea_name").getAsString();
		questLevel = apiData.get("api_quest_level").getAsInt();
		questName = apiData.get("api_quest_name").getAsString();
		detail = apiData.get("api_detail").getAsString();
		getExp = apiData.get("api_get_exp").getAsInt();
		gettingItemFlag = StreamSupport.stream(apiData.get("api_useitem_flag").getAsJsonArray().spliterator(), false)
				.map(element -> element.getAsInt())
				.collect(Collectors.toList());
		JsonElement item = apiData.get("api_get_item1");
		if (item != null && !item.isJsonNull()) {
			gettingItem.add(new GettingItem(item));
		}
		item = apiData.get("api_get_item2");
		if (item != null && !item.isJsonNull()) {
			gettingItem.add(new GettingItem(item));
		}
		currentExp = StreamSupport.stream(apiData.get("api_get_exp_lvup").getAsJsonArray().spliterator(), false)
				.map(element -> element.getAsJsonArray())
				.map(ship -> Pair.of(ship.get(0).getAsInt(), ship.get(1).getAsInt())).collect(Collectors.toList());
		gettingResource = Resource.fromJsonArray(apiData.get("api_get_material").getAsJsonArray());
		gettingExp = StreamSupport.stream(apiData.get("api_get_ship_exp").getAsJsonArray().spliterator(), false).map(element -> element.getAsInt()).collect(Collectors.toList());
		shipIds = StreamSupport.stream(apiData.get("api_ship_id").getAsJsonArray().spliterator(), false).map(element -> element.getAsInt()).collect(Collectors.toList());
		提督経験値 = apiData.get("api_member_exp").getAsInt();
		司令部Level = apiData.get("api_member_lv").getAsInt();
	}

	public Result getClearResult() {
		return clearResult;
	}

	public String get海域名() {
		return mapareaName;
	}

	public String get遠征の説明() {
		return detail;
	}

	public int get入手提督経験値() {
		return getExp;
	}

	public List<Pair<Integer, Integer>> get現在の艦娘経験値() {
		return currentExp;
	}

	public Resource get入手資源() {
		return gettingResource;
	}

	public List<Integer> get入手艦娘経験値() {
		return gettingExp;
	}

	public List<Integer> get艦娘Id() {
		return shipIds;
	}

	public int get提督経験値() {
		return 提督経験値;
	}

	public int get司令部Level() {
		return 司令部Level;
	}

	/**
	 * 1: 高速修復材<br>
	 * 4: 家具箱小
	 * @return
	 */
	public List<Integer> get入手アイテムFlag() {
		return gettingItemFlag;
	}

	public int get艦隊番号() {
		return deckId;
	}

	public int getQuestLevel() {
		return questLevel;
	}

	public String getQuestName() {
		return questName;
	}

	public class GettingItem implements アイテム入手{
		private int アイテムId;
		private String アイテム名;
		private int 個数;
		public GettingItem(JsonElement item) {
			JsonObject obj = item.getAsJsonObject();
			JsonElement name = item.getAsJsonObject().get("api_useitem_name");
			アイテムId = obj.get("api_useitem_id").getAsInt();
			アイテム名 = name.isJsonNull() ? "" : name.getAsString();
			個数 = obj.get("api_useitem_count").getAsInt();
		}
		/**
		 * -1: 高速修復材<br>
		 * 1: 家具箱小
		 * @return
		 */
		public int getアイテムId() {
			return アイテムId;
		}
		/**
		 * 高速修復材の時はサーバからは null が来てるっぽい
		 * @return JSON のデータが null の時は空の文字列にしてあるからヌルヌルはしないよ。
		 */
		public String getアイテム名() {
			return アイテム名;
		}
		public int get個数() {
			return 個数;
		}
	}

	public List<GettingItem> get入手アイテム() {
		return gettingItem;
	}
}
