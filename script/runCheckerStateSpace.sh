#!/bin/bash

JAVA="java -jar "
INPUT=problem
PROBLEM_EXTENSION=.dot
PROPERTY_EXTENSION=-properties.txt
OUTPUTPREFIX=2017-
OUTPUTEXTENSION=-with-reduction-output.txt
DOTCHECKER=DotCheckerStateSpace.jar

run() {

	for j in {110..112}
	do
	    $JAVA $DOTCHECKER $INPUT$j$PROBLEM_EXTENSION $INPUT$j$PROPERTY_EXTENSION>> $OUTPUTPREFIX$j$OUTPUTEXTENSION
	done

}

run