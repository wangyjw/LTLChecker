package edu.udel.cis.vsl.dotchecker.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.udel.cis.vsl.gmc.ltl.formula.LTLFormula;

/**
 * A {@link ModelEnabler} tells you what {@link DotAutomatonTransition} should
 * be explored from a {@link DotAutomatonState}. The key methods include
 * {@link #ampleSet(State, LTLFormula)},
 * {@link #ampleSetCompliment(State, LTLFormula)}.
 * 
 * @author yanyihao
 *
 */
public class ModelEnabler {
	private ModelFactory modelFactory;

	private static final int CONTAIN_TRANSITION_IN_FORMULA = 1;
	private static final int CONTAIN_SYNC_TRANSITION = 1;

	public ModelEnabler(ModelFactory modelFactory) {
		this.modelFactory = modelFactory;
	}

	/**
	 * Return transitions for all processes, and transitions for all processes
	 * will be put into a separate list.
	 * 
	 * @param state
	 * @return
	 */
	public LinkedList<List<Transition>> transitionsForAllProcess(State state) {
		LinkedList<List<Transition>> result = new LinkedList<>();
		AutomatonCounter[] counters = state.getAutomatonCounters();
		Set<String> synchronizedTransitions = new HashSet<>();
		Map<String, Integer> syncTransitionsToProcessId = new HashMap<>();
		int len = counters.length;

		for (int i = 0; i < len; i++) {
			AutomatonCounter counter = counters[i];
			List<Transition> transitions = new LinkedList<>();
			LinkedList<DotAutomatonTransition> autoTransitions = counter
					.getDotAutomatonState().getOutGoingTransitions();

			for (DotAutomatonTransition t : autoTransitions) {
				String label = t.getLabel();

				if (t.isSynchronized()) {
					if (!synchronizedTransitions.contains(label)) {
						synchronizedTransitions.add(label);
						syncTransitionsToProcessId.put(label, i);
						continue;
					} else {
						result.get(syncTransitionsToProcessId.get(label))
								.add(modelFactory.newTransition(t));
					}
				}
				transitions.add(modelFactory.newTransition(t));
			}

			result.add(transitions);
		}

		return result;
	}

	/**
	 * Get transitions for a single automaton/process. This implementation may
	 * not be right, since some processes will miss sync transition, but this
	 * function is fine using in the full set.
	 * 
	 * @param counter
	 * @param synchronizedTransitions
	 * @return
	 */
	private List<Transition> getTransitionFromAutomaton(
			AutomatonCounter counter, Set<String> synchronizedTransitions) {
		List<Transition> transitions = new LinkedList<>();
		LinkedList<DotAutomatonTransition> autoTransitions = counter
				.getDotAutomatonState().getOutGoingTransitions();

		for (DotAutomatonTransition t : autoTransitions) {
			String label = t.getLabel();

			if (t.isSynchronized()
					&& !synchronizedTransitions.contains(label)) {
				synchronizedTransitions.add(label);
				continue;
			}
			transitions.add(modelFactory.newTransition(t));
		}

		return transitions;
	}

	public TransitionSequence fullSet(State state) {
		LinkedList<Transition> transitions = new LinkedList<>();
		AutomatonCounter[] counters = state.getAutomatonCounters();
		Set<String> synchronizedTransitions = new HashSet<>();

		for (AutomatonCounter counter : counters) {
			transitions.addAll(getTransitionFromAutomaton(counter,
					synchronizedTransitions));
		}
		return modelFactory.newTransitionSequence(transitions);
	}

