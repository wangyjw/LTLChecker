package edu.udel.cis.vsl.dotchecker.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.udel.cis.vsl.dotchecker.util.Pair;

/**
 * <p>
 * Transitions of {@link DotAutomaton}. Each {@link DotAutomatonTransition} will
 * have a unique label.
 * </p>
 * 
 * <p>
 * {@link DotAutomatonTransition}s can be synchronized: one
 * {@link DotAutomatonTransition} can appear in multiple processes /
 * {@link DotAutomaton}s. At each process, this {@link DotAutomatonTransition}
 * will have one source {@link DotAutomatonState} and one target
 * {@link DotAutomatonState}.
 * </p>
 * 
 * @author yanyihao
 *
 */
public class DotAutomatonTransition {
	/**
	 * The unique label of this {@link DotAutomatonTransition}.
	 */
	private String label;

	/**
	 * The list of pairs of source {@link DotAutomatonState} and target
	 * {@link DotAutomatonState}. They are indexed by process /
	 * {@link DotAutomaton} id.
	 */
	private List<Pair<DotAutomatonState, DotAutomatonState>> source2Targets = new LinkedList<>();

	/**
	 * {@link #isSynchronized} is true iff this {@link DotAutomatonTransition}
	 * is synchronized.
	 */
	private boolean isSynchronized;

	public DotAutomatonTransition(String label) {
		this.label = label.toLowerCase();
		isSynchronized = false;
	}

	public void setSynchronized() {
		isSynchronized = true;
	}

	public boolean isSynchronized() {
		return isSynchronized;
	}

	public String getLabel() {
		return label;
	}

	public List<Pair<DotAutomatonState, DotAutomatonState>> getSource2Target() {
		return source2Targets;
	}

	void addSource2Target(
			Pair<DotAutomatonState, DotAutomatonState> source2Target) {
		source2Targets.add(source2Target);
	}

	/**
	 * @return the set of ids of {@link DotAutomaton}s that contain this
	 *         {@link DotAutomatonTransition}.
	 */
	public Set<Integer> getProcs() {
		Set<Integer> procIds = new HashSet<>();

		for (Pair<DotAutomatonState, DotAutomatonState> pair : source2Targets) {
			procIds.add(pair.left.getAutomaton().getAutomatonId());
		}
		return procIds;
	}
}
