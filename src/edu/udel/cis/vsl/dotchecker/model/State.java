package edu.udel.cis.vsl.dotchecker.model;

/**
 * A Model {@link State} consists of a set of process / Automaton counters. Each
 * Automaton counter will represent the process / Automaton location.
 * 
 * @author yanyihao
 *
 */
public class State {
	/**
	 * An array of automaton counters, each automaton is indexed by their id.
	 */
	private AutomatonCounter[] automatonCounters;

	private static int NOT_HASHED = -1;

	private int hashcode = NOT_HASHED;

	public State() {
	}

	public State(AutomatonCounter[] counters) {
		this.automatonCounters = counters;
		// this.predicate = predicate;
	}

	public AutomatonCounter[] getAutomatonCounters() {
		return automatonCounters;
	}

	@Override
	public int hashCode() {
		if (hashcode != NOT_HASHED)
			return hashcode;

		StringBuilder sb = new StringBuilder();
		int size = automatonCounters.length;

		for (int i = 0; i < size; i++) {
			sb.append(automatonCounters[i].getDotAutomatonState()
					.getAutomatonStateId() + "-");

			// if (i != size - 1)
			// sb.append("-");
		}
		// sb.append(predicate);
		return (hashcode = sb.toString().hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof State) {
			State s = (State) obj;
			AutomatonCounter[] counters = s.getAutomatonCounters();
			int l = counters.length;
			int len = automatonCounters.length;

			if (l == len) {
				for (int i = 0; i < len; i++) {
					if (!(counters[i].getDotAutomatonState()
							.getAutomatonStateId() == automatonCounters[i]
									.getDotAutomatonState()
									.getAutomatonStateId()))
						return false;
				}
				return true;
			} else
				return false;
		} else
			return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int size = automatonCounters.length;

		for (int i = 0; i < size; i++) {
			if (i != size - 1)
				sb.append(automatonCounters[i].getDotAutomatonState()
						.getAutomatonStateId() + "-");
			else
				sb.append(automatonCounters[i].getDotAutomatonState()
						.getAutomatonStateId());

		}
		return sb.toString();
	}
}
