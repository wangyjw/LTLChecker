package edu.udel.cis.vsl.dotchecker.product;

import java.util.LinkedList;

import edu.udel.cis.vsl.dotchecker.model.State;
import edu.udel.cis.vsl.dotchecker.model.Transition;
import edu.udel.cis.vsl.dotchecker.property.PropertyState;
import edu.udel.cis.vsl.dotchecker.property.PropertyTransition;

/**
 * This {@link ProductFactory} includes all methods which construct objects that
 * are used in the product graph.
 * 
 * @author yanyihao
 *
 */
public class ProductFactory {
	/**
	 * Construct an instance of {@link ProductTransition} which is a wrapper of
	 * a model {@link Transition} and a {@link PropertyTransition}.
	 * 
	 * @param mt
	 *            A model {@link Transition}.
	 * @param pt
	 *            A {@link PropertyTransition}.
	 * @return the newly constructed {@link ProductTransition}.
	 */
	public ProductTransition newProductTransition(Transition mt,
			PropertyTransition pt) {
		return new ProductTransition(mt, pt);
	}

	/**
	 * Construct an instance of {@link ProductTransitionSequence}.
	 * 
	 * @param transitions
	 *            A list of {@link ProductTransition} that will be put into the
	 *            {@link ProductTransitionSequence}.
	 * @param state
	 *            The source {@link ProductState} of this
	 *            {@link ProductTransitionSequence}.
	 * @return the newly constructed {@link ProductTransitionSequence}.
	 */
	public ProductTransitionSequence newProductTransitionSequence(
			LinkedList<ProductTransition> transitions, ProductState state) {
		return new ProductTransitionSequence(transitions, state);
	}

	/**
	 * Construct an instance of {@link ProductState} which is a wrapper of a
	 * model {@link State} and a {@link PropertyState}. Also, each
	 * {@link ProductState} will also keep a record of the last executed action.
	 * 
	 * @param state
	 *            Model {@link State}.
	 * @param propertyState
	 *            {@link PropertyState}.
	 * @param predicate
	 *            The label of the last executed action.
	 * @return the newly constructed {@link ProductState}.
	 */
	public ProductState newProductState(State state,
			PropertyState propertyState, String predicate) {
		return new ProductState(state, propertyState, predicate);
	}
}
