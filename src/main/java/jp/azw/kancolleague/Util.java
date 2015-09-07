package jp.azw.kancolleague;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.json.JSONArray;

public class Util {
	public static List<Integer> jsonArrayToIntList (JSONArray json) {
		List<Integer> list = new ArrayList<>();
		IntStream.range(0, json.length()).forEach(i -> list.add(json.getInt(i)));
		return list;
	}
	
	public static List<String> jsonArrayToStringList (JSONArray json) {
		List<String> list = new ArrayList<>();
		IntStream.range(0, json.length()).forEach(i -> list.add(json.getString(i)));
		return list;
	}
	
	public static List<Boolean> jsonArrayToBoolList (JSONArray json) {
		List<Boolean> list = new ArrayList<>();
		IntStream.range(0, json.length()).forEach(i -> list.add(json.getBoolean(i)));
		return list;
	}
	
	public void laadJson() {
		
		try (BufferedReader br = Files.newBufferedReader(LoadJson.getPath("api_start2"))) {
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
}
