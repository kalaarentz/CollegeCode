# CS441/541 Project 4

## Author(s):

(Group 1)
Kala Arentz
Ben Klipfel


## Date:

3/8/18

## Description:

The main goal of this project is to be an introduction to synchronization problems and gaining experience working with Pthreads library and using synchronization primitives for concurrency control.

This part of the assignment will be implementing two solutions to the Dining Philosophers Problem. We will have one solution that has deadlock, provided by the figure 6.15 in the textbook.
```
do {
  wait(chopstick[i]);
  wait(chopstick[(i+l) %5]);
  // eat
  signal(chopstick[i]);
  signal(chopstick[(i+l) %5]);
  //think
} while (TRUE);
```

The second solution will be deadlock free, and will be implemented by the pseudo code taken from the "Operating Systems: Design and Implementation" by Tanenbaum and Woodhull. The program must only use semaphores as your synchronization primitives.
```
#define NUM_DINERS 5
#define THINKING   0
#define HUNGRY     1
#define EATING     2
// This pseudocode assumes exactly 5 diners - you may not.
// LEFT  defined as ( (i + NUM_DINERS-1) % NUM_DINERS )
// RIGHT defined as ( (i + 1) % NUM_DINERS )
int state[NUM_DINERS]; /* Initialize all states to ’THINKING’ */
semaphore_t mutex = 1;
semaphore_t chopstick[NUM_DINERS]; /* Initialize all to ’0’ */
void philosopher(int i) {
  while(TRUE) {
    think();
    take_chopsticks( i );
    eat();
    release_chopsticks( i );
} }
void take_chopsticks(int i)
  // wait - mutex
  state[i] = HUNGRY;
  test( i );
  // signal - mutex
  // wait on i’th chopstick
}
void release_chopsticks(int
  // wait - mutex
  state[i] = THINKING;
  test( LEFT  );
  test( RIGHT );
  // signal - mutex
}
void test(int i){
  if(state[i] == HUNGRY &&
    state[RIGHT] != EATING &&
    state[LEFT] != EATING )
  state[i] = EATING;
  // signal i’th chopstick
}
```

The program will have two command line parameters

1. Time to run in seconds, REQUIRED, this is the length of time that your application will run before terminating itself. the user must specify a positive integer greater than 0
2. Number of philosophers at the table, OPTIONAL, default will be 5 if no number is given. The user should specify any positive integer grater than 1

The program will output the state of the philosopher whenever their state changes to THINKING, EATING, or DONE, each philosopher will needed to be given an unique integer identifier which should be printed each time their state changes.

Questions will be answered in the SPECIAL SECTION

## How to build the software

To run software, run the command
```
make
```
If no errors were created, the following prompt will be displayed on the console.
```
gcc -c -o semaphore_support.o semaphore_support.c -Wall -g
gcc -o diners-v1 diners-v1.c ../lib/semaphore_support.o -Wall -g -O0 -I../lib -pthread
gcc -o diners-v2 diners-v2.c ../lib/semaphore_support.o -Wall -g -O0 -I../lib -pthread
```

Before updating our bitbucket, run the command
```
make clean
```
If no errors were created, the console will output
```
rm -f diners-v1 diners-v2 *.o
rm -f -rf *.dSYM
cd ../lib && make clean
rm -f sum *.o
rm -f -rf *.dSYM
```

## How to use the software

After successful compilation, begin by simply running
```
./diners-v1 2 5
```
for the program to run a solution to the Philosopher's Dining problem that includes deadlock.
For the program to run a solution to the Philosopher's Dining problem without deadlock, run the command
```
./diners-v2 2 5
```

The program has two parameters that are needed. The first one will be the time the philosophers will eat, while the second one will be the number of Philosophers at the table eating. The second one, if not provided will go to the default number of philosophers which is 5.
## How the software was tested

##### Parameter testing:
We tested the command line arguments by passing the following commands, and the output for each error.

Testing number of seconds parameter
```
./diners-v1 -2 5
```
Output:
```
Error: the time to run in seconds must be greater than 0
```

Testing not giving the time parameter
```
./diners-v1
```
Output:
```
Error: time is required as a parameter
```

Testing number of philosophers
```
./diners-v1 5 -5
```
Output:
```
Error: philosophers at the table should be greater than 1
```
##### Other testing


## Known bugs and problem areas

Couldn't figure out a non-deadlock version, so the special section we will answer
assuming each version works as it should even though that is not true.

## Special Section

#### How can you determine when one or more philosophers are deadlocked in your application?

When the program halts and does not exit.

#### How can you determine when one or more philosophers are starving in your application?

When they are not posting any of their progress while other philosophers are, or if in the final
results one has way time in eating and thinking state

#### Did deadlock occur every time you ran the deadlock prone version of the solution (diner-v1)?
Not all the time, but it occurred more frequently when we increased the number of
philosophers.

#### What happens when you change the number of philosophers (in both solutions) to 2? or 4? or 7? or 100?

In v1, it increases the chance of deadlock as the number of philosophers increases

In v2, it has no effect on deadlocked

Both instances decreases the amount of time each philosopher visits a state (assuming time is consistent)

#### While running the Tanenbaum solution (above in pseudo code - diner-v2) with 5 philosophers, did you notice any one philosopher periodically not consuming as much as the others?
Once in a while, this is probably due to the luck of which thread runs first and exits next
