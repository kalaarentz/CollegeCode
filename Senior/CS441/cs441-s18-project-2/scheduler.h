/*
 *
 *
 * CS 441/541: CPU Scheduler (Project 1)
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdbool.h>


/******************************
 * Defines
 ******************************/
#define MAXLINE 1024

/******************************
 * Structures
 ******************************/

/*
* this struct will hold the information given in the file.
*/
 struct Process_Block {
   int identifer;
   int cpu_len;
   int priority;
   int start;
   int end;
   bool is_done;
 };

/*
* This struct is created to help with sorting for the SJF and priority
* algorthmn, it is to keep the orginal all_processes struct array
* in its orginal order. In addition, this struct is used in the Round
* Robin algorithm to keep the current amount of time left of the CPU
* burst for that individual process.
*/
 struct Process_Info {
   int cpu_burst;
   int priority;
   int index;
 };

/******************************
 * Global Variables
 ******************************/
struct Process_Block **all_processes;

struct Process_Info **sorting_process;

int quantum, num_of_processes, schedule_alg, waiting_time, turnaround_time;

char * file_to_read;

const char *SCHEDULE_ALG_STRINGS[] = { "FCFS", "SJF", "Priority", "RR"};


/******************************
 * Function declarations
 ******************************/
/*
* This function will read all the arguments from the commandline and
* determine if the arguments are valid. If all arguments are valid
* the funtion will return TRUE;, if the arguments are not valid, meaning
* a error has been found, it will return FALSE;
*/
bool checking_commandline_arguments(int argc, char **argv );

/*
* This function will determine the schedule algorithmn given by the
* the commandline
* will not return anythng, it will be setting the Global variable
* schedule_assigned
*/
void create_schedule_enum(int alg_num);

/*
* This function will read the file given in the checking_commandline_arguments
* The file is in the formation with all the information needed to do the
* schedule.
* First line is the number of processes you are preforming
* each subsequent line is the process identifier, the CPU burst length,
* and the priority of the processes
*
* this function will be allocating the all_processes array with pointers
* to struct Process;
*/
void read_file();

/*
* This function will perform the first come first serve algorithm.
* It will updated the all_processes array with the waiting and Turnaround
* time by completing it in in the order of, first come, first served.
*/
void first_come_first_serve();

/*
* We can sort the sorting_process by comparing the cpu burst in the
* struct Process_Info, and use the corresponding index to do the calculations
* and keep the integrity of the orginal all_processes;
*
* After the sorting, we will call the first_come_first_serve() Function
* and perform the calculations;
*/
void shortest_job_first();

/*
* We can sort the sorting_process by comparing the priority in the
* struct Process_Info, and use the corresponding index to do the calculations
* and keep the integrity of the orginal all_processes;
*
* After the sorting, we will call the first_come_first_serve() Function
* and perform the calculations;
*/
void priority_algthm();

/*
* This function will perform the round robin algorithm.
* It will updated the all_processes array with the waiting and Turnaround
* time by completing it in in the order of, first come, first served, but
* it will take into account the quantum.
*/
void round_robin();

/*
* This function will perform the algorithm that is given by the user.
* This will be used in the print_information function, to 'make it seem'
* as if the algorithm is actually Running.
*/
void run_algorithm();

/*
* This function will print all the importantant informaiton, and following
* the format given in the documentation. For example, given in the
* level1-exp-s-1.txt file the formation will be the following:
*
* Scheduler   :  1 FCFS
* Quantum     :  0
* Sch. File   : given-tests/level1.txt
* -------------------------------
* Arrival Order:  1,  2,  3
* Process Information:
*  1	 27	  1
*  2	  3	  1
*  3	  3	  1
* -------------------------------
* Running...
* ##################################################
* #  #	CPU	Pri	  W	  T
* #  1	 27	  1	  0	 27
* #  2	  3	  1	 27	 30
* #  3	  3	  1	 30	 33
* ##################################################
* # Avg. Waiting Time   :  19.00
* # Avg. Turnaround Time:  30.00
* ##################################################
*
*/
void print_information();

/*
* This function will be used to clean up (free) memory created by
* this all_processes;
*/
void clean_up_memory();

/************************************
* Functions Created for Testing
************************************/

/*
* This function will be used for testing, it will print off all the
* Process_Block structures that are held in a all_processes array.
*/
void debug_processes();

/*
* This function will be used for testing, it will print off all the
* Process_Info structures that are held in a sorting_process array.
*/
void debug_info_processes();
