# CS441/541 Project 3

## Author(s):

(Group 3)
Kala Arentz
Ben Klipfel


## Date:

2/27/18


## Description:

This project will implement a command line interpreter or shell. The shell must be able to be run in two different modes: interactive and batch. In both the interactive and batch mode, multiple jobs can be specified on a command line, and these jobs will be separated by [;]. For example:
```
mysh$ date ; pwd ; ls tests
Wed Sept 17 10:00:00 CDT 2017
/home/grace/project2/part2
file1.txt file.txt
```

In both the interactive and batch modes, a job may run in the 'background.' The job can be processing while the user is entering more commands or other jobs are running. Jobs are being run in the background are followed by an ampersand [&]. Background jobs that have been completed will be marked as 'Done' and can be cleaned up only after the user calls the 'job' or 'exit' command.

This program will support the following commands:
  1. jobs : The ability to display a list of jobs that are currently running in the background.
  2. history : In both interactive and batch modes, the ability to display the full history of job commands typed into the shell.
  3. wait : In both interactive and batch modes, the ability to wait for all currently backgrounded jobs to complete.
  4. fg : In both interactive and batch modes, the ability to wait for a specific currently backgrounded job. If no jobs, than the command returns an error. It will take one or zero arguments.
  5. exit : In both interactive and batch modes, the shell will terminate when it sees the 'exit' command or reaches the end of the input stream.


###### Interactive
The shell will display the prompt. An example of this would be:
```
[grace@CS441 ~]$ ./mysh
mysh$ date
Wed Sept 17 10:00:00 CDT 2017
mysh$ pwd
/home/grace/project2/part2
mysh$ ls tests
 file1.txt  file.txt
```
The users of this shell will type commands after the prompt that will be interpreted by the we designed. In addition, the shell will need to keep jobs contained, and they are executed sequentially unless told otherwise.

###### Batch
The shell is started by specifying one or more batch files to the shell on the command line. In this mode, the shell will not display a prompt. The batch file will contain a list of commands (one per line!) that should be executed. If the program is running multiple batch files, the files will be executed in the order they are presented on the command line. This portion of the project deals with running concurrent jobs. After all the files are finished processing, the job totals will be displayed when the shell is exiting. For example:
```
[grace@CS441 ~]$ cat tests/file1.txt date
pwd
ls tests
[grace@CS441 ~]$ ./mysh tests/file1.txt
Wed Sept 17 10:00:00 CDT 2017
/home/grace/project2/part2
file1.txt  file.txt
-------------------------------
Total number of jobs               = 3
Total number of jobs in history    = 3
Total number of jobs in background = 0
[grace@CS441 ~]$ ./mysh tests/file1.txt tests/file1.txt Wed Sept 17 10:00:00 CDT 2017 /home/grace/project2/part2
file1.txt file.txt
Wed Sept 17 10:00:00 CDT 2017 /home/grace/project2/part2
file1.txt file.txt
-------------------------------
Total number of jobs               = 6
Total number of jobs in history    = 6
Total number of jobs in background = 0
[grace@CS441 ~]$
```

###### job_linked_list.c and job_linked_list.h
These files are the funtionality of a linked list that holds all the jobs that are running in the background. This will keep track of the jobs status, by determining if the command is still running, and will change the status. In additon, this will have some funtionality to keep of the most recent that is still running in the background.

###### history_linked_list.c and history_linked_list.h
These files are the functionality of a linked list that holds all the jobs that been written in the console, or run by using the batch mode. This will keep track of the if the job is a background job, all the arguments, and the full command. This will be utilized only by the history command.

## How to build the software

To run software, run the command
```
make
```
If no errors were created, the following prompt will be displayed on the console.
```
gcc -c -o support.o support.c -Wall -g
gcc -c -o job_linked_list.o job_linked_list.c -Wall -g
gcc -c -o history_linked_list.o history_linked_list.c -Wall -g
gcc -o mysh mysh.c support.o job_linked_list.o history_linked_list.o -Wall -g
```

To run software for testing, run the command
```
make check
```
It will display the following prompt. If any errors are created, the error checking will display the differences between our files and files given by Foley.
```
This is going to run a variety of tests against your program.
These tests are not comprehensive, and not validated.
You will need to manually inspect the output to make sure that it is correct

```
To check the individual testing for batch and interactive run the following commands.
```
make check-batch
```
```
make check-interactive
```

Before adding any files to Bitbucket, clean the project up. Run the command
```
make clean
```
If no errors are created, the following prompt will be displayed.
```
rm -f mysh *.o
rm -f -rf *.dSYM
```

## How to use the software

After successful compilation, begin by simply Running

```
./mysh
```

This will fire up the custom shell we have created, which will operate like a normal
shell but it will support a few extra functions. Now that the shell is running,
feel free to run whatever commands you wish just like normal.

Typing the following commands provides various functionality:

jobs - displays the jobs that are currently in the background

history - displays all of the jobs executed by the shell (whether successful or
          not) in order from earliest to latest. Also displays the the job
          number and the full command with arguments. A '&' symbol after the job
          indicates that it was a background job.

wait - waits for all current background jobs to finish before allowing the users
       to enter anything

fg - similar to wait, but fg waits for only one backgrounded job, which can be
     a specific job if the name is supplied as a second argument, or the most
     recent job if not supplied any additional arguments

exit - exits the shell, if a job is still running, wait is called automatically

If you wish to run the batch program, you will call the command
```
./mysh tests/file1.txt
```
This will run all the commands in the folder tests/file1.txt. The second argument is the path to the file to be read.

## How the software was tested

Running the command "make check" on the command line ran a series of 10 tests
varying in sizes from large to small. Being able to pass these tests shows that
a large part of the program works but it does not check everything. In addition,
a series of files and programs are stored in a folder called "tests". The following
are the names of the files and what they were used to test for:

file.txt - reinforces that the history command works properly and also checks that
           using ";" and "&" between command names does not crash the programs

file2.txt - this is a file just containing a string to be read in the hello.name
            program

file3.txt - tests a large amount of redirections

hello.name.c - this is a program that will test that our shell can run a program
               that reads an input file and prints to the console without crashing
               In addition, this file allows you to see if compile a C program
               in the shell

write_out.c - this program simply prints hello world, we will test our shell to see
               if it can handle a redirect out In addition, this file allows you to
               see if compile a C program in the shell


## Known bugs and problem areas

No known bugs or problem areas

## Resources
https://stackoverflow.com/questions/15776381/deleting-a-node-from-a-singly-linked-list-using-malloc-free
https://linux.die.net/man/3/open
