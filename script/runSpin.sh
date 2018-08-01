#!/bin/bash

SPIN=spin
GCC=gcc
OUTPUT=output101.txt
FILE=problem101.pml
SpinRun="./pan -a -n -m100000000 -N "
SpinCleanAll="rm pan*"

spin_compile() {
   $SPIN -a $FILE
   $GCC -O3 -DNOFAIR -DNOBOUNDCHECK -o pan pan.c
}

run_spin() {
    spin_compile
    for i in {1..20}
    do
	echo "===============$i==============">>$OUTPUT
	$SpinRun p$i>> $OUTPUT
    done
}

run_spin