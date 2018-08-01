package edu.udel.cis.vsl.dotchecker.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Each {@link DotAutomaton} represents a process of the concurrent system.
 * 
 * @author yanyihao
 *
 */
public class DotAutomaton {
	/**
	 * The unique id of the {@link DotAutomaton} which starts from 0.
	 */
	private int automatonId;

	/**
	 * The initial state of the {@link DotAutomaton}.
	 */
	private DotAutomatonState initAutomatonState;

	private Set<String> transitions = new HashSet<>();

	public DotAutomaton(int automatonId) {
		this.automatonId = automatonId;
	}

	public DotAutomaton(int automatonId, DotAutomatonState init) {
		this(automatonId);
		initAutomatonState = init;
	}

	public DotAutomatonState getInitAutomatonState() {
		return initAutomatonState;
	}

	public void setInitAutomatonState(DotAutomatonState automatonState) {
		this.initAutomatonState = automatonState;
	}

	public int getAutomatonId() {
		return automatonId;
	}

	public Set<String> getTransitions() {
		return transitions;
	}

	public void addTransition(String transition) {
		transitions.add(transition);
	}
}
