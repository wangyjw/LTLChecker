package edu.udel.cis.vsl.dotchecker.util;

/**
 * Utility class used to deal with operations on strings.
 * 
 * @author yanyihao
 *
 */
public class StringUtil {
	/**
	 * Remove the quotes that surrounds a String.
	 * 
	 * @param str
	 *            The String that will be processed.
	 * @return the string without quotes.
	 */
	public static String removeQuote(String str) {
		int len = str.length();

		if (len >= 2 && str.charAt(0) == '"' && str.charAt(len - 1) == '"') {
			return str.substring(1, len - 1);
		}
		return str;
	}

	/**
	 * <ul>
	 * <li>Replace "G" with "[]"</li>
	 * <li>Replace "F" with "<>"</li>
	 * <li>Replace "=" with "-"</li>
	 * <li>Replace "&" with "&&"</li>
	 * <li>Replace "|" with "||"</li>
	 * <li>Convert all upper letters in identifier with lower letters</li>
	 * </ul>
	 * 
	 * @param formula
	 *            The original formula from RERS competition.
	 * @return the processed formula
	 */
	public static String formulaProcessing(String formula) {
		formula = formula.replace("G(", "[](");
		formula = formula.replace("F(", "<>(");
		formula = formula.replace("=", "-");
		formula = formula.replace("&", "&&");
		formula = formula.replace("|", "||");

		char[] chars = formula.toCharArray();
		int len = chars.length;
		boolean processingIdentifer = false;

		for (int i = 0; i < len; i++) {
			char cur = chars[i];

			if (processingIdentifer) {
				if (cur >= 'A' && cur <= 'Z')
					chars[i] = Character.toLowerCase(cur);
				if (cur == ' ' || cur == '(' || cur == ')')
					processingIdentifer = false;
			} else {
				if ('a' <= cur && cur <= 'z') {
					processingIdentifer = true;
				}
			}
		}
		formula = new String(chars);
		return formula;
	}

	public static void main(String[] args) {
		System.out.println(formulaProcessing(
				"( <>(a19_SIGTTOU || (!a3_SIGBUS W a16_SIGTTIN)) )"));
	}
}
