/*
 * Josh Hursey, Samantha Foley, Kala Arentz, Ben Klipfel
 *
 * CS441/541: Project 3
 *
 * This will hold all the jobs in the history
 */

 #include <stdlib.h>
 #include <stdio.h>
 #include <memory.h>
 #include <sys/wait.h>

 #include "support.h"

 /******************************
  * Defines
  ******************************/

 /******************************
  * Structures
  ******************************/
  // This will hold all the info for a background job
  struct job_h {
    int argc;
    char **argv;
    char *command_string;
    int is_background;
    struct job_h *next;
  };
  typedef struct job_h job_h;

 /******************************
  * Global Variables
  ******************************/
  // starting background job
  struct job_h *history;

  int history_size;

 /******************************
  * Function declarations
  ******************************/

  /**
 * Creates a jobs linked list.  Must call destroy when finished.
 * @return int for success or error
 */
int create_history_list( char *command, int argc, char **argv,
   int is_background);

/**
 * Creates and adds job to given job linked list.
 *
 * @param job_status Status of the job, running initially
 * @param command Command string used to run job
 * @param pid Process id of job
 * @param argc Number of args in argv
 * @param argv Arguments array
 * @return int for success or error
 */
int create_and_add_history_job( char *command, int argc, char **argv,
  int is_background);

/*
* This will print all the jobs that are in the history linked list.
*/
void print_history_jobs();


/**
 * This destroys the job array and all parts of the job struct in the job array pointer.
 */
void destroy_history();
