package edu.udel.cis.vsl.dotchecker.model;

import java.util.LinkedList;

/**
 * State of {@link DotAutomaton}.
 * 
 * @author yanyihao
 *
 */
public class DotAutomatonState {
	/**
	 * Unique id of this {@link DotAutomatonState}.
	 */
	private int automatonStateId;

	/**
	 * The {@link DotAutomaton} which this {@link DotAutomatonState} resides.
	 */
	private DotAutomaton automaton;

	/**
	 * All outgoing transitions of this {@link DotAutomatonState}.
	 */
	private LinkedList<DotAutomatonTransition> outgoingTransitions;

	/**
	 * All incoming transitions of this {@link DotAutomatonState}.
	 */
	private LinkedList<DotAutomatonTransition> incomingTransition;

	public DotAutomatonState(int stateId, DotAutomaton automaton) {
		automatonStateId = stateId;
		outgoingTransitions = new LinkedList<>();
		incomingTransition = new LinkedList<>();
		this.automaton = automaton;
	}

	public DotAutomaton getAutomaton() {
		return automaton;
	}

	public int getAutomatonStateId() {
		return automatonStateId;
	}

	public LinkedList<DotAutomatonTransition> getOutGoingTransitions() {
		return outgoingTransitions;
	}

	void addOutGoingTransitions(DotAutomatonTransition transition) {
		outgoingTransitions.add(transition);
	}

	void addIncomingTransitions(DotAutomatonTransition transition) {
		incomingTransition.add(transition);
	}

	void removeOutGoingTransitions(DotAutomatonTransition transition) {
		outgoingTransitions.remove(transition);
	}

	void removeIncomingTransition(DotAutomatonTransition transition) {
		incomingTransition.remove(transition);
	}

	LinkedList<DotAutomatonTransition> getIncomingTransitions() {
		return incomingTransition;
	}
}
