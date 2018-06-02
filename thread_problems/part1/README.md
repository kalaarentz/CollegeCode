# CS441/541 Project 4 - Part 1

## Author(s):

Derek Stoner, Ben Black


## Date:

10/19/2017


## Description:

Program that mimics a bounded buffer with consumer(s)/producer(s)


## How to build the software

make bounded-buffer


## How to use the software

./bounded-buffer (time to live) (number of producer threads) (number of consumer threads)
(buffer size[optional])


## How the software was tested

The program has built in testing to report errors if wither the producer or consumer get call functions when they shouldn't
Input less than three args to test for an error being thrown
Input invalid args to test for an error to be thrown
Input a multiple producers with only one consumer to test a full buffer
Input one producer with multiple consumers to test empty buffer
Input multiple producers and consumers to check that each thread attains the mutex with no starvation
Input only three ardgs to test the default buffer size
Input four args to test the user defined buffer size
Input small time to live and large time to live


## Known bugs and problem areas

None
