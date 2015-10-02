package jp.azw.kancolleague.util;

public class Range<T extends Comparable<T>>{
	private T first;
	private T second;

	protected Range(T first, T second) {
		this.first = first;
		this.second = second;
	}
	
	public T getMax() {
		return first.compareTo(second) >= 0 ? first : second;
	}
	
	public T getMin() {
		return first.compareTo(second) < 0 ? first : second;
	}
	
	public boolean includes(T object) {
		return getMin().compareTo(object) <= 0 && getMax().compareTo(object) >= 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Range) {
			Range<?> range = (Range<?>) obj;
			return getMin().equals(range.getMin()) && getMax().equals(range.getMax());
		}
		
		return false;
	}

	public static <T extends Comparable<T>> Range<T> of (T first, T second) {
		return new Range<T>(first, second);
	}

}