package edu.udel.cis.vsl.dotchecker.model;

/**
 * An {@link AbstractModelTrasformer} will take a {@link Model} as input and
 * return an optimized {@link Model}
 * 
 * @author yanyihao
 */
public abstract class AbstractModelTrasformer {
	public abstract void transform(Model model);
}
