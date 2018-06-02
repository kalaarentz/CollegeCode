/*
* Josh Hursey, Samantha Foley
*
* Authors:
*  Kala Arentz
*  Ben Klipfel
*
* This project will implement a command line interpreter or shell. The
* shell must be able to be run in two different modes: interactive and batch.
* In both the interactive and batch mode, multiple jobs can be specified on
* a command line, and these jobs will be separated by [;]
*
* In both the interactive and batch modes, a job may run in the 'background.'
* The job can be processing while the user is entering more commands or other
* jobs are running. Jobs are being run in the background are followed by
* an ampersand [&]. Background jobs that have been completed will be marked
* as 'Done' and can be cleaned up only after the user calls the 'job' or
* 'exit' command.
*
* This program will support the following commands:
*   1. jobs : The ability to display a list of jobs that are currently
*      running in the background.
*   2. history : In both interactive and batch modes, the ability to
*      display the full history of job commands typed into the shell.
*   3. wait : In both interactive and batch modes, the ability to wait
*      for all currently backgrounded jobs to complete.
*   4. fg : In both interactive and batch modes, the ability to wait for a
*      specific currently backgrounded job. If no jobs, than the command
*      returns an error. It will take one or zero arguments.
*   5. exit : In both interactive and batch modes, the shell will terminate
*      when it sees the 'exit' command or reaches the end of the input stream.
*
* CS441/541: Project 3
*
*/
#include "mysh.h"

int main(int argc, char * argv[]) {
  int ret = 0;
  background_size = 0;
  history_size = 0;

  /*
  * Parse Command line arguments to check if this is an interactive or batch
  * mode run.
  */
  if( 0 != (ret = parse_args_main(argc, argv)) ) {
    fprintf(stderr, "Error: Invalid command line!\n");
    return -1;
  }

  /*
  * If in batch mode then process all batch files
  */
  if( TRUE == is_batch) {
    if( TRUE == is_debug ) {
      printf("Batch Mode!\n");
    }

    if( 0 != (ret = batch_mode()) ) {
      fprintf(stderr, "Error: Batch mode returned a failure!\n");
    }
  }
  /*
  * Otherwise proceed in interactive mode
  */
  else if( FALSE == is_batch ) {
    if( TRUE == is_debug ) {
      printf("Interactive Mode!\n");
    }

    if( 0 != (ret = interactive_mode()) ) {
      fprintf(stderr, "Error: Interactive mode returned a failure!\n");
    }
  }
  /*
  * This should never happen, but otherwise unknown mode
  */
  else {
    fprintf(stderr, "Error: Unknown execution mode!\n");
    return -1;
  }

  /*
  * Display counts
  */
  printf("-------------------------------\n");
  printf("Total number of jobs               = %d\n", total_jobs);
  printf("Total number of jobs in history    = %d\n", total_history);
  printf("Total number of jobs in background = %d\n", total_jobs_bg);

  /*
  * Cleanup
  */
  if( NULL != batch_files) {
    free(batch_files);
    batch_files = NULL;
    num_batch_files = 0;
  }

  return 0;
}

int parse_args_main(int argc, char **argv)
{
  int i;

  /*
  * If no command line arguments were passed then this is an interactive
  * mode run.
  */
  if( 1 >= argc ) {
    is_batch = FALSE;
    return 0;
  }

  /*
  * If command line arguments were supplied then this is batch mode.
  */
  is_batch = TRUE;
  num_batch_files = argc - 1;
  batch_files = (char **) malloc(sizeof(char *) * num_batch_files);
  if( NULL == batch_files ) {
    fprintf(stderr, "Error: Failed to allocate memory! Critical failure on %d!", __LINE__);
    exit(-1);
  }

  for( i = 1; i < argc; ++i ) {
    batch_files[i-1] = strdup(argv[i]);
  }

  return 0;
}

