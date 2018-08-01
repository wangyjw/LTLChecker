package edu.udel.cis.vsl.dotchecker.ui;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import edu.udel.cis.vsl.dotchecker.model.CollapseBlankTransitionsTransformer;
import edu.udel.cis.vsl.dotchecker.model.Model;
import edu.udel.cis.vsl.dotchecker.model.ModelBuilder;
import edu.udel.cis.vsl.dotchecker.model.ModelEnabler;
import edu.udel.cis.vsl.dotchecker.model.ModelFactory;
import edu.udel.cis.vsl.dotchecker.model.ModelStateManager;
import edu.udel.cis.vsl.dotchecker.model.State;
import edu.udel.cis.vsl.dotchecker.product.ProductEnabler;
import edu.udel.cis.vsl.dotchecker.product.ProductFactory;
import edu.udel.cis.vsl.dotchecker.product.ProductState;
import edu.udel.cis.vsl.dotchecker.product.ProductStateManager;
import edu.udel.cis.vsl.dotchecker.product.ProductTransition;
import edu.udel.cis.vsl.dotchecker.product.ProductTransitionSequence;
import edu.udel.cis.vsl.dotchecker.property.AbstractPropertyAutomaton;
import edu.udel.cis.vsl.dotchecker.property.LTL2BuchiTranslator;
import edu.udel.cis.vsl.dotchecker.property.PropertyState;
import edu.udel.cis.vsl.dotchecker.util.FileUtil;
import edu.udel.cis.vsl.gmc.ltl.NdfsSearcher;
import edu.udel.cis.vsl.gmc.ltl.formula.LTLFormula;
import edu.udel.cis.vsl.gmc.ltl.formula.LTLFormulaBuilder;
import edu.udel.cis.vsl.gmc.ltl.formula.LTLFormulaFactory;

/**
 * User Interface.
 * 
 * @author yanyihao
 *
 */
public class UserInterface {
	/**
	 * @param args
	 *            args[0] is the path of the source dot file, args[1] is the
	 *            property file, args[2] is index of the property you want to
	 *            verify.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		int argLen = args.length;
		String inputPath = "examples/training/problem109.dot";
		String propertyPath = "examples/training/problem109-properties-with-solution.txt";
		int propertyIndex = 2;
		if (argLen != 3 && argLen != 0)
			System.err.println(
					"Wrong number of arguments! The first arg is the dot model file, "
							+ "the second arg is the property (txt) file, "
							+ "and the third arg is the property index (1-20)");
		if (argLen == 3) {
			inputPath = args[0];
			propertyPath = args[1];
			propertyIndex = Integer.parseInt(args[2]);
		}

		File input = new File(inputPath);

		if (!input.exists()) {
			System.out.println("file not exists");
			return;
		}

		// PrintWriter pw = new PrintWriter(new FileWriter(
		// "training-109-with-reduction-no-collapse-output.txt", true));
		// pw.println("=========================================================");

		MemoryMonitor memoryMonitor = new MemoryMonitor();

		/**** Here enable and disable statistics *****/
		// memoryMonitor.start();

		String[] formulas = FileUtil.extractFormulas(propertyPath);
		// String formula = "( ((c1_t6 -> [](!c1_t1)) W (c3_t7 ||
		// c0_t12__c3_t1)) )";
		// String ltlProperty = "!" + formula;