	public TransitionSequence ampleSet(State state, LTLFormula formula) {
		Set<String> propositions = new HashSet<>();

		propositions.addAll(formula.getNegativePredicate());
		propositions.addAll(formula.getPositivePredicates());

		AutomatonCounter[] counters = state.getAutomatonCounters();
		int len = counters.length;

		// whether a process contains a sync outgoing edge
		int[] containTransitionsInFormula = new int[len];
		int[] containSyncTransition = new int[len];
		Map<String, Integer> synchronizedTransitions = new HashMap<>();
		List<LinkedList<Transition>> transitionsForProcesses = new LinkedList<>();
		Map<Integer, Set<Integer>> syncPairs = new HashMap<>();
		LinkedList<Transition> fullSet = new LinkedList<>();

		for (int i = 0; i < len; i++) {
			AutomatonCounter counter = counters[i];
			LinkedList<DotAutomatonTransition> autoTransitions = counter
					.getDotAutomatonState().getOutGoingTransitions();
			LinkedList<Transition> modelTransitions = new LinkedList<>();

			for (DotAutomatonTransition t : autoTransitions) {
				String label = t.getLabel();

				if (t.isSynchronized()) {
					containSyncTransition[i] = CONTAIN_SYNC_TRANSITION;
					Set<Integer> procs = t.getProcs();

					if (syncPairs.containsKey(i)) {
						for (int j : procs)
							if (j != i)
								syncPairs.get(i).add(j);
					} else {
						Set<Integer> dependent = new HashSet<>();

						for (int j : procs)
							if (j != i)
								dependent.add(j);
						syncPairs.put(i, dependent);
					}
					if (!synchronizedTransitions.containsKey(label)) {
						synchronizedTransitions.put(label, i);
						continue;
					} else {
						int index = synchronizedTransitions.get(label);

						transitionsForProcesses.get(index)
								.add(modelFactory.newTransition(t));
						if (propositions.contains(label))
							containTransitionsInFormula[index] = CONTAIN_TRANSITION_IN_FORMULA;
					}
				}
				Transition newT = modelFactory.newTransition(t);
				modelTransitions.add(newT);
				fullSet.add(newT);
				if (propositions.contains(label))
					containTransitionsInFormula[i] = CONTAIN_TRANSITION_IN_FORMULA;
			}
			transitionsForProcesses.add(modelTransitions);
		}
		// look for a process that contains neither involved actions nor sync
		// actions.
		for (int i = 0; i < len; i++) {
			if (containSyncTransition[i] != CONTAIN_SYNC_TRANSITION
					&& containTransitionsInFormula[i] != CONTAIN_TRANSITION_IN_FORMULA) {
				LinkedList<Transition> ampleSet = transitionsForProcesses
						.get(i);
				// System.out.println("ample set");

				if (!ampleSet.isEmpty()) {
					return modelFactory.newTransitionSequence(ampleSet);
				}
			}
		}
		LinkedList<Transition> ampleSet = new LinkedList<>();
		// look for a set of processes that don't contain any involved actions
		// and synchronized together on a certain synch action.
		Set<Integer> procIds = new HashSet<>();
		for (int i = 0; i < len; i++) {
			if (containTransitionsInFormula[i] == CONTAIN_TRANSITION_IN_FORMULA
					|| transitionsForProcesses.get(i).isEmpty())
				continue;

			LinkedList<Integer> queue = new LinkedList<>();

			queue.add(i);
			procIds.add(i);
			boolean involveTransitionInFormula = false;
			while (!queue.isEmpty()) {
				int procId = queue.pop();
				Set<Integer> dependents = syncPairs.get(procId);

				if (dependents != null)
					for (int id : dependents) {
						if (!procIds.contains(id)) {
							if (containTransitionsInFormula[id] == CONTAIN_TRANSITION_IN_FORMULA) {
								procIds.clear();
								involveTransitionInFormula = true;
								break;
							} else {
								procIds.add(id);
								queue.add(id);
							}
						}
					}
				if (involveTransitionInFormula)
					break;
			}
			if (!procIds.isEmpty())
				break;
		}
		if (procIds.isEmpty()) {
			// System.out.println("ff set");
			return modelFactory.newTransitionSequence(fullSet);
		} else {
			for (int i : procIds) {
				ampleSet.addAll(transitionsForProcesses.get(i));
			}
		}

		// System.out.println("aa set");
		return modelFactory.newTransitionSequence(ampleSet);
	}

