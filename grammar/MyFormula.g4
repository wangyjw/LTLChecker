grammar MyFormula;

@header {
   package edu.udel.cis.vsl.dotchecker.property.ltlformula;
}

mycat
	: ALWAYS cat1
	| EVENTUALLY cat2 
	| cat1 UNTIL cat2
	| cat1 WEAKLYUNTIL cat2 
	| mycat AND mycat 
	| mycat OR mycat 
	| mycat IMPLY mycat
	| NOT mycat 
	| ALWAYS mycat 
	| mycat UNTIL cat2 
	| mycat WEAKLYUNTIL cat2
	| '('mycat')'
	| '('ALWAYS cat1')'
	| '('EVENTUALLY cat2')'  
	| '('cat1 UNTIL cat2')'
	| '('cat1 WEAKLYUNTIL cat2')' 
	| '('mycat AND mycat')'
	| '('mycat OR mycat')'
	| '('mycat IMPLY mycat')'
	| '('NOT mycat')'
	| '('ALWAYS mycat')'
	| '('mycat UNTIL cat2')' 
	| '('mycat WEAKLYUNTIL cat2')'
	;

cat1
	: NOT ID 
	| '('NOT ID')' 
	| ID IMPLY mycat 
	| '('ID IMPLY mycat')'
	| cat1 AND cat1
	| '('cat1 AND cat1')'
	;
	
cat2
	: ID 
	| '('ID')'
	| mycat 
	| '('mycat')'
	| cat2 OR cat2
	| '('cat2 OR cat2')'
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