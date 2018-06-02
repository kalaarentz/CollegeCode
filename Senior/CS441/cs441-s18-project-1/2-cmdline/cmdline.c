/*
 * Kala Arentz
 * 1/30/18
 *
 * Processing the arguments in the command line and doing
 * the correct forms of error handling for the program.
 *  -Must have -n (with number that is greater than 0)
 *  -Optional, -d for debugging, default is off
 *  -Optional, -stage followed by a captialized string,
 *    the default is ALL stages
 */
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int main(int argc, char **argv) {
    int i;
    int iter = 0;
    char *stages = NULL;
    char *debugging = NULL;

    // atoi() for converting string to int
    if (argc == 1) {
      printf("Error: -n option required to be specified.\n");
      return -1;
    }

    for (i = 0; i < argc; i++ ) {
      if ( strcmp(argv[i], "-d") == 0 ) {
        debugging = "Enabled";
      } else if ( strcmp(argv[i], "-n") == 0 ) {
        // if no number is given or if the -n is followed by -d or -stage
        if ( i+1 == argc || strcmp(argv[i+1], "-d") == 0
          || strcmp(argv[i+1], "-stage") ==0 ) {
          printf("Error: Not enough arguments to the -n option.\n" );
          return -1;
        } else {
          iter = atoi(argv[i+1]);
          i = i + 1;
        }

        if ( iter <= 0 ) {
          printf("Error: -n option requires a positive integer greater than 0\n");
          return -1;
        }
      } else if ( strcmp(argv[i], "-stage") == 0 ) {
        // if no string is given or if the -stage is followed by -d or -n
        if ( i+1 == argc || strcmp(argv[i+1], "-d") == 0
          || strcmp(argv[i+1], "-n") ==0 ) {
          printf("Error: Not enough arguments to the -stage option.\n" );
          return -1;
        } else {
          stages = strdup(argv[i + 1]);
          i = i + 1;
        }
      } else if ( !(strcmp(argv[i], "./cmdline") == 0) ) {
        printf("Error: Unknown option: %s\n", argv[i] );
        return -1;
      }
    }

    if ( !stages ) {
      stages = "All (Default)";
    }
    if ( !debugging ) {
      debugging = "Disabled (Default)";
    }

    // printing off informtion
    printf( "#-----------------------\n");
    printf( "# Iterations : %d\n# Debugging : %s\n# Stage(s)  : %s\n"
      ,iter,debugging,stages );
    printf( "#-----------------------\n");
    return 0;
}
