package edu.udel.cis.vsl.dotchecker.model;

/**
 * An {@link AutomatonCounter} indicates which state an {@link DotAutomaton} is
 * at.
 * 
 * @author yanyihao
 *
 */
public class AutomatonCounter {
	/**
	 * The {@link DotAutomaton} this {@link AutomatonCounter} belongs to.
	 */
	private DotAutomaton dotAutomaton;

	/**
	 * The {@link DotAutomatonState} the {@code #dotAutomaton} is at.
	 */
	private DotAutomatonState dotAutomatonState;

	private static int NOT_HASHED = -1;

	private int hashcode = NOT_HASHED;

	public AutomatonCounter(DotAutomaton atomaton, DotAutomatonState state) {
		this.dotAutomaton = atomaton;
		this.dotAutomatonState = state;
	}

	public DotAutomatonState getDotAutomatonState() {
		return dotAutomatonState;
	}

	static AutomatonCounter newAutomatonCounter(DotAutomaton automaton,
			DotAutomatonState newState) {
		return new AutomatonCounter(automaton, newState);
	}

	public DotAutomaton getDotAutomaton() {
		return dotAutomaton;
	}

	/**
	 * The hash code of an {@link AutomatonCounter} is the string with format:
	 * {automatonId-automatonStateId}. Therefore, two {@link AutomatonCounter}s
	 * who have the same source {@link DotAutomaton} and at the same
	 * {@link DotAutomatonState} will have the same hash code.
	 */
	@Override
	public int hashCode() {
		if (hashcode != NOT_HASHED)
			return hashcode;

		StringBuilder sb = new StringBuilder();

		sb.append(dotAutomaton.getAutomatonId() + "-"
				+ dotAutomatonState.getAutomatonStateId());
		return sb.toString().hashCode();
	}

	/**
	 * Two {@link AutomatonCounter}s are the same iff their source
	 * {@link DotAutomaton}s have the same id, and also their location state (
	 * {@link #dotAutomatonState})s should also have the same id.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AutomatonCounter) {
			AutomatonCounter counter = (AutomatonCounter) obj;

			return counter.getDotAutomaton().getAutomatonId() == dotAutomaton
					.getAutomatonId()
					&& counter.getDotAutomatonState()
							.getAutomatonStateId() == dotAutomatonState
									.getAutomatonStateId();
		} else
			return false;
	}
}