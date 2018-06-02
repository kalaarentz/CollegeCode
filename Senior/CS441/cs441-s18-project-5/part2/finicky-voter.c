/*
*
*
* CS 441/541: Finicky Voter (Project 5)
*
*/
#include "finicky-voter.h"

int main(int argc, char * argv[]) {
  int i;
  int ret = parse_arguments( argc, argv );
  if( ret != 0 ) {
    exit(-1);
  }
  total = num_republican + num_democrat + num_independent;
  voters = calloc(total, sizeof(struct voter));

  republicans_thread = calloc(num_republican, sizeof(pthread_t));
  democrats_thread = calloc( num_democrat, sizeof(pthread_t));
  independents_thread = calloc( num_independent, sizeof(pthread_t));

  // Initialize booth_states
  booth_states = calloc(num_booths, sizeof(int));
  for(i = 0; i < num_booths; i++ ) {
    // if empty it is -1
    booth_states[i] = -1;
  }
  print_starting_information();
  srandom(time(NULL));
  // creating all the semaphores
  ret = create_semaphores();
  if( ret != 0 ) {
    exit(-1);
  }
  // THREAD Function
  ret = thread_creation_joining();
  if( ret != 0 ) {
    // some error occured
    exit(-1);
  }
  print_line();
  clean_up_code();
  return 0;
}

/**************************************************************
* FUCNTIONS FOR THREADS
**************************************************************/

//Begin the actions of each political party------------------------------------
void * republicans( void *tid ){
  busy_wait_function();

  while( leaving_count != total  ) {
    semaphore_wait(&Rmutex);
    struct voter * v = find(REPUBLICAN,tid);
    republican_count++;
    if( republican_count == 1){
      semaphore_wait(&empty);
    }
    semaphore_post(&Rmutex);

    semaphore_wait(&booths);
    determine_status(v);
    semaphore_post(&booths);

    semaphore_wait(&Rmutex);
    republican_count--;
    if( republican_count == 0 ){
      semaphore_post(&empty);
    }
    semaphore_post(&Rmutex);
  }

  return 0;
}

void * democrats(void *tid ){
  busy_wait_function();

  while( leaving_count != total  ) {
    //wait for it to be possible for democrats to vote
    semaphore_wait(&Dmutex);
    //increment the democrat counting
    struct voter * v = find(DEMOCRAT,tid);
    democrat_count++;
    //if there is one democrat in the booth
    if( democrat_count == 1 ){
      semaphore_wait(&empty);
    }
    semaphore_post(&Dmutex);

    semaphore_wait(&booths);
    determine_status(v);
    semaphore_post(&booths);

    semaphore_wait(&Dmutex);
    democrat_count--;
    if( democrat_count == 0 ){
      semaphore_post(&empty);
    }
    semaphore_post(&Dmutex);
  }

  return 0;
}

void * independents(void *tid ){
  busy_wait_function();
  while( leaving_count != total ) {
    //wait for a booth to be open
    semaphore_wait(&Imutex);
    struct voter * v = find(INDEPENDENT,tid);
    semaphore_post(&Imutex);

    semaphore_wait(&booths);
    determine_status(v);
    semaphore_post(&booths);
  }

  return 0;
}

void determine_status(struct voter * v ) {
  if( v->status == OPENING ) {
    print_current_status(v);\
    v->status = ENTERING;
    entering_count++;
    if(entering_count == total ) {
      print_line();
    }
  } else if ( v->status == ENTERING  && entering_count == total ) {
    // the second condition is used so the opening is only printed
    // (no other thread status) will be printed. we have the two sections
    // divided by the line
    random_num = random()%50000;
    print_current_status(v);
    usleep(random_num);
    if( booth_index == num_booths ) {
      v->status = CANT_VOTE;
    } else {
      v->status = WAITING;
    }
  } else if( v->status == WAITING ){
    print_current_status(v);
    v->status = VOTING;
  } else if ( v->status == VOTING ) {
    booth_states[booth_index] = v->party;
    v->booth_index = booth_index;
    booth_index++;

    print_current_status(v);
    // sleep some random amount between 1 and 100,000 microseconds
    random_num = random()%100000;
    usleep(random_num);
    booth_states[v->booth_index] = -1;
    if( v->booth_index < booth_index ) {
      booth_index = v->booth_index;
    } else if( booth_index < num_booths && booth_index > 0) {
      booth_index--;
    }
    v->booth_index = -1;
    v->status = LEAVING;
  } else if ( v->status == LEAVING ) {
    print_current_status(v);
    leaving_count++;
    v->status = DONE;
  } else if ( v->status == CANT_VOTE ) {
    // this makes sure that after a booth opens, a thread can enter
    // a booth to vote, before it would enter a booth that did not
    // exist
    if( booth_index < num_booths ) {
      v->status = WAITING;
    }
  }
};

