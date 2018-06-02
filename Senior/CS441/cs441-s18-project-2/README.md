# CS441/541 Project 2

## Author(s):

Kala Arentz


## Date:

2/14/18


## Description:

This program will implement a CPU scheduling simulator. The program will read a file, and it will include the following information:\n
    - number of processes\n
    - process arrival order\n
    - CPU burst per processes\n
    - priority per process.

 In addition, the program will analyze the command line arguments, it will include a three possible items. They may occur in any order:\n
    - -s # -> number will identify the scheduling algorithm\n
    - -q # -> the quantum to use for the Round-Robin scheduling algorithms\n
    - handling the first file

The file will provide all the information for scheduling. They first line will contain a single number identifying the number of processes in the file that will need to be scheduled.
Every following line will have the Process Identifier, the CPU burst length, and the priority
level. The file will be formatted such as given-tests/level1.txt:
```
3
1 27 1
2 3 1
3 3 1
```

This program will print out this formatted string of information after the
algorithm has run:

Scheduler : 4 RR\n
Quantum : 3\n
Sch. File : given-tests/level3.txt\n
/-------------------------------\n
Arrival Order: 4, 2, 3, 1\n
Process Information:\n
 4    3   7\n
 2    3  10\n
 3    5   7\n
 1    7   1\n
/-------------------------------\n
Running...\n
/##################################################\n
/# #  CPU   Pri   W   T\n
/# 4  3     7     0   3\n
/# 2  3     10    3   6\n
/# 3  5     7     9   14\n
/# 1  7     1     11  18\n
/##################################################\n
/# Avg. Waiting Time : 5.75\n
/# Avg. Turnaround Time:  10.25\n
/##################################################\n


## How to build the software

To build the software, you will call the command:
```
make
```
. If no errors or warnings are generated you will see the following:
```
gcc -o scheduler -Wall -g -O0 scheduler.c
```
. To run the the program, you will run the command:
```
./scheduler -s 1 -q 5 given-tests/level1.txt
```
. There are three items that can be given to the scheduler program.
  - -s # -> number will identify the scheduling algorithm
  - -q # -> the quantum to use for the Round-Robin scheduling algorithms
  - the file to be read
The file to be read, and scheduling algorithm must be given when calling
this program. -q is only needed when the 4th scheduling algorithm is used,
or Round Robin.

## How to use the software

If the file has been compiled correctly, you may run the program.
To do this, you will need to run the command:
```
./scheduler -s 1 -q 5 given-tests/level1.txt
```
. There are three items that can be given to the scheduler program.
  - -s # -> number will identify the scheduling algorithm
  - -q # -> the quantum to use for the Round-Robin scheduling algorithms
  - the file to be read
The file to be read, and scheduling algorithm must be given when calling
this program. -q is only needed when the 4th scheduling algorithm is used,
or Round Robin. The order in which the arguments does not matter, except
"./scheduler" must be first.

After using the program, you should clean up all the files. To clean up
the files, run the following command:
```
make clean
```
. The console will print out the following lines of code if the cleaning
was successful.
```
rm -f scheduler
rm -f -rf *.dSYM
rm -f test_out
```


## How the software was tested

The first section of testing was done by utilizing the given test cases
given by Professor Foley. To test the program, run the following command:
```
make check
```
. This will check all 7 levels of the files given by Foley. The seven text files
give the process identifier, process CPU burst length, and the priority level.
Each of these 7 files are tested 6 times by giving different arguments in the
command lines. Each of the files are testing the First Come First Serve,
Shortest Job First, Priority, and Round Robin. The Round Robin scheduling
algorithm was tested 3 different times with 3 different quantum numbers. The
quantum numbers tested are 2, 5, and 10.

If all the files are successful, the following code will be displayed on the
console:

