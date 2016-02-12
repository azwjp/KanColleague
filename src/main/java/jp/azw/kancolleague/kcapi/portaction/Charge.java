package jp.azw.kancolleague.kcapi.portaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jp.azw.kancolleague.kcapi.Root;
import jp.azw.kancolleague.util.JsonUtil;
import jp.azw.kancolleague.util.Resource;

/**
 * api_req_hokyu/charge
 * 
 * @author sayama
 *
 */
public class Charge extends Root{
	enum Kind{
		燃料のみ(1), 弾薬のみ(2), 全補給(3),
		/**
		 * 知らない数字が送られてきた場合
		 */
		UNKNOWN(0x8000_0000);
		
		int value;
		private Kind(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static Kind getResult(int value) {
			return Arrays.stream(values()).parallel().filter(state -> state.getValue() == value ).findAny().orElse(UNKNOWN);
		}
	}
	
	private List<Integer> shipId;
	private Kind kind;
	private int onSlot;
	/**
	 * 
	 */
	private List<ChargeElement> ship = new ArrayList<>();
	/**
	 * 結局資源がどうなったか
	 */
	private Resource material;
	private int useBou;
	
	public Charge(JsonObject charge, Map<String, String[]> params) {
		super(charge, params);
		shipId = Arrays.stream(params.get("api_id_items")[0].split(",")).parallel().map(Integer::valueOf).collect(Collectors.toList());
		kind = Kind.getResult(Integer.valueOf(params.get("api_kind")[0]));
		onSlot = Integer.valueOf(params.get("api_onslot")[0]);

		JsonObject apiData = charge.get("api_data").getAsJsonObject();
		this.ship = JsonUtil.fromJsonArray(apiData.get("api_ship"))
				.map(JsonElement::getAsJsonObject)
				.map(ship -> new ChargeElement(ship.get("api_id").getAsInt(),
						new Resource(ship.get("api_fuel").getAsInt(),
								ship.get("api_bull").getAsInt(), 0, 0),
						JsonUtil.fromJsonArray(ship.get("api_onslot")).map(element -> element.getAsInt()).collect(Collectors.toList())))
				.collect(Collectors.toList());
		this.material = Resource.fromJsonArray(apiData.get("api_material").getAsJsonArray());
		this.useBou = apiData.get("api_use_bou").getAsInt();
	}

	public List<ChargeElement> get各艦補給情報() {
		return ship;
	}

	public Resource get現在資源() {
		return material;
	}

	/**
	 * 消費ボーキサイト
	 * @return
	 */
	public int getUsedBauxite() {
		return useBou;
	}

	/**
	 * 補給した艦娘の固有番号
	 * @return
	 */
	public List<Integer> getShipId() {
		return shipId;
	}

	/**
	 * 補給の種類
	 * 
	 * @return
	 */
	public Kind getKind() {
		return kind;
	}

	/**
	 * ?? 常に 1
	 * @return
	 */
	public int getOnSlot() {
		return onSlot;
	}
	
	public Resource 消費資源合計(){
		return get各艦補給情報().parallelStream().map(ChargeElement::getCharged).reduce(new Resource(0, 0, 0, getUsedBauxite()), Resource::sum);
	}
}
