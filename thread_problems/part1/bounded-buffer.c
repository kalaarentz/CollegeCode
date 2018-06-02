/*
 * Derek Stoner, Ben Black
 *
 * CS 441/541: Bounded Buffer (Project 4 Part 1)
 *
 */
#include "bounded-buffer.h"
int test = 1;

int main(int argc, char * argv[]) {
    int time_to_live;
    int number_of_producer_threads;
    int number_of_consumer_threads;
    int index;
    int buffer_size = 10;
    
    if(argc < 4){
        printf("Error with inputs!\n");
        print_correct_use();
        return -1;
    }
    if (process_args(&time_to_live, &number_of_producer_threads, &number_of_consumer_threads, &buffer_size, argv) == -1){
        fprintf(stderr, "Error with inputs!\n");
        print_correct_use();
        return -1;
    }
    buffer = (int *) malloc(buffer_size * sizeof(int*));
    size = buffer_size;
    print_intro(buffer_size, time_to_live, number_of_producer_threads, number_of_consumer_threads);
    srandom(time(NULL));
    
    //initialization
    semaphore_create(&mutex, 1);
    semaphore_create(&full_buffer, size);
    semaphore_create(&empty_buffer, 0);
    pthread_attr_init(&attr);
    counter = 0;
    p_id_nums = 0;
    c_id_nums = 0;
    prod_position = 0;
    cons_position = 0;
    consumed = 0;
    produced = 0;
    for(index = 0; index < buffer_size; index++)
        buffer[index]= -1;
    
    printf("Initial Buffer:                       ");
    print_buffer();
    
    //create threads
    for(index = 0; index < number_of_producer_threads; index++) {
        pthread_create(&tid,&attr,producer,&index);
    }
    for(index = 0; index < number_of_consumer_threads; index++) {
        pthread_create(&tid,&attr,consumer,&index);
    }
    
    sleep(time_to_live);
    semaphore_wait(&mutex);
    
    printf("---------------------\n");
    printf("Produced%10d\n", produced);
    printf("Consumed%10d\n", consumed);
    printf("---------------------\n");
    
    free(buffer);
    buffer = NULL;
    
    return 0;
}

int process_args(int *time, int *prod, int *con, int *buff, char **args){
    
    if (parse_int(time, args[1]) || parse_int(prod, args[2]) || parse_int(con, args[3])
        || !*time || !*prod || !*con)
        return -1;
    if(args[4])
        if(parse_int(buff, args[4]) || !*buff)
            return -1;
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

void print_correct_use(){
    
    printf("This program requires at least three positive integers as arguments with an optional fourth!\n");
}

void print_intro(int b, int t, int p, int c){
    
    printf("Buffer Size               :%4d\n", b);
    printf("Time To Live              :%4d\n", t);
    printf("Number of Producer threads:%4d\n", p);
    printf("Number of Consumer threads:%4d\n", c);
    printf("-------------------------------\n");
}

void print_buffer(){
    int index;
    
    printf("[");
    for(index = 0; index < size; index ++){
        printf("%4d", buffer[index]);
        if(prod_position%size == index)
            printf("^");
        if(cons_position%size == index)
            printf("v");
    }
    printf("]\n");
}

void *producer(void *num) {
    int item;
    int id;
    
    semaphore_wait(&mutex);
    id = p_id_nums++;
    semaphore_post(&mutex);
    
    while(TRUE) {
        usleep(random()%100000);
        item = random()%10;
        semaphore_wait(&full_buffer);
        semaphore_wait(&mutex);
        
        if(insert_item(item)) {
            fprintf(stderr, " Producer report error condition\n");
        }
        else {
            produced++;
            printf("Producer%4d: Total%4d, Value%4d    ", id, produced, item);
            print_buffer();
            semaphore_post(&empty_buffer);
        }
        semaphore_post(&mutex);
        
    }
}

int insert_item(int item) {
    
    if(counter < size) {
        buffer[prod_position%size] = item;
        counter++;
        prod_position++;
        return 0;
    }
    else {
        return -1;
    }
}

void *consumer(void *num) {
    int item;
    int id;
    
    semaphore_wait(&mutex);
    id = c_id_nums++ ;
    semaphore_post(&mutex);
    
    while(TRUE) {
        usleep(random()%100000);
        semaphore_wait(&empty_buffer);
        semaphore_wait(&mutex);
        if(remove_item(&item)) {
            fprintf(stderr, "Consumer report error condition\n");
        }
        else {
            consumed++;
            printf("Consumer%4d: Total%4d, Value%4d    ", id, consumed, item);
            print_buffer();
            semaphore_post(&full_buffer);
        }
        semaphore_post(&mutex);
        
    }
}

int remove_item(int *item) {
    if(size > 0) {
        *item = buffer[cons_position%size];
        buffer[cons_position%size] = -1;
        counter--;
        cons_position++;
        return 0;
    }
    else {
        return -1;
    }
}

