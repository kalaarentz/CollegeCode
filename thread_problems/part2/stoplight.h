/*
 * Samantha Foley
 *
 * CS 441/541 : Project 4 Part 2 Template
 */
#include "support.h"


/*****************************
 * Defines
 *****************************/


/*****************************
 * Structures
 *****************************/



/*****************************
 * Global Variables
 *****************************/
/*
 * Time to live (Seconds)
 */
int ttl = 0;

/*
 * Number of cars (threads) in the system
 */
int num_cars = 0;

/*
 * Indicate when for threads to stop processing and exit
 */
int time_to_exit = FALSE;

int *north_lane;
int *south_lane;
int *west_lane;
int *east_lane;

int north_position = 0;
int south_position = 0;
int east_position = 0;
int west_position = 0;

int next_north = 0;
int next_south = 0;
int next_east = 0;
int next_west = 0;

semaphore_t *car_sems;
semaphore_t dir_mutex;
semaphore_t cars_in_intersection;

/*****************************
 * Function Declarations
 *****************************/
/*
 * Parse command line arguments
 */
int parse_args(int argc, char **argv);

/*
 * Main thread function that picks an arbitrary direction to approach from,
 * and to travel to for each car.
 *
 * Write and comment this function
 *
 * Arguments:
 *   param = The car ID number for printing purposes
 *
 * Returns:
 *   NULL
 */
void *start_car(void *param);
/*
 * Function to parse a character and convert it to an integer
 *
 * Arguments:
 *   A pointer to an integer and a pointer to a character
 *
 * Returns:
 *   int
 */
int parse_int(int *x, char *a);
/*
 * Function to parse a string and check if any of the contents are digits
 *
 * Arguments:
 *   A pointer to a character
 *
 * Returns:
 *   int
 */
int check_digits(char *a);
/*
 * Function to add a car the lane array
 *
 * Arguments:
 *   A direction that we want to add a car to, and the id of the car
 *   being added
 *
 * Returns:
 *   int
 */
int add_to_lane_queue(int dir, int id);
/*
 * Function to signal the next car in the lane that it
 * can cross
 *
 * Arguments:
 *   One int, the direction of the lane we need to signal
 *
 * Returns:
 *   int
 */
void signal_next_car(int dir);
/*
 * Function to grab the section/s needed for when a car is
 * traveling across the intersection
 *
 * Arguments:
 *   param = The car struct we want to change the direction of
 *
 * Returns:
 *   int
 */
int change_direction(int from, int to);

void print_totals();
