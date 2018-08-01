package edu.udel.cis.vsl.dotchecker.product;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A sequence of {@link ProductTransition}.
 * 
 * @author yanyihao
 *
 */
public class ProductTransitionSequence {
	/**
	 * The source {@link ProductState}.
	 */
	private ProductState sourceState;
	
	private LinkedList<ProductTransition> transitions;

	public ProductTransitionSequence(LinkedList<ProductTransition> ts,
			ProductState state) {
		sourceState = state;
		transitions = ts;
	}

	public LinkedList<ProductTransition> getTransitions() {
		return transitions;
	}

	public void addTransitions(Collection<ProductTransition> ts) {
		transitions.addAll(ts);
	}

	public ProductState getSourceState() {
		return sourceState;
	}

	public boolean hasNext() {
		return transitions.size() > 0;
	}

	public ProductTransition next() {
		return transitions.pop();
	}

	public ProductTransition peek() {
		return transitions.peek();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (ProductTransition t : transitions)
			sb.append(t.getModelTransition().getTransition().getLabel() + "--");
		sb.append("\n");
		// System.out.println();
		return sb.toString();
	}
}
