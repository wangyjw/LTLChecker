package edu.udel.cis.vsl.dotchecker.property;

import java.util.ArrayList;
import java.util.List;

/**
 * States in the {@link AbstractPropertyAutomaton}.
 * 
 * @author yanyihao
 *
 */
public class PropertyState {
	/**
	 * Each {@link PropertyState} will have a unique id.
	 */
	private int id;

	/**
	 * The list of outgoing edges that emanates from this {@link PropertyState}.
	 */
	private List<PropertyTransition> transitions;

	/**
	 * True iff this {@link PropertyState} is an accepting state.
	 */
	private boolean acceptingState;

	public PropertyState(int id, boolean accepting) {
		this.id = id;
		this.acceptingState = accepting;
		transitions = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<PropertyTransition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<PropertyTransition> transitions) {
		this.transitions = transitions;
	}

	public void addTransition(PropertyTransition transition) {
		this.transitions.add(transition);
	}

	public boolean isAcceptingState() {
		return acceptingState;
	}
}
