package jp.azw.kancolleague.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

import jp.azw.kancolleague.Util;
import jp.azw.kancolleague.util.Range;

/**
 * api_start2 中の api_mst_ships 部分のデータを格納<br />
 * それぞれの艦娘の基本的なデータ。 火力などの上限・下限のデータも含むが、能動的に強化できない回避などについてはデータがない。
 * 
 * @author Akane Sayama
 *
 */
public class BasicShipData {
	public enum VoiceF {
		NONE (0), LEAVING(1), TIMESIGNAL(2), FULL(3), UNKNOWN(-1);
		int value;

		private VoiceF(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static VoiceF getState(int value) {
			return Arrays.stream(values()).filter(state -> {
				return state.getValue() == value;
			}).findAny().orElse(UNKNOWN);
		}
	}
	private Optional<Integer> remodelAmmo; // api_after_bull 次の改造に必要な弾薬
	private Optional<Integer> remodelFuel; // api_after_fuel 改造に必要な燃料
	private Optional<Integer> remodelLv; // api_after_lv 改造レベル
	private Optional<Integer> remodeledShipid; // api_aftershipid 改造後の艦種
	private int id; // api_id 管理用 id
	private Optional<Integer> background; // api_backs 背景
	private Optional<List<Integer>> scrapValue; // api_broken [4]
												// 解体で出てくる資材。2-4-11
	private Optional<Integer> timeToBuild; // api_buildtime 建造にかかる時間
	private Optional<Integer> ammoConsumption; // api_bull_max 弾薬全消費
	private Optional<Integer> fuelConsumption; // api_fuel_max 燃料全消費
	private Optional<String> gettingMessage; // api_getmes 入手時の台詞
	private Optional<Integer> range; // api_leng 射程
	private Optional<List<Integer>> planeSpace; // api_max_eq [5] スロットごとの艦載機数
	private Optional<String> name; // api_name 表示名
	private Optional<List<Integer>> modernization; // api_powup [4] 回収の餌にされた時の効果
	private Optional<Integer> slotSize; // api_slot_num 装備数
	private Optional<Integer> speed; // api_soku 速度
	private Optional<Integer> sortNo; // api_sort_no 図鑑番号
	private Optional<Integer> shipType; // api_stype ship type
	private Optional<Integer> voiceType; // api_voicef 放置・時報
	private Optional<String> yomi; // api_yomi よみがな

	private Optional<Range<Integer>> firepower; // api_houg 火力
	private Optional<Range<Integer>> torpedo; // api_raig 雷装
	private Optional<Range<Integer>> antiAir; // api_tyku 対空
	private Optional<Range<Integer>> armor; // api_souk 装甲
	private Optional<Range<Integer>> luck; // api_luck 運
	private Optional<Range<Integer>> hp; // api_taik 耐久

	private boolean isFriend;


	public BasicShipData(JSONObject apiStart2, int index) {
		JSONObject apiMstShip = apiStart2.getJSONObject("api_data").getJSONArray("api_mst_ship").getJSONObject(index);
		id = apiMstShip.getInt("api_id");
		name = Optional.of(apiMstShip.getString("api_name"));
		speed = Optional.of(apiMstShip.getInt("api_soku"));
		slotSize = Optional.of(apiMstShip.getInt("api_slot_num"));
		shipType = Optional.of(apiMstShip.getInt("api_stype"));
		yomi = Optional.of(apiMstShip.getString("api_yomi"));
		if (isFriend = apiMstShip.length() == 27) {
			remodelAmmo = Optional.of(apiMstShip.getInt("api_afterbull"));
			remodelFuel = Optional.of(apiMstShip.getInt("api_afterfuel"));
			remodelLv = Optional.of(apiMstShip.getInt("api_afterlv"));
			remodeledShipid = Optional.of(Integer.valueOf(apiMstShip.getString("api_aftershipid")));
			background = Optional.of(apiMstShip.getInt("api_backs"));
			scrapValue = Optional.of(Util.jsonArrayToIntList(apiMstShip.getJSONArray("api_broken")));
			timeToBuild = Optional.of(apiMstShip.getInt("api_buildtime"));
			ammoConsumption = Optional.of(apiMstShip.getInt("api_bull_max"));
			fuelConsumption = Optional.of(apiMstShip.getInt("api_fuel_max"));
			gettingMessage = Optional.ofNullable(apiMstShip.getString("api_getmes"));
			range = Optional.of(apiMstShip.getInt("api_leng"));
			planeSpace = Optional.of(Util.jsonArrayToIntList(apiMstShip.getJSONArray("api_maxeq")));
			modernization = Optional.of(Util.jsonArrayToIntList(apiMstShip.getJSONArray("api_powup")));
			sortNo = Optional.of(apiMstShip.getInt("api_sortno"));
			voiceType = Optional.of(apiMstShip.getInt("api_voicef"));
			firepower = Optional.of(Range.of(apiMstShip.getJSONArray("api_houg").getInt(0),
					apiMstShip.getJSONArray("api_houg").getInt(1)));
			torpedo = Optional.of(Range.of(apiMstShip.getJSONArray("api_houg").getInt(0),
					apiMstShip.getJSONArray("api_raig").getInt(1)));
			antiAir = Optional.of(Range.of(apiMstShip.getJSONArray("api_houg").getInt(0),
					apiMstShip.getJSONArray("api_tyku").getInt(1)));
			armor = Optional.of(Range.of(apiMstShip.getJSONArray("api_houg").getInt(0),
					apiMstShip.getJSONArray("api_souk").getInt(1)));
			luck = Optional.of(Range.of(apiMstShip.getJSONArray("api_houg").getInt(0),
					apiMstShip.getJSONArray("api_luck").getInt(1)));
			hp = Optional.of(Range.of(apiMstShip.getJSONArray("api_houg").getInt(0),
					apiMstShip.getJSONArray("api_taik").getInt(1)));
		} else {
			remodelAmmo = Optional.empty();
			remodelFuel = Optional.empty();
			remodelLv = Optional.empty();
			remodeledShipid = Optional.empty();
			background = Optional.empty();
			scrapValue = Optional.empty();
			timeToBuild = Optional.empty();
			ammoConsumption = Optional.empty();
			fuelConsumption = Optional.empty();
			gettingMessage = Optional.empty();
			range = Optional.empty();
			planeSpace = Optional.empty();
			modernization = Optional.empty();
			sortNo = Optional.empty();
			voiceType = Optional.empty();
			firepower = Optional.empty();
			torpedo = Optional.empty();
			antiAir = Optional.empty();
			armor = Optional.empty();
			luck = Optional.empty();
			hp = Optional.empty();
		}
	}

