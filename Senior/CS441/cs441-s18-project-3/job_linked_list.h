/*
 * Josh Hursey, Samantha Foley, Kala Arentz, Ben Klipfel
 *
 * CS441/541: Project 3
 *
 * This will hold all the jobs with background status
 */

 #include <stdlib.h>
 #include <stdio.h>
 #include <memory.h>
 #include <sys/wait.h>

 /******************************
  * Defines
  ******************************/
  /**
   * Job status is status of job, running, done, or can be replaced (skip)
   */
  enum job_status {
      RUNNING_JOB,
      DONE_JOB,
      SKIP_JOB
    };

 /******************************
  * Structures
  ******************************/
  // This will hold all the info for a background job
  struct job_b {
    int pid;
    int argc;
    char **argv;
    enum job_status job_status;
    char *command_string;
    struct job_b *next;
  };
  typedef struct job_b job_b;

 /******************************
  * Global Variables
  ******************************/

  // starting background job
  struct job_b *background;

  int background_size;

 /******************************
  * Function declarations
  ******************************/

  /**
 * Creates a jobs linked list.  Must call destroy when finished.
 * @return int for success or error
 */
int create_jobs_list(enum job_status job_status, char *command, int pid,
   int argc, char **argv);

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
int create_and_add_job( enum job_status job_status, char *command, int pid,
  int argc, char **argv);

/**
* Print all the background jobs
*/
void print_background_jobs();

/**
 * Waits on all jobs in the job array and marks them to done.
 */
void wait_on_all_jobs();

/**
 * Counts all jobs still running
 * @return Number of running jobs
 */
int count_all_waiting_jobs();

/**
 * Deletes job from the jobs array.  This is called after a job is done.
 * @param job_to_delete Job that should be removed and freed
 */
void delete_job_from_job_array(struct job_b *job_to_delete);

/**
 * This returns the last background job or null if none are in the background.
 * @return pointer to last backgrounded job
 */
struct job_b *get_last_background_job();

/**
 * This returns a job based on the job id
 * @param job_id Job id of process requested
 * @return Pointer to job based on job id
 */
struct job_b * get_job(int job_id);

/**
 * This destroys the job array and all parts of the job struct in the job array pointer.
 */
void destroy_jobs_array();
