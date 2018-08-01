package edu.udel.cis.vsl.dotchecker.product;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.dotchecker.model.ModelEnabler;
import edu.udel.cis.vsl.dotchecker.model.State;
import edu.udel.cis.vsl.dotchecker.model.Transition;
import edu.udel.cis.vsl.dotchecker.property.PropertyState;
import edu.udel.cis.vsl.dotchecker.property.PropertyTransition;
import edu.udel.cis.vsl.gmc.ltl.NdfsEnablerIF;
import edu.udel.cis.vsl.gmc.ltl.formula.LTLFormula;

/**
 * {@link ProductEnabler} tells you what {@link ProductTransition}s should be
 * explored given a {@link ProductState}.
 * 
 * @author yanyihao
 *
 */
public class ProductEnabler
		implements
			NdfsEnablerIF<ProductState, ProductTransition, ProductTransitionSequence> {

	private ModelEnabler modelEnabler;

	private ProductFactory productFactory;

	private PrintWriter writer;

	public ProductEnabler(ModelEnabler modelEnabler,
			ProductFactory productFactory) {
		this.modelEnabler = modelEnabler;
		this.productFactory = productFactory;
	}

	/**
	 * Return fullSet of the given state if reduce is false, otherwise return
	 * the ample set of the give state. The LTL formula is needed because the
	 * partial order reduction may need information from the LTL formula.
	 */
	@Override
	public ProductTransitionSequence getTransitions(ProductState state,
			LTLFormula formula, boolean reduce) {
		State modelState = state.getModelState();
		PropertyState propertyState = state.getPropertyState();
		LinkedList<Transition> modelTransitions = reduce
				? modelEnabler.ampleSet(modelState, formula).getTransitions()
				: modelEnabler.fullSet(modelState).getTransitions();
		List<PropertyTransition> propertyTransitions = propertyState
				.getTransitions();
		LinkedList<ProductTransition> productTransitions = new LinkedList<>();

		for (Transition mt : modelTransitions)
			for (PropertyTransition pt : propertyTransitions) {
				if (satisfy(mt.getTransition().getLabel(), pt.getLabel()))
					productTransitions
							.add(productFactory.newProductTransition(mt, pt));
			}

		return productFactory.newProductTransitionSequence(productTransitions,
				state);
	}

	/**
	 * <p>
	 * Assume among all the propositions, only p is true, the rest is false,
	 * return true iff the formula(CNF) also has a truth value of true.
	 * </p>
	 * 
	 * <p>
	 * This method is used to determine whether a property transition and a
	 * model transition can be executed at the same time.
	 * </p>
	 * 
	 */
	private boolean satisfy(String p, String formula) {
		if (formula.equals("-"))
			return true;

		StringBuilder sb = new StringBuilder();
		LinkedList<String> propositions = new LinkedList<>();
		LinkedList<String> positiveSet = new LinkedList<>();
		LinkedList<String> negtiveSet = new LinkedList<>();
		int len = formula.length();

		for (int i = 0; i < len; i++) {
			char cur = formula.charAt(i);

			if (cur == '&') {
				propositions.add(sb.toString());
				sb.delete(0, sb.length());
			} else {
				sb.append(cur);
			}
		}
		propositions.add(sb.toString());

		for (String str : propositions) {
			if (str.charAt(0) == '!')
				negtiveSet.add(str.substring(1));
			else
				positiveSet.add(str);
		}

		int posSize = positiveSet.size();

		if (posSize == 0) {
			return !negtiveSet.contains(p);
		} else if (posSize == 1) {
			return positiveSet.get(0).equals(p);
		} else
			return false;
	}

	@Override
	public ProductState source(ProductTransitionSequence sequence) {
		return sequence.getSourceState();
	}

	@Override
	public boolean hasNext(ProductTransitionSequence sequence) {
		return sequence.hasNext();
	}

	@Override
	public ProductTransition next(ProductTransitionSequence sequence) {
		return sequence.next();
	}

	@Override
	public ProductTransition peek(ProductTransitionSequence sequence) {
		return sequence.peek();
	}

	/**
	 * Expand a {@link ProductState}. In this implementation, just add the
	 * ampleSetComplement into the {@link ProductTransitionSequence}.
	 */
	@Override
	public void expand(ProductTransitionSequence sequence, LTLFormula formula) {
		ProductState state = sequence.getSourceState();
		State modelState = state.getModelState();
		PropertyState propertyState = state.getPropertyState();
		LinkedList<Transition> ampleSetComplement = modelEnabler
				.ampleSetCompliment(modelState, formula).getTransitions();
		List<PropertyTransition> propertyTransitions = propertyState
				.getTransitions();
		LinkedList<ProductTransition> productTransitions = new LinkedList<>();

		for (Transition mt : ampleSetComplement)
			for (PropertyTransition pt : propertyTransitions) {
				if (satisfy(mt.getTransition().getLabel(), pt.getLabel())) {
					productTransitions
							.add(productFactory.newProductTransition(mt, pt));
				}
			}
		sequence.addTransitions(productTransitions);
	}

	@Override
	public void print(PrintWriter out, ProductTransitionSequence sequence) {
		// TODO Auto-generated method stub

	}

	@Override
	public void printRemaining(PrintWriter out,
			ProductTransitionSequence sequence) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDebugging(boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean debugging() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDebugOut(PrintWriter out) {
		// TODO Auto-generated method stub

	}

	@Override
	public PrintWriter getDebugOut() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public PrintWriter getDebugWriter() {
		return writer;
	}
}
