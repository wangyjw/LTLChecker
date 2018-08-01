// Generated from MyFormula.g4 by ANTLR 4.5.3

   package edu.udel.cis.vsl.dotchecker.property.ltlformula;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MyFormulaParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, NOT=3, OR=4, AND=5, NEXT=6, UNTIL=7, ALWAYS=8, EVENTUALLY=9, 
		RELEASE=10, WEAKLYUNTIL=11, IMPLY=12, ID=13, NUMBER=14, WS=15;
	public static final int
		RULE_mycat = 0, RULE_cat1 = 1, RULE_cat2 = 2;
	public static final String[] ruleNames = {
		"mycat", "cat1", "cat2"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "'!'", null, null, "'X'", "'U'", null, null, "'R'", 
		"'W'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, "NOT", "OR", "AND", "NEXT", "UNTIL", "ALWAYS", "EVENTUALLY", 
		"RELEASE", "WEAKLYUNTIL", "IMPLY", "ID", "NUMBER", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "MyFormula.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MyFormulaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class MycatContext extends ParserRuleContext {
		public TerminalNode ALWAYS() { return getToken(MyFormulaParser.ALWAYS, 0); }
		public Cat1Context cat1() {
			return getRuleContext(Cat1Context.class,0);
		}
		public TerminalNode EVENTUALLY() { return getToken(MyFormulaParser.EVENTUALLY, 0); }
		public Cat2Context cat2() {
			return getRuleContext(Cat2Context.class,0);
		}
		public TerminalNode UNTIL() { return getToken(MyFormulaParser.UNTIL, 0); }
		public TerminalNode WEAKLYUNTIL() { return getToken(MyFormulaParser.WEAKLYUNTIL, 0); }
		public TerminalNode NOT() { return getToken(MyFormulaParser.NOT, 0); }
		public List<MycatContext> mycat() {
			return getRuleContexts(MycatContext.class);
		}
		public MycatContext mycat(int i) {
			return getRuleContext(MycatContext.class,i);
		}
		public TerminalNode AND() { return getToken(MyFormulaParser.AND, 0); }
		public TerminalNode OR() { return getToken(MyFormulaParser.OR, 0); }
		public TerminalNode IMPLY() { return getToken(MyFormulaParser.IMPLY, 0); }
		public MycatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mycat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MyFormulaListener ) ((MyFormulaListener)listener).enterMycat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MyFormulaListener ) ((MyFormulaListener)listener).exitMycat(this);
		}
	}

	public final MycatContext mycat() throws RecognitionException {
		return mycat(0);
	}

	private MycatContext mycat(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		MycatContext _localctx = new MycatContext(_ctx, _parentState);
		MycatContext _prevctx = _localctx;
		int _startState = 0;
		enterRecursionRule(_localctx, 0, RULE_mycat, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(7);
				match(ALWAYS);
				setState(8);
				cat1(0);
				}
				break;
			case 2:
				{
				setState(9);
				match(EVENTUALLY);
				setState(10);
				cat2(0);
				}
				break;
			case 3:
				{
				setState(11);
				cat1(0);
				setState(12);
				match(UNTIL);
				setState(13);
				cat2(0);
				}
				break;
			case 4:
				{
				setState(15);
				cat1(0);
				setState(16);
				match(WEAKLYUNTIL);
				setState(17);
				cat2(0);
				}
				break;
			case 5:
				{
				setState(19);
				match(NOT);
				setState(20);
				mycat(14);
				}
				break;
			case 6:
				{
				setState(21);
				match(ALWAYS);
				setState(22);
				mycat(13);
				}
				break;
			case 7:
				{
				setState(23);
				match(EVENTUALLY);
				setState(24);
				mycat(12);
				}
				break;
			case 8:
				{
				setState(25);
				match(T__0);
				setState(26);
				mycat(0);
				setState(27);
				match(T__1);
				}
				break;
			case 9:
				{
				setState(29);
				match(T__0);
				setState(30);
				match(ALWAYS);
				setState(31);
				cat1(0);
				setState(32);
				match(T__1);
				}
				break;
			case 10:
				{
				setState(34);
				match(T__0);
				setState(35);
				match(EVENTUALLY);
				setState(36);
				cat2(0);
				setState(37);
				match(T__1);
				}
				break;
			case 11:
				{
				setState(39);
				match(T__0);
				setState(40);
				cat1(0);
				setState(41);
				match(UNTIL);
				setState(42);
				cat2(0);
				setState(43);
				match(T__1);
				}
				break;
			case 12:
				{
				setState(45);
				match(T__0);
				setState(46);
				cat1(0);
				setState(47);
				match(WEAKLYUNTIL);
				setState(48);
				cat2(0);
				setState(49);
				match(T__1);
				}
				break;
			case 13:
				{
				setState(51);
				match(T__0);
				setState(52);
				mycat(0);
				setState(53);
				match(AND);
				setState(54);
				mycat(0);
				setState(55);
				match(T__1);
				}
				break;
			case 14:
				{
				setState(57);
				match(T__0);
				setState(58);
				mycat(0);
				setState(59);
				match(OR);
				setState(60);
				mycat(0);
				setState(61);
				match(T__1);
				}
				break;
			case 15:
				{
				setState(63);
				match(T__0);
				setState(64);
				mycat(0);
				setState(65);
				match(IMPLY);
				setState(66);
				mycat(0);
				setState(67);
				match(T__1);
				}
				break;
			case 16:
				{
				setState(69);
				match(T__0);
				setState(70);
				match(NOT);
				setState(71);
				mycat(0);
				setState(72);
				match(T__1);
				}
				break;
			case 17:
				{
				setState(74);
				match(T__0);
				setState(75);
				match(ALWAYS);
				setState(76);
				mycat(0);
				setState(77);
				match(T__1);
				}
				break;
			case 18:
				{
				setState(79);
				match(T__0);
				setState(80);
				match(EVENTUALLY);
				setState(81);
				mycat(0);
				setState(82);
				match(T__1);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(97);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(95);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
					case 1:
						{
						_localctx = new MycatContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_mycat);
						setState(86);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(87);
						match(AND);
						setState(88);
						mycat(18);
						}
						break;
					case 2:
						{
						_localctx = new MycatContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_mycat);
						setState(89);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(90);
						match(OR);
						setState(91);
						mycat(17);
						}
						break;
					case 3:
						{
						_localctx = new MycatContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_mycat);
						setState(92);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(93);
						match(IMPLY);
						setState(94);
						mycat(16);
						}
						break;
					}
					} 
				}
				setState(99);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Cat1Context extends ParserRuleContext {
		public TerminalNode NOT() { return getToken(MyFormulaParser.NOT, 0); }
		public TerminalNode ID() { return getToken(MyFormulaParser.ID, 0); }
		public TerminalNode IMPLY() { return getToken(MyFormulaParser.IMPLY, 0); }
		public MycatContext mycat() {
			return getRuleContext(MycatContext.class,0);
		}
		public List<Cat1Context> cat1() {
			return getRuleContexts(Cat1Context.class);
		}
		public Cat1Context cat1(int i) {
			return getRuleContext(Cat1Context.class,i);
		}
		public TerminalNode AND() { return getToken(MyFormulaParser.AND, 0); }
		public TerminalNode OR() { return getToken(MyFormulaParser.OR, 0); }
		public TerminalNode NEXT() { return getToken(MyFormulaParser.NEXT, 0); }
		public Cat1Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cat1; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MyFormulaListener ) ((MyFormulaListener)listener).enterCat1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MyFormulaListener ) ((MyFormulaListener)listener).exitCat1(this);
		}
	}

	public final Cat1Context cat1() throws RecognitionException {
		return cat1(0);
	}

	private Cat1Context cat1(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Cat1Context _localctx = new Cat1Context(_ctx, _parentState);
		Cat1Context _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_cat1, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				setState(101);
				match(NOT);
				setState(102);
				match(ID);
				}
				break;
			case 2:
				{
				setState(103);
				match(T__0);
				setState(104);
				match(NOT);
				setState(105);
				match(ID);
				setState(106);
				match(T__1);
				}
				break;
			case 3:
				{
				setState(107);
				match(ID);
				setState(108);
				match(IMPLY);
				setState(109);
				mycat(0);
				}
				break;
			case 4:
				{
				setState(110);
				match(T__0);
				setState(111);
				match(ID);
				setState(112);
				match(IMPLY);
				setState(113);
				mycat(0);
				setState(114);
				match(T__1);
				}
				break;
			case 5:
				{
				setState(116);
				match(T__0);
				setState(117);
				cat1(0);
				setState(118);
				match(AND);
				setState(119);
				cat1(0);
				setState(120);
				match(T__1);
				}
				break;
			case 6:
				{
				setState(122);
				match(T__0);
				setState(123);
				cat1(0);
				setState(124);
				match(OR);
				setState(125);
				cat1(0);
				setState(126);
				match(T__1);
				}
				break;
			case 7:
				{
				setState(128);
				match(ID);
				setState(129);
				match(IMPLY);
				setState(130);
				match(T__0);
				setState(131);
				match(NEXT);
				setState(132);
				mycat(0);
				setState(133);
				match(T__1);
				}
				break;
			case 8:
				{
				setState(135);
				match(T__0);
				setState(136);
				match(ID);
				setState(137);
				match(IMPLY);
				setState(138);
				match(T__0);
				setState(139);
				match(NEXT);
				setState(140);
				mycat(0);
				setState(141);
				match(T__1);
				setState(142);
				match(T__1);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(154);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(152);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
					case 1:
						{
						_localctx = new Cat1Context(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_cat1);
						setState(146);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(147);
						match(AND);
						setState(148);
						cat1(7);
						}
						break;
					case 2:
						{
						_localctx = new Cat1Context(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_cat1);
						setState(149);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(150);
						match(OR);
						setState(151);
						cat1(5);
						}
						break;
					}
					} 
				}
				setState(156);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Cat2Context extends ParserRuleContext {
		public TerminalNode ID() { return getToken(MyFormulaParser.ID, 0); }
		public List<Cat2Context> cat2() {
			return getRuleContexts(Cat2Context.class);
		}
		public Cat2Context cat2(int i) {
			return getRuleContext(Cat2Context.class,i);
		}
		public TerminalNode OR() { return getToken(MyFormulaParser.OR, 0); }
		public TerminalNode AND() { return getToken(MyFormulaParser.AND, 0); }
		public TerminalNode NEXT() { return getToken(MyFormulaParser.NEXT, 0); }
		public MycatContext mycat() {
			return getRuleContext(MycatContext.class,0);
		}
		public Cat2Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cat2; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MyFormulaListener ) ((MyFormulaListener)listener).enterCat2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MyFormulaListener ) ((MyFormulaListener)listener).exitCat2(this);
		}
	}

	public final Cat2Context cat2() throws RecognitionException {
		return cat2(0);
	}

	private Cat2Context cat2(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Cat2Context _localctx = new Cat2Context(_ctx, _parentState);
		Cat2Context _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_cat2, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(192);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(158);
				match(ID);
				}
				break;
			case 2:
				{
				setState(159);
				match(T__0);
				setState(160);
				match(ID);
				setState(161);
				match(T__1);
				}
				break;
			case 3:
				{
				setState(162);
				match(T__0);
				setState(163);
				cat2(0);
				setState(164);
				match(OR);
				setState(165);
				cat2(0);
				setState(166);
				match(T__1);
				}
				break;
			case 4:
				{
				setState(168);
				match(T__0);
				setState(169);
				cat2(0);
				setState(170);
				match(AND);
				setState(171);
				cat2(0);
				setState(172);
				match(T__1);
				}
				break;
			case 5:
				{
				setState(174);
				match(T__0);
				setState(175);
				match(ID);
				setState(176);
				match(AND);
				setState(177);
				match(T__0);
				setState(178);
				match(NEXT);
				setState(179);
				mycat(0);
				setState(180);
				match(T__1);
				setState(181);
				match(T__1);
				}
				break;
			case 6:
				{
				setState(183);
				match(ID);
				setState(184);
				match(AND);
				setState(185);
				mycat(0);
				}
				break;
			case 7:
				{
				setState(186);
				match(T__0);
				setState(187);
				match(ID);
				setState(188);
				match(AND);
				setState(189);
				mycat(0);
				setState(190);
				match(T__1);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(202);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(200);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
					case 1:
						{
						_localctx = new Cat2Context(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_cat2);
						setState(194);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(195);
						match(OR);
						setState(196);
						cat2(8);
						}
						break;
					case 2:
						{
						_localctx = new Cat2Context(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_cat2);
						setState(197);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(198);
						match(AND);
						setState(199);
						cat2(6);
						}
						break;
					}
					} 
				}
				setState(204);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 0:
			return mycat_sempred((MycatContext)_localctx, predIndex);
		case 1:
			return cat1_sempred((Cat1Context)_localctx, predIndex);
		case 2:
			return cat2_sempred((Cat2Context)_localctx, predIndex);
		}
		return true;
	}
	private boolean mycat_sempred(MycatContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 17);
		case 1:
			return precpred(_ctx, 16);
		case 2:
			return precpred(_ctx, 15);
		}
		return true;
	}
	private boolean cat1_sempred(Cat1Context _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 6);
		case 4:
			return precpred(_ctx, 4);
		}
		return true;
	}
	private boolean cat2_sempred(Cat2Context _localctx, int predIndex) {
		switch (predIndex) {
		case 5:
			return precpred(_ctx, 7);
		case 6:
			return precpred(_ctx, 5);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\21\u00d0\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2W\n\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\7\2b\n\2\f\2\16\2e\13\2\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\5\3\u0093\n\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3\u009b\n\3\f\3"+
		"\16\3\u009e\13\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\5\4\u00c3\n\4\3\4\3\4\3\4\3\4\3\4\3\4\7\4\u00cb\n\4\f"+
		"\4\16\4\u00ce\13\4\3\4\2\5\2\4\6\5\2\4\6\2\2\u00f1\2V\3\2\2\2\4\u0092"+
		"\3\2\2\2\6\u00c2\3\2\2\2\b\t\b\2\1\2\t\n\7\n\2\2\nW\5\4\3\2\13\f\7\13"+
		"\2\2\fW\5\6\4\2\r\16\5\4\3\2\16\17\7\t\2\2\17\20\5\6\4\2\20W\3\2\2\2\21"+
		"\22\5\4\3\2\22\23\7\r\2\2\23\24\5\6\4\2\24W\3\2\2\2\25\26\7\5\2\2\26W"+
		"\5\2\2\20\27\30\7\n\2\2\30W\5\2\2\17\31\32\7\13\2\2\32W\5\2\2\16\33\34"+
		"\7\3\2\2\34\35\5\2\2\2\35\36\7\4\2\2\36W\3\2\2\2\37 \7\3\2\2 !\7\n\2\2"+
		"!\"\5\4\3\2\"#\7\4\2\2#W\3\2\2\2$%\7\3\2\2%&\7\13\2\2&\'\5\6\4\2\'(\7"+
		"\4\2\2(W\3\2\2\2)*\7\3\2\2*+\5\4\3\2+,\7\t\2\2,-\5\6\4\2-.\7\4\2\2.W\3"+
		"\2\2\2/\60\7\3\2\2\60\61\5\4\3\2\61\62\7\r\2\2\62\63\5\6\4\2\63\64\7\4"+
		"\2\2\64W\3\2\2\2\65\66\7\3\2\2\66\67\5\2\2\2\678\7\7\2\289\5\2\2\29:\7"+
		"\4\2\2:W\3\2\2\2;<\7\3\2\2<=\5\2\2\2=>\7\6\2\2>?\5\2\2\2?@\7\4\2\2@W\3"+
		"\2\2\2AB\7\3\2\2BC\5\2\2\2CD\7\16\2\2DE\5\2\2\2EF\7\4\2\2FW\3\2\2\2GH"+
		"\7\3\2\2HI\7\5\2\2IJ\5\2\2\2JK\7\4\2\2KW\3\2\2\2LM\7\3\2\2MN\7\n\2\2N"+
		"O\5\2\2\2OP\7\4\2\2PW\3\2\2\2QR\7\3\2\2RS\7\13\2\2ST\5\2\2\2TU\7\4\2\2"+
		"UW\3\2\2\2V\b\3\2\2\2V\13\3\2\2\2V\r\3\2\2\2V\21\3\2\2\2V\25\3\2\2\2V"+
		"\27\3\2\2\2V\31\3\2\2\2V\33\3\2\2\2V\37\3\2\2\2V$\3\2\2\2V)\3\2\2\2V/"+
		"\3\2\2\2V\65\3\2\2\2V;\3\2\2\2VA\3\2\2\2VG\3\2\2\2VL\3\2\2\2VQ\3\2\2\2"+
		"Wc\3\2\2\2XY\f\23\2\2YZ\7\7\2\2Zb\5\2\2\24[\\\f\22\2\2\\]\7\6\2\2]b\5"+
		"\2\2\23^_\f\21\2\2_`\7\16\2\2`b\5\2\2\22aX\3\2\2\2a[\3\2\2\2a^\3\2\2\2"+
		"be\3\2\2\2ca\3\2\2\2cd\3\2\2\2d\3\3\2\2\2ec\3\2\2\2fg\b\3\1\2gh\7\5\2"+
		"\2h\u0093\7\17\2\2ij\7\3\2\2jk\7\5\2\2kl\7\17\2\2l\u0093\7\4\2\2mn\7\17"+
		"\2\2no\7\16\2\2o\u0093\5\2\2\2pq\7\3\2\2qr\7\17\2\2rs\7\16\2\2st\5\2\2"+
		"\2tu\7\4\2\2u\u0093\3\2\2\2vw\7\3\2\2wx\5\4\3\2xy\7\7\2\2yz\5\4\3\2z{"+
		"\7\4\2\2{\u0093\3\2\2\2|}\7\3\2\2}~\5\4\3\2~\177\7\6\2\2\177\u0080\5\4"+
		"\3\2\u0080\u0081\7\4\2\2\u0081\u0093\3\2\2\2\u0082\u0083\7\17\2\2\u0083"+
		"\u0084\7\16\2\2\u0084\u0085\7\3\2\2\u0085\u0086\7\b\2\2\u0086\u0087\5"+
		"\2\2\2\u0087\u0088\7\4\2\2\u0088\u0093\3\2\2\2\u0089\u008a\7\3\2\2\u008a"+
		"\u008b\7\17\2\2\u008b\u008c\7\16\2\2\u008c\u008d\7\3\2\2\u008d\u008e\7"+
		"\b\2\2\u008e\u008f\5\2\2\2\u008f\u0090\7\4\2\2\u0090\u0091\7\4\2\2\u0091"+
		"\u0093\3\2\2\2\u0092f\3\2\2\2\u0092i\3\2\2\2\u0092m\3\2\2\2\u0092p\3\2"+
		"\2\2\u0092v\3\2\2\2\u0092|\3\2\2\2\u0092\u0082\3\2\2\2\u0092\u0089\3\2"+
		"\2\2\u0093\u009c\3\2\2\2\u0094\u0095\f\b\2\2\u0095\u0096\7\7\2\2\u0096"+
		"\u009b\5\4\3\t\u0097\u0098\f\6\2\2\u0098\u0099\7\6\2\2\u0099\u009b\5\4"+
		"\3\7\u009a\u0094\3\2\2\2\u009a\u0097\3\2\2\2\u009b\u009e\3\2\2\2\u009c"+
		"\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d\5\3\2\2\2\u009e\u009c\3\2\2\2"+
		"\u009f\u00a0\b\4\1\2\u00a0\u00c3\7\17\2\2\u00a1\u00a2\7\3\2\2\u00a2\u00a3"+
		"\7\17\2\2\u00a3\u00c3\7\4\2\2\u00a4\u00a5\7\3\2\2\u00a5\u00a6\5\6\4\2"+
		"\u00a6\u00a7\7\6\2\2\u00a7\u00a8\5\6\4\2\u00a8\u00a9\7\4\2\2\u00a9\u00c3"+
		"\3\2\2\2\u00aa\u00ab\7\3\2\2\u00ab\u00ac\5\6\4\2\u00ac\u00ad\7\7\2\2\u00ad"+
		"\u00ae\5\6\4\2\u00ae\u00af\7\4\2\2\u00af\u00c3\3\2\2\2\u00b0\u00b1\7\3"+
		"\2\2\u00b1\u00b2\7\17\2\2\u00b2\u00b3\7\7\2\2\u00b3\u00b4\7\3\2\2\u00b4"+
		"\u00b5\7\b\2\2\u00b5\u00b6\5\2\2\2\u00b6\u00b7\7\4\2\2\u00b7\u00b8\7\4"+
		"\2\2\u00b8\u00c3\3\2\2\2\u00b9\u00ba\7\17\2\2\u00ba\u00bb\7\7\2\2\u00bb"+
		"\u00c3\5\2\2\2\u00bc\u00bd\7\3\2\2\u00bd\u00be\7\17\2\2\u00be\u00bf\7"+
		"\7\2\2\u00bf\u00c0\5\2\2\2\u00c0\u00c1\7\4\2\2\u00c1\u00c3\3\2\2\2\u00c2"+
		"\u009f\3\2\2\2\u00c2\u00a1\3\2\2\2\u00c2\u00a4\3\2\2\2\u00c2\u00aa\3\2"+
		"\2\2\u00c2\u00b0\3\2\2\2\u00c2\u00b9\3\2\2\2\u00c2\u00bc\3\2\2\2\u00c3"+
		"\u00cc\3\2\2\2\u00c4\u00c5\f\t\2\2\u00c5\u00c6\7\6\2\2\u00c6\u00cb\5\6"+
		"\4\n\u00c7\u00c8\f\7\2\2\u00c8\u00c9\7\7\2\2\u00c9\u00cb\5\6\4\b\u00ca"+
		"\u00c4\3\2\2\2\u00ca\u00c7\3\2\2\2\u00cb\u00ce\3\2\2\2\u00cc\u00ca\3\2"+
		"\2\2\u00cc\u00cd\3\2\2\2\u00cd\7\3\2\2\2\u00ce\u00cc\3\2\2\2\13Vac\u0092"+
		"\u009a\u009c\u00c2\u00ca\u00cc";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}