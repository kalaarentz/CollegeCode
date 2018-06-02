/*
 * Samantha Foley
 * 12/14/15
 *
 * This program will test your solve expression function.
 */

#include"solver.h"

int main(int argc, char ** argv){
  char * input_buf = NULL;
  size_t bytes = 0;
  int better_len = 0;

  // get input line from user
  if(getline(&input_buf, &bytes, stdin) > 0){
    //printf("Number of bits needed to represent %s is %d.\n", input_buf, how_many_bits(input_buf, bytes));
    //printf("The binary representation of %s is %s.\n", input_buf, string_to_binary(input_buf, bytes, how_many_bits(input_buf, bytes)));

    // clean up string
    better_len = strlen(input_buf);
    if('\n' == input_buf[better_len - 1]){
      input_buf[better_len - 1] = '\0';
      better_len--;
    }
    printf("The binary representation of %s is %s.\n", input_buf, solve_exp(input_buf, better_len));
  }
  else {
    printf("Error reading from stdin.\n");
  } 

  free(input_buf);
  return 0;
}
