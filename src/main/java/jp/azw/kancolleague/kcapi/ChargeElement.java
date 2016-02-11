package jp.azw.kancolleague.kcapi;

import java.util.ArrayList;
import java.util.List;

import jp.azw.kancolleague.util.Resource;

/**
 * {@link Charge} での
 * @author sayama
 *
 */
public class ChargeElement {
	/**
	 * api_id
	 * 所持艦それぞれ固有にもつ番号
	 */
	int id;
	/**
	 * api_fuel, api_bull
	 */
	Resource charged;
	/**
	 * api_onslot
	 */
	List<Integer> onslot = new ArrayList<>();
	
	public ChargeElement(int id, Resource charged, List<Integer> onslot) {
		super();
		this.id = id;
		this.charged = charged;
		this.onslot = onslot;
	}
}