	public BasicShipData() {
		id = 0;
		name = Optional.empty();
		speed = Optional.empty();
		slotSize = Optional.empty();
		shipType = Optional.empty();
		yomi = Optional.empty();

		remodelAmmo = Optional.empty();
		remodelFuel = Optional.empty();
		remodelLv = Optional.empty();
		remodeledShipid = Optional.empty();
		background = Optional.empty();
		scrapValue = Optional.empty();
		timeToBuild = Optional.empty();
		ammoConsumption = Optional.empty();
		fuelConsumption = Optional.empty();
		gettingMessage = Optional.empty();
		range = Optional.empty();
		planeSpace = Optional.empty();
		modernization = Optional.empty();
		sortNo = Optional.empty();
		voiceType = Optional.empty();
		firepower = Optional.empty();
		torpedo = Optional.empty();
		antiAir = Optional.empty();
		armor = Optional.empty();
		luck = Optional.empty();
		hp = Optional.empty();
	}

	/**
	 * 改造できるなら true を返す。 改造できないか、敵艦戦などで改造後のデータがないなら false を返す。 改造後の艦の ID
	 * (getRemodeledShipid()) で判別。
	 * 
	 * @return 改造可能かどうか。
	 */
	public boolean isRemodelable() {
		return getRemodelLv().orElse(0) != 0;
	}

	/**
	 * 改造に必要な弾薬
	 * 
	 * @return api_after_bull
	 */
	public Optional<Integer> getRemodelAmmo() {
		return remodelAmmo;
	}

	/**
	 * 改造に必要な燃料
	 * 
	 * @return api_after_fuel
	 */
	public Optional<Integer> getRemodelFuel() {
		return remodelFuel;
	}

	/**
	 * 改造レベル。改造できないなら 0。敵艦戦は null。
	 * 
	 * @return api_after_lv
	 */
	public Optional<Integer> getRemodelLv() {
		return remodelLv;
	}

	/**
	 * 改造後の艦の ID
	 * 
	 * @return api_aftershipid
	 */
	public Optional<Integer> getRemodeledShipid() {
		return remodeledShipid;
	}

	/**
	 * この船の ID。内部データ用の ID であり図鑑 No. ではない。
	 * 
	 * @return api_id
	 */
	public int getId() {
		return id;
	}

	/**
	 * 背景の種類。レア度。
	 * 
	 * @return api_backs
	 */
	public Optional<Integer> getBackground() {
		return background;
	}

	public Optional<List<Integer>> getScrapValue() {
		return scrapValue;
	}

	public Optional<Integer> getTimeToBuild() {
		return timeToBuild;
	}

	public Optional<Integer> getAmmoConsumption() {
		return ammoConsumption;
	}

	public Optional<Integer> getFuelConsumption() {
		return fuelConsumption;
	}

	public Optional<String> getGettingMessage() {
		return gettingMessage;
	}

	public Optional<Integer> getRange() {
		return range;
	}

	public Optional<List<Integer>> getPlaneSpace() {
		return planeSpace;
	}

