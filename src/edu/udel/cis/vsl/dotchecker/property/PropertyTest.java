package edu.udel.cis.vsl.dotchecker.property;

public class PropertyTest {

	public static void main(String[] args) {
		LTL2BuchiTranslator translator = new LTL2BuchiTranslator();
		AbstractPropertyAutomaton automaton = translator
				.translate(" ( [](!c1_t5 && (c0_t15 -> <>(c3_t3))) ) ");
		String filename = "/Users/yanyihao/Desktop/a.dot";

		AutomatonDebugUntil.automatonToDot(automaton, filename);

		System.out.println(translator.isPropertyBlankInvariant(automaton));
	}
}
