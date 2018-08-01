package edu.udel.cis.vsl.dotchecker.product;

import java.util.LinkedList;
import java.util.List;

import edu.udel.cis.vsl.dotchecker.model.ModelEnabler;
import edu.udel.cis.vsl.dotchecker.model.State;
import edu.udel.cis.vsl.dotchecker.model.Transition;
import edu.udel.cis.vsl.dotchecker.property.PropertyState;
import edu.udel.cis.vsl.dotchecker.property.PropertyTransition;
import edu.udel.cis.vsl.dotchecker.search.concurrent.ConcurrentEnablerIF;

/**
 * The implementation of {@link ConcurrentEnablerIF}.
 * 
 * @author yanyihao
 *
 */
public class ConcurrentProductEnabler
		implements
			ConcurrentEnablerIF<ProductState, ProductTransition> {
	private ModelEnabler modelEnabler;

	private ProductFactory productFactory;

	public ConcurrentProductEnabler(ModelEnabler modelEnabler,
			ProductFactory productFactory) {
		this.modelEnabler = modelEnabler;
		this.productFactory = productFactory;
	}

	@Override
	/**
	 * Get the fullSet of a state which includes all the enabled transitions of
	 * all the processes.
	 */
	public List<ProductTransition> fullSet(ProductState state) {
		State modelState = state.getModelState();
		PropertyState propertyState = state.getPropertyState();
		LinkedList<Transition> modelTransitions = modelEnabler
				.fullSet(modelState).getTransitions();
		List<PropertyTransition> propertyTransitions = propertyState
				.getTransitions();
		LinkedList<ProductTransition> productTransitions = new LinkedList<>();

		for (Transition mt : modelTransitions)
			for (PropertyTransition pt : propertyTransitions) {
				if (satisfy(mt.getTransition().getLabel(), pt.getLabel()))
					productTransitions
							.add(productFactory.newProductTransition(mt, pt));
			}

		return productTransitions;
	}

	/**
	 * <p>
	 * Assume among all the propositions, only p is true, the rest is false,
	 * return true iff the formula also has a truth value of true.
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

		String[] atomics = formula.split("&");
		LinkedList<String> positiveSet = new LinkedList<>();
		LinkedList<String> negtiveSet = new LinkedList<>();

		for (String str : atomics) {
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
}
