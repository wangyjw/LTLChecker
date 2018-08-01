package edu.udel.cis.vsl.dotchecker.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.udel.cis.vsl.dotchecker.util.Pair;
import edu.udel.cis.vsl.gmc.ltl.formula.LTLFormula;

/**
 * <p>
 * This transformer will collapse those transitions that do not appear in the
 * formula (also called "blank transitions").
 * </p>
 * 
 * <p>
 * In other words, multiple consecutive "blank transitions" can be reduced to a
 * single one.
 * </p>
 * 
 * @author yanyihao
 *
 */
public class CollapseBlankTransitionsTransformer
		extends
			AbstractModelTrasformer {
	/**
	 * {@link #propositionsInFormula} stores those propositions that are not in
	 * a given ltlformula.
	 */
	Set<String> propositionsInFormula = new HashSet<>();

	public CollapseBlankTransitionsTransformer(LTLFormula ltlformula) {
		propositionsInFormula.addAll(ltlformula.getPositivePredicates());
		propositionsInFormula.addAll(ltlformula.getNegativePredicate());
	}

	@Override
	public void transform(Model model) {
		List<DotAutomaton> automata = model.getAutomata();

		for (DotAutomaton automaton : automata) {
			transformAutomaton(automaton);
		}
	}

	private void transformAutomaton(DotAutomaton automaton) {
		DotAutomatonState successorOfInit = automaton.getInitAutomatonState()
				.getOutGoingTransitions().get(0).getSource2Target()
				.get(automaton.getAutomatonId()).right;

		Set<Integer> seen = new HashSet<>();

		dealWithStraightLine(successorOfInit, seen);
		seen.clear();
		dealWithABlankFollowedByABranchOfBlanks(successorOfInit, seen);
		seen.clear();
		dealWithMultipleBlankFollowedByASingleBlank(successorOfInit, seen);
	}

	/**
	 * A straight line of consecutive transitions all of which are neither synch
	 * nor in the formula can be collapsed into one blank transition.
	 * 
	 * @param state
	 * @param seen
	 */
	private void dealWithStraightLine(DotAutomatonState state,
			Set<Integer> seen) {
		if (seen.contains(state.getAutomatonStateId()))
			return;
		seen.add(state.getAutomatonStateId());

		List<DotAutomatonTransition> outgoingTransitions = state
				.getOutGoingTransitions();

		if (!oneBranchStateWithBlankTransition(state)) {
			for (DotAutomatonTransition dotTransition : outgoingTransitions) {
				List<Pair<DotAutomatonState, DotAutomatonState>> source2Targets = dotTransition
						.getSource2Target();

				for (Pair<DotAutomatonState, DotAutomatonState> source2Target : source2Targets)
					if (source2Target.left.getAutomatonStateId() == state
							.getAutomatonStateId())
						dealWithStraightLine(source2Target.right, seen);
			}
		} else {
			DotAutomatonTransition firstOutgoingTransition = state
					.getOutGoingTransitions().getFirst();

			while (true) {
				DotAutomatonState nextState = firstOutgoingTransition
						.getSource2Target().get(0).right;

				if (nextState.getAutomatonStateId() == state
						.getAutomatonStateId())
					return;

				if (oneBranchStateWithBlankTransition(nextState)) {
					List<DotAutomatonTransition> nextIncoming = nextState
							.getIncomingTransitions();
					DotAutomatonTransition nextFirstOutgoing = nextState
							.getOutGoingTransitions().getFirst();
					DotAutomatonState nextNextState = nextFirstOutgoing
							.getSource2Target().get(0).right;

					firstOutgoingTransition.getSource2Target()
							.get(0).right = nextNextState;
					nextNextState
							.addIncomingTransitions(firstOutgoingTransition);
					nextNextState.removeIncomingTransition(nextFirstOutgoing);
					for (DotAutomatonTransition nextIncomingTransition : nextIncoming) {
						List<Pair<DotAutomatonState, DotAutomatonState>> source2Targets = nextIncomingTransition
								.getSource2Target();

						for (Pair<DotAutomatonState, DotAutomatonState> source2Target : source2Targets) {
							if (source2Target.right
									.getAutomatonStateId() == nextState
											.getAutomatonStateId()) {
								source2Target.right = state;
							}
						}
						state.addIncomingTransitions(nextIncomingTransition);
					}
				} else {
					dealWithStraightLine(nextState, seen);
					return;
				}
			}
		}
	}

	/**
	 * when a blank transition followed by a state with multiple outgoing edges
	 * all of which are blank, then this transition can be removed.
	 * 
	 * @param state
	 * @param seen
	 */
	private void dealWithABlankFollowedByABranchOfBlanks(
			DotAutomatonState state, Set<Integer> seen) {
		if (seen.contains(state.getAutomatonStateId()))
			return;
		seen.add(state.getAutomatonStateId());

		List<DotAutomatonTransition> outgoingTransitions = state
				.getOutGoingTransitions();

		if (oneBranchStateWithBlankTransition(state)) {
			DotAutomatonTransition firstOutGoingTransition = state
					.getOutGoingTransitions().getFirst();
			DotAutomatonState next = firstOutGoingTransition.getSource2Target()
					.get(0).right;

			if (stateWithMultipleBlankTransitions(next)) {
				List<DotAutomatonTransition> currentIncomingTransition = state
						.getIncomingTransitions();

				for (DotAutomatonTransition incomingTransition : currentIncomingTransition) {
					List<Pair<DotAutomatonState, DotAutomatonState>> source2Targets = incomingTransition
							.getSource2Target();

					for (Pair<DotAutomatonState, DotAutomatonState> source2Target : source2Targets) {
						if (source2Target.right.getAutomatonStateId() == state
								.getAutomatonStateId()) {
							source2Target.right = next;
							break;
						}
					}
					next.addIncomingTransitions(incomingTransition);
				}
				next.removeIncomingTransition(firstOutGoingTransition);
			}
			dealWithABlankFollowedByABranchOfBlanks(next, seen);
		} else {
			for (DotAutomatonTransition dotTransition : outgoingTransitions) {
				List<Pair<DotAutomatonState, DotAutomatonState>> source2Targets = dotTransition
						.getSource2Target();

				for (Pair<DotAutomatonState, DotAutomatonState> source2Target : source2Targets)
					if (source2Target.left.getAutomatonStateId() == state
							.getAutomatonStateId())
						dealWithABlankFollowedByABranchOfBlanks(
								source2Target.right, seen);
			}
		}
	}

	/**
	 * When a state has multiple outgoing edges all of which are blank
	 * transitions. Collapse each of its branch if we could.
	 * 
	 * @param state
	 * @param seen
	 */
	private void dealWithMultipleBlankFollowedByASingleBlank(
			DotAutomatonState state, Set<Integer> seen) {
		if (seen.contains(state.getAutomatonStateId()))
			return;
		seen.add(state.getAutomatonStateId());

		List<DotAutomatonTransition> outgoingTransitions = state
				.getOutGoingTransitions();

		if (stateWithMultipleBlankTransitions(state)) {
			for (DotAutomatonTransition outgoingTransition : outgoingTransitions) {
				DotAutomatonState next = outgoingTransition.getSource2Target()
						.get(0).right;

				if (oneBranchStateWithBlankTransition(next)) {
					DotAutomatonTransition nextFirstOutgoingTransition = next
							.getOutGoingTransitions().getFirst();
					DotAutomatonState nextNext = nextFirstOutgoingTransition
							.getSource2Target().get(0).right;

					outgoingTransition.getSource2Target()
							.get(0).right = nextNext;
					nextNext.addIncomingTransitions(outgoingTransition);
					nextNext.removeIncomingTransition(
							nextFirstOutgoingTransition);
					dealWithMultipleBlankFollowedByASingleBlank(nextNext, seen);
				} else
					dealWithMultipleBlankFollowedByASingleBlank(next, seen);
			}
		} else {
			for (DotAutomatonTransition outgoingTransition : outgoingTransitions) {
				List<Pair<DotAutomatonState, DotAutomatonState>> source2Targets = outgoingTransition
						.getSource2Target();

				for (Pair<DotAutomatonState, DotAutomatonState> source2Target : source2Targets)
					if (source2Target.left.getAutomatonStateId() == state
							.getAutomatonStateId())
						dealWithMultipleBlankFollowedByASingleBlank(
								source2Target.right, seen);
			}
		}
	}

	/**
	 * Blank transition is a transition that is neither synch nor in the
	 * formula.
	 * 
	 * @param state
	 * @return
	 */
	private boolean oneBranchStateWithBlankTransition(DotAutomatonState state) {
		List<DotAutomatonTransition> outgoingTransitions = state
				.getOutGoingTransitions();

		if (outgoingTransitions.size() > 1)
			return false;
		else {
			DotAutomatonTransition transition = outgoingTransitions.get(0);

			if (transition.isSynchronized()
					|| propositionsInFormula.contains(transition.getLabel()))
				return false;
		}
		return true;
	}

	private boolean stateWithMultipleBlankTransitions(DotAutomatonState state) {
		List<DotAutomatonTransition> outgoingTransitions = state
				.getOutGoingTransitions();

		if (outgoingTransitions.size() < 2)
			return false;

		for (DotAutomatonTransition transition : outgoingTransitions) {
			if (sychOrInFormula(transition))
				return false;
		}
		return true;
	}

	private boolean sychOrInFormula(DotAutomatonTransition transition) {
		String label = transition.getLabel();

		return propositionsInFormula.contains(label)
				|| transition.isSynchronized();
	}
}
