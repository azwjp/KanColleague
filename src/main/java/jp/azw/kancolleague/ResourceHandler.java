package jp.azw.kancolleague;

import java.io.ByteArrayInputStream;
import java.util.Map;

public interface ResourceHandler {
	default public void text (String uri, ByteArrayInputStream byteArrayInputStream, Map<String, String[]> requestParam) {}
	default public void audio (String uri, ByteArrayInputStream byteArrayInputStream, Map<String, String[]> requestParam) {}
	default public void flash (String uri, ByteArrayInputStream byteArrayInputStream, Map<String, String[]> requestParam) {}
	default public void png (String uri, ByteArrayInputStream byteArrayInputStream, Map<String, String[]> requestParam) {}

	default public void other (String uri, ByteArrayInputStream byteArrayInputStream, Map<String, String[]> requestParam) {}
	
	public static ResourceHandler createEmptyHandler() {
		return new ResourceHandler() {};
	}
}
