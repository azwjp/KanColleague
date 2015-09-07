package jp.azw.kancolleague.data;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import jp.azw.kancolleague.LoadJson;

@RunWith(JUnit4.class)
public class BasicShipDataTest {
	@Test
	public void test_loadjson() {
		try (BufferedReader br = Files.newBufferedReader(LoadJson.getPath("api_start2"))) {
			JsonReader jr = Json.createReader(br);
			JsonObject json = jr.readObject();
			BasicShipData ams = new BasicShipData(json, 0);
			for (int i = 0; i < json.getJsonObject("api_data").getJsonArray("api_mst_ship").size(); i++) {
				System.out.println(i);
				ams = new BasicShipData(json, i);
				assertThat(ams.isFriend(), is(ams.getTimeToBuild().isPresent()));
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

}
