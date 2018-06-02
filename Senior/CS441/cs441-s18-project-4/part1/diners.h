/*
 * Samantha Foley
 *
 * Project 4: Dining Philosophers (Common header)
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
#define TRUE  1
#define FALSE 0

/* 1.0 second = 1000000 usec */
//#define TIME_TO_SLEEP 100000
/* 0.5 second = 500000 usec */
//#define TIME_TO_SLEEP 50000
#define TIME_TO_SLEEP 5000


/*****************************
 * Structures
 *****************************/
enum phil_status {THINKING, HUNGRY, EATING, DONE };

/*****************************
 * Global Variables
 *****************************/
 int num_diners = 5;
 int time_allowed;
 int *current_statuses;
 long int random_num;

 int * thought;
 int * ate;

 semaphore_t mutex;
 semaphore_t * sig_diners;

/*****************************
 * Function Declarations
 *****************************/

 /*
  * parses the command line arguments and initializes global variables
  *
  * Parameters:
  *   argc : the number of arguments
  *   argv : the array of arguments
  *
  * Returns: negative number on failure, 0 on success
  */
int parse_command( int argc, char * argv[] );


 /*
  * Checks what state a philosopher is in, if they are hungry and left and right
  * are not eating, philosopher starts eating, sleep amount of time allowed to eat,
  * wakes up a hungry philosopher after eating
  *
  * Parameters:
  *   phil_num : the philosopher index to be tested
  *
  * Returns: none (void)
  */
void test_philosopher( int phil_num );


/*
 * make sure philospher can pick up a fork, updates state
 *
 * Parameters:
 *   phil_num : the philosopher index to be tested
 *
 * Returns: none (void)
 */
void pickup_fork( int phil_num );

/*
 *
 *
 * Parameters:
 *   phil_num : the philosopher index to be tested
 *
 * Returns: none (void)
 */
void drop_fork( int phil_num );

/*
 * Get the index that is to the left of the philosopher
 *
 * Parameters:
 *   phil_num : the philosopher index to be tested
 *
 * Returns: the left index
 */
int get_left( int phil_num );


/*
 * Get the index that is to the right of the philosopher
 *
 * Parameters:
 *   phil_num : the philosopher index to be tested
 *
 * Returns: the right index
 */
int get_right( int phil_num );


 /*
  * This will have the do while from figure 6.15
  *
  * Parameters:
  *   phil_num : the philosopher index to be tested
  *
  * Returns: none (void)
  */
 void *deadlock( void *phil_num );


 /*
  * This will have the do while from a working Algorithm
  *
  * Parameters
  *   phil_num : the philosopher index
  *
  */
 void *no_deadlock( void *phil_num );
