// Generated from MyFormula.g4 by ANTLR 4.5.3

   package edu.udel.cis.vsl.dotchecker.property.ltlformula;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MyFormulaParser}.
 */
public interface MyFormulaListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MyFormulaParser#mycat}.
	 * @param ctx the parse tree
	 */
	void enterMycat(MyFormulaParser.MycatContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyFormulaParser#mycat}.
	 * @param ctx the parse tree
	 */
	void exitMycat(MyFormulaParser.MycatContext ctx);
	/**
	 * Enter a parse tree produced by {@link MyFormulaParser#cat1}.
	 * @param ctx the parse tree
	 */
	void enterCat1(MyFormulaParser.Cat1Context ctx);
	/**
	 * Exit a parse tree produced by {@link MyFormulaParser#cat1}.
	 * @param ctx the parse tree
	 */
	void exitCat1(MyFormulaParser.Cat1Context ctx);
	/**
	 * Enter a parse tree produced by {@link MyFormulaParser#cat2}.
	 * @param ctx the parse tree
	 */
	void enterCat2(MyFormulaParser.Cat2Context ctx);
	/**
	 * Exit a parse tree produced by {@link MyFormulaParser#cat2}.
	 * @param ctx the parse tree
	 */
	void exitCat2(MyFormulaParser.Cat2Context ctx);
}