```
Running test 1: ./scheduler -s 1       given-tests/level1.txt
Running test 2: ./scheduler -s 2       given-tests/level1.txt
Running test 3: ./scheduler -s 3       given-tests/level1.txt
Running test 4: ./scheduler -s 4 -q 10 given-tests/level1.txt
Running test 5: ./scheduler -s 4 -q 2  given-tests/level1.txt
Running test 6: ./scheduler -s 4 -q 5  given-tests/level1.txt
Running test 7: ./scheduler -s 1       given-tests/level2.txt
Running test 8: ./scheduler -s 2       given-tests/level2.txt
Running test 9: ./scheduler -s 3       given-tests/level2.txt
Running test 10: ./scheduler -s 4 -q 10 given-tests/level2.txt
Running test 11: ./scheduler -s 4 -q 2  given-tests/level2.txt
Running test 12: ./scheduler -s 4 -q 5  given-tests/level2.txt
Running test 13: ./scheduler -s 1       given-tests/level3.txt
Running test 14: ./scheduler -s 2       given-tests/level3.txt
Running test 15: ./scheduler -s 3       given-tests/level3.txt
Running test 16: ./scheduler -s 4 -q 10 given-tests/level3.txt
Running test 17: ./scheduler -s 4 -q 2  given-tests/level3.txt
Running test 18: ./scheduler -s 4 -q 5  given-tests/level3.txt
Running test 19: ./scheduler -s 1       given-tests/level4.txt
Running test 20: ./scheduler -s 2       given-tests/level4.txt
Running test 21: ./scheduler -s 3       given-tests/level4.txt
Running test 22: ./scheduler -s 4 -q 10 given-tests/level4.txt
Running test 23: ./scheduler -s 4 -q 2  given-tests/level4.txt
Running test 24: ./scheduler -s 4 -q 5  given-tests/level4.txt
Running test 25: ./scheduler -s 1       given-tests/level5.txt
Running test 26: ./scheduler -s 2       given-tests/level5.txt
Running test 27: ./scheduler -s 3       given-tests/level5.txt
Running test 28: ./scheduler -s 4 -q 10 given-tests/level5.txt
Running test 29: ./scheduler -s 4 -q 2  given-tests/level5.txt
Running test 30: ./scheduler -s 4 -q 5  given-tests/level5.txt
Running test 31: ./scheduler -s 1       given-tests/level6.txt
Running test 32: ./scheduler -s 2       given-tests/level6.txt
Running test 33: ./scheduler -s 3       given-tests/level6.txt
Running test 34: ./scheduler -s 4 -q 10 given-tests/level6.txt
Running test 35: ./scheduler -s 4 -q 2  given-tests/level6.txt
Running test 36: ./scheduler -s 4 -q 5  given-tests/level6.txt
Running test 37: ./scheduler -s 1       given-tests/level7.txt
Running test 38: ./scheduler -s 2       given-tests/level7.txt
Running test 39: ./scheduler -s 3       given-tests/level7.txt
Running test 40: ./scheduler -s 4 -q 10 given-tests/level7.txt
Running test 41: ./scheduler -s 4 -q 2  given-tests/level7.txt
Running test 42: ./scheduler -s 4 -q 5  given-tests/level7.txt
Autograde score: 42 / 42

Code Inspection Notes:
Total:  /  
Autolab score:
{"scores":{"Correctness":42}}
```



## Test Suite

### Testing Command Line arguments
The command line arguments were tested with a various array of different
arguments passed along.\n
  - If Round Robin is chosen and no quantum number is given greater than 0
    then error will be displayed:\n
    ```
    Error: Need a quantum greater than zero for Round Robin.
    ```
    If the quantum number given is not greater than 0, an Error is displayed as
    well.\n
    ```
    Error: Quantum must be greater than zero.
    ```
  - If no file to read is passed, than an error is passed about no file to read\n
    ```
    Error: Need a file to read.
    ```
  - If no scheduling algorithm number is given than an Error is displayed.\n
    ```
    Error: Need a schedule to run.
    ```
    In addition, error checking for the schedule algorithm includes
    checking if the number given is between 1-4. If any number is less than that
    or greater, an Error is given\n
    ```
    Error: Scheduling algorithm must be between 1 and 4.
    ```
  - If the value following -s or -q are not a number, a error message will
    be displayed. Each one will display this message:\n
    * -q\n
    ```
    Error: Not enough arguments for the -q option.
    ```
    * -s\n
    ```
    Error: Not enough arguments for the -s option.
    ```
  - If the file given is cannot be read, the program will output and error
    and will exit the program. The project documentation said the files would
    be formatted correctly, so error checking goes toward the file format.\n
    ```
    Cannot open the file: giventests/file
    ```

### Files in Tests Folder
These are test cases I created to test certain items.
More details will be provided in the tests/README.md file

  - test_format.txt: This file is specifically allowing me to test formatting
    strings without having to look at the level7.txt files. It is easier to test
    a smaller file, with larger numbers automatically.

  - priority_cases.txt: This file is specifically allowing me to test priority
    cases in which the priority numbers are the same, and how you determine
    which ones are chosen. If they priorities are equal, you choose whatever one
    was in the arrival order first.

  - sjf_cases.txt: This file is specifically testing the order in which
    to take the short job first if the CPU time is the same. You would choose
    whatever one was in the arrival order first.

  - rr_cases.txt: This file is designed to help me test the Round Robin 
    algorithm. The file was used during the development of the algorithm
    I used in the scheduler.c program.I tested with multiple different
    quantum numbers.

## Known bugs and problem areas

No known bugs or problem areas.

## Other resources used in development

- https://www.tutorialspoint.com/cprogramming/c_file_io.htm
- http://www.sanfoundry.com/c-program-illustrate-reading-datafile/
