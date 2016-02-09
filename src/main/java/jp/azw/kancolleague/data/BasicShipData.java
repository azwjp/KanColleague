package jp.azw.kancolleague.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jp.azw.kancolleague.util.Range;
import jp.azw.kancolleague.util.Resource;
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
	
	private Optional<Resource> remodel;// api_after_bull 次の改造に必要な弾薬 // api_after_fuel 改造に必要な燃料
	private Optional<Integer> remodelLv; // api_after_lv 改造レベル
	private Optional<Integer> remodeledShipid; // api_aftershipid 改造後の艦種
	private int id; // api_id 管理用 id
	private Optional<Integer> background; // api_backs 背景
	private Optional<Resource> scrapValue; // api_broken [4]
												// 解体で出てくる資材。2-4-11
	private Optional<Integer> timeToBuild; // api_buildtime 建造にかかる時間
	private Optional<Resource> consumption; // api_bull_max 弾薬全消費 // api_fuel_max 燃料全消費
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


	public BasicShipData(JsonObject apiMstShip) {
		id = apiMstShip.get("api_id").getAsInt();
		name = Optional.of(apiMstShip.get("api_name").getAsString());
		speed = Optional.of(apiMstShip.get("api_soku").getAsInt());
		slotSize = Optional.of(apiMstShip.get("api_slot_num").getAsInt());
		shipType = Optional.of(apiMstShip.get("api_stype").getAsInt());
		yomi = Optional.of(apiMstShip.get("api_yomi").getAsString());
		if (isFriend = apiMstShip.entrySet().size() == 27) {
			remodel = Optional.of(new Resource(apiMstShip.get("api_afterfuel").getAsInt(), apiMstShip.get("api_afterbull").getAsInt(), 0, 0));
			remodelLv = Optional.of(apiMstShip.get("api_afterlv").getAsInt());
			remodeledShipid = Optional.of(apiMstShip.get("api_aftershipid").getAsInt());
			background = Optional.of(apiMstShip.get("api_backs").getAsInt());
			JsonArray scrapValues = apiMstShip.get("api_broken").getAsJsonArray();
			scrapValue = Optional.of(new Resource(scrapValues.get(0).getAsInt(), scrapValues.get(1).getAsInt(), scrapValues.get(2).getAsInt(), scrapValues.get(3).getAsInt()));
			timeToBuild = Optional.of(apiMstShip.get("api_buildtime").getAsInt());
			consumption = Optional.of(new Resource(apiMstShip.get("api_fuel_max").getAsInt(), apiMstShip.get("api_bull_max").getAsInt(), 0, 0));
			gettingMessage = Optional.ofNullable(apiMstShip.get("api_getmes").getAsString());
			range = Optional.of(apiMstShip.get("api_leng").getAsInt());
			planeSpace = Optional.of(Util.jsonArrayToIntList(apiMstShip.get("api_maxeq").getAsJsonArray()));
			modernization = Optional.of(Util.jsonArrayToIntList(apiMstShip.get("api_powup").getAsJsonArray()));
			sortNo = Optional.of(apiMstShip.get("api_sortno").getAsInt());
			voiceType = Optional.of(apiMstShip.get("api_voicef").getAsInt());
			firepower = Optional.of(Range.of(apiMstShip.get("api_houg").getAsJsonArray().get(0).getAsInt(),
					apiMstShip.get("api_houg").getAsJsonArray().get(1).getAsInt()));
			torpedo = Optional.of(Range.of(apiMstShip.get("api_raig").getAsJsonArray().get(0).getAsInt(),
					apiMstShip.get("api_raig").getAsJsonArray().get(1).getAsInt()));
			antiAir = Optional.of(Range.of(apiMstShip.get("api_tyku").getAsJsonArray().get(0).getAsInt(),
					apiMstShip.get("api_tyku").getAsJsonArray().get(1).getAsInt()));
			armor = Optional.of(Range.of(apiMstShip.get("api_souk").getAsJsonArray().get(0).getAsInt(),
					apiMstShip.get("api_souk").getAsJsonArray().get(1).getAsInt()));
			luck = Optional.of(Range.of(apiMstShip.get("api_luck").getAsJsonArray().get(0).getAsInt(),
					apiMstShip.get("api_luck").getAsJsonArray().get(1).getAsInt()));
			hp = Optional.of(Range.of(apiMstShip.get("api_taik").getAsJsonArray().get(0).getAsInt(),
					apiMstShip.get("api_taik").getAsJsonArray().get(1).getAsInt()));
		} else {
			remodel = Optional.empty();
			remodelLv = Optional.empty();
			remodeledShipid = Optional.empty();
			background = Optional.empty();
			scrapValue = Optional.empty();
			timeToBuild = Optional.empty();
			consumption = Optional.empty();
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
		remodel = Optional.empty();
		remodelLv = Optional.empty();
		remodeledShipid = Optional.empty();
		background = Optional.empty();
		scrapValue = Optional.empty();
		timeToBuild = Optional.empty();
		consumption = Optional.empty();
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
	 * 改造に必要な燃料
	 * 改造に必要な弾薬
	 * 
	 * @return api_after_fuel, api_after_bull, 0, 0
	 */
	public Optional<Resource> getRemodelResource() {
		return remodel;
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

	public Optional<Resource> getScrapValue() {
		return scrapValue;
	}

	public Optional<Integer> getTimeToBuild() {
		return timeToBuild;
	}

	public Optional<Resource> getConsumption() {
		return consumption;
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

	public BasicShipData setRemodelResource(Resource remodel) {
		this.remodel = Optional.ofNullable(remodel);
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

	public BasicShipData setScrapValue(Resource scrapValue) {
		this.scrapValue = Optional.ofNullable(scrapValue);
		return this;
	}

	public BasicShipData setTimeToBuild(Integer timeToBuild) {
		this.timeToBuild = Optional.ofNullable(timeToBuild);
		return this;
	}

	public BasicShipData setConsumption(Resource consumption) {
		this.consumption = Optional.ofNullable(consumption);
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
	public static List<BasicShipData> buildList(JsonObject apiStart2) {
		JsonArray apiMstShips = apiStart2.get("api_data").getAsJsonObject().get("api_mst_ship").getAsJsonArray();
		List<BasicShipData> list = Collections.synchronizedList(new LinkedList<>());
		StreamSupport.stream(apiMstShips.spliterator(), true)
				.map(ship -> ship.getAsJsonObject())
				.map(ship -> new BasicShipData(ship))
				.forEach(bsd -> list.add(bsd));
		return list;
	}
	
	/**
	 * apiStart2 のデータを利用して {@link Map} を作成します。
	 * 
	 * @param apiStart2
	 * @return {@link HashMap}: (key, value) = (api_id, <code>BasicShipData</code>), synchronizedMap
	 */
	public static Map<Integer, BasicShipData> buildMap(JsonObject apiStart2) {
		JsonArray apiMstShips = apiStart2.get("api_data").getAsJsonObject().get("api_mst_ship").getAsJsonArray();
		Map<Integer, BasicShipData> map = Collections.synchronizedMap(new HashMap<>());
		StreamSupport.stream(apiMstShips.spliterator(), true)
				.map(ship -> ship.getAsJsonObject())
				.map(ship -> new BasicShipData(ship))
				.forEach(bsd -> map.put(bsd.getId(), bsd));
		return map;
	}

}
