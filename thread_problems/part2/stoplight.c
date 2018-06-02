/*
 * Samantha Foley
 *
 * CS 441/541 : Project 4 Part 2 Template
 *
 */
#include "stoplight.h"


int main(int argc, char * argv[]) {
    int ret;
    pthread_t *cars;
    int i;

    /*
     * Parse Command Line arguments
     */
    if( argc < 3 || 0 != (ret = parse_args(argc, argv)) ) {
        fprintf(stderr, "This program requires two positive integers as arguments\n");
        return -1;
    }

    /*
     * Initialize:
     * - random number generator
     */
    srand(time(NULL));
    north_lane = (int *) malloc(num_cars * sizeof(int));
    south_lane = (int *) malloc(num_cars * sizeof(int));
    east_lane = (int *) malloc(num_cars * sizeof(int));
    west_lane = (int *) malloc(num_cars * sizeof(int));
    car_sems = (semaphore_t *) malloc(num_cars * sizeof(semaphore_t));
    semaphore_create(&dir_mutex, 1);
    semaphore_create(&north_west, 1);
    semaphore_create(&north_east, 1);
    semaphore_create(&south_west, 1);
    semaphore_create(&south_east, 1);
    semaphore_create(&cars_in_intersection, 3);
    for (i = 0; i < num_cars; i++){
        semaphore_create(&car_sems[i], 0);
    }
    min = 500000;
    max = 0;
    total = 0;
    num_cars_entered = 0;

    /*
     * Create Car Thread(s)
     *
     */
    cars =(pthread_t *) malloc(num_cars*sizeof(pthread_t));
    for (i = 0; i < num_cars; i++)
        pthread_create(&(cars[i]), NULL, start_car, (void*)(size_t)i);
    print_header();
    /*
     * Wait for the TTL to expire
     */
    // sleep specified amount of time
    // set time_to_exit to TRUE
    sleep(ttl);
    time_to_exit = TRUE;
    /*
     * Reap threads
     */
    for (i = 0; i < num_cars; i++)
    pthread_join(cars[i], NULL);

    /*
     * Print timing information
     */
    print_totals();


    /*
     * Cleanup
     *
     */


    /*
     * Finalize support library
     */
    support_finalize();

    return 0;
}

int parse_args(int argc, char **argv)
{
    if (parse_int(&ttl, argv[1]) || parse_int(&num_cars, argv[2])
        || !ttl || !num_cars)
        return -1;

    /*
     * Initialize support library
     */
    support_init();

    return 0;
}

int parse_int(int *x, char *a){

    if(check_digits(a) == -1)
        return -1;
    *x = atoi(a);

    return 0;
}

int check_digits(char *a){
    int i;

    for(i = 0; i < strlen(a); i++)
        if(!isdigit(a[i]))
            return -1;

    return 0;
}

/*
 * Approach intersection
 * param = Car Number (car_id)
 */
void *start_car(void *param) {
    int car_id = (intptr_t)param;
    car_t this_car;
    this_car.car_id = car_id;

    /*
     * Keep cycling through
     */
    while( time_to_exit == FALSE ) {

        /*
         * Sleep for a bounded random amount of time before approaching the
         * intersection
         */
        usleep(random()%TIME_TO_APPROACH);
        this_car.state = STATE_WAITING_I1;

        /*
         * Setup the car's direction, where it is headed, set its state
         */
        this_car.appr_dir = get_random_direction(-1);
        this_car.dest_dir = get_random_direction(this_car.appr_dir);


        /*
         * Mark start time for car
         */
        gettimeofday(&this_car.start_time, NULL);
        gettimeofday(&this_car.end_time, NULL);
        print_state(this_car, NULL);

        if(!add_to_lane_queue(this_car.appr_dir, this_car.car_id)){
            semaphore_wait(&car_sems[this_car.car_id]);
            sleep(3);
        }

        /*
         * Move the car in the direction and change its state accordingly
         */
        this_car.state = STATE_APPROACH_I1;
        print_state(this_car, NULL);

        change_direction(this_car.appr_dir, this_car.dest_dir);
        signal_next_car(this_car.appr_dir);

        /*
         * Mark leave time for car
         */
        this_car.state = STATE_LEAVE_I1;
        gettimeofday(&this_car.end_time, NULL);
        print_state(this_car, NULL);

        /*
         * Save statistics about the cars travel
         */


    }

    /*
     * All done
     */
    pthread_exit((void *) 0);

    return NULL;
}

int add_to_lane_queue(int dir, int id){
    int ret = 0;

    semaphore_wait(&dir_mutex);
    switch(dir){
        case 0:
            if(north_position == next_north)
                ret = 1;
            north_lane[next_north % num_cars] = id;
            next_north++;
            break;
        case 1:
            if(west_position == next_west)
                ret = 1;
            west_lane[next_west % num_cars] = id;
            next_west++;
            break;
        case 2:
            if(south_position == next_south)
                ret = 1;
            south_lane[next_south % num_cars] = id;
            next_south++;
            break;
        case 3:
            if(east_position == next_east)
                ret = 1;
            east_lane[next_east % num_cars] = id;
            next_east++;
            break;
    }
    semaphore_post(&dir_mutex);
    return ret;
}

