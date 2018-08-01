package edu.udel.cis.vsl.dotchecker.property;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import gov.nasa.ltl.graph.Edge;
import gov.nasa.ltl.graph.Graph;
import gov.nasa.ltl.graph.Node;
import gov.nasa.ltl.trans.LTL2Buchi;
import gov.nasa.ltl.trans.ParseErrorException;

/**
 * <p>
 * There are two functionalities of this class.
 * <ul>
 * <li>Translate a LTL formula into an {@link AbstractPropertyAutomaton}</li>
 * <li>Check whether a LTL formula is Blank Invariant.</li>
 * </ul>
 * </p>
 * 
 * @author yanyihao
 *
 */
public class LTL2BuchiTranslator {

	/**
	 * Translate a LTL formula into an {@link AbstractPropertyAutomaton}.
	 * 
	 * @param ltlFormula
	 * @return null if ltlFormula is not satisfiable.
	 */
	public AbstractPropertyAutomaton translate(String ltlFormula) {
		Graph graph;
		try {
			graph = LTL2Buchi.translate(ltlFormula);
			return constructAutomatonFromGraph(graph);
		} catch (ParseErrorException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param graph
	 * @return null if the formula (negate) is not satisfiable.
	 */
	private AbstractPropertyAutomaton constructAutomatonFromGraph(Graph graph) {
		AbstractPropertyAutomaton resultAutomaton = new AbstractPropertyAutomaton();
		PropertyState initState = resultAutomaton.getInitState();
		Map<Integer, PropertyState> nodeStateMap = new HashMap<>();
		List<Node> nodes = graph.getNodes();

		if (graph.getInit() == null)
			return null;

		int initNodeId = graph.getInit().getId();

		for (Node node : nodes) {
			int id = node.getId();
			PropertyState propertyState = new PropertyState(id,
					node.getBooleanAttribute("accepting"));

			nodeStateMap.put(id, propertyState);
			resultAutomaton.addState(propertyState);
		}
		initState.addTransition(new PropertyTransition(initState,
				nodeStateMap.get(initNodeId), ""));

		for (Node node : nodes) {
			List<Edge> edges = node.getOutgoingEdges();
			PropertyState propertyState = nodeStateMap.get(node.getId());

			for (Edge edge : edges) {
				PropertyTransition transition = new PropertyTransition(
						propertyState, nodeStateMap.get(edge.getNext().getId()),
						edge.getGuard());

				propertyState.addTransition(transition);
			}
		}

		return resultAutomaton;
	}

	/**
	 * A property is blank invariant needs to satisfy two conditions:
	 * <ul>
	 * <li>Each non-initial property state must have a self-loop transition and
	 * this transition should accept 'blank' input.</li>
	 * <li>Transitions between states can not accept 'blank' input.</li>
	 * </ul>
	 * 
	 * @param automaton
	 * @return
	 */
	public boolean isPropertyBlankInvariant(
			AbstractPropertyAutomaton automaton) {
		LinkedList<PropertyState> automatonStates = automaton.getStates();

		for (PropertyState state : automatonStates) {
			int curId = state.getId();

			if (curId == AbstractPropertyAutomaton.INIT_STATE_ID)
				continue;

			boolean containSelfBlankLoop = false;
			List<PropertyTransition> propertyTransitions = state
					.getTransitions();

			for (PropertyTransition transition : propertyTransitions) {
				String curLabel = transition.getLabel();

				if (transition.getTargetState().getId() != curId) {
					// transition between states
					if (acceptBlank(curLabel))
						return false;
				} else {
					// self-loop transitions
					if (!containSelfBlankLoop && acceptBlank(curLabel)) {
						containSelfBlankLoop = true;
						break;
					}
				}
			}
			if (!containSelfBlankLoop)
				return false;
		}

		return true;
	}

	/**
	 * Tell if a {@link PropertyTransition} accepts "Blank Action" (
	 * "Blank Action" is an action that is not involved in the given property
	 * formula)
	 * 
	 * @param transitionLabel
	 *            The label of a {@link PropertyTransition}.
	 * @return true iff the transition label of the {@link PropertyTransition}
	 *         accepts "Blank Transition".
	 */
	private boolean acceptBlank(String transitionLabel) {
		if (transitionLabel.equals("-"))
			return true;

		String[] propositions = transitionLabel.split("&");

		for (String proposition : propositions) {
			if (proposition.charAt(0) != '!')
				return false;
		}
		return true;
	}
}
