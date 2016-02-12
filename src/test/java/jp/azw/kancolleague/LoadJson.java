package jp.azw.kancolleague;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class LoadJson {
	public static JsonObject loadJson(String api) {
		return new Gson().fromJson(load(api), JsonObject.class);
	}

	public static String load(String api) {
		StringBuilder sb = new StringBuilder();
		try {
			Files.lines(getPath(api), Charset.forName("UTF-8")).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		while (sb.charAt(0) != '{') {
			sb.deleteCharAt(0);
		};
		return sb.toString();
	}

	public static Path getPath(String api) {
		return Paths
				.get(new StringBuilder("defaultjson").append(File.separator).append(api).append(".json").toString());
	}
}
