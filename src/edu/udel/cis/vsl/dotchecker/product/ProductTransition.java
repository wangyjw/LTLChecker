package edu.udel.cis.vsl.dotchecker.product;

import edu.udel.cis.vsl.dotchecker.model.Transition;
import edu.udel.cis.vsl.dotchecker.property.PropertyTransition;

/**
 * A {@link ProductTransition} consists of a model transition (
 * {@link Transition}) and a {@link PropertyTransition}. Executing a
 * {@link ProductTransition} means executing both a model transition (
 * {@link Transition}) and a {@link PropertyTransition} at the same time.
 * 
 * @author yanyihao
 *
 */
public class ProductTransition {
	private Transition modelTransition;

	private PropertyTransition propertyTransition;

	public ProductTransition(Transition mt, PropertyTransition pt) {
		modelTransition = mt;
		propertyTransition = pt;
	}

	public Transition getModelTransition() {
		return modelTransition;
	}

	public PropertyTransition getPropertyTransition() {
		return propertyTransition;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProductTransition) {
			ProductTransition transition = (ProductTransition) obj;

			return modelTransition.getTransition().getLabel()
					.equals(transition.modelTransition.getTransition()
							.getLabel())
					&& propertyTransition.getLabel()
							.equals(transition.propertyTransition.getLabel());
		} else
			return false;
	}
}
