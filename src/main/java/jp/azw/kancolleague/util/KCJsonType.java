package jp.azw.kancolleague.util;

import java.util.Arrays;

public enum KCJsonType {
	API_START2("/kcsapi/api_start2"),
	API_REQ_HOKYU__CHARGE("/kcapi/api_req_hokyu/charge"),
	EXPEDITION_RESULT("/kcapi/api_req_mission/result"),
	UNKNOWN(""),
	NON_KC(""),
	;
	
	private String uri;
	
	private KCJsonType(String uri) {
		this.uri = uri;
	}
	
	public String getUri() {
		return uri;
	}
	
	public static KCJsonType detect(String uri) {
		return Arrays.stream(KCJsonType.values()).parallel().filter(type -> type != NON_KC).filter(type -> type.getUri().equals(uri)).findAny().orElse(UNKNOWN);
	}
}
