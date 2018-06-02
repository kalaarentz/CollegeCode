/*
 * Josh Hursey, Samantha Foley, Kala Arentz, Ben Klipfel
 *
 * CS441/541: Project 3
 *
 */
 #ifndef MYSHELL_H
 #define MYSHELL_H

 #include "support.h"
 #include "job_linked_list.h"
 #include "history_linked_list.h"

 /* For fork, exec, sleep */
 #include <sys/types.h>
 #include <unistd.h>
 /* For waitpid */
 #include <sys/wait.h>

 // added for redirection
 #include <fcntl.h>

 /******************************
  * Defines
  ******************************/
 #define PROMPT ("mysh$ ")


 /******************************
  * Structures
  ******************************/

 /******************************
  * Global Variables
  ******************************/
 /*
  * Debugging toggle
  */

 int is_debug = FALSE;

 /*
  * Interactive or batch mode
  */
 int is_batch = FALSE;

 /*
  * Batch file names
  */
 int num_batch_files = 0;
 char **batch_files = NULL;

 /*
  * Counts
  */
 int total_jobs_display_ctr = 0;
 int total_jobs    = 0;
 int total_jobs_bg = 0;
 int total_history = 0;
 int index_num = 0;
 /*
  * If we are exiting
  */
 int exiting = FALSE;

 /******************************
  * Function declarations
  ******************************/
 /*
  * Parse command line arguments passed to myshell upon startup.
  *
  * Parameters:
  *  argc : Number of command line arguments
  *  argv : Array of pointers to strings
  *
  * Returns:
  *   0 on success
  *   Negative value on error
  */
 int parse_args_main(int argc, char **argv);

 /*
  * Main routine for batch mode
  *
  * Parameters:
  *  None
  *
  * Returns:
  *   0 on success
  *   Negative value on error
  */
 int batch_mode(void);

 /*
  * Main routine for interactive mode
  *
  * Parameters:
  *  None
  *
  * Returns:
  *   0 on success
  *   Negative value on error
  */
 int interactive_mode(void);

 /*
  * Split possible multiple jobs on a command line, then call parse_and_run()
  *
  * Parameters:
  *  command : command line string (may contain multiple jobs)
  *
  * Returns:
  *   0 on success
  *   Negative value on error
  */
 int split_parse_and_run(char * command);

 /*
  * Parse and execute a single job given to the prompt.
  *
  * Parameters:
  *   loc_job : job to execute
  *
  * Returns:
  *   0 on success
  *   Negative value on error
  */
 int parse_and_run(job_t * loc_job);

 /*
  * Launch a job
  *
  * Parameters:
  *   loc_job : job to execute
  *
  * Returns:
  *   0 on success
  *   Negative value on error
  */
 int launch_job(job_t * loc_job);

 /*
  * Built-in 'exit' command
  *
  * Parameters:
  *   None
  *
  * Returns:
  *   0 on success
  *   Negative value on error
  */
 int builtin_exit(void);

 /*
  * Built-in 'jobs' command
  *
  * Parameters:
  *   None
  *
  * Returns:
  *   0 on success
  *   Negative value on error
  */
 int builtin_jobs(void);

 /*
  * Built-in 'history' command
  *
  * Parameters:
  *   None
  *
  * Returns:
  *   0 on success
  *   Negative value on error
  */
 int builtin_history(void);

 /*
  * Built-in 'wait' command
  *
  * Parameters:
  *   None
  *
  * Returns:
  *   0 on success
  *   Negative value on error
  */
 int builtin_wait(void);

 /*
  * Built-in 'fg' command
  *
  * Parameters:
  *   None, or job id
  *
  * Returns:
  *   0 on success
  *   Negative value on error
  */
 int builtin_fg(void);

 /*
 * This determines how the job should be run
 *
 * Parameters:
 *   job_t *loc_job : current local job
 *   int job_num : total number of jobs
 *
 * Returns:
 *   0 on success
 *   Negative value on error
 */
 int run_command( job_t *loc_job, int job_num );

 /**
 * Built-in `fg` command with job id
 * @param job_id Job id stored in jobs for job to forground
 * @return
 *   0 on success
 *   Negative value on error
 */
int builtin_fg_with_job_id(int job_id);


 #endif /* MYSHELL_H */
