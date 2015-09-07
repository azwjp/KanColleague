package jp.azw.kancolleague.util;

public class Range<T extends Comparable<T>> extends Pair<T, T> {

	private boolean isFirstGreater;

	protected Range(T first, T second, boolean isFirstGreater) {
		super(first, second);
		this.isFirstGreater = isFirstGreater;
	}
	
	public T getMax() {
		return isFirstGreater ? getFirst() : getSecond();
	}
	
	public T getMin() {
		return isFirstGreater ? getSecond() : getFirst();
	}
	
	public boolean isFirstGreater() {
		return isFirstGreater;
	}


	public static <T extends Comparable<T>> Range<T> of (T first, T second) {
			return new Range<T>(first, second, first.compareTo(second) > 0);
	}

}