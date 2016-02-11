package jp.azw.kancolleague.kcapi.quest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jp.azw.kancolleague.kcapi.Root;
import jp.azw.kancolleague.util.Resource;
import jp.azw.kancolleague.util.アイテム入手;

/**
 * /kcsapi/api_req_quest/clearitemget
 * 
 * @author sayama
 *
 */
public class QuestClear extends Root{
	private int 遠征Id;
	private Resource 入手資源;
	private int 入手アイテム数;
	private List<GettingItem> 入手アイテム = new ArrayList<>();

	public QuestClear(JsonObject json, Map<String, String[]> requestParam) {
		super(json, requestParam);
		JsonObject apiData = json.get("api_data").getAsJsonObject();
		遠征Id = Integer.valueOf(requestParam.get("api_quest_id")[0]);
		入手資源 = Resource.fromJsonArray(apiData.get("api_material").getAsJsonArray());
		入手アイテム数 = apiData.get("api_bonus_count").getAsInt();
		入手アイテム = StreamSupport.stream(apiData.get("api_bonus").getAsJsonArray().spliterator(), false).map(element -> new GettingItem(element)).collect(Collectors.toList());
	}

	public int get遠征Id() {
		return 遠征Id;
	}

	public Resource get入手資源() {
		return 入手資源;
	}

	public int get入手アイテム数() {
		return 入手アイテム数;
	}

	public List<GettingItem> get入手アイテム() {
		return 入手アイテム;
	}
	
	public class GettingItem implements アイテム入手{
		private int アイテムId;
		private String アイテム名; // 入ってない
		private int 個数;
		private int type;
		public GettingItem(JsonElement bonus) {
			JsonObject obj = bonus.getAsJsonObject();
			this.アイテムId = obj.get("api_item").getAsJsonObject().get("api_name").getAsInt();
			JsonElement name = obj.get("api_item").getAsJsonObject().get("api_name");
			this.アイテム名 = name.isJsonNull() ? "" : name.getAsString();
			this.個数 = obj.get("api_count").getAsInt();
			this.type = obj.get("api_type").getAsInt();
		}
		public int getアイテムId() {
			return アイテムId;
		}
		public String getアイテム名() {
			return アイテム名;
		}
		public int get個数() {
			return 個数;
		}
		public int getType() {
			return type;
		}
	}
}