int batch_mode(void)
{
  int i;
  int ret;
  char * command = NULL;
  char * cmd_rtn = NULL;
  FILE *batch_fd = NULL;

  command = (char *) malloc(sizeof(char) * (MAX_COMMAND_LINE+1));
  if( NULL == command ) {
    fprintf(stderr, "Error: Failed to allocate memory! Critical failure on %d!", __LINE__);
    exit(-1);
  }

  for(i = 0; i < num_batch_files; ++i) {
    if( TRUE == is_debug ) {
      printf("-------------------------------\n");
      printf("Processing Batch file %2d of %2d = [%s]\n", i, num_batch_files, batch_files[i]);
      printf("-------------------------------\n");
    }

    /*
    * Open the batch file
    * If there was an error then print a message and move on to the next file.
    */
    if( NULL == (batch_fd = fopen(batch_files[i], "r")) ) {
      fprintf(stderr, "Error: Unable to open the Batch File [%s]\n", batch_files[i]);
      continue;
    }

    /*
    * Read one line at a time.
    */
    while( FALSE == exiting && 0 == feof(batch_fd) ) {

      /* Read one line */
      command[0] = '\0';
      if( NULL == (cmd_rtn = fgets(command, MAX_COMMAND_LINE, batch_fd)) ) {
        break;
      }

      /* Strip off the newline */
      if( '\n' == command[strlen(command)-1] ) {
        command[strlen(command)-1] = '\0';
      }

      /*
      * Parse and execute the command
      */
      if( 0 != (ret = split_parse_and_run(command)) ) {
        fprintf(stderr, "Error: Unable to run the command \"%s\"\n", command);
      }
    }

    /*
    * Close the batch file
    */
    fclose(batch_fd);
  }

  /*
  * Cleanup
  */
  if( NULL != command ) {
    free(command);
    command = NULL;
  }

  return 0;
}

int interactive_mode(void)
{
  int ret;
  char * command = NULL;
  char * cmd_rtn = NULL;

  /*
  * Display the prompt and wait for input
  */
  command = (char *) malloc(sizeof(char) * (MAX_COMMAND_LINE+1));
  if( NULL == command ) {
    fprintf(stderr, "Error: Failed to allocate memory! Critical failure on %d!", __LINE__);
    exit(-1);
  }

  do {
    /*
    * Print the prompt
    */
    printf("%s", PROMPT);
    fflush(NULL);

    /*
    * Read stdin, break out of loop if Ctrl-D
    */
    command[0] = '\0';
    if( NULL == (cmd_rtn = fgets(command, MAX_COMMAND_LINE, stdin)) ) {
      printf("\n");
      fflush(NULL);
      break;
    }

    /* Strip off the newline */
    if( '\n' == command[strlen(command)-1] ) {
      command[strlen(command)-1] = '\0';
    }

    /*
    * Parse and execute the command
    */
    if( 0 != (ret = split_parse_and_run(command)) ) {
      fprintf(stderr, "Error: Unable to run the command \"%s\"\n", command);
      /* This is not critical, just try the next command */
    }
    //printf("Exiting: %d\n", exiting);
    //printf("cmd_rtn: %s\n", cmd_rtn);

  } while( NULL != cmd_rtn && FALSE == exiting);

  /*
  * Cleanup
  */
  if( NULL != command ) {
    free(command);
    command = NULL;
  }

  return 0;
}