void signal_next_car(int dir){
    int car_id;

//    semaphore_wait(&dir_mutex);
    switch(dir){
        case 0:
            car_id = north_lane[north_position%num_cars];
            north_position++;
            break;
        case 1:
            car_id = west_lane[west_position%num_cars];
            west_position++;
            break;
        case 2:
            car_id = south_lane[south_position%num_cars];
            south_position++;
            break;
        case 3:
            car_id = east_lane[east_position%num_cars];
            east_position++;
            break;
    }
//    semaphore_post(&dir_mutex);
    usleep(TIME_USEC);
    semaphore_post(&car_sems[car_id]);
}

int change_direction(int from, int to)
{
//  car_direction_t approach = car.appr_dir;
//  car_direction_t destination = car.dest_dir;

    semaphore_wait(&cars_in_intersection);
    switch(from){
        case 0 :
            switch(to){
                case 1 :
                    semaphore_wait(&north_west);
                    semaphore_post(&north_west);
                    usleep(random()%TIME_TO_CROSS);
                    break;
                case 2 :
                    semaphore_wait(&north_west);
                    semaphore_wait(&south_west);
                    semaphore_post(&north_west);
                    semaphore_post(&south_west);
                    usleep(random()%TIME_TO_CROSS);
                    break;
                case 3 :
                    semaphore_wait(&north_west);
                    semaphore_wait(&south_west);
                    semaphore_post(&north_west);
                    semaphore_wait(&south_east);
                    semaphore_post(&south_west);
                    semaphore_post(&south_east);
                    usleep(random()%TIME_TO_CROSS);
                    break;
            }
            break;
        case 1 :
            switch(to){
                case 0 :
                    semaphore_wait(&south_west);
                    semaphore_wait(&south_east);
                    semaphore_post(&south_west);
                    semaphore_wait(&north_east);
                    semaphore_post(&south_east);
                    semaphore_post(&north_east);
                    usleep(random()%TIME_TO_CROSS);
                    break;
                case 2 :
                    semaphore_wait(&south_west);
                    semaphore_post(&south_west);
                    usleep(random()%TIME_TO_CROSS);
                    break;
                case 3 :
                    semaphore_wait(&south_west);
                    semaphore_wait(&south_east);
                    semaphore_post(&south_west);
                    semaphore_post(&south_east);
                    usleep(random()%TIME_TO_CROSS);
                    break;
            }
            break;
        case 2 :
            switch(to){
                case 0 :
                    semaphore_wait(&south_east);
                    semaphore_wait(&north_east);
                    semaphore_post(&south_east);
                    semaphore_post(&north_east);
                    usleep(random()%TIME_TO_CROSS);
                    break;
                case 1 :
                    semaphore_wait(&south_east);
                    semaphore_wait(&north_east);
                    semaphore_post(&south_east);
                    semaphore_wait(&north_west);
                    semaphore_post(&north_east);
                    semaphore_post(&north_west);
                    usleep(random()%TIME_TO_CROSS);
                    break;
                case 3 :
                    semaphore_wait(&south_east);
                    //sleep(2);
                    semaphore_post(&south_east);
                    usleep(random()%TIME_TO_CROSS);
                    break;
            }
            break;
        case 3 :
            switch(to){
                case 0 :
                    semaphore_wait(&north_east);
                    //sleep(2);
                    semaphore_post(&north_east);
                    usleep(random()%TIME_TO_CROSS);
                    break;
                case 1 :
                    semaphore_wait(&north_east);
                    //sleep(2);
                    semaphore_wait(&north_west);
                    //sleep(2);
                    semaphore_post(&north_east);
                    semaphore_post(&north_west);
                    usleep(random()%TIME_TO_CROSS);
                    break;
                case 2 :
                    semaphore_wait(&north_east);
                    //sleep(2);
                    semaphore_wait(&north_west);
                    semaphore_post(&north_east);
                    semaphore_wait(&south_west);
                    semaphore_post(&north_west);
                    semaphore_post(&south_west);
                    usleep(random()%TIME_TO_CROSS);
                    break;
            }
            break;
        }

    semaphore_post(&cars_in_intersection);
  //printf( "%d, %d\n", approach, destination );

  return 0;
}

void print_totals(){

    print_footer();
    printf("Min.  Time :%12.3f msec\n", min);
    printf("Avg.  Time :%12.3f msec\n", total/num_cars_entered);
    printf("Max.  Time :%12.3f msec\n", max);
    printf("Total Time :%12.3f msec\n", total);
    print_footer();


}
