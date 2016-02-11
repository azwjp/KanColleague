package jp.azw.kancolleague.util;

public class MapToNumber {
	enum Maps {
		鎮守府正面海域(1),
		/**
		 * 知らない数字が送られてきた場合
		 */
		UNKNOWN(0x8000_0000);
		
		
		int value;
		private Maps(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public Maps fromName(String name) {
			switch (name) {
			case "鎮守府海域":
				return 鎮守府正面海域;
			default:
				return UNKNOWN;
			}
		}
	}
}
