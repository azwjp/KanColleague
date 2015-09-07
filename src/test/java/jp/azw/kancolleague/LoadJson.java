package jp.azw.kancolleague;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;

public class LoadJson {
	public static JSONObject loadJson (String api) {
		return new JSONObject(load(api));
	}
	
	public static String load(String api) {
		StringBuilder sb = new StringBuilder();
		try {
			Files.lines(getPath(api), Charset.forName("UTF-8")).forEach(line -> sb.append(line));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return sb.toString();
	}
	
	public static Path getPath(String api) {
		return Paths.get(new StringBuilder("defaultjson").append(File.separator).append(api).append(".json").toString());
	}
}