	public Optional<String> getName() {
		return name;
	}

	public Optional<List<Integer>> getModernization() {
		return modernization;
	}

	public Optional<Integer> getSlotSize() {
		return slotSize;
	}

	public Optional<Integer> getSpeed() {
		return speed;
	}

	public Optional<Integer> getSortNo() {
		return sortNo;
	}

	public Optional<Integer> getShipType() {
		return shipType;
	}

	public Optional<Integer> getVoiceType() {
		return voiceType;
	}

	public Optional<String> getYomi() {
		return yomi;
	}

	public Optional<Range<Integer>> getFirepower() {
		return firepower;
	}

	public Optional<Range<Integer>> getTorpedo() {
		return torpedo;
	}

	public Optional<Range<Integer>> getAntiAir() {
		return antiAir;
	}

	public Optional<Range<Integer>> getArmor() {
		return armor;
	}

	public Optional<Range<Integer>> getLuck() {
		return luck;
	}

	public Optional<Range<Integer>> getHp() {
		return hp;
	}

	public boolean isFriend() {
		return isFriend;
	}

	public void setRemodelAmmo(Optional<Integer> remodelAmmo) {
		this.remodelAmmo = remodelAmmo;
	}

	public void setRemodelFuel(Optional<Integer> remodelFuel) {
		this.remodelFuel = remodelFuel;
	}

	public void setRemodelLv(Optional<Integer> remodelLv) {
		this.remodelLv = remodelLv;
	}

	public void setRemodeledShipid(Optional<Integer> remodeledShipid) {
		this.remodeledShipid = remodeledShipid;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setBackground(Optional<Integer> background) {
		this.background = background;
	}

	public void setScrapValue(Optional<List<Integer>> scrapValue) {
		this.scrapValue = scrapValue;
	}

	public void setTimeToBuild(Optional<Integer> timeToBuild) {
		this.timeToBuild = timeToBuild;
	}

	public void setAmmoConsumption(Optional<Integer> ammoConsumption) {
		this.ammoConsumption = ammoConsumption;
	}

	public void setFuelConsumption(Optional<Integer> fuelConsumption) {
		this.fuelConsumption = fuelConsumption;
	}

	public void setGettingMessage(Optional<String> gettingMessage) {
		this.gettingMessage = gettingMessage;
	}

	public void setRange(Optional<Integer> range) {
		this.range = range;
	}

	public void setPlaneSpace(Optional<List<Integer>> planeSpace) {
		this.planeSpace = planeSpace;
	}

	public void setName(Optional<String> name) {
		this.name = name;
	}

	public void setModernization(Optional<List<Integer>> modernization) {
		this.modernization = modernization;
	}

	public void setSlotSize(Optional<Integer> slotSize) {
		this.slotSize = slotSize;
	}

	public void setSpeed(Optional<Integer> speed) {
		this.speed = speed;
	}

	public void setSortNo(Optional<Integer> sortNo) {
		this.sortNo = sortNo;
	}

	public void setShipType(Optional<Integer> shipType) {
		this.shipType = shipType;
	}

	public void setVoiceType(Optional<Integer> voiceType) {
		this.voiceType = voiceType;
	}

	public void setYomi(Optional<String> yomi) {
		this.yomi = yomi;
	}

	public void setFirepower(Optional<Range<Integer>> firepower) {
		this.firepower = firepower;
	}

	public void setTorpedo(Optional<Range<Integer>> torpedo) {
		this.torpedo = torpedo;
	}

	public void setAntiAir(Optional<Range<Integer>> antiAir) {
		this.antiAir = antiAir;
	}

	public void setArmor(Optional<Range<Integer>> armor) {
		this.armor = armor;
	}

	public void setLuck(Optional<Range<Integer>> luck) {
		this.luck = luck;
	}

	public void setHp(Optional<Range<Integer>> hp) {
		this.hp = hp;
	}

	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}
	
	public VoiceF getVoiceTypeEnum() {
		return VoiceF.getState(getVoiceType().orElse(-1));
	}
	
	public void setVoiceTypeEnum (VoiceF voicef) {
		setVoiceType(Optional.of(voicef.getValue()));
	}

	/**
	 * api_start2 のデータを利用して List を作成します。 index と id は一致していません。 順序に保証はありません。 List
	 * は LinkedList を使用しています。 ShipGraph は null です。
	 * 
	 * @param apiStart2
	 * @return LinkedList
	 */
	public static List<BasicShipData> buildList(JSONObject apiStart2) {
		/* api_mst_ship */
		JSONArray apiMstShips = apiStart2.getJSONObject("api_data").getJSONArray("api_mst_ship");
		List<BasicShipData> list = Collections.synchronizedList(new LinkedList<>());
		IntStream.range(0, apiMstShips.length()).parallel().forEach(i -> list.add(new BasicShipData(apiStart2, i)));
		return list;
	}

}
