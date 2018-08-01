package edu.udel.cis.vsl.dotchecker.model;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A {@link TransitionSequence} is a sequence of {@link Transition}s.
 * 
 * @author yanyihao
 *
 */
public class TransitionSequence {
	private LinkedList<Transition> transitions;

	public TransitionSequence() {
	}

	public TransitionSequence(LinkedList<Transition> ts) {
		transitions = ts;
	}

	public LinkedList<Transition> getTransitions() {
		return transitions;
	}

	public void addTransitions(Collection<Transition> ts) {
		transitions.addAll(ts);
	}
}