int split_parse_and_run(char * command)
{
  int ret, i, j;
  int num_jobs = 0;
  job_t *loc_jobs = NULL;
  char * dup_command = NULL;
  int bg_idx;
  int valid = FALSE;

  /*
  * Sanity check
  */
  if( NULL == command ) {
    return 0;
  }

  /*
  * Check for multiple sequential or background operations on the same
  * command line.
  */
  /* Make a duplicate of command so we can sort out a mix of ';' and '&' later */
  dup_command = strdup(command);

  /******************************
  * Split the command into individual jobs
  ******************************/
  /* Just get some space for the function to hold onto */
  loc_jobs = (job_t*)malloc(sizeof(job_t) * 1);
  if( NULL == loc_jobs ) {
    fprintf(stderr, "Error: Failed to allocate memory! Critical failure on %d!", __LINE__);
    exit(-1);
  }
  split_input_into_jobs(command, &num_jobs, &loc_jobs);

  /*
  * For each job, check for background or foreground
  * Walk the command string looking for ';' and '&' to identify each job as either
  * sequential or background
  */
  bg_idx = 0;
  valid = FALSE;
  for(i = 0; i < strlen(dup_command); ++i ) {
    /* Sequential separator */
    if( dup_command[i] == ';' ) {
      if( TRUE == valid ) {
        loc_jobs[bg_idx].is_background = FALSE;
        ++bg_idx;
        valid = FALSE;
      }
      else {
        fprintf(stderr, "Error: syntax error near unexpected token ';'\n");
      }
    }
    /* Background separator */
    else if( dup_command[i] == '&' ) {
      if( TRUE == valid ) {
        loc_jobs[bg_idx].is_background = TRUE;
        // this will be setting background to True
        //set_background(1, bg_idx);
        ++bg_idx;
        valid = FALSE;
      }
      else {
        fprintf(stderr, "Error: syntax error near unexpected token '&'\n");
      }
    }
    /*
    * Look for valid characters. So we can print an error if the user
    * types: date ; ; date
    */
    else if( dup_command[i] != ' ' ) {
      valid = TRUE;
    }
  }

  /*
  * For each job, parse and execute it
  */
  for( i = 0; i < num_jobs; ++i ) {
    if( 0 != (ret = parse_and_run( &loc_jobs[i] )) ) {
      fprintf(stderr, "Error: The following job failed! [%s]\n", loc_jobs[i].full_command);
    }
  }

  /*
  * Cleanup
  */
  if( NULL != loc_jobs ) {
    /* Cleanup struct fields */
    for( i = 0; i < num_jobs; ++i ) {
      if( NULL != loc_jobs[i].full_command ) {
        free( loc_jobs[i].full_command );
        loc_jobs[i].full_command = NULL;
      }

      if( NULL != loc_jobs[i].argv ) {
        for( j = 0; j < loc_jobs[i].argc; ++j ) {
          if( NULL != loc_jobs[i].argv[j] ) {
            free( loc_jobs[i].argv[j] );
            loc_jobs[i].argv[j] = NULL;
          }
        }
        free( loc_jobs[i].argv );
        loc_jobs[i].argv = NULL;
      }

      loc_jobs[i].argc = 0;

      if( NULL != loc_jobs[i].binary ) {
        free( loc_jobs[i].binary );
        loc_jobs[i].binary = NULL;
      }
    }
    /* Free the array */
    free(loc_jobs);
    loc_jobs = NULL;
  }

  if( NULL != dup_command ) {
    free(dup_command);
    dup_command = NULL;
  }

  return 0;
}

