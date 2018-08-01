package edu.udel.cis.vsl.dotchecker.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.udel.cis.vsl.dotchecker.util.Pair;

/**
 * The most important method of this class is compute the next {@link State}
 * given a {@link State} and a {@link Transition}.
 * 
 * @author yanyihao
 *
 */
public class ModelStateManager {
	/**
	 * This {@link #stateMap} is used to canonicalize {@link State}.
	 */
	private Map<State, State> stateMap = new ConcurrentHashMap<>();

	/**
	 * This {@link #countersMap} is used to canonicalize
	 * {@link AutomatonCounter}.
	 */
	private Map<AutomatonCounter, AutomatonCounter> countersMap = new ConcurrentHashMap<>();

	private ModelFactory modelFactory;

	public ModelStateManager(ModelFactory modelFactory) {
		this.modelFactory = modelFactory;
	}

	/**
	 * Given a {@link State} and a {@link Transition}, return the next
	 * {@link State}.
	 * 
	 * @param state
	 *            The source state.
	 * @param transition
	 *            The transition which leads to the next {@link State}.
	 * @return The next {@link State}.
	 */
	public State nextState(State state, Transition transition) {
		
		AutomatonCounter[] counters = state.getAutomatonCounters();
		int len = counters.length;
		AutomatonCounter[] newCounters = new AutomatonCounter[len];
		List<Pair<DotAutomatonState, DotAutomatonState>> source2Targets = transition
				.getTransition().getSource2Target();

		for (Pair<DotAutomatonState, DotAutomatonState> source2Target : source2Targets) {
			DotAutomatonState target = source2Target.right;
			DotAutomaton automaton = target.getAutomaton();
			int autoId = automaton.getAutomatonId();
			AutomatonCounter canonicalizedCounter = canonicalize(
					AutomatonCounter.newAutomatonCounter(automaton, target));

			newCounters[autoId] = canonicalizedCounter;
		}

		for (int i = 0; i < len; i++) {
			if (newCounters[i] == null)
				newCounters[i] = counters[i];
		}

		State newState = modelFactory.newState(newCounters);

		return canonicalize(newState);
	}

	/**
	 * Canonicalize a {@link State}. If two {@link State} have the same set of
	 * process / automata counters, they are canonically the same.
	 * 
	 * @param state
	 *            The {@link State} that is canonicalized.
	 * @return The canonicalized {@link State}.
	 */
	private State canonicalize(State state) {
		State canonicalizedState = stateMap.get(state);

		if (canonicalizedState == null) {
			canonicalizedState = stateMap.putIfAbsent(state, state);

			return canonicalizedState == null ? state : canonicalizedState;
		} else
			return canonicalizedState;
	}

	/**
	 * Canonicalize a {@link AutomatonCounter}. Two {@link AutomatonCounter}s
	 * who have the same source {@link DotAutomaton} and
	 * {@link DotAutomatonState} are canonically the same.
	 * 
	 * @param counter
	 *            The {@link AutomatonCounter} that is being canonicalized.
	 * @return the canonicalized {@link AutomatonCounter}.
	 */
	private AutomatonCounter canonicalize(AutomatonCounter counter) {
		AutomatonCounter canonicalizedCounter = countersMap.get(counter);

		if (canonicalizedCounter == null) {
			canonicalizedCounter = countersMap.putIfAbsent(counter, counter);

			return canonicalizedCounter == null
					? counter
					: canonicalizedCounter;
		} else
			return canonicalizedCounter;
	}

	/**
	 * Get the initial {@link State} of a {@link Model}.
	 * 
	 * @param model
	 *            The {@link Model} that you want to extract initial
	 *            {@link State} from.
	 * @return the initial {@link State} of a {@link Model}.
	 */
	public State getInitState(Model model) {
		LinkedList<DotAutomaton> dotAutomatons = model.getAutomata();
		int size = dotAutomatons.size();
		AutomatonCounter[] counters = new AutomatonCounter[size];
		int index = 0;
		for (DotAutomaton automaton : dotAutomatons) {
			DotAutomatonState next = automaton.getInitAutomatonState()
					.getOutGoingTransitions().get(0).getSource2Target()
					.get(index).right;
			AutomatonCounter nextCounter = canonicalize(
					AutomatonCounter.newAutomatonCounter(automaton, next));

			counters[index++] = nextCounter;
		}
		return canonicalize(modelFactory.newState(counters));
	}
}
