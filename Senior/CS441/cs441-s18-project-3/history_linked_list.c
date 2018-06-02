/*
 * Kala Arentz, Ben Klipfel
 *
 * Jobs array holds all jobs in the history
 *
 */

#include "history_linked_list.h"

int create_history_list(char *command, int argc, char **argv, int is_background) {
  int i;
  history = ( struct job_h* ) calloc(1, sizeof(struct job_h) );

  history->argc = argc;
  history->command_string = strdup(command);
  history->is_background = is_background;

  history->argv = malloc(sizeof(char *) * argc);
  for (i = 0; i < argc; i++) {
    history->argv[i] = strdup(argv[i]);
  }

  history->next = NULL;
  history_size++;

  return 0;
}

int create_and_add_history_job(char *command, int argc, char **argv,  int is_background) {

    struct job_h *current = history;
    struct job_h *new_entry = calloc(1,sizeof(struct job_h));
    int i;

    if(!new_entry) {
      fprintf(stderr, "Error allocating memory, create_add_job \n");
      return 1;
    }

    while( current->next != NULL ) {
      current = current->next;
    }

    new_entry->next = NULL;
    new_entry->argc = argc;
    new_entry->command_string = strdup(command);
    new_entry->argv = malloc(sizeof(char *) * argc);
    new_entry->is_background = is_background;

    for (i = 0; i < argc; i++) {
      new_entry->argv[i] = strdup(argv[i]);
    }

    current->next = new_entry;
    history_size++;

    return 0;

}

void print_history_jobs() {
  struct job_h * current = history;
  int i = 1;
  while( current != NULL ) {
    printf("%4d   %s%s\n", i , current->command_string,
     (TRUE == current->is_background ? " &" : ""));
    current = current->next;
    i++;
  }
}

void destroy_history() {
    int i = 0;
    struct job_h *current = history;

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
    free(history);
}
