package info.itsthesky.itemcreator.utils;

public class Pair<T1, T2> {

	private final T1 first;
	private final T1 second;

	public Pair(T1 first, T1 second) {
		this.first = first;
		this.second = second;
	}

	public T1 getFirst() {
		return first;
	}

	public T1 getSecond() {
		return second;
	}
}
