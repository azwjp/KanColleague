package jp.azw.kancolleague.util;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public interface 艦娘経験値情報 {
	public List<Integer> get艦娘Id();
	public List<Pair<Integer, Integer>> get現在の艦娘経験値();
}
