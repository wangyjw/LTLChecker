package edu.udel.cis.vsl.dotchecker.processoutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ProcessSpinOutput {
	public static int processSpinOutput(String problemNum, String inputpath,
			PrintWriter pw) throws IOException {
		File input = new File(inputpath);
		FileReader fileReader = new FileReader(input);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;
		String[] data = {problemNum, "0", "0", "0", "0", "0"};
		int counter = 1;
		int numSolved = 0;

		while ((line = bufferedReader.readLine()) != null) {
			if (line.startsWith("==")) {
				// get the property number
				int propertyNum = Integer
						.parseInt(line.replaceAll("[^0-9.]", ""));

				// if omit some properties
				while (counter < propertyNum) {
					data[1] = counter + "";
					printData(data, pw);
					counter++;
					clearData(data);
				}
			}
			if (line.contains("errors: 0"))
				data[5] = "1";
			if (line.contains("errors: 1"))
				data[5] = "-1";
			if (line.contains("states, stored")) {
				data[2] = line.substring(0, line.indexOf("states")).trim();
			}
			if (line.contains("transitions")) {
				data[3] = line.substring(0, line.indexOf("transitions")).trim();
			}
			if (line.contains("elapsed time")) {
				line = line.replaceAll("pan: elapsed time ", "");
				line = line.replaceAll(" seconds", "");
				data[4] = line;
				numSolved++;
			}
		}
		while (counter <= 20) {
			data[1] = counter + "";
			printData(data, pw);
			counter++;
			clearData(data);
		}
		bufferedReader.close();
		return numSolved;
	}

	public static void printData(String[] data, PrintWriter pw) {
		int len = data.length;

		for (int i = 0; i < len; i++) {
			if (i == len - 1) {
				pw.printf("%-20s\n", data[i]);
			} else {
				pw.printf("%-20s", data[i] + ",");
			}
		}
	}

	public static void clearData(String[] data) {
		int len = data.length;

		for (int i = 1; i < len; i++)
			data[i] = "0";
	}

	public static void main(String[] args) throws IOException {
		PrintWriter pw = new PrintWriter(
				new FileWriter("SpinResult/2017/2017SpinOuput.txt", true));
		int problemSolved = 0;

		pw.printf("%-20s", "Problem");
		pw.printf("%-20s", "Property");
		pw.printf("%-20s", "States");
		pw.printf("%-20s", "Transitions");
		pw.printf("%-20s", "Time(s)");
		pw.printf("%-20s\n", "Result");

		for (int i = 101; i <= 115; i++) {
			String problemNum = i + "";
			String inputPath = "SpinResult/2017/output" + problemNum + ".txt";

			problemSolved = problemSolved
					+ processSpinOutput(problemNum, inputPath, pw);
		}
		pw.print(problemSolved + "/" + (274) + " problems solved.");
		pw.close();
	}
}
