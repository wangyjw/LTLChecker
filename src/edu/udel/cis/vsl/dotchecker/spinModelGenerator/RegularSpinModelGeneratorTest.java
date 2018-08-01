package edu.udel.cis.vsl.dotchecker.spinModelGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import edu.udel.cis.vsl.dotchecker.model.Model;
import edu.udel.cis.vsl.dotchecker.model.ModelBuilder;
import edu.udel.cis.vsl.dotchecker.model.ModelFactory;
import edu.udel.cis.vsl.dotchecker.util.FileUtil;

public class RegularSpinModelGeneratorTest {
	public static void main(String[] args) throws IOException {
		String problemNum = "112";
		String inputPath = "examples/2017/problem" + problemNum + ".dot";
		String spinPropertiesPath = "examples/2017Spin/spin" + problemNum
				+ "-properties.txt";
		String spinFilePath = "SpinResult/2017/problem" + problemNum + ".pml";
		PrintStream out = null;

		File input = new File(inputPath);
		ModelFactory modelFactory = new ModelFactory();
		ModelBuilder modelBuilder = new ModelBuilder(modelFactory);
		Model model = modelBuilder.build(input);
		RegularSpinModelGenerator generator = new RegularSpinModelGenerator(
				out);
		String[] spinFormulas = FileUtil
				.extractSpinFormulas(spinPropertiesPath);

		File spinFile = new File(spinFilePath);

		if (!spinFile.exists()) {
			spinFile.getParentFile().mkdirs();
			spinFile.createNewFile();
		}
		out = new PrintStream(new FileOutputStream(spinFile));
		generator.setPrintStream(out);
		generator.generateSpinModel(model, spinFormulas);

	}
}