	public TransitionSequence ampleSetCompliment2(State state,
			LTLFormula formula) {
		Set<String> propositions = new HashSet<>();

		propositions.addAll(formula.getNegativePredicate());
		propositions.addAll(formula.getPositivePredicates());

		LinkedList<Transition> ampleSet = new LinkedList<>();
		AutomatonCounter[] counters = state.getAutomatonCounters();
		int len = counters.length;
		int[] containTransitionsInFormula = new int[len];
		int[] containPartialEnabledTransition = new int[len];
		int[] containSyncTransition = new int[len];
		Map<String, Integer> synchronizedTransitions = new HashMap<>();
		List<LinkedList<Transition>> transitionsForProcesses = new LinkedList<>();
		Map<Integer, Set<Integer>> syncPairs = new HashMap<>();
		LinkedList<Transition> fullSet = new LinkedList<>();

		for (int i = 0; i < len; i++) {
			AutomatonCounter counter = counters[i];
			LinkedList<DotAutomatonTransition> autoTransitions = counter
					.getDotAutomatonState().getOutGoingTransitions();
			LinkedList<Transition> modelTransitions = new LinkedList<>();

			for (DotAutomatonTransition t : autoTransitions) {
				String label = t.getLabel();

				if (t.isSynchronized()) {
					containSyncTransition[i] = CONTAIN_SYNC_TRANSITION;
					if (!synchronizedTransitions.containsKey(label)) {
						synchronizedTransitions.put(label, i);
						containPartialEnabledTransition[i]++;
						continue;
					} else {
						int index = synchronizedTransitions.get(label);

						if (syncPairs.containsKey(index)) {
							syncPairs.get(index).add(i);
						} else {
							Set<Integer> newSet = new HashSet<>();

							newSet.add(i);
							syncPairs.put(index, newSet);
						}
						containPartialEnabledTransition[index]--;
						transitionsForProcesses.get(index)
								.add(modelFactory.newTransition(t));

						if (propositions.contains(label))
							containTransitionsInFormula[index] = CONTAIN_TRANSITION_IN_FORMULA;
					}
				}
				Transition newT = modelFactory.newTransition(t);
				modelTransitions.add(newT);
				fullSet.add(newT);
				if (propositions.contains(label))
					containTransitionsInFormula[i] = CONTAIN_TRANSITION_IN_FORMULA;
			}
			transitionsForProcesses.add(modelTransitions);
		}

		for (int i = 0; i < len; i++) {
			LinkedList<Transition> transitionsForProcess = transitionsForProcesses
					.get(i);

			if (containTransitionsInFormula[i] == CONTAIN_TRANSITION_IN_FORMULA
					|| containPartialEnabledTransition[i] > 0)
				continue;

			if (containSyncTransition[i] != CONTAIN_SYNC_TRANSITION) {
				ampleSet.addAll(transitionsForProcess);
				break;
			} else {
				LinkedList<Integer> queue = new LinkedList<>();
				boolean isvalid = true;

				queue.add(i);
				while (!queue.isEmpty()) {
					int index = queue.pop();

					if (containTransitionsInFormula[i] == CONTAIN_TRANSITION_IN_FORMULA
							|| containPartialEnabledTransition[i] > 0) {
						ampleSet.clear();
						isvalid = false;
						break;
					}
					ampleSet.addAll(transitionsForProcesses.get(index));
					if (syncPairs.get(index) != null) {
						queue.addAll(syncPairs.get(index));
					}
				}
				if (isvalid)
					break;
			}
		}

		if (ampleSet.size() == 0)
			return modelFactory.newTransitionSequence(ampleSet);

		LinkedList<Transition> ampleSetComplement = new LinkedList<>();

		for (Transition tInFull : fullSet) {
			boolean inAmple = false;

			for (Transition tInAmple : ampleSet) {
				if (tInFull.equals(tInAmple)) {
					inAmple = true;
					break;
				}
			}
			if (!inAmple)
				ampleSetComplement.add(tInFull);
		}
		return modelFactory.newTransitionSequence(ampleSetComplement);
	}

