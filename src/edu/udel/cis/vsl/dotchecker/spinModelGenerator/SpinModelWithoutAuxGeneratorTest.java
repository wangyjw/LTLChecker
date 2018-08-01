package edu.udel.cis.vsl.dotchecker.spinModelGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import edu.udel.cis.vsl.dotchecker.model.Model;
import edu.udel.cis.vsl.dotchecker.model.ModelBuilder;
import edu.udel.cis.vsl.dotchecker.model.ModelFactory;
import edu.udel.cis.vsl.dotchecker.util.FileUtil;
import edu.udel.cis.vsl.gmc.ltl.formula.LTLFormula;
import edu.udel.cis.vsl.gmc.ltl.formula.LTLFormulaBuilder;
import edu.udel.cis.vsl.gmc.ltl.formula.LTLFormulaFactory;

public class SpinModelWithoutAuxGeneratorTest {
	public static void main(String[] args) throws IOException {
		String problemNum = "102";
		String propertyPath = "examples/2017/problem" + problemNum
				+ "-properties.txt";
		String inputPath = "examples/2017/problem" + problemNum + ".dot";
		String spinPropertiesPath = "examples/2017Spin/spin" + problemNum
				+ "-properties.txt";
		String spinReductionModelFilePathDir = "examples/2017SpinReductionModelWithoutAux/problem"
				+ problemNum + "/";
		PrintStream out = null;

		File input = new File(inputPath);
		ModelFactory modelFactory = new ModelFactory();
		ModelBuilder modelBuilder = new ModelBuilder(modelFactory);
		Model model = modelBuilder.build(input);
		SpinModelGeneratorWithoutAux generator = new SpinModelGeneratorWithoutAux(out);
		String[] formulas = FileUtil.extractFormulas(propertyPath);
		String[] spinFormulas = FileUtil
				.extractSpinFormulas(spinPropertiesPath);
		LTLFormulaFactory ltlFormulaFactory = new LTLFormulaFactory();
		LTLFormulaBuilder formulaBuilder = new LTLFormulaBuilder(
				ltlFormulaFactory);

		for (int i = 0; i < 20; i++) {
			LTLFormula ltlformula = formulaBuilder.build(formulas[i]);
			String spinFilePath = spinReductionModelFilePathDir + "problem"
					+ problemNum + "-p" + (i + 1) + ".pml";
			File spinReductionFile = new File(spinFilePath);

			if (!spinReductionFile.exists()) {
				spinReductionFile.getParentFile().mkdirs();
				spinReductionFile.createNewFile();
			}
			out = new PrintStream(new FileOutputStream(spinReductionFile));
			generator.setPrintStream(out);
			generator.generateSpinModel(model, ltlformula, spinFormulas[i]);
		}
	}
}
