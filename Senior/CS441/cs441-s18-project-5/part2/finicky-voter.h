/*
*
*
* CS 441/541: Finicky Voter (Project 5)
*
*/
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>
#include <errno.h>
#include <pthread.h>
#include <string.h>
#include "semaphore_support.h"

/*****************************
* Defines
*****************************/
#define TRUE 1;
#define FALSE 0;
/*****************************
* Structures
*****************************/

enum Political_Party { REPUBLICAN, DEMOCRAT, INDEPENDENT };

enum Voting_States { OPENING, ENTERING, WAITING, VOTING, LEAVING, DONE, CANT_VOTE };

struct voter {
  enum Political_Party party;
  enum Voting_States status;
  long thread_id;
  int booth_index;
};

/*****************************
* Global Variables
*****************************/
int num_booths = 10;
int num_republican = 5;
int num_democrat = 5;
int num_independent = 5;
int booth_index = 0;

int busy_wait = 0;
// helps determine when the first state is completed
// (waiting for polling station is completed)
int entering_count = 0;
int leaving_count = 0;
int total = 0;

int republican_count;
int democrat_count;

int * booth_states;

struct voter **voters;

long int random_num;

// this will be used for determining what to print for on the
// console depending on there voting status

const char * Voting_String_Status[] = {
  "Waiting for polling station to open...\n",
  "Entering the polling station\n",
  "Waiting on a voting booth\n",
  "Voting!\n",
  "Leaving the polling station\n"
};

const char * Poltical_Party_Strings[] = {
  "Republican ", "Democrat   ", "Independent"
};

// semaphore variables
semaphore_t empty;
// semaphore for Republicans
semaphore_t Rmutex;
// semaphore for Democrats
semaphore_t Dmutex;
//semaphore for independents
semaphore_t Imutex;
// this is going to be used for printing
semaphore_t printing;
// this will be a counting semaphore; will be passing the num_booths;
semaphore_t booths;

semaphore_t wait_busy;

// thread Variables
pthread_t *republicans_thread;
pthread_t *democrats_thread;
pthread_t *independents_thread;


/*****************************
* Function Declarations
*****************************/

/*
* this will parse the arguments given by user
* return 0 if no errors, or -1 for errors.
*/
int parse_arguments(int argc, char * argv[]);

/* this will check that the valut is greater than zero. this is specifically
* created for the parsing parse_arguments
* it will print an error message in this one!
* returns 0 if no errors, or -1 for errors,
*/
int check_greater_than_zero( int argument, char * str );

/*
* This function creates all the threads, and joins them.
* Returns 0 for success, returns -1 for any errors
*/
int thread_creation_joining();

/*
* This function Initializes all the semaphores.
* Returns 0 for sucess, returns -1 for any errors.
*/
int create_semaphores();

/*
* This is the method used by republican threads
*/
void * republicans(void *tid );

/*
* This is the method used by democrats threads
*/
void * democrats(void *tid );

/*
* This is the method used by independents threads
*/
void * independents(void *tid );

/**
* This will help determine what exactly should be done during the CRITICAL
* section of the code.
*/
void determine_status(struct voter * v);

/*
* This function is used to have for the busy_wait for the waiting of the
* polling station to open
*/
void busy_wait_function();

/*
* This method is used to find a voter in athe all voters array.
* this holds are the important information for each voter, or each thread.
* The information includes thread_id the political party, status, and the
* booth index if they are currently in a booth
*/
struct voter * find( enum Political_Party p, void * tid );

/*
* This is used to make the [D][.][R][.] string to be printed on the console.
*/
void print_booths();

/*
* This is used to print the status of each voter
*/
void print_current_status( struct voter * v );

/*
* This prints the number of booths, republicans, democrats, and independents.
*/
void print_starting_information();

/*
* This prints the line that seperates the different sections of printing
* done by the program.
* -------+--------+---------
*/
void print_line();

/*
* This will be used to clean up all memory after the threads are done
*/
void clean_up_code();
