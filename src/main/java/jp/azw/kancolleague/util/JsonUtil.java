package jp.azw.kancolleague.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.gson.JsonElement;

public class JsonUtil {
	
	private static Stream<JsonElement> pre(JsonElement json) {
		return fromJsonArray(json);
	}
	
	private static <T> List<T> post(Stream<T> stream) {
		return stream.collect(Collectors.toList());
	}
	
	public static List<Integer> jsonArrayToIntList (JsonElement json) {
		return post(pre(json).map(JsonElement::getAsInt));
	}
	
	public static List<String> jsonArrayToStringList (JsonElement json) {
		return post(pre(json).map(JsonElement::getAsString));
	}
	
	public static List<Boolean> jsonArrayToBoolList (JsonElement json) {
		return post(pre(json).map(JsonElement::getAsBoolean));
	}

	public static Stream<JsonElement> fromJsonArray(JsonElement json) {
		return StreamSupport.stream(json.getAsJsonArray().spliterator(), true).parallel();
	}
}
