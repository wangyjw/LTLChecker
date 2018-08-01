package edu.udel.cis.vsl.dotchecker.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import edu.udel.cis.vsl.dotchecker.frontend.DOTLexer;
import edu.udel.cis.vsl.dotchecker.frontend.DOTParser;
import edu.udel.cis.vsl.dotchecker.frontend.DOTParser.Edge_stmtContext;
import edu.udel.cis.vsl.dotchecker.frontend.DOTParser.GraphContext;
import edu.udel.cis.vsl.dotchecker.frontend.DOTParser.Node_stmtContext;
import edu.udel.cis.vsl.dotchecker.frontend.DOTParser.StmtContext;
import edu.udel.cis.vsl.dotchecker.frontend.DOTParser.Stmt_listContext;
import edu.udel.cis.vsl.dotchecker.frontend.DOTParser.SubgraphContext;
import edu.udel.cis.vsl.dotchecker.util.Pair;
import edu.udel.cis.vsl.dotchecker.util.StringUtil;

/**
 * A {@link ModelBuilder} will build a {@link Model} of a RERS parallel system
 * from a *.dot file provided by the RERS competition.
 * 
 * @author yanyihao
 *
 */
public class ModelBuilder {
	/**
	 * Each label will be mapped to a unique {@link DotAutomatonTransition}.
	 */
	private Map<String, DotAutomatonTransition> transitionMap = new HashMap<>();

	/**
	 * This set stores all the labels of synchronized
	 * {@link DotAutomatonTransition}.
	 */
	private Set<String> synchronizedTransition = new HashSet<>();

	private Set<String> transitions = new HashSet<>();

	private int automatonId = 0;

	private ModelFactory modelFactory;

	public ModelBuilder(ModelFactory modelFactory) {
		this.modelFactory = modelFactory;
	}

	/**
	 * Build a {@link Model} from a *.dot file.
	 * 
	 * @param input
	 *            The input .dot file provided by the RERS competition.
	 * @return The {@link Model} of the parallel system represented by the input
	 *         file.
	 */
	public Model build(File input) {
		try {
			GraphContext root = parse(input);

			return constructModelFromTree(root);
		} catch (IOException e) {
			System.err.println("parse error");
			return null;
		}
	}

	private GraphContext parse(File input) throws IOException {
		if (!input.exists()) {
			throw new FileNotFoundException();
		}

		String code = readFile(input, Charset.forName("UTF-8"));
		DOTLexer lexer = new DOTLexer(new ANTLRInputStream(code));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		DOTParser parser = new DOTParser(tokens);
		GraphContext root = parser.graph();

		return root;
	}

	/**
	 * Construct the model from the ast.
	 * 
	 * @param root
	 * @return
	 */
	private Model constructModelFromTree(GraphContext root) {
		Model model = new Model();
		List<SubgraphContext> subgraphs = extractSubgraphs(root);

		for (SubgraphContext subgraph : subgraphs) {
			model.addAutomaton(constructAutomatonFromSubgraph(subgraph));
		}
		model.setSynchornizedTransitions(synchronizedTransition);
		model.setTransitions(transitions);
		analyseSynchTransitions(model);
		return model;
	}

	/**
	 * Extract subgraphs from the dot file, each subgraph is an automaton.
	 * 
	 * @param root
	 * @return
	 */
	private List<SubgraphContext> extractSubgraphs(GraphContext root) {
		List<SubgraphContext> subgraphs = new LinkedList<>();
		Stmt_listContext stmtList = root.stmt_list();
		List<StmtContext> stmts = stmtList.stmt();

		for (StmtContext stmt : stmts) {
			SubgraphContext subgraph;
			if ((subgraph = stmt.subgraph()) != null)
				subgraphs.add(subgraph);
		}

		return subgraphs;
	}

