#include <stdio.h>
int
main( int argc, char* argv[] ){

  // give an array of char 20 spaces, Ben is only 3 lets not get wild
  char name[20];
  printf("Hello, what is your name?\n");
  scanf("%s", name );
  printf( "Hello %s!\n" , name);

}
