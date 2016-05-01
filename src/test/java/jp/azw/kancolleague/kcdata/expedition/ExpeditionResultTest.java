package jp.azw.kancolleague.kcdata.expedition;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import jp.azw.kancolleague.LoadJson;
import jp.azw.kancolleague.kcapi.expedition.ExpeditionResult;
import jp.azw.kancolleague.kcapi.expedition.ExpeditionResult.Result;
import jp.azw.kancolleague.kcdata.RootTest;
import jp.azw.kancolleague.util.Resource;

@RunWith(JUnit4.class)
public class ExpeditionResultTest extends RootTest<ExpeditionResult>  {
	@Before
	public void setup() {
		param.put("api_deck_id", new String[]{"1"});
		json = ExpeditionResult.instance(LoadJson.loadJson("api_req_mission/result"), param);
	}
	
	@Test
	public void test_loadjson2() {
		asserts();
	}
	
	@Test
	public void test_Result() {
		assertThat(ExpeditionResult.Result.getResult(1), is(ExpeditionResult.Result.SUCCESS));
	}
	
	@SuppressWarnings("unchecked")
	private void asserts() {
		assertThat(json.get入手アイテムFlag(), is(contains(1, 0)));
		assertThat(json.get入手提督経験値(), is(20));
		assertThat(json.get艦娘Id(), is(contains(-1, 9094, 7570, 8777, 8968)));
		assertThat(json.get結果(),	is(Result.SUCCESS));
		assertThat(json.get入手提督経験値(), is(20));
		assertThat(json.get司令部Level(), is(108));
		assertThat(json.get提督経験値(), is(4811813));
		assertThat(json.get入手艦娘経験値(), is(contains(22, 15, 15, 15)));
		assertThat(json.get現在の艦娘経験値(), is(contains(Pair.of(8892, 9100), Pair.of(14458, 15300), Pair.of(7155, 7800), Pair.of(7545, 7800))));
		assertThat(json.get海域名(), is("鎮守府海域"));
		assertThat(json.get遠征の説明(), is("外海まで足を延ばし、艦隊の練度を高めよう！"));
		assertThat(json.get遠征名(), is("長距離練習航海"));
		assertThat(json.get遠征難度(), is(1));
		assertThat(json.get入手資源(), is(new Resource(0, 100, 30, 0)));
		assertThat(json.get入手アイテム().size(), is(1));
		assertThat(json.get入手アイテム().get(0).getアイテムId(), is(-1));
		assertThat(json.get入手アイテム().get(0).getアイテム名(), is(""));
		assertThat(json.get入手アイテム().get(0).get個数(), is(1));
	}
}
