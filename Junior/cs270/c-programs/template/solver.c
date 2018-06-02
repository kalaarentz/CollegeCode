/**
 * A program to convert numbers to a binary representation in a char array, and
 * perform two's complement arithmetic.
 *
 *  - void init_pows()
 *  - int how_many_bits(char * input, int len)
 *  - char * twos_complement(char * input, int len)
 *  - char * string_to_binary(char * input, int len, int ret_len)
 *  - char * solve_exp(char * input, int len)
 *
 * @author Samantha Foley
 *
 */

#include "stdlib.h"
#include"solver.h"
#include "math.h"

// other methods
char * division_of_binary_value(char * input, int len);
char * adding_expressions(char * orginal, int org_len, char * added, int added_len);
char * extend_sign_of_bits(char * input, int len, int num_of_bits );
char * evaluate_expression(char * binary1, int binary1_len, char * binary2, int binary2_len, char op );


// globals
int * pows;
const int num_pows = 16;
int powersOf2[16];


/*
 * init_pows is a method that sets the num_pows and pows global parameters.
 *
 */

void init_pows(){
  
    int i;

    for ( i = 0;i < 16; i++) {
        powersOf2[i] = (int)pow(2,i);
    }
    // set pointer of pows to the begining powersOf2 array
    pows = powersOf2;
}

/*
 * How many bits method takes a number and finds how many bits are needed to properly turn it into binary
 *
 * Parameters:
 * input: char* parameter array to find number of bits of
 * len: integer parameter length
 *
 *
 * Returns:
 * int of how many bits
 */


int how_many_bits(char * input, int len){
  
    int i;
    char *eptr;
    long val;
    init_pows();
    // if the result is not found then automatically send result = 0;
    val = strtol(input,&eptr,10);
    
    for( i = 0; i < num_pows; i++ ){
        if( val >= -(powersOf2[i]) && val <= (powersOf2[i] - 1) ) {
            
            return i+1;
        }
    }
    
  return 0;
}


/*
 * Negates a number using two's complement.
 *
 * Parameters:
 * input: char parameter of user input
 * len: int parameter length
 *
 * Returns:
 * input: char paramerter altered
 */

char * twos_complement(char * input, int len){
  
    static char* changed;
    char added[2] = {'0','1'};
    changed = malloc(len* sizeof(char));
    int count = 0;
    char tmp, *ptr;
    
    while( count < len ){
        tmp = input[count];
        if(tmp == '1') {
            changed[count] = '0';
        } else {
            changed[count] = '1';
        }
        count++;
    }
    
    ptr = adding_expressions(changed, len, added, 2);
    input = ptr;
    
  return input;
}



/*
 * Converts a string holding a numeric value into an array of '0's and '1's.
 *
 * Parameters:
 * input: char parameter input passed in
 * len: int parameter length
 * ret_len: result parameter return length
 *
 * Returns:
 * Description of the range of return values.
 */


char * string_to_binary(char * input, int len, int ret_len){
  
    char *eptr, *ptr_from_divsion, *ptr_from_twos_complement;
    long val;
    
    
    val = strtol(input,&eptr,10);
    
    if( val < 0 ){
        // TODO: must twos complement negate the expression
        // the do the division method
        
        ptr_from_divsion = division_of_binary_value(input, len);
        ptr_from_twos_complement = twos_complement(ptr_from_divsion, len);
        return ptr_from_twos_complement;
    } else {
        // return only the division method array
        ptr_from_divsion = division_of_binary_value(input, len);
    }
  return ptr_from_divsion;
}


/*
 * Returns a character array of '0's and '1's which is the result of expression
 * in binary.
 *
 * Parameters:
 * input: char parameter description
 * len: int parameter length
 *
 * Returns:
 * Character array with the result.  If overflow occurs, replace '0's and '1's with '*'s.
 */


char * solve_exp(char * input, int len){
  // TODO
    int i, endIdx, begIdx, binary2_len;
    char operator;
    static char* binary1;
    static char* binary2;
    char * ptr_from_evaluating_expression;
    
    endIdx = len;
    begIdx = 0;
    
    // find what the two char strings are for creating the two binary numbers
    for( i = 0; i < len;  i++) {
        if( input[i] == '+' || input[i] == '-' ) {
            operator = input[i];
            endIdx = i - 1;
            begIdx = i + 1;
            binary2_len = begIdx - len;
        }
    }
    
    binary1 = malloc((endIdx)* sizeof(char));
    binary2_len = begIdx - len;
    binary2 = malloc((binary2_len)* sizeof(char));
   
    ptr_from_evaluating_expression = evaluate_expression( binary1, endIdx, binary2, binary2_len, operator );
    
  return NULL;
}

/*
 * This converts the decimal number to the binary string
 * input: char paraeter description
 * len: int parameter length
 * return array: need to have the array be pointed to pointer in the method
 * so it can be used
 */
