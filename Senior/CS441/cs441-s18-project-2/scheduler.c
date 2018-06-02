/*
 * Author: Kala Arentz
 * Date: 1/30/2018
 *
 * This program will implement a CPU scheduling simulator.
 * The program will read a file, and it will include the
 * following information:
 *    - number of processes
 *    - process arrival order
 *    - CPU burst per processes
 *    - priority per process.
 *
 * In addition, the program will analyze the command line
 * arguments, it will include a three possible items. They
 * may occur in any order:
 *    - -s # -> number will identify the scheduling algorithm
 *    - -q # -> the quantum to use for the Round-Robin scheduling
 *        algorithmns
 *    - handling the first file
 *
 * The file will provide all the information for scheduling. They
 * first line will contain a single number identifying the number
 * of processes in the file that will need to be scheudled.
 *
 * Resources: https://www.tutorialspoint.com/cprogramming/c_file_io.htm
 *            http://www.sanfoundry.com/c-program-illustrate-reading-datafile/
 *
 *
 * CS 441/541: CPU Scheduler (Project 1)
 */

#include "scheduler.h"

int main(int argc, char **argv) {
  bool valid_arguments = checking_commandline_arguments(argc,argv);
  if( !valid_arguments ) {
    return -1;
  } else {
    read_file();
    print_information();
    //debug_processes();
  }

  clean_up_memory();
  return 0;
}

bool checking_commandline_arguments(int argc, char **argv ) {
  int i;

  if( argc == 1 ){
    printf("Error: Not enough arguments given, need a file, and -s\n");
    return false;
  }

  for( i = 1; i < argc; i++ ) {
    if ( strcmp(argv[i], "-s") == 0 ) {
      // if no number is given, error
      if( i+1 == argc || strcmp( argv[i+1], "-q") == 0 ) {
        printf("Error: Not enough arguments for the -s option.\n");
        return false;
      } else {
        schedule_alg = atoi(argv[i+1]);
        i = i + 1;
        if( schedule_alg <= 0 || schedule_alg > 4 ) {
          printf("Error: Scheduling algorithm must be between 1 and 4.\n");
          return false;
        }
      }
    } else if ( strcmp(argv[i], "-q") == 0 ){
      // quantum is to be used only for the Round-Robin algorithm
      if( i+1 == argc || strcmp( argv[i+1], "-s") == 0 ) {
        printf("Error: Not enough arguments for the -q option.\n");
        return false;
      } else {
        quantum = atoi(argv[i+1]);
        if( quantum <= 0 ) {
          printf("Error: Quantum must be greater than zero.\n");
        }
        i = i + 1;
      }
    } else {
      file_to_read = strdup(argv[i]);
    }
  }

  // error checking after reading all the arguments
  if ( !file_to_read ) {
    printf("Error: Need a file to read.\n");
    return false;
  } else if ( !schedule_alg ) {
    printf("Error: Need a schedule to run.\n");
    return false;
  } else if ( schedule_alg == 4 && !quantum ) {
    printf("Error: Need a quantum greater than zero for Round Robin.\n");
    return false;
  }

  return true;
}

void run_algorithm() {

  switch(schedule_alg) {
    case 1:
      first_come_first_serve();
      break;
    case 2:
      shortest_job_first();
      break;
    case 3:
      priority_algthm();
      break;
    case 4:
      round_robin();
      break;
    default:
      printf("Error: error in run algorithm\n");
      exit(0);
  }
}

void read_file() {
  FILE *fp;
  char line[MAXLINE];
  bool is_first_line = true;
  int i = 0, j = 0;
  char * split;
  int numbers[3];

  fp = fopen(file_to_read, "r");
  if ( !fp ) {
    printf("Cannot open the file: %s\n", file_to_read);
    exit(0);
  }

  //first line holds only the number of processes
  // any line following is the process identifier, cpu burst length, priority
  while( fgets(line, sizeof(line), fp ) != NULL ) {
    if( is_first_line ) {
      num_of_processes = atoi(line);
      all_processes = calloc(num_of_processes, sizeof(struct Process_Block *));
      sorting_process = calloc(num_of_processes, sizeof(struct Process_Info *));
      is_first_line = false;
    } else {
      struct Process_Block * new = calloc(1,sizeof(struct Process_Block));
      struct Process_Info * info = calloc(1, sizeof(struct Process_Info));
      split = strtok (line," ");
      // spliting the string into three numbers
      while ( split != NULL ) {
        numbers[j] = atoi(split);
        split = strtok (NULL," ");
        j++;
      }
      // Process_Block
      new->identifer = numbers[0];
      new->cpu_len = numbers[1];
      new->priority = numbers[2];
      new->start = 0;
      new->end = 0;
      new->is_done = false;
      all_processes[i] = new;

      //Process_Info
      info->cpu_burst = numbers[1];
      info->priority = numbers[2];
      info->index = i;
      sorting_process[i] = info;
      i++;
      // reset to j to zero for other lines of the file
      j=0;
    }

  }
  fclose(fp);
};

