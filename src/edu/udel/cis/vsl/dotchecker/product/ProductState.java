package edu.udel.cis.vsl.dotchecker.product;

import edu.udel.cis.vsl.dotchecker.model.State;
import edu.udel.cis.vsl.dotchecker.property.PropertyState;

/**
 * A {@link ProductState} consists of a mode state ({@link State}) and a
 * {@link PropertyState}. It is an accepting state iff the {@link PropertyState}
 * is accepting. Also, {@link ProductState} keep a record of the last executed
 * action, and the {@link ProductState #predicate} is the label of the last
 * executed action.
 * 
 * @author yanyihao
 *
 */
public class ProductState {
	private State modelState;

	private PropertyState propertyState;

	private volatile int hashcode = NOT_HASHED;

	private static int NOT_HASHED = -1;

	/**
	 * The only predicate that is true at this {@link ProductState} and it is
	 * the label of the last executed action.
	 */
	private String predicate;

	public ProductState(State mState, PropertyState pState, String predicate) {
		this.modelState = mState;
		this.propertyState = pState;
		this.predicate = predicate;
	}

	public boolean isAccepting() {
		return propertyState.isAcceptingState();
	}

	public State getModelState() {
		return modelState;
	}

	public PropertyState getPropertyState() {
		return propertyState;
	}

	public String getPredicate() {
		return predicate;
	}

	@Override
	public int hashCode() {
		if (hashcode == NOT_HASHED)
			hashcode = modelState.hashCode() ^ propertyState.hashCode()
					^ predicate.hashCode();

		return hashcode;
	}

	/**
	 * Two {@link ProductState}s are equal iff both the model state and property
	 * state components are the same. Note that model state ({@link State}) and
	 * {@link PropertyState} are canonicalized.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProductState) {
			ProductState state = (ProductState) obj;

			return state.getModelState() == modelState
					&& state.getPropertyState() == propertyState;
			// && state.getPredicate().equals(predicate);
		} else
			return false;
	}

	@Override
	public String toString() {
		return modelState.toString();
	}
}
