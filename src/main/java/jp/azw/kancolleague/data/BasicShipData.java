package jp.azw.kancolleague.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

import jp.azw.kancolleague.util.Range;
import jp.azw.kancolleague.util.Util;

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

	public BasicShipData setRemodelAmmo(Integer remodelAmmo) {
		this.remodelAmmo = Optional.ofNullable(remodelAmmo);
		return this;
	}

	public BasicShipData setRemodelFuel(Integer remodelFuel) {
		this.remodelFuel = Optional.ofNullable(remodelFuel);
		return this;
	}

	public BasicShipData setRemodelLv(Integer remodelLv) {
		this.remodelLv = Optional.ofNullable(remodelLv);
		return this;
	}

	public BasicShipData setRemodeledShipid(Integer remodeledShipid) {
		this.remodeledShipid = Optional.ofNullable(remodeledShipid);
		return this;
	}

	public BasicShipData setId(int id) {
		this.id = id;
		return this;
	}

	public BasicShipData setBackground(Integer background) {
		this.background = Optional.ofNullable(background);
		return this;
	}

	public BasicShipData setScrapValue(List<Integer> scrapValue) {
		this.scrapValue = Optional.ofNullable(scrapValue);
		return this;
	}

	public BasicShipData setTimeToBuild(Integer timeToBuild) {
		this.timeToBuild = Optional.ofNullable(timeToBuild);
		return this;
	}

	public BasicShipData setAmmoConsumption(Integer ammoConsumption) {
		this.ammoConsumption = Optional.ofNullable(ammoConsumption);
		return this;
	}

	public BasicShipData setFuelConsumption(Integer fuelConsumption) {
		this.fuelConsumption = Optional.ofNullable(fuelConsumption);
		return this;
	}

	public BasicShipData setGettingMessage(String gettingMessage) {
		this.gettingMessage = Optional.ofNullable(gettingMessage);
		return this;
	}

	public BasicShipData setRange(Integer range) {
		this.range = Optional.ofNullable(range);
		return this;
	}

	public BasicShipData setPlaneSpace(List<Integer> planeSpace) {
		this.planeSpace = Optional.ofNullable(planeSpace);
		return this;
	}

	public BasicShipData setName(String name) {
		this.name = Optional.ofNullable(name);
		return this;
	}

	public BasicShipData setModernization(List<Integer> modernization) {
		this.modernization = Optional.ofNullable(modernization);
		return this;
	}

	public BasicShipData setSlotSize(Integer slotSize) {
		this.slotSize = Optional.ofNullable(slotSize);
		return this;
	}

	public BasicShipData setSpeed(Integer speed) {
		this.speed = Optional.ofNullable(speed);
		return this;
	}

	public BasicShipData setSortNo(Integer sortNo) {
		this.sortNo = Optional.ofNullable(sortNo);
		return this;
	}

	public BasicShipData setShipType(Integer shipType) {
		this.shipType = Optional.ofNullable(shipType);
		return this;
	}

	public BasicShipData setVoiceType(Integer voiceType) {
		this.voiceType = Optional.ofNullable(voiceType);
		return this;
	}

	public BasicShipData setYomi(String yomi) {
		this.yomi = Optional.ofNullable(yomi);
		return this;
	}

	public BasicShipData setFirepower(Range<Integer> firepower) {
		this.firepower = Optional.ofNullable(firepower);
		return this;
	}

	public BasicShipData setTorpedo(Range<Integer> torpedo) {
		this.torpedo = Optional.ofNullable(torpedo);
		return this;
	}

	public BasicShipData setAntiAir(Range<Integer> antiAir) {
		this.antiAir = Optional.ofNullable(antiAir);
		return this;
	}

	public BasicShipData setArmor(Range<Integer> armor) {
		this.armor = Optional.ofNullable(armor);
		return this;
	}

	public BasicShipData setLuck(Range<Integer> luck) {
		this.luck = Optional.ofNullable(luck);
		return this;
	}

	public BasicShipData setHp(Range<Integer> hp) {
		this.hp = Optional.ofNullable(hp);
		return this;
	}

	public BasicShipData setFriend(boolean isFriend) {
		this.isFriend = isFriend;
		return this;
	}
	
	public VoiceF getVoiceTypeEnum() {
		return VoiceF.getState(getVoiceType().orElse(-1));
	}
	
	public BasicShipData setVoiceTypeEnum (VoiceF voicef) {
		setVoiceType(voicef == null ? null : voicef.getValue());
		return this;
	}

	/**
	 * api_start2 のデータを利用して {@link List} を作成します。 index と id は一致していません。 順序に保証はありません。
	 * 
	 * @param apiStart2
	 * @return {@link LinkedList}, synchronizedList
	 */
	public static List<BasicShipData> buildList(JSONObject apiStart2) {
		JSONArray apiMstShips = apiStart2.getJSONObject("api_data").getJSONArray("api_mst_ship");
		List<BasicShipData> list = Collections.synchronizedList(new LinkedList<>());
		IntStream.range(0, apiMstShips.length()).parallel().forEach(i -> list.add(new BasicShipData(apiStart2, i)));
		return list;
	}
	
	/**
	 * apiStart2 のデータを利用して {@link Map} を作成します。
	 * 
	 * @param apiStart2
	 * @return {@link HashMap}: (key, value) = (api_id, <code>BasicShipData</code>), synchronizedMap
	 */
	public static Map<Integer, BasicShipData> buildMap(JSONObject apiStart2) {
		JSONArray apiMstShips = apiStart2.getJSONObject("api_data").getJSONArray("api_mst_ship");
		Map<Integer, BasicShipData> map = Collections.synchronizedMap(new HashMap<>());
		IntStream.range(0, apiMstShips.length()).parallel().forEach(i -> {
			BasicShipData ship = new BasicShipData(apiStart2, i);
			map.put(ship.getId(), ship);
		});
		return map;
	}

}
