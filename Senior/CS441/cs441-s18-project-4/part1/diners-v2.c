/*
* Samantha Foley
*
* Project 4: Dining Philosophers
* Deadlock Free Algorithm
*
*/
#include "diners.h"

int main(int argc, char * argv[]) {
  int i, ret, c;
  int err = parse_command( argc, argv);
  pthread_t threads[num_diners];
  //these arrays will keep track of the times each philospher thought and ate
  ate = malloc(sizeof(int) * num_diners);
  thought = malloc(sizeof(int) * num_diners);
  for( c = 0; c < num_diners; c++ ){
    thought[c] = 0;
    ate[c] = 0;
  }

  if( err != 0 ) {
    // error exit out
    exit(-1);
  }
  printf("Time to Live (seconds)       :   %d\n", time_allowed );
  printf("Number of Dining Philosophers:   %d\n", num_diners);
  printf("-------------------------------\n");
  /*
  * Initialize:
  * - random number generator
  * - semaphores
  */
  srandom(time(NULL));
  //set mutext to 1, because critical section is open by default
  semaphore_create( &mutex, 1);
  //
  sig_diners = (semaphore_t *) malloc (sizeof(semaphore_t) * num_diners);
  current_statuses = calloc(num_diners, sizeof(int));
  for( i = 0; i < num_diners; i++ ) {
    semaphore_create( &sig_diners[i], 0);
    current_statuses[i] = THINKING;
  }

  for( i = 0; i < num_diners; i++ ) {
    ret = pthread_create(&threads[i],
      NULL,
      no_deadlock,
      (void *)(intptr_t)i);
      if(0 != ret ) {
        fprintf(stderr, "Error: Cannot Create thread\n");
        exit(-1);
      }
    }

    /*
    * Join Thread(s)
    */
    for(i = 0; i < num_diners; ++i ) {
      ret = pthread_join(threads[i], NULL);
      if( 0 != ret ) {
        fprintf(stderr, "Error: Cannot Join Thread %d\n", i);
        exit(-1);
      }
    }
    //print the results
    printf("-------------------------------\n");
    for( i = 0; i < num_diners; i++ ){
      printf("Philosopher   %d: Ate %d / Thought %d\n", i, ate[i], thought[i]);
    }
    return 0;
  }

  //This is where we have to implement -------------------------------------------

  void * no_deadlock( void * phil_num ) {
    time_t start, end;
    double elapsed; //keep track of time
    start = time(NULL);
    int terminate = 1;
    do {
      int tid = (intptr_t)phil_num;
      //sleep( 1 );
      pickup_fork(tid);
      //sleep(0);
      drop_fork(tid);
      //check the timer
      end = time(NULL);
      elapsed = difftime(end, start);
      if( elapsed >= time_allowed ){
        terminate = 0;
      }
    } while ( terminate );

    return 0;
  }

  void check_status( int phil_num ){
    random_num = random()%5000;
    //check to see if current is philosopher is hungry, if he is, then check to
    //see if philosophers to the left and right are eating, if neither are, the
    //current philosopher begins eating
    int left = get_left(phil_num);
    int right = get_right(phil_num);

    if( current_statuses[phil_num] == HUNGRY
      && current_statuses[left] != EATING
      && current_statuses[right] != EATING ) {
        //change status
        current_statuses[phil_num] = EATING;
        ate[phil_num] = ate[phil_num] + 1;
        // update the new phil_status
        printf("Philosopher   %d: .....  Eat!\n", phil_num );
        //sleep a random amount of time -> need to intialize the srandom generator
        usleep(random_num);
        semaphore_post( &sig_diners[phil_num]);
      }


    }

    void pickup_fork( int phil_num ){
      //wait for the critical section to be open
      semaphore_wait( &mutex );
      //the philosopher is hungry
      current_statuses[phil_num] = HUNGRY;
      //check to see if this philosopher can eat or not
      check_status( phil_num );
      //let it be known that critical section is open
      semaphore_post( &mutex );
      //wait to be signaled if unable to eat
      semaphore_wait( &sig_diners[phil_num] );
    }

    void drop_fork( int phil_num ){
      random_num = random()%5000;
      int left = get_left(phil_num);
      int right = get_right(phil_num);
      semaphore_wait( &mutex );
      //set the state to thinking
      current_statuses[phil_num] = THINKING;
      thought[phil_num] = thought[phil_num]+1;
      usleep(random_num);
      //print that the philosopher is thinking
      printf("Philosopher   %d: Think!\n", phil_num );
      check_status( left );
      check_status( right );
      //open up CS
      semaphore_post( &mutex );
    }

    //--------------------------------------------------
    int get_left( int phil_num ){
      return( phil_num + (num_diners-1) % num_diners );
    }

    int get_right( int phil_num ){
      return( phil_num + 1) % num_diners;
    }

    int parse_command( int argc, char * argv[]){
      //make sure we declare the time for sure
      if( argc <= 1 ){
        printf("Error: time is required as a parameter\n");
        return -1;
      }
      time_allowed = atoi(argv[1]);
      if( time_allowed <= 0 ) {
        printf("Error: the time to run in seconds must be greater than 0\n");
        return -1;
      }
      //number of diners is optional, default is 5;
      if( argc > 2 ){
        num_diners = atoi(argv[2]);
        if( num_diners <= 1 ) {
          printf("Error: philosophers at the table should be greater than 1\n");
          return -1;
        }
      }
      return 0;
    }
