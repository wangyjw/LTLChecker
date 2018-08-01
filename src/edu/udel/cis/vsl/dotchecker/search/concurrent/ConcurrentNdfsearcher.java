package edu.udel.cis.vsl.dotchecker.search.concurrent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import edu.udel.cis.vsl.gmc.ltl.NdfsStateManagerIF;

public class ConcurrentNdfsearcher<STATE, TRANSITION> {
	private ConcurrentEnablerIF<STATE, TRANSITION> enabler;

	private NdfsStateManagerIF<STATE, TRANSITION> stateManager;

	private ConcurrentNodeFactory<STATE, TRANSITION> concurrentNodeFactory;

	private int threadNum;

	private volatile boolean acceptingCycleFound = false;

	public ConcurrentNdfsearcher(ConcurrentEnablerIF<STATE, TRANSITION> enabler,
			NdfsStateManagerIF<STATE, TRANSITION> stateManager, int threadNum) {
		this.enabler = enabler;
		this.stateManager = stateManager;
		this.concurrentNodeFactory = new ConcurrentNodeFactory<>(threadNum);
		this.threadNum = threadNum;
	}

	public boolean search(STATE initState) {
		Thread[] threads = new Thread[threadNum];

		for (int i = 0; i < threadNum; i++) {
			Thread t = new Thread(new SearchTask(i, initState));

			threads[i] = t;
			t.start();
		}
		try {
			for (int i = 0; i < threadNum; i++)
				threads[i].join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return acceptingCycleFound;
	}

	class SearchTask implements Runnable {
		private int threadId;

		private Stack<ConcurrentStackEntry<STATE>> blueStack = new Stack<>();

		private Stack<ConcurrentStackEntry<STATE>> redStack = new Stack<>();

		public SearchTask(int threadId, STATE initState) {
			ConcurrentSearchNode<STATE> searchNode = concurrentNodeFactory
					.getSearchNode(initState);
			List<TRANSITION> fullSet = enabler.fullSet(initState);
			Collection<ConcurrentSearchNode<STATE>> successors = randomizedSuccessors(
					initState, fullSet);
			ConcurrentStackEntry<STATE> stackEntry = concurrentNodeFactory
					.newStackEntry(successors, searchNode);

			searchNode.seen = true;
			this.threadId = threadId;
			blueStack.push(stackEntry);
			searchNode.cyan[threadId] = true;
		}

		@Override
		public void run() {
			while (blueProceedToNewState()) {
			}
		}

		private boolean blueProceedToNewState() {
			// System.out.println("thread " + threadId + " running ...");

			if (acceptingCycleFound)
				return false;

			while (!blueStack.isEmpty()) {
				ConcurrentStackEntry<STATE> stackEntry = blueStack.peek();
				ConcurrentSearchNode<STATE> currentNode = stackEntry.searchNode;
				STATE currentState = currentNode.sourceState;

				while (stackEntry.hasNext()) {
					ConcurrentSearchNode<STATE> newSearchNode = stackEntry
							.next();
					STATE newState = newSearchNode.sourceState;

					newSearchNode.seen = true;
					if (!newSearchNode.cyan[threadId] && !newSearchNode.blue) {
						List<TRANSITION> fullSet = enabler.fullSet(newState);
						Collection<ConcurrentSearchNode<STATE>> successors = randomizedSuccessors(
								newState, fullSet);
						ConcurrentStackEntry<STATE> newStackEntry = concurrentNodeFactory
								.newStackEntry(successors, newSearchNode);

						blueStack.push(newStackEntry);
						newSearchNode.cyan[threadId] = true;
						return true;
					}
				}
				currentNode.blue = true;
				if (stateManager.accepting(currentState)) {
					Set<ConcurrentSearchNode<STATE>> reachable = new HashSet<>();
					Set<ConcurrentSearchNode<STATE>> reachableAccepting = new HashSet<>();

					reachable.add(currentNode);
					redStack.push(concurrentNodeFactory
							.newStackEntry(stackEntry.nodes, currentNode));
					redDfs(reachable, reachableAccepting);
					while (!allRed(reachableAccepting)) {
					}
					setRed(reachable);
				}
				currentNode.cyan[threadId] = false;
				blueStack.pop();
			}

			return false;
		}

		private void redDfs(Set<ConcurrentSearchNode<STATE>> reachable,
				Set<ConcurrentSearchNode<STATE>> reachableAccepting) {
			// System.out.println("red search start");
			while (redProceedToNewState(reachable, reachableAccepting)) {
			}
			// System.out.println("red search end");
		}

		private boolean allRed(Set<ConcurrentSearchNode<STATE>> set) {
			for (ConcurrentSearchNode<STATE> node : set) {
				if (!node.red)
					return false;
			}

			return true;
		}

		private void setRed(Set<ConcurrentSearchNode<STATE>> set) {
			for (ConcurrentSearchNode<STATE> node : set) {
				node.red = true;
			}
		}

		private boolean redProceedToNewState(
				Set<ConcurrentSearchNode<STATE>> reachable,
				Set<ConcurrentSearchNode<STATE>> reachableAccepting) {
			if (acceptingCycleFound)
				return false;
			while (!redStack.isEmpty()) {
				ConcurrentStackEntry<STATE> stackEntry = redStack.peek();

				while (stackEntry.hasNext()) {
					ConcurrentSearchNode<STATE> newSearchNode = stackEntry
							.next();
					STATE newState = newSearchNode.sourceState;

					if (newSearchNode.cyan[threadId]) {
						acceptingCycleFound = true;
						return false;
					}
					if (!reachable.contains(newSearchNode)
							&& !newSearchNode.red) {
						reachable.add(newSearchNode);
						if (stateManager.accepting(newState))
							reachableAccepting.add(newSearchNode);

						List<TRANSITION> transitions = enabler
								.fullSet(newState);
						Collection<ConcurrentSearchNode<STATE>> successors = randomizedSuccessors(
								newState, transitions);
						ConcurrentStackEntry<STATE> newStackEntry = concurrentNodeFactory
								.newStackEntry(successors, newSearchNode);

						redStack.push(newStackEntry);
						return true;
					}
				}
				redStack.pop();
			}

			return false;
		}

		private Collection<ConcurrentSearchNode<STATE>> randomizedSuccessors(
				STATE state, Collection<TRANSITION> fullSet) {
			List<ConcurrentSearchNode<STATE>> seen = new LinkedList<>();
			List<ConcurrentSearchNode<STATE>> notSeen = new LinkedList<>();
			Collection<ConcurrentSearchNode<STATE>> result = new LinkedList<>();

			for (TRANSITION transition : fullSet) {
				STATE newState = stateManager.nextState(state, transition);
				ConcurrentSearchNode<STATE> newNode = concurrentNodeFactory
						.getSearchNode(newState);

				if (newNode.seen) {
					// System.out.println("seen");
					seen.add(newNode);
				} else {
					// System.out.println("not seen");
					notSeen.add(newNode);
				}
			}
			Collections.shuffle(seen);
			Collections.shuffle(notSeen);
			result.addAll(notSeen);
			result.addAll(seen);

			return result;
		}
	}
}
