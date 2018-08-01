grammar LTL;

@header {
   package edu.udel.cis.vsl.ltl.frontend;
}

formula
   : ID
   | formula binaryOperator formula
   | '(' formula binaryOperator formula ')'
   | '(' formula ')'
   | unaryFormula
   ;

unaryFormula
   : unaryOperator formula 
   | unaryOperator '(' formula ')'
   | '(' unaryOperator formula ')' 
   | '(' unaryOperator '(' formula ')' ')'
   ; 

unaryOperator
   : NOT | NEXT | ALWAYS | EVENTUALLY
   ;

binaryOperator
   : AND | OR | UNTIL | WEAKLYUNTIL | RELEASE | IMPLY
   ;

NOT
   : '!'
   ;

OR
   : '|''|'
   ;

AND
   : '&''&'
   ;

NEXT
   : 'X'
   ;

UNTIL
   : 'U'
   ;

ALWAYS
   : 'G' | '['']'
   ;

EVENTUALLY
   : 'F' | '<''>'
   ;

RELEASE
   : 'R'
   ;

WEAKLYUNTIL
   : 'W'
   ;

IMPLY
   : '-''>'
   ;

ID
   : LETTER ( LETTER | DIGIT )*
   ;

NUMBER
   :  DIGIT+
   ;

fragment DIGIT
   : [0-9]
   ;

fragment LETTER
   : [a-zA-Z\u0080-\u00FF_]
   ;

WS
   : [ \t\n\r]+ -> skip
   ;