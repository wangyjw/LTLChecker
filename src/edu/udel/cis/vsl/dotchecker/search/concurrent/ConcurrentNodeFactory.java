package edu.udel.cis.vsl.dotchecker.search.concurrent;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentNodeFactory<STATE, TRANSITION> {
	private int numThread;

	private Map<STATE, ConcurrentSearchNode<STATE>> concurrentSearchNodes;

	public ConcurrentNodeFactory(int numThread) {
		this.numThread = numThread;
		this.concurrentSearchNodes = new ConcurrentHashMap<>();
	}

	public ConcurrentSearchNode<STATE> getSearchNode(STATE state) {
		ConcurrentSearchNode<STATE> node = concurrentSearchNodes.get(state);

		if (node == null) {
			node = new ConcurrentSearchNode<>(numThread, state);

			ConcurrentSearchNode<STATE> result = concurrentSearchNodes.putIfAbsent(state, node);

			return result == null ? node : result;
		} else
			return node;
	}

	public ConcurrentStackEntry<STATE> newStackEntry(Collection<ConcurrentSearchNode<STATE>> nodes,
			ConcurrentSearchNode<STATE> searchNode) {
		return new ConcurrentStackEntry<STATE>(nodes, searchNode);
	}
}
