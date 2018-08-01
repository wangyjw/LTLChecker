package edu.udel.cis.vsl.dotchecker.property;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AutomatonDebugUntil {
	/**
	 * Translate an {@link AbstractPropertyAutomaton} into .dot format. Then the
	 * automaton can be visualized using Graphviz.
	 * 
	 * @param automaton
	 *            The {@link AbstractPropertyAutomaton} that will be converted
	 *            into .dot format.
	 * @param filePath
	 *            The output path.
	 */
	public static void automatonToDot(AbstractPropertyAutomaton automaton,
			String filePath) {
		StringBuilder content = new StringBuilder();
		LinkedList<PropertyState> states = automaton.getStates();

		content.append("digraph G {\n");
		content.append("  subgraph component0 {\n");
		for (PropertyState state : states) {
			content.append(constructNodeInDot(state));
		}
		for (PropertyState state : states) {
			List<PropertyTransition> transitions = state.getTransitions();

			for (PropertyTransition transition : transitions) {
				content.append(constructEdgeInDot(transition));
			}
		}
		content.append("  }\n");
		content.append("}\n");

		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			File file = new File(filePath);

			if (file.exists())
				file.delete();
			file.createNewFile();
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(content.toString());
			bw.flush();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static String constructNodeInDot(PropertyState state) {
		StringBuffer sb = new StringBuffer();
		int id = state.getId();
		boolean accepting = state.isAcceptingState();
		String color = accepting ? "#AAAAAA" : "#6699FF";

		sb.append(id);
		sb.append(" [label=\"");
		sb.append(id);
		sb.append("\" fillcolor=\"" + color + "\" style=filled];\n");
		return sb.toString();
	}

	private static String constructEdgeInDot(PropertyTransition transition) {
		StringBuffer sb = new StringBuffer();

		sb.append(transition.getSourceState().getId() + "->"
				+ transition.getTargetState().getId());
		sb.append(" [label=\"" + transition.getLabel() + "\" color=black ];\n");
		return sb.toString();
	}
}
