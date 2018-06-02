/*
* Kala Arentz
* December 1, 2016
*
* C program that displays 12 values in decreasing order by 7
*/

# include <stdio.h>

int main(int arg, char **argv) {
  int i;
  int val = 700;

  for(i=0; i < 12; i++) {
    printf("%d, ", val);
    val = val - 7;
  }

}