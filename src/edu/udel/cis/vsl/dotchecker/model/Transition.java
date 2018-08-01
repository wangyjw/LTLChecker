package edu.udel.cis.vsl.dotchecker.model;

import java.util.Set;

/**
 * A Model {@link Transition} is wrapper of {@link DotAutomatonTransition}.
 * Executing a {@link Transition} means moving to the target
 * {@link DotAutomatonState} of the {@link DotAutomatonTransition}.
 * 
 * @author yanyihao
 *
 */
public class Transition {
	private DotAutomatonTransition transition;

	public Transition(DotAutomatonTransition transition) {
		this.transition = transition;
	}

	public DotAutomatonTransition getTransition() {
		return transition;
	}

	/**
	 * @return the set of ids of {@link DotAutomaton}s that contain this
	 *         {@link Transition}.
	 */
	public Set<Integer> getProcs() {
		return transition.getProcs();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Transition) {
			return transition.getLabel()
					.equals(((Transition) obj).transition.getLabel());
		} else
			return false;
	}
}
