package edu.udel.cis.vsl.dotchecker.spinModelGenerator;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.udel.cis.vsl.dotchecker.model.DotAutomaton;
import edu.udel.cis.vsl.dotchecker.model.DotAutomatonState;
import edu.udel.cis.vsl.dotchecker.model.DotAutomatonTransition;
import edu.udel.cis.vsl.dotchecker.model.Model;
import edu.udel.cis.vsl.dotchecker.util.Pair;

public class RegularSpinModelGeneratorWithNextOp {
	private PrintStream out;

	public RegularSpinModelGeneratorWithNextOp(PrintStream out) {
		this.out = out;
	}

	public void generateSpinModel(Model model, String[] spinFormulas) {
		Set<String> actions = model.getTransitions();
		// Set<String> synchActions = model.getSynchornizedTransitions();
		Map<String, Pair<Integer, Integer>> synchActions = model
				.getNewSynchTransitions();
		LinkedList<DotAutomaton> dotAutomata = model.getAutomata();

		generateSpinModeForActions(actions);
		generateSpinModelForChannels(synchActions.values());
		generateLastactionStatement();
		for (DotAutomaton automaton : dotAutomata) {
			generateSpinModelForProcess(automaton, synchActions);
		}
		generateSpinModelForPredicateDefine(actions);
		addSpinFormula(spinFormulas);
	}

	private void addSpinFormula(String[] spinFormulas) {
		for (String spinFormula : spinFormulas) {
			out.print("\n" + spinFormula);
		}
	}

	private void generateLastactionStatement() {
		out.print("\n\n");
		out.print("mtype lastAction = nop;");
	}

	private void generateSpinModelForPredicateDefine(Set<String> actions) {
		out.print("\n\n");
		for (String action : actions) {
			if (!action.equals(""))
				out.print("#define p_" + action + " (lastAction == " + action
						+ ")\n");
		}
	}

	private void generateSpinModelForChannels(
			Collection<Pair<Integer, Integer>> pairs) {
		Set<String> visited = new HashSet<>();

		out.print("\n\n");
		for (Pair<Integer, Integer> pair : pairs) {
			String chanName = generateChannelFromSynchActionName(pair);

			if (visited.contains(chanName))
				continue;
			visited.add(chanName);
			out.print("chan " + chanName + " = [0] of {mtype}\n");
		}
	}

	private String generateChannelFromSynchActionName(
			Pair<Integer, Integer> pair) {
		return "p" + pair.right + "_" + pair.left;
	}

	public void generateSpinModeForActions(Set<String> actions) {
		out.print("mtype = {");
		out.print("\n\t" + "nop, ");
		int counter = 0;

		for (String action : actions) {
			counter++;
			out.print(action + ", ");

			if (counter % 4 == 0)
				out.print("\n");
		}
		out.print("\n}");
	}

	public void generateSpinModelForProcess(DotAutomaton dotAutomaton,
			Map<String, Pair<Integer, Integer>> synchActions) {
		int automatonId = dotAutomaton.getAutomatonId();
		int initStateId = dotAutomaton.getInitAutomatonState()
				.getAutomatonStateId();
		LinkedList<DotAutomatonState> queue = new LinkedList<>();
		Set<Integer> visited = new HashSet<>();

		queue.add(dotAutomaton.getInitAutomatonState());
		out.print("\n\n");
		out.print("active proctype Proc" + automatonId + "()");
		out.print("\n{");
		out.print("\n\t" + "int state = " + initStateId + ";");
		out.print("\n\t" + "do");
		// all states
		while (!queue.isEmpty()) {
			DotAutomatonState currentState = queue.pop();
			List<DotAutomatonTransition> transitions = currentState
					.getOutGoingTransitions();
			int currentStateId = currentState.getAutomatonStateId();
			// System.out.println("+++" + currentStateId);

			if (!visited.contains(currentStateId)) {
				visited.add(currentStateId);
				out.print("\n\t" + "::");
				generateSpinModelForState(currentState, automatonId,
						synchActions);
				for (DotAutomatonTransition transition : transitions) {
					DotAutomatonState targetState = getTargetState(
							currentStateId, transition);

					if (!visited.contains(targetState.getAutomatonStateId())) {
						queue.addLast(targetState);
						// System.out.println(currentStateId + "->"
						// + targetState.getAutomatonStateId());
					}
				}
			}
		}
		out.print("\n\t" + "od");
		out.print("\n}");
	}

