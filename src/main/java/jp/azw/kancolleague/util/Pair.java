package jp.azw.kancolleague.util;

import java.util.Map;

public class Pair<F, S> implements Map.Entry<F,S>{
	private F first;
	private S second;
	
	protected Pair(F first, S second) {
		super();
		this.first = first;
		this.second = second;
	}

	public F getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pair) {
			@SuppressWarnings("rawtypes")
			Pair p = (Pair) obj;
			return first.equals(p.getFirst()) && second.equals(p.getSecond());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return first.hashCode() ^ second.hashCode();
	}

	@Override
	public F getKey() {
		return first;
	}

	@Override
	public S getValue() {
		return second;
	}

	@Override
	public S setValue(S value) {
		S old = second;
		second = value;
		return old;
	}

	public static <F, S> Pair<F, S> of (F first, S second) {
		return new Pair<F, S>(first, second);
	}
}
