/*
* Kala Arentz, Ben Klipfel
*
* Jobs array holds all jobs put in background with current status
*
*/

#include "job_linked_list.h"

// ask foley why this gives a duplicate variable if
char *enum_strings[] = { "Running", "Done", "ERROR" };

int create_jobs_list(enum job_status job_status, char *command, int pid,
  int argc, char **argv) {
    int i;
    background = ( struct job_b* ) calloc(1, sizeof(struct job_b) );

    background->pid = pid;
    background->argc = argc;
    background->job_status = job_status;
    background->command_string = strdup(command);

    background->argv = malloc(sizeof(char *) * argc);
    for (i = 0; i < argc; i++) {
      background->argv[i] = strdup(argv[i]);
    }

    background->next = NULL;
    background_size++;

    return 0;
  }

  int create_and_add_job( enum job_status job_status, char *command, int pid,
    int argc, char **argv) {

      struct job_b *current = background;
      struct job_b *new_entry = calloc(1,sizeof(struct job_b));
      int i;

      if(!new_entry) {
        fprintf(stderr, "Error allocating memory, create_add_job \n");
        return 1;
      }

      while( current->next != NULL ) {
        current = current->next;
      }

      new_entry->next = NULL;
      new_entry->pid = pid;
      new_entry->argc = argc;
      new_entry->job_status = job_status;
      new_entry->command_string = strdup(command);
      new_entry->argv = malloc(sizeof(char *) * argc);

      for (i = 0; i < argc; i++) {
        new_entry->argv[i] = strdup(argv[i]);
      }

      current->next = new_entry;
      background_size++;

      return 0;

    }

    void print_background_jobs(){
      char * job_status_str;
      int ret = 0;
      struct job_b *current = background;
      int i, count_of_jobs;

      count_of_jobs = 1;
      while( current != NULL ) {
        // check the status of the process
        ret = waitpid(current->pid,0,WNOHANG);
        if( current->job_status == RUNNING_JOB && (ret == current->pid) ) {
          current->job_status = DONE_JOB;
        }
        job_status_str = enum_strings[current->job_status];
        if( strcmp(job_status_str, "ERROR") == 0 ) {
          // delete this job from the linked list because there is a problem
          delete_job_from_job_array(current);
        } else {
          printf("[%d] %s", count_of_jobs, job_status_str);
          for (i = 0; i < current->argc; i++) {
            printf(" %s ", current->argv[i]);
          }
          printf("\n");
        }

        if (current->job_status == DONE_JOB) {
          current->job_status++;
          delete_job_from_job_array(current);
        }
        current = current->next;
        count_of_jobs++;
      }
    }

    void wait_on_all_jobs() {
      struct job_b *current = background;
      while( current != NULL )  {
        if( current && current->job_status == RUNNING_JOB ) {
          waitpid(current->pid,0,0);
          current->job_status = DONE_JOB;
        }
        current = current->next;
      }
    }

    int count_all_waiting_jobs() {
      struct job_b *current = background;
      int count = 0;

      while( current != NULL )  {
        if( current && !waitpid(current->pid, 0 , WNOHANG) ) {
          count++;
        }
        current = current->next;
      }
      return count;
    }

    void delete_job_from_job_array(struct job_b *job_to_delete) {
      //https://stackoverflow.com/questions/15776381/deleting-a-node-
      // from-a-singly-linked-list-using-malloc-free
      struct job_b * current = background;
      struct job_b * previous = NULL;

      while( current != NULL ) {
        if( current && current->pid == job_to_delete->pid ) {
          if( previous == NULL ) {
            current = current->next;
          } else {
            previous->next = current->next;
            current = previous->next;
          }
          background_size--;
        } else {
          previous = current;
          current = current->next;
        }
      }
    }

    struct job_b *get_last_background_job() {
      struct job_b * current = background;
      struct job_b * ret_b = NULL;

      while( current != NULL ) {
        if (waitpid(current->pid, NULL, WNOHANG)) {
          current->job_status = SKIP_JOB;
        }
        if (current->job_status == RUNNING_JOB ) {
          ret_b = current;
        }
        current = current->next;
      }

      return ret_b;
    }

    struct job_b * get_job(int job_id) {
      struct job_b * current = background;

      while(current != NULL ) {
        if( current && current->pid == job_id) {
          return current;
        }
        current = current->next;
      }
      return NULL;
    }

    void destroy_jobs_array() {
      int i = 0;
      struct job_b *current = background;

      while( current != NULL ) {
        if( current ) {
          for( i = 0; i < current->argc; i++ ){
            free(current->argv[i]);
          }
          free(current->argv);
          free(current->command_string);
        }
        current = current->next;
      }
      free(background);
    }
