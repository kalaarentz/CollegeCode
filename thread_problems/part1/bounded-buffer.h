/*
 *
 *
 * CS 441/541: Bounded Buffer (Project 4 Part 1)
 *
 */
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>
#include <errno.h>
#include <pthread.h>
#include <ctype.h>
#include <string.h>
#include "semaphore_support.h"

/*****************************
 * Defines
 *****************************/
#define TRUE 1

/*****************************
 * Structures
 *****************************/


/*****************************
 * Global Variables
 *****************************/
semaphore_t mutex;
semaphore_t full_buffer;
semaphore_t empty_buffer;
int counter;
pthread_attr_t attr;
pthread_t tid;
int size;
int *buffer;
int p_id_nums;
int c_id_nums;
int prod_position;
int cons_position;
int consumed;
int produced;

/*****************************
 * Function Declarations
 *****************************/
/*
 *  Process the command line arguments, along with the time, prod,
 *  con, buff, and args, arguments.
 *
 * Parameters:
 *
 *  time: Int variable that tracks how long a thread lives for
 *  prod: Number of producer threads
 *  con:  Number of consumer threads
 *  buff: Int that states the size of the buffer
 *  args: Array of pointers to strings
 *
 *
 * Returns:
 *  0 on success
 *  Negative value on error
 */
int process_args(int *time, int *prod, int *con, int *buff, char **args);

/*
 * Convert the given character to an integer and store in the correct vaiable.
 *
 * Parameters:
 *
 *  x: Pointer to the int variable that stores the converion
 *  a: Character to be converted to a number
 *
 *
 * Returns:
 *  0 on success
 *  Negative value on error
 */
int parse_int(int *x, char *a);

/*
 * Print a message when the incorrect amount of parameters are given
 *
 * Parameters:
 *
 * None! Just a print statement.
 *
 * Returns:
 *  No return, void method.
 */
void print_correct_use();

/*
 * Check if a certain string passed in contains any invalid characters
 *
 * Parameters:
 *
 *  a: String to be checked.
 *
 * Returns:
 *  0 on success
 *  Negative value on error
 */
int check_digits(char *a);


/*
 * Prints the initial given values.
 *
 * Parameters:
 *
 *  b: Buffer size
 *  t: Time the thread gets to live
 *  p: Number of producer threads
 *  c: Number of consumer threads
 *
 *
 * Returns:
 *  None! Just a print statement.
 */
void print_intro(int b, int t, int p, int c);

/*
 * Prints the initial given values.
 *
 * Parameters:
 * None
 *
 *
 * Returns:
 *  None! Just a print statement.
 */
void print_buffer();

/*
 * a producer thread function that contimuously tries to produce ints and store in buffer
 *
 * Parameters:
 * num no use
 *
 *
 * Returns:
 *  None! void
 */
void *producer(void *num);

/*
* a consumer thread function that contimuously tries to consume ints stored in buffer
*
* Parameters:
* num no use
*
*
* Returns:
*  None! void
*/
void *consumer(void *num);

/*
 * Insert an int into the buffer
 *
 * Parameters:
 *
 *  item: int to be inserted into buffer
 *
 * Returns:
 *  0 on success
 *  Negative value on error
 */
int insert_item(int item);

/*
 * Consume an int from the buffer
 *
 * Parameters:
 *
 *  *item: place to store the consumed int
 *
 * Returns:
 *  0 on success
 *  Negative value on error
 */
int remove_item(int *item);

