package edu.udel.cis.vsl.dotchecker.util;

public class Test {
	public static void main(String[] args) {
		String path = "examples/2017Spin/spin101-properties.txt";
		String[] formulas = FileUtil.extractFormulas(path);

		for (String formula : formulas) {
			System.out.println("a:" + formula);
		}
	}
}