int parse_and_run(job_t * loc_job)
{
  int ret, error, i;
  int input_file = -1;
  int output_file = -1;

  int adjusted_argc = 1;

  /*
  * Sanity check
  */
  if( NULL == loc_job ||
    NULL == loc_job->full_command ) {
      return 0;
    }

    /*
    * No command specified
    */
    if(0 >= strlen( loc_job->full_command ) ) {
      return 0;
    }

    if( TRUE == is_debug ) {
      printf("        \"%s\"\n", loc_job->full_command );
    }

    ++total_history;

    /******************************
    * Parse the string into the binary, and argv
    ******************************/
    split_job_into_args(loc_job);

    //this deals with the redirection of files
    for (i = 1; i < loc_job->argc; i++) {
        // we want to redirect the output to a file
        if (!strcmp(loc_job->argv[i], ">")) {
            loc_job->argv[i] = NULL;
            // open file to write to
            if (++i < loc_job->argc) {
                output_file = open(loc_job->argv[i], O_WRONLY | O_CREAT | O_TRUNC,
                                   S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH);
                if (output_file == -1) {
                    fprintf(stderr, "Error: Failed to open file: %s\n", loc_job->argv[i]);
                    return 0;
                }
                loc_job->stdout_copy = dup(STDOUT_FILENO);
                if (dup2(output_file, STDOUT_FILENO) == -1) {
                    fprintf(stderr, "Error: Failed to switch to file!\n");
                    return 0;
                }
            } else {
                fprintf(stderr, "Not enough command line args.\n");
                return 0;
            }
            // input
        } else if (!strcmp(loc_job->argv[i], "<")) {
            loc_job->argv[i] = NULL;
            // open file to read to
            if (++i < loc_job->argc) {
                input_file = open(loc_job->argv[i], O_RDONLY);
                if (input_file == -1) {
                    fprintf(stderr, "Error: Failed to open file: %s\n", loc_job->argv[i]);
                    return 0;
                }
                loc_job->stdin_copy = dup(STDIN_FILENO);
                if (dup2(input_file, STDIN_FILENO) == -1) {
                    fprintf(stderr, "Error: Failed to switch to file!\n");
                    return 0;
                }
            } else {
                fprintf(stderr, "Not enough command line args.\n");
                return 0;
            }
        } else {
            adjusted_argc++;
        }
    }

    loc_job->argc = adjusted_argc;
    loc_job->fd_input = input_file;
    loc_job->fd_output = output_file;


    /* Check if the command was just spaces */
    if( 0 >= loc_job->argc ) {
      return 0;
    }

    /* Grab the binary from the list of arguments */
    if( 0 < loc_job->argc ) {
      loc_job->binary = strdup(loc_job->argv[0]);
    }

    // ADD ALL JOBS TO HISTORY, EVEN IF THEY ARE ERRORS
    if(history_size == 0) {
      error = create_history_list(loc_job->binary,loc_job->argc,
        loc_job->argv,loc_job->is_background);
    } else {
      error = create_and_add_history_job(loc_job->binary,loc_job->argc,
        loc_job->argv,loc_job->is_background);
    }

    if( error != 0 ) {
      fprintf(stderr, "Error in creating and adding history jobs! \n");
      exit(1);
    }

    /******************************
    * Check for built-in commands:
    * - jobs
    * - exit
    * - history
    * - wait
    * - fg
    ******************************/
    if( 0 == strncmp("exit", loc_job->binary, strlen(loc_job->binary)) ) {
      if( 0 != (ret = builtin_exit() ) ) {
        fprintf(stderr, "Error: exit command failed!\n");
      }
    }
    else if( 0 == strncmp("jobs", loc_job->binary, strlen(loc_job->binary)) ) {
      if( 0 != (ret = builtin_jobs() ) ) {
        fprintf(stderr, "Error: jobs command failed!\n");
      }
    }
    else if( 0 == strncmp("history", loc_job->binary, strlen(loc_job->binary)) ) {
      if( 0 != (ret = builtin_history() ) ) {
        fprintf(stderr, "Error: history command failed!\n");
      }
    }
    else if( 0 == strncmp("wait", loc_job->binary, strlen(loc_job->binary)) ) {
      if( 0 != (ret = builtin_wait() ) ) {
        fprintf(stderr, "Error: wait command failed!\n");
      }
    }
    else if( 0 == strncmp("fg", loc_job->binary, strlen(loc_job->binary)) ) {
      if (loc_job->argc == 2) {
        if (0 != (ret = builtin_fg_with_job_id(atoi(loc_job->argv[1])))) {
          fprintf(stderr, "Error: fg command_string failed!\n");

        }
      } else if (0 != (ret = builtin_fg())) {
        fprintf(stderr, "Error: fg command_string failed!\n");
      }
    }
    /*
    * Launch the job
    */
    else {
      if( 0 != (ret = launch_job(loc_job)) ) {
        fprintf(stderr, "Error: Unable to launch the job! \"%s\"\n", loc_job->binary);
      }
    }

    return 0;
  }

  int launch_job(job_t * loc_job)
  {
    /*
    * Display the job
    */
    //     printf("Job %2d%c: \"%s\" ",
    //            total_jobs_display_ctr + 1,
    //            (TRUE == loc_job->is_background ? '*' : ' '),
    //            loc_job->binary);
    //     fflush(NULL);
    //
    //     for( i = 1; i < loc_job->argc; ++i ) {
    //         printf(" [%s]", loc_job->argv[i]);
    //         fflush(NULL);
    //     }
    //     printf("\n");
    //     fflush(NULL);

    /*
    * Launch the job in either the foreground or background
    */
    run_command( loc_job, total_jobs );
    /*
    * Some accounting
    */
    ++total_jobs;
    ++total_jobs_display_ctr;
    if( TRUE == loc_job->is_background ) {
      ++total_jobs_bg;
    }

    return 0;
  }

  int builtin_exit(void)
  {
    int waiting_jobs = count_all_waiting_jobs();
    exiting = TRUE;

    if (waiting_jobs) {
      printf("Waiting on %2d jobs to finish running in the background!\n", waiting_jobs);
    }

    wait_on_all_jobs();

    // printf("Job %2dx: \"exit\"\n", total_jobs_display_ctr + 1);
    // ++total_jobs_display_ctr;

    // destroy all the linked_lists
    destroy_history();
    destroy_jobs_array();

    fflush(NULL);

    return 0;
  }

  int builtin_jobs(void)
  {
    //printf("Job %2dx: \"jobs\"\n", total_jobs_display_ctr + 1);
    print_background_jobs();
    ++total_jobs_display_ctr;
    fflush(NULL);

    return 0;
  }

  int builtin_history(void)
  {
    //printf("Job %2dx: \"history\"\n", total_jobs_display_ctr + 1);
    print_history_jobs();
    ++total_jobs_display_ctr;
    fflush(NULL);

    return 0;
  }

  int builtin_wait(void)
  {

    //printf("Job %2dx: \"wait\"\n", total_jobs_display_ctr + 1);
    wait_on_all_jobs();
    ++total_jobs_display_ctr;
    fflush(NULL);

    return 0;
  }

  int builtin_fg(void)
  {
    struct job_b * job_to_front = get_last_background_job();
    //printf("Job %2dx: \"fg\"\n", total_jobs_display_ctr + 1);
    if (job_to_front) {
      waitpid(job_to_front->pid, 0, 0);
      job_to_front->job_status = SKIP_JOB;
    } else {
      printf("Error: no job to fg.\n");
    }

    ++total_jobs_display_ctr;
    fflush(NULL);

    return 0;
  }

  int builtin_fg_with_job_id(int job_id) {

    struct job_b *job_to_foreground = get_job(job_id);
    if (!job_to_foreground) {
      printf("Error job %d is not running.\n", job_id);
    } else {
      if (job_to_foreground->job_status == RUNNING_JOB && !waitpid(job_to_foreground->pid, 0, WNOHANG)) {
        waitpid(job_to_foreground->pid, 0, 0);
      } else {
        printf("Error job %d is not running.\n", job_id);
      }
      job_to_foreground->job_status = SKIP_JOB;
    }

    fflush(NULL);

    return 0;
  }

  int run_command(job_t *loc_job, int job_num) {
    int c_pid = 0;
    int status = 0;
    int error;

    /*
    * Launch the job in either the foreground or background
    */

    // fork a child process
    c_pid = fork();

    // check for an error
    if (c_pid < 0) {
      fprintf(stderr, "Error: Fork failed! \n");
      return -1;
    } else if (c_pid == 0) { // child
      execvp(loc_job->binary, loc_job->argv);
      // exec does not return on success.  If we are here then error out
      fprintf(stderr, "Error: Exec failed!\n");
      exit(-1);
    } else { // parent
      if (loc_job->is_background) {
        waitpid(c_pid, &status, WNOHANG);

        if(background_size == 0) {
          error = create_jobs_list(RUNNING_JOB, loc_job->binary,
            c_pid,loc_job->argc,loc_job->argv);
          } else {
            error = create_and_add_job(RUNNING_JOB, loc_job->binary,
              c_pid,loc_job->argc,loc_job->argv);
            }

            if( error != 0 ) {
              fprintf(stderr, "Error in creating and adding background job! \n");
              exit(1);
            }

          } else {
            waitpid(c_pid, &status, 0);
            if (loc_job->fd_output != -1) {
                dup2(loc_job->stdout_copy, STDOUT_FILENO);
                close(loc_job->fd_output);
            }
            if (loc_job->fd_input != -1) {
                dup2(loc_job->stdin_copy, STDIN_FILENO);
                close(loc_job->fd_output);
            }
          }
        }
        return 0;
      }