	private DotAutomatonState getTargetState(int sourceStateId,
			DotAutomatonTransition transition) {
		List<Pair<DotAutomatonState, DotAutomatonState>> pairs = transition
				.getSource2Target();

		for (Pair<DotAutomatonState, DotAutomatonState> pair : pairs) {
			if (sourceStateId == pair.left.getAutomatonStateId())
				return pair.right;
		}
		return null;
	}

	public void generateSpinModelForState(DotAutomatonState state,
			int automatonId, Map<String, Pair<Integer, Integer>> synchActions) {
		int stateId = state.getAutomatonStateId();
		List<DotAutomatonTransition> transitions = state
				.getOutGoingTransitions();
		int numTransitions = transitions.size();

		out.print("state == " + stateId + " -> ");
		if (numTransitions == 1) {
			DotAutomatonTransition transition = transitions.get(0);
			generateSpinModelForTransition(transition, synchActions, stateId,
					automatonId, "");
		} else {
			out.print("\n\t\t" + "if");
			for (DotAutomatonTransition transition : transitions) {
				out.print("\n\t\t" + "::");
				generateSpinModelForTransition(transition, synchActions,
						stateId, automatonId, "\t");
			}
			out.print("\n\t\t" + "fi");
		}
	}

	public void generateSpinModelForTransition(
			DotAutomatonTransition transition,
			Map<String, Pair<Integer, Integer>> synchActions, int stateId,
			int automatonId, String prefix) {
		String label = transition.getLabel();
		label = label.equals("") ? label : fixName(label);

		if (synchActions.containsKey(label) && !label.equals(""))
			generateSpinModelForSynchTransition(transition, stateId,
					automatonId, synchActions, prefix);
		else
			generateSpinModelForRegularTransition(transition, stateId, "");

	}

	public void generateSpinModelForRegularTransition(
			DotAutomatonTransition transition, int stateId, String prefix) {
		int targetStateId = getTargetState(stateId, transition)
				.getAutomatonStateId();
		String label = transition.getLabel();

		out.print("\n\t" + prefix);
		out.print("atomic { lastAction = "
				+ (label.equals("") ? "nop" : fixName(label)) + ";");
		out.print(" state = " + targetStateId + "}");
	}

	public void generateSpinModelForSynchTransition(
			DotAutomatonTransition transition, int stateId, int automatonId,
			Map<String, Pair<Integer, Integer>> synchActions, String prefix) {
		List<Pair<DotAutomatonState, DotAutomatonState>> s2t = transition
				.getSource2Target();
		int targetStateId = 0, targetAutomatonId = 0;
		boolean send = false;
		String channelName;
		String label = fixName(transition.getLabel());
		for (Pair<DotAutomatonState, DotAutomatonState> p : s2t) {
			if ((p.left.getAutomatonStateId() == stateId)) {
				targetStateId = p.right.getAutomatonStateId();
			}
		}
		Pair<Integer, Integer> pair = synchActions.get(label);
		targetAutomatonId = (pair.left == automatonId ? pair.right : pair.left);

		if (automatonId > targetAutomatonId) {
			send = true;
			channelName = "p" + automatonId + "_" + targetAutomatonId;
		} else {
			send = false;
			channelName = "p" + targetAutomatonId + "_" + automatonId;
		}

		out.print("\n\t" + prefix + "atomic " + "{ ");
		out.print(channelName + (send ? " ! " : " ? ")
				+ label + " -> state = " + targetStateId + ";");
		out.print(" lastAction = " + label + ";");
		out.print(" }");
	}

	public void setPrintStream(PrintStream out) {
		this.out = out;
	}

	public String fixName(String str) {
		return str.substring(0, 1) + str.substring(1).toUpperCase();
	}
}