	/**
	 * Construct an {@link DotAutomaton} from a subgraph: each node statement
	 * will be a {@link DotAutomatonState}, each edge statement will be a
	 * {@link DotAutomatonTransition}. When initializing a {@link DotAutomaton},
	 * you need to initialize the initial state and automaton counter.
	 * 
	 * @param subgraph
	 * @return
	 */
	private DotAutomaton constructAutomatonFromSubgraph(
			SubgraphContext subgraph) {
		DotAutomaton automaton = modelFactory.newDotAutomaton(automatonId++);
		List<StmtContext> stmts = subgraph.stmt_list().stmt();
		List<Node_stmtContext> nodeStmts = new LinkedList<>();
		List<Edge_stmtContext> edgeStmts = new LinkedList<>();
		Map<Integer, DotAutomatonState> states = new HashMap<>();

		for (StmtContext stmt : stmts) {
			ParseTree firstChild = stmt.getChild(0);

			if (firstChild instanceof Node_stmtContext)
				nodeStmts.add((Node_stmtContext) firstChild);
			if (firstChild instanceof Edge_stmtContext)
				edgeStmts.add((Edge_stmtContext) firstChild);
		}

		int nodeSize = nodeStmts.size();

		if (nodeSize > 0) {
			// *************
			DotAutomatonState initState = constructStateFromNode(
					nodeStmts.get(0), automaton);

			automaton.setInitAutomatonState(initState);
			states.put(initState.getAutomatonStateId(), initState);
		}
		// **************
		for (int i = 1; i < nodeSize; i++) {
			DotAutomatonState state = constructStateFromNode(nodeStmts.get(i),
					automaton);

			states.put(state.getAutomatonStateId(), state);
		}
		for (Edge_stmtContext edge : edgeStmts) {
			constructTransitionFromEdge(edge, states, automaton);
		}

		return automaton;
	}

	private DotAutomatonState constructStateFromNode(Node_stmtContext node,
			DotAutomaton process) {
		TerminalNode idNum = node.node_id().id().NUMBER();

		assert idNum != null;
		int id = Integer.parseInt(idNum.getText());

		return modelFactory.newDotAutomatonState(id, process);
	}

	private DotAutomatonTransition constructTransitionFromEdge(
			Edge_stmtContext edge, Map<Integer, DotAutomatonState> stateMap,
			DotAutomaton automaton) {
		TerminalNode sourceNum = edge.node_id().id().NUMBER();
		TerminalNode targetNum = edge.edgeRHS().node_id().get(0).id().NUMBER();
		TerminalNode label = edge.attr_list().a_list().get(0).id().get(1)
				.STRING();

		assert sourceNum != null && targetNum != null && label != null;

		int sourceId = Integer.parseInt(sourceNum.getText());
		int targetId = Integer.parseInt(targetNum.getText());
		DotAutomatonState sourceState = stateMap.get(sourceId);
		DotAutomatonState targetState = stateMap.get(targetId);
		Pair<DotAutomatonState, DotAutomatonState> pair = new Pair<DotAutomatonState, DotAutomatonState>(
				sourceState, targetState);
		String transitionLabel = StringUtil.removeQuote(label.getText());
		DotAutomatonTransition transition;

		automaton.addTransition(transitionLabel);
		transitions.add(transitionLabel);
		if (transitionMap.containsKey(transitionLabel)) {
			transition = transitionMap.get(transitionLabel);
			transition.addSource2Target(pair);
			synchronizedTransition.add(transitionLabel);
			transition.setSynchronized();
		} else {
			transition = modelFactory
					.newDotAutomatonTransition(transitionLabel);
			transition.addSource2Target(pair);
			transitionMap.put(transitionLabel, transition);
		}
		sourceState.addOutGoingTransitions(transition);
		targetState.addIncomingTransitions(transition);
		return transition;
	}

	private String readFile(File file, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(file.toPath());
		return new String(encoded, encoding);
	}

	private void analyseSynchTransitions(Model model) {
		LinkedList<DotAutomaton> automata = model.getAutomata();
		int size = automata.size();

		for (int i = 0; i < size; i++) {
			DotAutomaton sautomaton = automata.get(i);
			int sid = sautomaton.getAutomatonId();
			Set<String> stransitions = sautomaton.getTransitions();

			for (int j = i + 1; j < size; j++) {
				DotAutomaton bautomaton = automata.get(j);
				int bid = bautomaton.getAutomatonId();
				Set<String> btransitions = bautomaton.getTransitions();

				for (String t : btransitions) {
					if (t.equals(""))
						continue;
					if (stransitions.contains(t)) {
						Pair<Integer, Integer> pair = new Pair<Integer, Integer>(
								sid, bid);

						model.addSynchTransitions(t, pair);
					}
				}
			}
		}
	}
}
