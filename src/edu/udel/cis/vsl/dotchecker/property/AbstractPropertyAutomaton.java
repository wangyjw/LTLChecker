package edu.udel.cis.vsl.dotchecker.property;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Each Property (LTL formula) will be translated into an
 * {@link AbstractPropertyAutomaton}. Each {@link PropertyState} in the
 * {@link AbstractPropertyAutomaton} will have a unique id and the id of the
 * initial {@link PropertyState} is -1.
 * </p>
 * 
 * <p>
 * Each {@link PropertyState} will have edges emanating from itself.
 * </p>
 * 
 * @author yanyihao
 *
 */
public class AbstractPropertyAutomaton {
	/**
	 * The list that stores all the {@link PropertyState} in this
	 * {@link AbstractPropertyAutomaton}.
	 */
	private LinkedList<PropertyState> states;

	/**
	 * The initial {@link PropertyState} of this
	 * {@link AbstractPropertyAutomaton}.
	 */
	private PropertyState initState;

	public static final int INIT_STATE_ID = -1;

	AbstractPropertyAutomaton() {
		states = new LinkedList<>();
		addState(initState = new PropertyState(INIT_STATE_ID, false));
	}

	public void addState(PropertyState state) {
		states.add(state);
	}

	public LinkedList<PropertyState> getStates() {
		return states;
	}

	public PropertyState getInitState() {
		return initState;
	}

	/**
	 * This method is needed because the initial state of a property automaton
	 * is a trivial state.
	 * 
	 * @return The successor of the initial state of a property automaton.
	 */
	public PropertyState getSuccessorOfInitState() {
		List<PropertyTransition> propertyTransitions = initState
				.getTransitions();

		assert !propertyTransitions.isEmpty();
		return propertyTransitions.get(0).getTargetState();
	}
}
