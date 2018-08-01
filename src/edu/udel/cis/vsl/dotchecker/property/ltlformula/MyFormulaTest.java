package edu.udel.cis.vsl.dotchecker.property.ltlformula;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import edu.udel.cis.vsl.dotchecker.util.FileUtil;
import edu.udel.cis.vsl.ltl.userInterface.LTLProcessor;

public class MyFormulaTest {
	public static void main(String[] args) throws IOException {
		String propertyPath = "examples/2017/problem115-properties.txt";
		String[] formulas = FileUtil.extractFormulas(propertyPath);
		PrintWriter pw = new PrintWriter(
				new FileWriter("non-blankInvariantProperties2.txt", true));
		pw.println("+++++++++++++" + propertyPath);
		int count = 1;
		for (String formula : formulas) {
			System.out.println(formula);
			formula = LTLProcessor.process(formula);
			System.out.println(formula);
			PipedOutputStream pipeOut = new PipedOutputStream();
			PipedInputStream pipeIn = new PipedInputStream(pipeOut);
			System.setErr(new PrintStream(pipeOut));

			MyFormulaLexer lexer = new MyFormulaLexer(
					new ANTLRInputStream(formula));
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			MyFormulaParser parser = new MyFormulaParser(tokens);
			parser.mycat();
			if (pipeIn.available() > 0) {
				System.out.println("property is not blank invariant.");
				pw.println(count + ": " + formula+ "\n\n\n");
			} else
				System.out.println("property is blank invariant.");
			System.out.println("avaliable:" + pipeIn.available());
			pipeIn.close();
			count++;
		}
		pw.close();
	}
}
