package edu.udel.cis.vsl.dotchecker.search.concurrent;

public class ConcurrentSearchNode<STATE> {
	STATE sourceState;

	boolean cyan[];

	volatile boolean red = false;

	volatile boolean blue = false;

	private static final int NOT_HASHED = -1;

	private volatile int hashCode = NOT_HASHED;

	/**
	 * for optimization.
	 */
	volatile boolean seen = false;

	public ConcurrentSearchNode(int threadNum, STATE sourceState) {
		cyan = new boolean[threadNum];
		this.sourceState = sourceState;
	}

	@Override
	public int hashCode() {
		if (hashCode == NOT_HASHED) {
			hashCode = sourceState.hashCode();
		}

		return hashCode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ConcurrentSearchNode) {
			return sourceState.equals(((ConcurrentSearchNode<STATE>) obj).sourceState);
		} else
			return false;
	}
}