	public TransitionSequence ampleSetCompliment(State state,
			LTLFormula formula) {
		Set<String> propositions = new HashSet<>();

		propositions.addAll(formula.getNegativePredicate());
		propositions.addAll(formula.getPositivePredicates());

		AutomatonCounter[] counters = state.getAutomatonCounters();
		int len = counters.length;
		// whether a process contains a sync outgoing edge
		int[] containTransitionsInFormula = new int[len];
		int[] containSyncTransition = new int[len];
		Map<String, Integer> synchronizedTransitions = new HashMap<>();
		List<LinkedList<Transition>> transitionsForProcesses = new LinkedList<>();
		Map<Integer, Set<Integer>> syncPairs = new HashMap<>();
		LinkedList<Transition> fullSet = new LinkedList<>();
		LinkedList<Transition> ampleSet = new LinkedList<>();

		for (int i = 0; i < len; i++) {
			AutomatonCounter counter = counters[i];
			LinkedList<DotAutomatonTransition> autoTransitions = counter
					.getDotAutomatonState().getOutGoingTransitions();
			LinkedList<Transition> modelTransitions = new LinkedList<>();

			for (DotAutomatonTransition t : autoTransitions) {
				String label = t.getLabel();

				if (t.isSynchronized()) {
					containSyncTransition[i] = CONTAIN_SYNC_TRANSITION;
					Set<Integer> procs = t.getProcs();

					if (syncPairs.containsKey(i)) {
						for (int j : procs)
							if (j != i)
								syncPairs.get(i).add(j);
					} else {
						Set<Integer> dependent = new HashSet<>();

						for (int j : procs)
							if (j != i)
								dependent.add(j);
						syncPairs.put(i, dependent);
					}
					if (!synchronizedTransitions.containsKey(label)) {
						synchronizedTransitions.put(label, i);
						continue;
					} else {
						int index = synchronizedTransitions.get(label);

						transitionsForProcesses.get(index)
								.add(modelFactory.newTransition(t));
						if (propositions.contains(label))
							containTransitionsInFormula[index] = CONTAIN_TRANSITION_IN_FORMULA;
					}
				}
				Transition newT = modelFactory.newTransition(t);
				modelTransitions.add(newT);
				fullSet.add(newT);
				if (propositions.contains(label))
					containTransitionsInFormula[i] = CONTAIN_TRANSITION_IN_FORMULA;
			}
			transitionsForProcesses.add(modelTransitions);
		}
		// look for a process that contains neither involved actions nor sync
		// actions.
		for (int i = 0; i < len; i++) {
			if (containSyncTransition[i] != CONTAIN_SYNC_TRANSITION
					&& containTransitionsInFormula[i] != CONTAIN_TRANSITION_IN_FORMULA
					&& !transitionsForProcesses.get(i).isEmpty()) {
				ampleSet.addAll(transitionsForProcesses.get(i));
				break;
			}
		}

		if (ampleSet.isEmpty()) {
			// look for a set of processes that don't contain any involved
			// actions
			// and synchronized together on a certain synch action.
			Set<Integer> procIds = new HashSet<>();
			for (int i = 0; i < len; i++) {
				if (containTransitionsInFormula[i] == CONTAIN_TRANSITION_IN_FORMULA
						|| transitionsForProcesses.get(i).isEmpty())
					continue;

				LinkedList<Integer> queue = new LinkedList<>();

				queue.add(i);
				procIds.add(i);
				boolean involveTransitionInFormula = false;
				while (!queue.isEmpty()) {
					int procId = queue.pop();
					Set<Integer> dependents = syncPairs.get(procId);

					if (dependents != null)
						for (int id : dependents) {
							if (!procIds.contains(id)) {
								if (containTransitionsInFormula[id] == CONTAIN_TRANSITION_IN_FORMULA) {
									procIds.clear();
									involveTransitionInFormula = true;
									break;
								} else {
									procIds.add(id);
									queue.add(id);
								}
							}
						}
					if (involveTransitionInFormula)
						break;
				}
				if (!procIds.isEmpty())
					break;
			}
			for (int i : procIds) {
				ampleSet.addAll(transitionsForProcesses.get(i));
			}
		}
		if (ampleSet.isEmpty())
			// return an empty sequence
			return modelFactory.newTransitionSequence(ampleSet);
		else {
			LinkedList<Transition> ampleSetComplement = new LinkedList<>();

			for (Transition tInFull : fullSet) {
				boolean inAmple = false;

				for (Transition tInAmple : ampleSet) {
					if (tInFull.equals(tInAmple)) {
						inAmple = true;
						break;
					}
				}
				if (!inAmple)
					ampleSetComplement.add(tInFull);
			}
			return modelFactory.newTransitionSequence(ampleSetComplement);
		}
	}
}
