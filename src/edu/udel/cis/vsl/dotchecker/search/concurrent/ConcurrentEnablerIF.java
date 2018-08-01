package edu.udel.cis.vsl.dotchecker.search.concurrent;

import java.util.List;

public interface ConcurrentEnablerIF<STATE, TRANSITION> {
	List<TRANSITION> fullSet(STATE state);
}
