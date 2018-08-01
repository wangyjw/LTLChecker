package edu.udel.cis.vsl.dotchecker.search.concurrent;

import java.util.Collection;
import java.util.Iterator;

public class ConcurrentStackEntry<STATE> {
	Iterator<ConcurrentSearchNode<STATE>> statesIterator;

	ConcurrentSearchNode<STATE> searchNode;

	Collection<ConcurrentSearchNode<STATE>> nodes;

	public ConcurrentStackEntry(Collection<ConcurrentSearchNode<STATE>> states,
			ConcurrentSearchNode<STATE> searchNode) {
		this.statesIterator = states.iterator();
		this.searchNode = searchNode;
		this.nodes = states;
	}

	public boolean hasNext() {
		return statesIterator.hasNext();
	}

	public ConcurrentSearchNode<STATE> next() {
		return statesIterator.next();
	}

	public Collection<ConcurrentSearchNode<STATE>> getTransitions() {
		return nodes;
	}
}
