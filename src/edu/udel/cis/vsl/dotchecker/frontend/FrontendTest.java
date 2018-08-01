package edu.udel.cis.vsl.dotchecker.frontend;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.dotchecker.frontend.DOTParser.GraphContext;
import edu.udel.cis.vsl.dotchecker.frontend.DOTParser.StmtContext;
import edu.udel.cis.vsl.dotchecker.frontend.DOTParser.Stmt_listContext;
import edu.udel.cis.vsl.dotchecker.frontend.DOTParser.SubgraphContext;
import edu.udel.cis.vsl.dotchecker.model.CollapseBlankTransitionsTransformer;
import edu.udel.cis.vsl.dotchecker.model.Model;
import edu.udel.cis.vsl.dotchecker.model.ModelBuilder;
import edu.udel.cis.vsl.dotchecker.model.ModelFactory;
import edu.udel.cis.vsl.gmc.ltl.formula.LTLFormula;
import edu.udel.cis.vsl.gmc.ltl.formula.LTLFormulaBuilder;
import edu.udel.cis.vsl.gmc.ltl.formula.LTLFormulaFactory;

public class FrontendTest {
	public static void main(String[] args) throws IOException {
		String formula = " ( a)";
		LTLFormulaFactory ltlFormulaFactory = new LTLFormulaFactory();
		LTLFormulaBuilder formulaBuilder = new LTLFormulaBuilder(
				ltlFormulaFactory);
		LTLFormula ltlformula = formulaBuilder.build(formula);
		File input = new File("examples/2017/problem110.dot");

		if (!input.exists()) {
			System.out.println("file not exists");
			return;
		}
		ModelFactory modelFactory = new ModelFactory();
		ModelBuilder mb = new ModelBuilder(modelFactory);

		Model model = mb.build(input);
		CollapseBlankTransitionsTransformer blankTransformer = new CollapseBlankTransitionsTransformer(
				ltlformula);
		blankTransformer.transform(model);
		model.print(System.out);
		// Map<String, Integer> synchronizedStmt = model
		// .getSynchornizedStatements();
		//
		// for (Entry<String, Integer> entry : synchronizedStmt.entrySet()) {
		// System.out.println(entry.getKey() + ":" + entry.getValue());
		// }
		//
		// System.out.println("process size:" + model.getProcesses().size());
		//
		// System.out.println(model.getProcesses().get(0).getInitLocation().getLocationId());
		// System.out.println(model.getProcesses().get(1).getInitLocation().getLocationId());

	}

	public static List<SubgraphContext> extractSubgraphs(GraphContext root) {
		List<SubgraphContext> subgraphs = new LinkedList<>();
		Stmt_listContext stmt_list = root.stmt_list();
		List<StmtContext> stmts = stmt_list.stmt();

		for (StmtContext stmt : stmts) {
			SubgraphContext subgraph;
			if ((subgraph = stmt.subgraph()) != null)
				subgraphs.add(subgraph);
		}

		return subgraphs;
	}
}
