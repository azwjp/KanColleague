package jp.azw.kancolleague.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class Util {
	
	private static Stream<JsonElement> pre(JsonArray json) {
		return StreamSupport.stream(json.spliterator(), false);
	}
	
	private static <T> List<T> post(Stream<T> stream) {
		List<T> list = new ArrayList<>();
		stream.forEach(t -> list.add(t));
		return list;
	}
	
	public static List<Integer> jsonArrayToIntList (JsonArray json) {
		return post(pre(json).map(element -> element.getAsInt()));
	}
	
	public static List<String> jsonArrayToStringList (JsonArray json) {
		return post(pre(json).map(element -> element.getAsString()));
	}
	
	public static List<Boolean> jsonArrayToBoolList (JsonArray json) {
		return post(pre(json).map(element -> element.getAsBoolean()));
	}	
}
