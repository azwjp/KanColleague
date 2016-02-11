package jp.azw.kancolleague.kcapi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.gson.JsonObject;

import jp.azw.kancolleague.util.Resource;

/**
 * api_req_hokyu/charge
 * 
 * @author sayama
 *
 */
public class Charge extends Root{
	/**
	 * 
	 */
	private List<ChargeElement> ship = new ArrayList<>();
	/**
	 * 結局資源がどうなったか
	 */
	private Resource material;
	private int useBou;
	
	public Charge(List<ChargeElement> ship, Resource material, int useBou) {
		this.ship = ship;
		this.material = material;
		this.useBou = useBou;
	}

	public List<ChargeElement> getShip() {
		return ship;
	}

	public Resource getMaterial() {
		return material;
	}

	public int getUseBou() {
		return useBou;
	}

	public static Charge instance(JsonObject charge) {
		JsonObject apiData = charge.get("api_data").getAsJsonObject();
		List<ChargeElement> list = StreamSupport.stream(apiData.get("api_ship").getAsJsonArray().spliterator(), false)
		.map(element -> element.getAsJsonObject())
		.map(ship -> new ChargeElement(ship.get("api_id").getAsInt(), new Resource(ship.get("api_fuel").getAsInt(), ship.get("api_bull").getAsInt(), 0, 0), StreamSupport.stream(ship.get("api_onslot").getAsJsonArray().spliterator(), false).map(element -> element.getAsInt()).collect(Collectors.toList())))
		.collect(Collectors.toList());
		
		
		return new Charge(list, Resource.fromJsonArray(apiData.get("api_material").getAsJsonArray()), apiData.get("api_use_bou").getAsInt());
	}
}