		int count = 0;
		for (String formula : formulas) {
			count++;
			/************
			 * if you only want to check a specific property formula
			 **************/
			if (propertyIndex > 0 && count != propertyIndex)
				continue;
			LTLFormulaFactory ltlFormulaFactory = new LTLFormulaFactory();
			LTLFormulaBuilder formulaBuilder = new LTLFormulaBuilder(
					ltlFormulaFactory);
			LTLFormula ltlformula = formulaBuilder.build(formula);

			CollapseBlankTransitionsTransformer transformer = new CollapseBlankTransitionsTransformer(
					ltlformula);

			ModelFactory modelFactory = new ModelFactory();
			ModelBuilder modelBuilder = new ModelBuilder(modelFactory);
			ModelStateManager modelStateManager = new ModelStateManager(
					modelFactory);
			ProductFactory productFactory = new ProductFactory();
			Model model = modelBuilder.build(input);
			/****************
			 * Here enable and disable collapse
			 *****************/
			transformer.transform(model);
			State modelInitState = modelStateManager.getInitState(model);
			LTL2BuchiTranslator translator = new LTL2BuchiTranslator();

			ModelEnabler modelEnabler = new ModelEnabler(modelFactory);
			ProductEnabler productEnabler = new ProductEnabler(modelEnabler,
					productFactory);
			ProductStateManager productStateManager = new ProductStateManager(
					modelStateManager, modelEnabler, productFactory);

			File output = new File("output.txt");
			PrintWriter writer = new PrintWriter(output);
			productStateManager.setPrintWriter(writer);
			productEnabler.setWriter(writer);

			NdfsSearcher<ProductState, ProductTransition, ProductTransitionSequence> ndfsSearcher;
			ndfsSearcher = new NdfsSearcher<>(productEnabler,
					productStateManager);
			ndfsSearcher.set(writer);
			memoryMonitor.setSearcher(ndfsSearcher);

			// concurrentSearcher = new
			// ConcurrentNdfsearcher<>(concurrentEnabler,
			// productStateManager, numCores)

			String negationFormula = "(!" + formula + ")";
			AbstractPropertyAutomaton automaton = translator
					.translate(negationFormula);

			if (automaton == null) {
				System.out.println(count
						+ ":not violated (true) since the property is valid.");
				continue;
			}

			ltlformula.setNegationBlankInvariant(
					translator.isPropertyBlankInvariant(automaton));

			PropertyState propertyInitState = automaton
					.getSuccessorOfInitState();
			ProductState initState = productFactory
					.newProductState(modelInitState, propertyInitState, "");

			/************ here enable and disable reduction *************/
			ndfsSearcher.setReduction(ltlformula);
			long time1 = System.currentTimeMillis();
			// ndfsSearcher.setBound(50);
			// timer.start();
			System.out.print(count + ":");
			boolean result = ndfsSearcher.search(initState);
			boolean timeout = ndfsSearcher.timeout();

			// pw.println("***Property " + count);
			// pw.println("saved states:" + ndfsSearcher.getSavedStates());
			// pw.println("transitions:" + ndfsSearcher.numTransitions());

			if (timeout) {
				System.out.println("time out");
				// pw.println("2 hours time out");
			} else {
				System.out.println(result
						? "violated(false)"
						: "not violated(true)");
				// pw.println(result ? "violated(false)" : "not
				// violated(true)");
				// pw.println((end - begin) + "ms");
			}
			// pw.flush();
			// timer.terminate();
			// System.out.println("count");
			writer.flush();
			// System.gc();
			long time2 = System.currentTimeMillis();
			// File trace = new File("trace.txt");
			// ndfsSearcher.printTrace(new PrintWriter(trace));

			System.out.println("time:" + ((time2 - time1) / 1000) + "s.");
			System.out.println("saved states:" + ndfsSearcher.getSavedStates());
			System.out.println(
					"transitions:" + ndfsSearcher.numTransitions() + "\n");
		}

		memoryMonitor.setRunning(false);
		// pw.close();
	}
}

class MemoryMonitor extends Thread {
	private Runtime runtime = Runtime.getRuntime();
	private boolean running = true;
	private NdfsSearcher<ProductState, ProductTransition, ProductTransitionSequence> searcher = null;

	@Override
	public void run() {
		try {
			while (running) {
				Thread.sleep(5000);
				System.out.println("used memory:"
						+ (runtime.totalMemory() - runtime.freeMemory())
								/ (1024 * 1024)
						+ "M");
				if (searcher != null) {
					System.out.println(
							"States saved:" + searcher.getSavedStates());
					System.out.println(
							"Transitions:" + searcher.numTransitions());
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void setSearcher(
			NdfsSearcher<ProductState, ProductTransition, ProductTransitionSequence> searcher) {
		this.searcher = searcher;
	}
}

class Timer extends Thread {
	private volatile boolean running = true;
	private NdfsSearcher<ProductState, ProductTransition, ProductTransitionSequence> searcher;
	private long startTime = System.currentTimeMillis();

	public Timer(
			NdfsSearcher<ProductState, ProductTransition, ProductTransitionSequence> searcher) {
		this.searcher = searcher;
	}

	@Override
	public void run() {
		while (running) {
			long currentTime = System.currentTimeMillis();
			long timeElapsed = currentTime - startTime;

			if (timeElapsed > 3600000) {
				searcher.terminate();
				System.out.println("one hour time out");
				return;
			}
		}
	}

	public void terminate() {
		running = false;
	}
}
