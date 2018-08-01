package edu.udel.cis.vsl.dotchecker.model;

import java.util.LinkedList;

/**
 * A {@link ModelFactory} is used to create new instances of objects used in
 * {@link Model}.
 * 
 * @author yanyihao
 *
 */
public class ModelFactory {

	/**
	 * Construct a new {@link DotAutomaton}.
	 * 
	 * @param id
	 *            The unique id of the {@link DotAutomaton}.
	 * @return the newly constructed {@link DotAutomaton}.
	 */
	DotAutomaton newDotAutomaton(int id) {
		return new DotAutomaton(id);
	}

	/**
	 * Construct a new {@link DotAutomatonState}.
	 * 
	 * @param id
	 *            The unique id of this {@link DotAutomatonState}.
	 * @param dotAutomaton
	 *            The {@link DotAutomaton} that the new
	 *            {@link DotAutomatonState} resides in.
	 * @return the newly constructed {@link DotAutomatonState}.
	 */
	DotAutomatonState newDotAutomatonState(int id, DotAutomaton dotAutomaton) {
		return new DotAutomatonState(id, dotAutomaton);
	}

	/**
	 * Construct a new {@link DotAutomatonTransition}.
	 * 
	 * @param label
	 *            The unique label of a {@link DotAutomatonTransition}.
	 * @return the newly constructed {@link DotAutomatonTransition}.
	 */
	DotAutomatonTransition newDotAutomatonTransition(String label) {
		return new DotAutomatonTransition(label);
	}

	/**
	 * Construct a new model {@link Transition} which is just a wrapper of
	 * {@link DotAutomatonTransition}.
	 * 
	 * @param autoTransition
	 * @return the newly constructed {@link Transition}.
	 */
	public Transition newTransition(DotAutomatonTransition autoTransition) {
		return new Transition(autoTransition);
	}

	/**
	 * Construct a {@link TransitionSequence} which is sequence of model
	 * {@link Transition}.
	 * 
	 * @param transitions
	 *            The list of transitions that are included in the
	 *            {@link TransitionSequence}.
	 * @return the newly constructed {@link TransitionSequence}.
	 */
	TransitionSequence newTransitionSequence(
			LinkedList<Transition> transitions) {
		return new TransitionSequence(transitions);
	}

	/**
	 * Construct a new model {@link State}.
	 * 
	 * @param counters
	 * @return the newly constructed model {@link State}.
	 */
	State newState(AutomatonCounter[] counters) {
		return new State(counters);
	}
}