void print_information() {
  int i;
  printf("Scheduler : %d %s\n", schedule_alg,
    SCHEDULE_ALG_STRINGS[schedule_alg - 1]);
  printf("Quantum : %d\nSch. File : %s\n", quantum,file_to_read);
  printf("-------------------------------\n");
  printf("Arrival Order: ");

  for( i = 0; i < num_of_processes; i++ ) {
    struct Process_Block * pb = all_processes[i];
    if ( i == num_of_processes - 1 ) {
      printf("%d\n", pb->identifer);
    } else {
      printf("%d, ", pb->identifer);
    }
  }

  printf("Process Information:\n");
  for( i = 0; i < num_of_processes; i++ ) {
    struct Process_Block * pb = all_processes[i];
    printf("%2d%5d%4d\n", pb->identifer,pb->cpu_len,pb->priority);
  }
  printf("-------------------------------\nRunning...\n");

  run_algorithm();

  printf("##################################################\n");
  printf("#  #  CPU Pri   W   T\n");

  for( i = 0; i < num_of_processes; i++ ) {
    struct Process_Block * pb = all_processes[i];
    printf("# %2d %4d %3d %3d %3d\n",pb->identifer,pb->cpu_len,
      pb->priority,pb->start,pb->end);
  }

  printf("##################################################\n");
  printf("# Avg. Waiting Time   : %4.2f\n",
    (double) waiting_time/num_of_processes);
  printf("# Avg. Turnaround Time: %4.2f\n",
    (double) turnaround_time/num_of_processes);
  printf("##################################################\n");

}

void clean_up_memory() {
  int i;
  for( i = 0; i < num_of_processes; i++ ) {
    struct Process_Block * entry = all_processes[i];
    struct Process_Info * info = sorting_process[i];
    free(entry);
    free(info);
  }
  free(all_processes);
  free(sorting_process);
}

/*
* Process algorithm
*/

void first_come_first_serve() {
  int i;
  int process_time = 0;

  for( i = 0; i < num_of_processes; i++ ) {
    struct Process_Info * pi = sorting_process[i];
    struct Process_Block * block = all_processes[pi->index];
    block->start = process_time;
    block->end = process_time + block->cpu_len;
    process_time += block->cpu_len;
    waiting_time += block->start;
    turnaround_time += block->end;
    block->is_done = true;
  }
}

void shortest_job_first() {
  int i, j;

  // find order for the shortest cpu burst length
  for( i = 0; i < num_of_processes; i++ ) {
    for( j = 0; j < (num_of_processes - i - 1); j++ ) {
      struct Process_Info * pi = sorting_process[j];
      struct Process_Info * pi_2 = sorting_process[j+1];
      if( pi->cpu_burst > pi_2->cpu_burst ) {
        sorting_process[j] = pi_2;
        sorting_process[j+1] = pi;
      } 
    }
  }

  first_come_first_serve();
}

void priority_algthm() {
  int i, j;

  // find order for the lowest priority level
  // than call the first_come_first_serve function
  for( i = 0; i < num_of_processes; i++ ) {
    for( j = 0; j < (num_of_processes - i - 1); j++ ) {
      struct Process_Info * pi = sorting_process[j];
      struct Process_Info * pi_2 = sorting_process[j+1];
      if( pi->priority > pi_2->priority ) {
        sorting_process[j] = pi_2;
        sorting_process[j+1] = pi;
      }
    }
  }
  //debug_info_processes();

  first_come_first_serve();

}

void round_robin() {
  int i, process_time = 0;
  bool all_processes_done, continue_rr = true;

  // will have a continuous loop to go through the entire time
  // the original variable is the information held in the all_processes
  // array, this will hold all the final information used in the printing
  // funtion. The changing_info variable is the variable that will
  // have the cpu_burst decreased by the quantum until it is equal to zero.
  // this one helps determine how much time is left on each process or if
  // the process is acutally finished
  while ( continue_rr ) {
    all_processes_done = true;
    for( i = 0; i < num_of_processes; i++ ) {
      struct Process_Block * original = all_processes[i];
      struct Process_Info * changing_info = sorting_process[i];

      if ( changing_info->cpu_burst > 0 ) {
        // this process is not done; it needs to has cpu_burst time left
        all_processes_done = false;
        if ( changing_info->cpu_burst > quantum ) {
          process_time += quantum;
          changing_info->cpu_burst -= quantum;
        } else {
          // the process will be finished now after this iteration.
          process_time += changing_info->cpu_burst;
          original->start = process_time - original->cpu_len;
          original->end = process_time;
          changing_info->cpu_burst = 0;
          original->is_done = true;
          waiting_time += original->start;
          turnaround_time += original->end;
        }
      }
    }

    if( all_processes_done ) {
      continue_rr = false;
    }
  }

}

/*
* TESTING FUNCTIONS
*/

void debug_processes() {
  int i;
  printf("num_of_processes: %d", num_of_processes);
  for( i = 0; i < num_of_processes; i++ ) {
    //printf("hello\n");
    struct Process_Block * p = all_processes[i];
    if( p ) {
      printf("\nidentifer: %d\ncpu length: %d\npriority: %d\nstart: %d\nend: %d\n",
        p->identifer,p->cpu_len,p->priority,p->start,p->end);
    }
  }
}

void debug_info_processes() {
  int i;
  printf("num_of_processes: %d", num_of_processes);
  for( i = 0; i < num_of_processes; i++ ) {
    //printf("hello\n");
    struct Process_Info * pi = sorting_process[i];
    printf("index: %d, cpu: %d, priority: %d\n", pi->index, pi->cpu_burst, pi->priority);
  }
}