void busy_wait_function() {
  // this is used for make sure it waits 2 seconds before it prints out
  // the waiting
  semaphore_wait(&wait_busy);
  if( busy_wait  == 0 ) {
    sleep(2);
    busy_wait++;
  }
  while( busy_wait == 0 ) {
    // do nothing
  }
  semaphore_post(&wait_busy);
}

struct voter * find( enum Political_Party p, void * tid ){
  int i;
  int start = 0;
  long t_id = (intptr_t)tid;

  if( p == DEMOCRAT ){
    start = num_republican;
  }
  else if( p == INDEPENDENT){
    start = num_republican + num_democrat;
  }
  for( i = start; i < total; i++ ){
    if(voters[i]->thread_id == t_id
      && voters[i]->party == p){
      return voters[i];
    }
  }
  return NULL;
}

/**************************************************************
* PRINTING FUNCTIONS
**************************************************************/

void print_starting_information(){
  // print all the important information
  printf("Number of Voting Booths   : %2d\n", num_booths);
  printf("Number of Republican      : %2d\n", num_republican);
  printf("Number of Democrat        : %2d\n", num_democrat);
  printf("Number of Independent     : %2d\n", num_independent);
  // this is to make it the same length as the number of voting booths
  print_line();
  fflush(stdout);
}

void print_line() {
  int i;
  printf("-----------------------+----");
  for( i = 0; i < num_booths; i++ ) {
    printf("----");
  }
  printf("-+--------------------------------\n");
}

void print_booths() {
  int i;
  for( i = 0; i < num_booths; i++ ){
    printf("[");
    if( booth_states[i] == -1 ){
      printf(".");
    }
    else if( booth_states[i] == 0 ){
      printf("R");
    }
    else if( booth_states[i] == 1){
      printf("D");
    }
    else{
      printf("I");
    }
    printf("] ");
  }
  fflush(stdout);

}

void print_current_status( struct voter * v ){
  //printf("whatup!\n");
  semaphore_wait(&printing);
  // second "%s is for the in"
  if( v->status == VOTING ) {
    //printf("current booth index: %d\n", booth_index);
    printf("%s %3ld in %3d |-> ",
      Poltical_Party_Strings[v->party], v->thread_id,v->booth_index+1);
  } else {
    printf("%s %3ld        |-> ",
      Poltical_Party_Strings[v->party], v->thread_id);
  }
  print_booths();
  printf("<-| %s", Voting_String_Status[v->status]);
  fflush(stdout);
  semaphore_post(&printing);
}

void clean_up_code() {
  int i;
  for( i = 0; i < total; i++ ) {
    free(voters[i]);
  }
  free(voters);
}

/**************************************************************
* **************************************************************
* **************************************************************
* Analyzing the arguments passed
* **************************************************************
* **************************************************************
**************************************************************/

int check_greater_than_zero( int argument, char * str ) {
  char * error = strdup(str);
  if( argument <= 0 ) {
    printf("Error: the number of %s must be greater than 0!\n", error);
    return -1;
  }
  return 0;
}

int parse_arguments(int argc, char * argv[] ) {
  int i, ret, value;
  char * str;
  if( argc == 1 ) {
    // no other parameter's are given, than return
    // automatically with the defualt numbers
    return 0;
  }
  for( i = 1; i < argc; i++ ) {
    value = atoi(argv[i]);
    switch( i ) {
      case 1 :
      num_booths = value;
      str = "Booths";
      break;
      case 2:
      num_republican = value;
      str = "Republicans";
      break;
      case 3:
      num_democrat = value;
      str = "Democrats";
      break;
      case 4:
      num_independent = value;
      str = "Independents";
      break;
    }
    ret = check_greater_than_zero(value, str);
    if( ret != 0 ) {
      return -1;
    }
  }
  return 0;
}

/**************************************************************
* Initializing THREADS and SEMAPHORES
**************************************************************/

