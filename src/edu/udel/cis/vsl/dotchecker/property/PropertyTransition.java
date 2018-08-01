package edu.udel.cis.vsl.dotchecker.property;

/**
 * The transitions or edges in {@link AbstractPropertyAutomaton}. Each
 * {@link PropertyTransition} will have a unique label, a source state and a
 * target state.
 * 
 * @author yanyihao
 *
 */
public class PropertyTransition {
	/**
	 * Unique label.
	 */
	private String label;
	
	/**
	 * Source {@link PropertyState}.
	 */
	private PropertyState sourceState;
	
	/**
	 * Target {@link PropertyState}.
	 */
	private PropertyState targetState;

	public PropertyTransition(PropertyState sourceState,
			PropertyState targetState, String label) {
		this.label = label;
		this.sourceState = sourceState;
		this.targetState = targetState;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public PropertyState getSourceState() {
		return sourceState;
	}

	public void setSourceState(PropertyState sourceState) {
		this.sourceState = sourceState;
	}

	public PropertyState getTargetState() {
		return targetState;
	}

	public void setTargetState(PropertyState targetState) {
		this.targetState = targetState;
	}

}
