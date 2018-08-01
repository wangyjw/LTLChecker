package edu.udel.cis.vsl.dotchecker.spinModelGenerator;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.udel.cis.vsl.dotchecker.model.DotAutomaton;
import edu.udel.cis.vsl.dotchecker.model.DotAutomatonState;
import edu.udel.cis.vsl.dotchecker.model.DotAutomatonTransition;
import edu.udel.cis.vsl.dotchecker.model.Model;
import edu.udel.cis.vsl.dotchecker.util.Pair;
import edu.udel.cis.vsl.gmc.ltl.formula.LTLFormula;

public class SpinModelGenerator {
	private PrintStream out;

	public SpinModelGenerator(PrintStream out) {
		this.out = out;
	}

	public void generateSpinModel(Model model, LTLFormula formula,
			String spinFormula) {
		Set<String> visibleActions = new HashSet<>();
		LinkedList<DotAutomaton> dotAutomata = model.getAutomata();
		Set<String> synchActions = model.getSynchornizedTransitions();

		visibleActions.addAll(formula.getNegativePredicate());
		visibleActions.addAll(formula.getPositivePredicates());
		generateSpinModeForActions(visibleActions, synchActions);
		generateSpinModelForChannels(synchActions);
		generateLastactionStatement();
		addAuxiliaryStuff();
		for (DotAutomaton automaton : dotAutomata) {
			generateSpinModelForProcess(automaton, visibleActions);
		}
		generateSpinModelForPredicateDefine(visibleActions);
		addSpinFormula(spinFormula);
	}

	private void addSpinFormula(String spinFormula) {
		int indexOfOpenBracket = spinFormula.indexOf('{');
		int indexOfCloseBracket = spinFormula.indexOf('}');
		String prefix = spinFormula.substring(0, indexOfOpenBracket);
		String formula = spinFormula.substring(indexOfOpenBracket + 1, indexOfCloseBracket);
		
		out.print("\n\n" + prefix + "{ ");
		out.print("((!<>[]flag && !<>[]!flag) || (<>[]p_nop)) -> (");
		out.print(formula + ") }");
	}

	private void generateLastactionStatement() {
		out.print("\n\n");
		out.print("mtype lastAction = nop;");
	}

	public void addAuxiliaryStuff() {
		out.print("\nbit flag = true;");
		out.print("\nactive proctype Auxiliary(){");
		out.print("\n\tlastAction = nop;");
		out.print("\n}");
	}

	private void generateSpinModelForPredicateDefine(
			Set<String> visibleActions) {
		out.print("\n\n");
		for (String visibleAction : visibleActions) {
			out.print("#define p_" + visibleAction + " (lastAction == "
					+ visibleAction + ")\n");
		}
		out.print("#define p_nop (lastAction == nop)\n");
	}

	private void generateSpinModelForChannels(Set<String> synchActions) {
		Set<String> visited = new HashSet<>();

		out.print("\n\n");
		for (String synchAction : synchActions) {
			String chanName = generateChannelFromSynchActionName(synchAction);

			if (visited.contains(chanName) || synchAction.equals(""))
				continue;
			visited.add(chanName);
			out.print("chan " + generateChannelFromSynchActionName(synchAction)
					+ " = [0] of {mtype}\n");
		}
	}

	private String generateChannelFromSynchActionName(String synchName) {
		String[] nums = synchName.split("[^0-9]");
		int[] indexes = new int[4];
		int index = 0;

		for (String s : nums) {
			if (!s.equals("")) {
				indexes[index++] = Integer.parseInt(s);
			}
		}

		int proc1 = indexes[0];
		int proc2 = indexes[2];

		if (proc1 > proc2)
			return "p" + proc1 + "_" + proc2;
		else
			return "p" + proc2 + "_" + proc1;
	}

	public void generateSpinModeForActions(Set<String> visibleActions,
			Set<String> synchActions) {
		Set<String> visited = new HashSet<>();

		out.print("mtype = {");
		out.print("\n\t" + "nop, ");
		for (String visibleAction : visibleActions) {
			out.print(visibleAction + ", ");
			visited.add(visibleAction);
		}
		int counter = 0;

		for (String synchAction : synchActions) {
			if (synchAction.equals("") || visited.contains(synchAction))
				continue;
			if (counter % 4 == 0)
				out.print("\n\t");
			out.print(synchAction + ", ");
			counter++;
		}
		out.print("\n}");
	}