int create_semaphores() {
  int ret;
  //Semaphore to keep track of whether all booths are empty
  if( 0 != (ret = semaphore_create(&empty, 1)) ) {
    fprintf(stderr, "Error: semaphore_create(empty) failed with %d\n", ret);
    return -1;
  }
  //Semapore to allow republicans to enter (currently open)
  if( 0 != (ret = semaphore_create(&Rmutex, 1)) ) {
    fprintf(stderr, "Error: semaphore_create(Rmutex) failed with %d\n", ret);
    return -1;
  }
  //Semaphore allowing democrats to enter (currently open)
  if( 0 != (ret = semaphore_create(&Dmutex, 1)) ) {
    fprintf(stderr, "Error: semaphore_create(Dmutex) failed with %d\n", ret);
    return -1;
  }
  //Semaphore allowing democrats to enter (currently open)
  if( 0 != (ret = semaphore_create(&Imutex, 1)) ) {
    fprintf(stderr, "Error: semaphore_create(Imutex) failed with %d\n", ret);
    return -1;
  }
  if( 0 != (ret = semaphore_create(&printing, 1)) ) {
    fprintf(stderr, "Error: semaphore_create(printing) failed with %d\n", ret);
    return -1;
  }
  if( 0 != (ret = semaphore_create(&wait_busy, 1)) ) {
    fprintf(stderr, "Error: semaphore_create(wait_busy) failed with %d\n", ret);
    return -1;
  }
  //counting semaphore keeping track of booth space (currently all booths are empty)
  if( 0 != (ret = semaphore_create(&booths, num_booths)) ) {
    fprintf(stderr, "Error: semaphore_create(booths) failed with %d\n", ret);
    return -1;
  }
  return 0;
}

int thread_creation_joining() {
  int i, ret, idx = 0;
  // creating all the threads
  //create republicans thread
  for( i = 0; i < num_republican; i++ ){
    ret = pthread_create( &republicans_thread[i], NULL, republicans,
       (void*)(intptr_t)i);
    voters[idx] = calloc(1, sizeof(struct voter));
    voters[idx]->status = OPENING;
    voters[idx]->party = REPUBLICAN;
    voters[idx]->thread_id = (intptr_t)i;
    voters[idx]->booth_index = -1;
    if( ret != 0 ){
      printf("ERROR: Could not create Republican thread %d\n", i);
      exit(-1);
    }
    idx++;
  }
  //create democrats thread
  for( i = 0; i < num_democrat; i++ ){
    ret = pthread_create( &democrats_thread[i], NULL, democrats, (void*)(intptr_t)i);
    voters[idx] = calloc(1, sizeof(struct voter));
    voters[idx]->status = OPENING;
    voters[idx]->party = DEMOCRAT;
    voters[idx]->thread_id = (intptr_t)i;
    voters[idx]->booth_index = -1;
    if( ret != 0 ){
      printf("ERROR: Could not create Democrat thread %d\n", i);
      exit(-1);
    }
    idx++;
  }
  //create independents thread
  for( i = 0; i < num_independent; i++ ){
    ret = pthread_create( &independents_thread[i], NULL, independents, (void*)(intptr_t)i);
    voters[idx] = calloc(1, sizeof(struct voter));
    voters[idx]->status = OPENING;
    voters[idx]->party = INDEPENDENT;
    voters[idx]->thread_id = (intptr_t)i;
    voters[idx]->booth_index = -1;
    if( ret != 0 ){
      printf("ERROR: Could not create Republican thread %d\n", i);
      exit(-1);
    }
    idx++;
  }

  //Joining the threads #######################################################
  for( i = 0; i < num_republican; i++ ){
    ret = pthread_join(republicans_thread[i], NULL);
    if( ret != 0 ){
      fprintf(stderr, "ERROR: Could not join thread %d\n", i);
      return -1;
    }
  }

  for( i = 0; i < num_democrat; i++ ){
    ret = pthread_join(democrats_thread[i], NULL);
    if( ret != 0 ){
      fprintf(stderr, "ERROR: Could not join thread %d\n", i);
      return -1;
    }
  }

  for( i = 0; i < num_independent; i++ ){
    ret = pthread_join(independents_thread[i], NULL);
    if( ret != 0 ){
      fprintf(stderr, "ERROR: Could not join thread %d\n", i);
      return -1;
    }
  }
  return 0;
}
