package edu.udel.cis.vsl.dotchecker.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Utility class that deals with input files.
 * 
 * @author yanyihao
 *
 */
public class FileUtil {

	public static String[] extractSpinFormulas(String path) {
		LinkedList<String> formulas = new LinkedList<>();
		File input = new File(path);
		BufferedReader reader = null;
		String[] result = null;

		try {
			String line;
			int index = 0;

			reader = new BufferedReader(new FileReader(input));
			while ((line = reader.readLine()) != null) {
				if (!line.equals(""))
					formulas.add(line);
			}
			result = new String[formulas.size()];
			for (String f : formulas)
				result[index++] = f;

			return result;
		} catch (FileNotFoundException e) {
			System.err.println("someting wrong reading the property file!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return result;
	}

	/**
	 * Extract LTL formula from a file. And the property files are provided by
	 * RERES competition.
	 * 
	 * @param path
	 *            The path of the input file.
	 * @return An array of LTL formulas.
	 */
	public static String[] extractFormulas(String path) {
		LinkedList<String> formulas = new LinkedList<>();
		File input = new File(path);
		BufferedReader reader = null;
		String[] result = null;

		try {
			String line;
			int index = 0;

			reader = new BufferedReader(new FileReader(input));
			while ((line = reader.readLine()) != null) {
				if (!line.equals("") && !line.startsWith("#"))
					formulas.add(StringUtil.formulaProcessing(line));
			}
			result = new String[formulas.size()];
			for (String f : formulas)
				result[index++] = f;

			return result;
		} catch (FileNotFoundException e) {
			System.err.println("someting wrong reading the property file!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return result;
	}

	public static void main(String[] args) {
		String[] result = extractFormulas("examples/problem101-properties.txt");

		for (String str : result)
			System.out.println(str);
	}
}
