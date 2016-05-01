package jp.azw.kancolleague.kcapi.portaction;

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
	private int id;
	/**
	 * api_fuel, api_bull
	 */
	private Resource charged;
	/**
	 * api_onslot
	 */
	private List<Integer> 最大艦載機数 = new ArrayList<>();
	
	public ChargeElement(int id, Resource charged, List<Integer> onslot) {
		this.id = id;
		this.charged = charged;
		this.最大艦載機数 = onslot;
	}

	/**
	 * 所持艦固有の番号
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * 補給した量。燃料と弾薬のみ。
	 * @return 燃料と弾薬以外は 0
	 */
	public Resource getCharged() {
		return charged;
	}

	/**
	 * それぞれの装備スロットの艦載機の搭載最大数
	 * @return
	 */
	public List<Integer> get最大艦載機数() {
		return 最大艦載機数;
	}
}