char * division_of_binary_value(char * input, int len) {
    char remainder;
    int num, value;
    char *eptr;
    static char* r;
    int length = how_many_bits(input, len );
    value = strtol(input,&eptr,10);
    
    // for converting the negative expression
    if( value < 0 ) {
        value = value * -1;
    }
    
    r = malloc(length* sizeof(char));
    
    while( length != 0 ) {
        num = value%2;
        remainder = num + '0';
        r[length - 1] = remainder;
        length--;
        value = value/2;
    }
    return r;
}

/**
 * This method will add expression together. It can be used for the
 * twosComplement method to add one, or for the evaluateExpression method
 * when you are adding two expressions together
 *
 * @param original The first expression that will be added
 * @param org_len the length of original array
 * @param added The second expression that will be added to the original
 * @param added_len teh length of added array
 * @return answer the expression that correlates to original + added arrays
 */
char * adding_expressions(char * original, int org_len, char * added, int added_len) {
    char carried[org_len];
    carried[org_len] = '0';
    static char* answ;
    answ = malloc(org_len* sizeof(char));
    // only use this array if overflow occurs
    static char* overflow;
    overflow = malloc( (org_len+1)* sizeof(char));
    int i, value, count;
    char *ptr_from_extend_sign_bits;
    
    // make the char longer if added_len is less then org_len
    if( added_len < org_len ) {
        // make char array longer by using extend sign bit
        ptr_from_extend_sign_bits = extend_sign_of_bits(added, added_len, how_many_bits(original, org_len));
        added = ptr_from_extend_sign_bits;
    }
    
    for( i = org_len-1; i >=0; i-- ) {
        value = atoi(&carried[i+1])+ atoi(&original[i]) + atoi(&added[i]);
        
        /*
         * These if statements take the numeric value of the char
         * so zero or one, and adds them to get a different answer.
         * Will be adding three char numeric values so it will give
         * value=0 is 00 in bit form
         * value=1 is 01 in bit form
         * value=2 is 10 in bit form
         * value=3 is 11 in bit form
         *
         * it will look like this form
         * carried   0 0 0 1 0
         * original  1 0 1 0 1
         * added     0 0 0 0 1
         * answer    1 0 1 1 1 
         */
        if( value == 0) {
            answ[i] = '0';
            carried[i] = '0';
        } else if ( value == 1 ) {
            answ[i] = '1';
            carried[i] = '0';
        } else if ( value == 2) {
            answ[i] ='0';
            carried[i] = '1';
        } else if( value == 3 ) {
            answ[i] = '1';
            carried[i] = '1';
        }
        
    }
    
    /*
     * Dealing with overflow
     * Carried array length is longer by one
     * to deal with over flow
     *
     * if a '1' is in the carried[0] then overflow
     * occurred
     * if a '0' is in the carried[0] then overflow
     * did not occur
     *
     * count = 0 and will be increamented by 1
     */
    count = 0;
    if( carried[0] == '1' ) {
        overflow[0] = '*';
        for(i = 1; i < org_len+1; i++) {
            overflow[i] = answ[count];
            count++;
        }
        return overflow;
    }
    
    
    return answ;
}

/**
 * Applies sign extension to the given two's complement binary number so
 * that it is stored using the given number of bits. If the number of bits
 * is smaller than the length of the input array, the input array itself
 * should be returned.
 *
 * @param input The binary number to sign-extend.
 * @param len length of input
 * @param num_of_bits The number of bits to use.
 * @return The sign-extended binary number.
 */
char * extend_sign_of_bits(char * input, int len, int num_of_bits ) {
    static char* answ;
    answ = malloc(num_of_bits* sizeof(char));
    char signBit = input[0];
    int count = 0;
    int i;
    
    if( num_of_bits < len ) {
        return input;
    }
    
    // for loops adds the sign bit until it hits the index that has
    // the other original bits from the input
    for( i = 0; i < (num_of_bits-len); i++ ){
        answ[i] = signBit;
    }
    
    // this loop adds teh orginal bits to the answ array to,
    // including the extended sign bits from the string array
    for( int i = num_of_bits-len; i < num_of_bits; i++ ) {
        answ[i] = input[count];
        count++;
    }
    
    return answ;
}

/**
 * Evaluates the expression given by the two's complement binary numbers
 * and the specified operator.
 *
 * @param binary1 The first number.
 * @param binary1_len the length of the first char input
 * @param binary2 The second number.
 * @param binary2_len the length of the second char input
 * @param op The operator to apply (either "+" or "-").
 * @return The result from evaluating the expression, in two's complement
 *         binary. Note that a '*' should be appended to the returned
 *         result if overflow occurred.
 */
char * evaluate_expression(char * binary1, int binary1_len, char * binary2, int binary2_len, char op ) {
    char *ptr_from_twos_complement;
    
    if( op == '+' ) {
        return adding_expressions(binary1, binary1_len, binary2, binary2_len);
    } else if ( op == '-' ) {
        ptr_from_twos_complement = twos_complement(binary2, binary2_len);
        binary2 = ptr_from_twos_complement;
        return adding_expressions(binary1, binary1_len, binary2, binary2_len);
    }
    
    return binary1;
}