	public void generateSpinModelForProcess(DotAutomaton dotAutomaton,
			Set<String> visibleActions) {
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

			visited.add(currentStateId);
			out.print("\n\t" + "::");
			generateSpinModelForState(currentState, visibleActions,
					automatonId);
			for (DotAutomatonTransition transition : transitions) {
				DotAutomatonState targetState = getTargetState(currentStateId,
						transition);

				if (!visited.contains(targetState.getAutomatonStateId()))
					queue.addLast(targetState);
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
			Set<String> visibleActions, int automatonId) {
		int stateId = state.getAutomatonStateId();
		List<DotAutomatonTransition> transitions = state
				.getOutGoingTransitions();
		int numTransitions = transitions.size();

		out.print("state == " + stateId + " -> ");
		if (numTransitions == 1) {
			DotAutomatonTransition transition = transitions.get(0);
			String label = transition.getLabel();

			generateSpinModelForTransition(transition,
					visibleActions.contains(label), transition.isSynchronized(),
					stateId, automatonId, "");
		} else {
			out.print("\n\t\t" + "if");
			for (DotAutomatonTransition transition : transitions) {
				String label = transition.getLabel();

				out.print("\n\t\t" + "::");
				generateSpinModelForTransition(transition,
						visibleActions.contains(label),
						transition.isSynchronized(), stateId, automatonId,
						"\t");
			}
			out.print("\n\t\t" + "fi");
		}
	}

	public void generateSpinModelForTransition(
			DotAutomatonTransition transition, boolean visible, boolean synch,
			int stateId, int automatonId, String prefix) {
		int weight = 1;
		String label = transition.getLabel();

		if (!label.equals("")) {
			if (visible)
				weight += 2;
			if (synch)
				weight += 4;
		}
		switch (weight) {
			case 1 :
				generateSpinModelForRegularTransition(transition, stateId, "");
				break;
			case 3 :
				generateSpinModelForVisibleTransition(transition, prefix);
				break;
			case 5 :
				generateSpinModelForSynchTransition(transition, stateId,
						automatonId, prefix);
				break;
			case 7 :
				generateSpinModelForBothSynchAndVisible(transition, stateId,
						automatonId, prefix);
				break;
			default :
				break;
		}
	}

	public void generateSpinModelForRegularTransition(
			DotAutomatonTransition transition, int stateId, String prefix) {
		int targetStateId = getTargetState(stateId, transition)
				.getAutomatonStateId();

		out.print(prefix + "state = " + targetStateId + ";");
	}

	public void generateSpinModelForSynchTransition(
			DotAutomatonTransition transition, int stateId, int automatonId,
			String prefix) {
		List<Pair<DotAutomatonState, DotAutomatonState>> s2t = transition
				.getSource2Target();
		int targetStateId = 0, targetAutomatonId = 0;
		boolean send = false;
		String channelName;
		String label = transition.getLabel();
		for (Pair<DotAutomatonState, DotAutomatonState> p : s2t) {
			if (!(p.left.getAutomatonStateId() == stateId)) {
				targetAutomatonId = p.right.getAutomaton().getAutomatonId();
			} else {
				targetStateId = p.right.getAutomatonStateId();
			}
		}
		if (automatonId > targetAutomatonId) {
			send = true;
			channelName = "p" + automatonId + "_" + targetAutomatonId;
		} else {
			send = false;
			channelName = "p" + targetAutomatonId + "_" + automatonId;
		}

		out.print("\n\t" + prefix + "atomic " + "{");
		out.print("\n\t\t" + prefix + channelName + (send ? " ! " : " ? ")
				+ label + " -> state = " + targetStateId + ";");
		out.print("\n\t" + prefix + "}");
	}

	public void generateSpinModelForVisibleTransition(
			DotAutomatonTransition transition, String prefix) {
		int targetStateId = transition.getSource2Target().get(0).right
				.getAutomatonStateId();
		String label = transition.getLabel();

		out.print("\n\t" + prefix);
		out.print("atomic { lastAction = " + label + ";");
		out.print(" flag = !flag;");
		out.print(" state = " + targetStateId + "}");
	}

	public void generateSpinModelForBothSynchAndVisible(
			DotAutomatonTransition transition, int stateId, int automatonId,
			String prefix) {
		List<Pair<DotAutomatonState, DotAutomatonState>> s2t = transition
				.getSource2Target();
		int targetStateId = 0, targetAutomatonId = 0;
		boolean send = false;
		String channelName;
		String label = transition.getLabel();

		for (Pair<DotAutomatonState, DotAutomatonState> p : s2t) {
			if (!(p.left.getAutomatonStateId() == stateId)) {
				targetAutomatonId = p.right.getAutomaton().getAutomatonId();
			} else {
				targetStateId = p.right.getAutomatonStateId();
			}
		}
		if (automatonId > targetAutomatonId) {
			send = true;
			channelName = "p" + automatonId + "_" + targetAutomatonId;
		} else {
			send = false;
			channelName = "p" + targetAutomatonId + "_" + automatonId;
		}
		out.print("\n\t" + prefix + "atomic " + "{");
		out.print("\n\t\t" + prefix + channelName + (send ? " ! " : " ? ")
				+ label + " -> state = " + targetStateId + ";");
		out.print("\n\t\t" + prefix + "lastAction = " + label + ";");
		out.print("\n\t\tflag = !flag;");
		out.print("\n\t" + prefix + "}");
	}

	public void setPrintStream(PrintStream out) {
		this.out = out;
	}
}
