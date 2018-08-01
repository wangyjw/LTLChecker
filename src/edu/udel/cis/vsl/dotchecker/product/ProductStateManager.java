package edu.udel.cis.vsl.dotchecker.product;

import java.io.PrintWriter;

import edu.udel.cis.vsl.dotchecker.model.ModelEnabler;
import edu.udel.cis.vsl.dotchecker.model.ModelStateManager;
import edu.udel.cis.vsl.dotchecker.model.State;
import edu.udel.cis.vsl.dotchecker.model.Transition;
import edu.udel.cis.vsl.dotchecker.property.PropertyState;
import edu.udel.cis.vsl.dotchecker.property.PropertyTransition;
import edu.udel.cis.vsl.gmc.ltl.NdfsStateManagerIF;

/**
 * The most important method of this class is computing the next
 * {@link ProductState} given a {@link ProductState} and
 * {@link ProductTransition}.
 * 
 * @author yanyihao
 *
 */
public class ProductStateManager
		implements
			NdfsStateManagerIF<ProductState, ProductTransition> {
	private ModelStateManager modelStateManager;

	private ProductFactory productFactory;

	public PrintWriter writer;

	public ProductStateManager(ModelStateManager modelStateManager,
			ModelEnabler modelEnabler, ProductFactory productFactory) {
		this.modelStateManager = modelStateManager;
		this.productFactory = productFactory;
	}

	/**
	 * Return the next {@link ProductState} given a source {@link ProductState}
	 * and a {@link ProductTransition}.
	 */
	@Override
	public ProductState nextState(ProductState productState,
			ProductTransition transition) {
		Transition modelTransition = transition.getModelTransition();
		PropertyTransition propertyTransition = transition
				.getPropertyTransition();
		PropertyState nextPropertyState = propertyTransition.getTargetState();
		State nextModelState = modelStateManager
				.nextState(productState.getModelState(), modelTransition);
		ProductState newState = productFactory.newProductState(nextModelState,
				nextPropertyState, modelTransition.getTransition().getLabel());

		return newState;
	}

	/**
	 * True iff this {@link ProductState} is an accepting {@link ProductState}.
	 */
	@Override
	public boolean accepting(ProductState state) {
		return state.isAccepting();
	}

	@Override
	public void printStateShort(PrintWriter out, ProductState state) {
		State modelState = state.getModelState();
		PropertyState propertyState = state.getPropertyState();

		out.println("(modelState:properyState) : (" + modelState.toString()
				+ ":" + propertyState.getId() + ")");
	}

	@Override
	public void printTransitionLong(PrintWriter out,
			ProductTransition transition) {
		Transition modelTransition = transition.getModelTransition();
		PropertyTransition propertyTransition = transition
				.getPropertyTransition();

		out.println(modelTransition.getTransition().getLabel() + ":"
				+ propertyTransition.getLabel());
	}

	@Override
	public void printStateLong(PrintWriter out, ProductState state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void printTransitionShort(PrintWriter out,
			ProductTransition transition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void printAllStatesShort(PrintWriter out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void printAllStatesLong(PrintWriter out) {
		// TODO Auto-generated method stub

	}

	public void setPrintWriter(PrintWriter writer) {
		this.writer = writer;
	}
}
