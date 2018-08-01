package edu.udel.cis.vsl.dotchecker.model;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.udel.cis.vsl.dotchecker.util.Pair;

/**
 * This is the model of the RERS parallel systems. Each model will consist of
 * multiple processes / {@link DotAutomaton}s. Also model will keep a record of
 * synchronized transitions.
 * 
 * @author yanyihao
 */
public class Model {
	/**
	 * The list of processes or {@link DotAutomaton}s.
	 */
	private LinkedList<DotAutomaton> automata = new LinkedList<>();

	/**
	 * {@link #synchornizedTransitions} stores all the labels of synchronized
	 * transitions.
	 */
	private Set<String> synchornizedTransitions;

	private Set<String> transitions;

	/**
	 * a new replacement for {@link #synchornizedTransitions}
	 */
	private Map<String, Pair<Integer, Integer>> newSynchTransitions = new HashMap<>();

	public void addAutomaton(DotAutomaton automaton) {
		automata.add(automaton);
	}

	public LinkedList<DotAutomaton> getAutomata() {
		return automata;
	}

	public Set<String> getSynchornizedTransitions() {
		return synchornizedTransitions;
	}

	public void setSynchornizedTransitions(
			Set<String> synchornizedTransitions) {
		this.synchornizedTransitions = synchornizedTransitions;
	}

	public Set<String> getTransitions() {
		return transitions;
	}

	public void setTransitions(Set<String> transitions) {
		this.transitions = transitions;
	}

	public Map<String, Pair<Integer, Integer>> getNewSynchTransitions() {
		return newSynchTransitions;
	}

	public void addSynchTransitions(String transition,
			Pair<Integer, Integer> pair) {
		newSynchTransitions.put(transition, pair);
	}

	/**
	 * Print each process / {@link DotAutomaton} of this {@link Model} one by
	 * one. And each process / {@link DotAutomaton} will be printed in a breath
	 * first manner.
	 * 
	 * @param out
	 */
	public void print(PrintStream out) {
		Set<Integer> seen = new HashSet<>();

		for (DotAutomaton automaton : automata) {
			LinkedList<DotAutomatonState> queue = new LinkedList<>();

			queue.add(automaton.getInitAutomatonState());
			out.println("automaton" + automaton.getAutomatonId());
			while (!queue.isEmpty()) {
				DotAutomatonState state = queue.pop();
				seen.add(state.getAutomatonStateId());
				List<DotAutomatonTransition> transitions = state
						.getOutGoingTransitions();

				for (DotAutomatonTransition transition : transitions) {
					List<Pair<DotAutomatonState, DotAutomatonState>> source2Targets = transition
							.getSource2Target();
					int size = source2Targets.size();
					DotAutomatonState newState;

					if (size > 1 && !transition.getLabel().equals("")) {
						out.print("synchronized ");

						if (source2Targets.get(0).left == state)
							newState = source2Targets.get(0).right;
						else
							newState = source2Targets.get(1).right;
					} else if (transition.getLabel().equals("")) {
						newState = source2Targets
								.get(automaton.getAutomatonId()).right;
					} else
						newState = source2Targets.get(0).right;

					out.print("state " + state.getAutomatonStateId());
					out.print("-->" + transition.getLabel());
					out.println("-->" + newState.getAutomatonStateId());
					if (!seen.contains(newState.getAutomatonStateId()))
						queue.add(newState);
				}
			}
		}
	}
}
