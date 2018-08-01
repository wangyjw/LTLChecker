package edu.udel.cis.vsl.dotchecker.processoutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ProcessCheckerOutput {
	public static int processSpinOutput(String problemNum, String inputpath,
			PrintWriter pw) throws IOException {
		File input = new File(inputpath);
		FileReader fileReader = new FileReader(input);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;
		int numSolved = 0;
		int counter = 1;
		String[] data = {problemNum, "0", "0", "0", "0", "0"};
		boolean flag = false;

		while ((line = bufferedReader.readLine()) != null) {
			if (line.contains("***")) {
				int propertyNum = Integer.parseInt(
						line.substring(line.indexOf(" ") + 1, line.length()));

				if (!flag) {
					clearData(data);
				}
				while (counter < propertyNum) {
					data[1] = counter + "";
					printData(data, pw);
					counter++;
					clearData(data);
				}
				flag = false;
			}
			if (line.contains("false"))
				data[5] = "-1";
			if (line.contains("true"))
				data[5] = "1";
			if (line.contains("states"))
				data[2] = line.substring(line.indexOf(":") + 1, line.length());
			if (line.contains("transitions"))
				data[3] = line.substring(line.indexOf(":") + 1, line.length());
			if (line.matches("[0-9]+(s|ms)")) {
				flag = true;
				if (line.endsWith("ms"))
					data[4] = Double.parseDouble(line.replaceAll("[^0-9.]", ""))
							/ 1000 + "";
				else
					data[4] = Double.parseDouble(line.replaceAll("[^0-9.]", ""))
							+ "";
				numSolved++;
			}
		}
		if (!flag)
			clearData(data);
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
		PrintWriter pw = new PrintWriter(new FileWriter(
				"trainingCheckerWithReductionOuput.txt", true));
		int problemSolved = 0;
		pw.printf("%-20s", "Problem");
		pw.printf("%-20s", "Property");
		pw.printf("%-20s", "States");
		pw.printf("%-20s", "Transitions");
		pw.printf("%-20s", "Time(s)");
		pw.printf("%-20s\n", "Result");
		for (int i = 101; i <= 120; i++) {
			String problemNum = i + "";
			String inputPath = "training-" + problemNum
					+ "-with-reduction-no-collapse-output.txt";

			problemSolved = problemSolved
					+ processSpinOutput(problemNum, inputPath, pw);
		}
		pw.print(problemSolved + "/" + (20 * 20 - 1) + " problems solved.");

		pw.close();
	}
}
