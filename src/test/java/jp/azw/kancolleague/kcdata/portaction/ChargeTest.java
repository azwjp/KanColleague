package jp.azw.kancolleague.kcdata.portaction;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import jp.azw.kancolleague.LoadJson;
import jp.azw.kancolleague.kcapi.portaction.Charge;
import jp.azw.kancolleague.kcapi.portaction.ChargeElement;
import jp.azw.kancolleague.util.Resource;

@RunWith(JUnit4.class)
public class ChargeTest {
	Charge charge;

	@Before
	public void setup() {
		Map<String, String[]> param = new HashMap<>();
		param.put("api_token", new String[] { "test" });
		param.put("api_verno", new String[] { "1" });
		param.put("api_kind", new String[] { "1" });
		param.put("api_id_items", new String[] { "9094,7570,8777,8968" });
		param.put("api_onslot", new String[] { "1" });
		charge = Charge.instance(LoadJson.loadJson("api_req_hokyu/charge"), param);
	}

	@Test
	public void test_消費資源合計() {
		int 燃料 = 0, 弾薬 = 0;
		for (ChargeElement e : charge.get各艦補給情報()) {
			燃料 += e.getCharged().getFuel();
			弾薬 += e.getCharged().getBullet();
		}
		assertThat(charge.消費資源合計(), is(new Resource(燃料, 弾薬, 0, charge.getUsedBauxite())));
	}
	
	@Test
	public void test_getShipId(){
		charge.get各艦補給情報().parallelStream().map(ChargeElement::getId).forEach(i -> assertThat(i, isIn(charge.getShipId())));
	}
}
