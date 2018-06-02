Author: Samantha Foley
Date: 11/28/2016

How to use this template
========================

This template contains the following source code files:

 - solver.c : the set of functions you are to write.  THIS IS THE ONLY FILE YOU SHOULD MODIFY!!
 - solver.h : the prototypes for the functions you are to write.
 - test_how_many_bits.c : a driver program that tests your how_many_bits() function.
 - test_string_to_binary.c : a driver program that tests your string_to_binary() function.
 - test_solve_exp.c : a driver program that tests your solve_exp() function.

Other files include:
 - Makefile : this will build all of the source files.
 - test_*.txt : testing input files.
 - test_*.out : correct output for the matching input file.

How to run a test
=================

To test your code, first compile all the code using the Makefile and fix any compiler errors.

shell> make

To run a test, for example test_how_many_bits, run the executable and redirect the input file as the input, redirect the output to another file, then compare your output to the reference output.  You can use the UNIX utility diff to do this for you.

shell> ./test_how_many_bits < test_how_many_bits_input1.txt > myoutput
shell> diff test_how_many_bits_input1.out myoutput

If there is no difference, nothing will be returned from diff.  If there is a difference then you will see which lines are different.

Below is an example of files that are not the same:

shell> diff myoutput test_how_many_bits_input1.out 
1c1
< Number of bits needed to represent 1 is 0.
---
> Number of bits needed to represent 1 is 2.