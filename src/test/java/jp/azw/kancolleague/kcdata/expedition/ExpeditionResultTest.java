package jp.azw.kancolleague.kcdata.expedition;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import jp.azw.kancolleague.LoadJson;
import jp.azw.kancolleague.kcapi.expedition.ExpeditionResult;
import jp.azw.kancolleague.kcapi.expedition.ExpeditionResult.Result;
import jp.azw.kancolleague.util.Resource;

@RunWith(JUnit4.class)
public class ExpeditionResultTest {
	ExpeditionResult er;

	@Before
	public void setup() {
		Map<String, String[]> param = new HashMap<>();
		String[] str = {"1"};
		param.put("api_deck_id", str);
		str[0] = "test";
		param.put("api_token", str);
		str[0] = "1";
		param.put("api_verno", str);
		er = new ExpeditionResult(LoadJson.loadJson("api_req_mission/result"), param);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_loadjson() {
		assertThat(er.get入手アイテムFlag(), is(contains(1, 0)));
		assertThat(er.get入手提督経験値(), is(20));
		assertThat(er.get艦娘Id(), is(contains(-1, 9094, 7570, 8777, 8968)));
		assertThat(er.getClearResult(),	is(Result.SUCCESS));
		assertThat(er.get入手提督経験値(), is(20));
		assertThat(er.get司令部Level(), is(108));
		assertThat(er.get提督経験値(), is(4811813));
		assertThat(er.get入手艦娘経験値(), is(contains(22, 15, 15, 15)));
		assertThat(er.get現在の艦娘経験値(), is(contains(Pair.of(8892, 9100), Pair.of(14458, 15300), Pair.of(7155, 7800), Pair.of(7545, 7800))));
		assertThat(er.get海域名(), is("鎮守府海域"));
		assertThat(er.get遠征の説明(), is("外海まで足を延ばし、艦隊の練度を高めよう！"));
		assertThat(er.getQuestName(), is("長距離練習航海"));
		assertThat(er.getQuestLevel(), is(1));
		assertThat(er.get入手資源(), is(new Resource(0, 100, 30, 0)));
		assertThat(er.get入手アイテム().size(), is(1));
		assertThat(er.get入手アイテム().get(0).getアイテムId(), is(-1));
		assertThat(er.get入手アイテム().get(0).getアイテム名(), is(""));
		assertThat(er.get入手アイテム().get(0).get個数(), is(1));
	}
}
