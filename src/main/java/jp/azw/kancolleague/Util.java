package jp.azw.kancolleague;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.json.JsonArray;

import jp.azw.kancolleague.util.Range;

public class Util {
	@SuppressWarnings("unchecked")
	private static <T> List<T> jsonArrayToList (JsonArray json, Class<T> clazz) {
		return Arrays.asList((T[])json.toArray());
	}
	
	public static List<Integer> jsonArrayToIntList (JsonArray json) {
//		List<Integer> list = new ArrayList<>();
//		IntStream.range(0, json.size()).forEach(i -> list.add(json.getInt(i)));
		return jsonArrayToList(json, Integer.class);
	}
	
	public static List<String> jsonArrayToStringList (JsonArray json) {
		return jsonArrayToList(json, String.class);
	}
	
	public static List<Boolean> jsonArrayToBoolList (JsonArray json) {
		return jsonArrayToList(json, Boolean.class);
	}
}
