/**
 * CS 270: Solver template for Assignment 02
 * See assignment document for details.
 */
public class Solver {

	private int[] powersOf2;

	/**
	 * Testing method for the Solver class that allows it to be run by itself
	 * without the Driver. This code may be helpful for testing and debugging.
	 * Feel free to modify the method for your own testing purposes.
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		Solver s = new Solver();
		int min = -8;
		int max = +7;
		for (int i = min; i <= max; ++i) {
			char[] binaryStr = s.decimalToBinary(i);
			System.out.format("%5d: %10s%n", i, Driver.arrayToString(binaryStr));
		}
	}

	/**
	 * Constructor for the Solver. It should allocate memory for and initialize
	 * the powersOf2 member array.
	 */
	public Solver() {

		//Initialize the powersOf2 array with the first 16 powers of 2.
		powersOf2 = new int[] {1,2,4,8,16,32,64,128,256,512,1024,2048,4096,8192,16384,32768}; 
	}

	/**
	 * Determines the number of bits needed to represent the given value in
	 * two's complement binary.
	 *
	 * @param value The value to represent.
	 * @return The number of bits needed.
	 */
	public int howManyBits(int value) {

		// goes through the powersOf2 array and if the value is 
		// between the -2^(n-1) and 2^(n-1)-1 then that is the bit
		// range 
		for( int i = 0; i < powersOf2.length; i++ )
		{

			if( value >= -(powersOf2[i]) && value <= (powersOf2[i] - 1 ) )
			{

				return i + 1;
			}
		}

		return 0;
	}

	/**
	 * Converts the given value into a two's complement binary number
	 * represented as an array of char. The array should use the minimal
	 * number of bits that is necessary.
	 *
	 * @param value The value to convert.
	 * @return A char array of 0's and 1's representing the number in two's
	 *         complement binary. The most significant bit should be stored in
	 *         position 0 in the array.
	 */
	public char[] decimalToBinary(int value) {

		//if value is less then 0 (negative) need to make the value positive
		// then find the binary string for it, and then do the twosComplement
		// to get the correct binary string
		if( value < 0 )
		{
			value = value * -1;
			return twosComplementNegate( divisionMethod(value) );
		}

		return divisionMethod(value);
	}

	/**
	 * Computes the two's complement (negation) of the given two's complement
	 * binary number and returns the result.
	 *
	 * @param binaryStr The binary number to negate.
	 * @return The negated number in two's complement binary.
	 */
	public char[] twosComplementNegate(char[] binaryStr) {

		char[] returnArray = new char[binaryStr.length];
		int count = 0;

		//negating the original binaryStr to the other
		// char values
		while( count < binaryStr.length )
		{
			char tmp = binaryStr[count];

			if( tmp == '1' )
			{
				returnArray[count] = '0';
			}
			else
			{
				returnArray[count] = '1';
			}

			count++;
		}

		// add one to the negated array
		returnArray = addingExpressions( returnArray, new char[] {'0','1'} );

		return returnArray;
	}

	/**
	 * Applies sign extension to the given two's complement binary number so
	 * that it is stored using the given number of bits. If the number of bits
	 * is smaller than the length of the input array, the input array itself
	 * should be returned.
	 *
	 * @param binaryStr The binary number to sign-extend.
	 * @param numBits The number of bits to use.
	 * @return The sign-extended binary number.
	 */
	public char[] signExtend(char[] binaryStr, int numBits) {

		// returning the input array if the numBits is smaller then
		// the input array length
		if( numBits < binaryStr.length )
		{
			return binaryStr;
		}

		// creating a specific char that represents the sign bit
		// taking from the binaryStr and the one in the first spot
		char signBit = binaryStr[0];
		char[] extendedStr = new char[numBits];
		int count = 0;

		//This for loop adds the sign bit until it hits the index that can
		// has the other original bits from the binaryStr
		for( int i = 0; i < (numBits - binaryStr.length); i++ )
		{
			extendedStr[i] = signBit;
		}

		//This loop adds the original bits to the extended array to,
		// including the extended sign bits from the first array
		for( int i = numBits - binaryStr.length; i < numBits; i++ )
		{
			extendedStr[i] = binaryStr[count];
			count++;
		}

		return extendedStr;
	}

	/**
	 * Evaluates the expression given by the two's complement binary numbers
	 * and the specified operator.
	 *
	 * @param binaryStr1 The first number.
	 * @param op The operator to apply (either "+" or "-").
	 * @param binaryStr2 The second number.
	 * @return The result from evaluating the expression, in two's complement
	 *         binary. Note that a '*' should be appended to the returned
	 *         result if overflow occurred.
	 */
	public char[] evaluateExpression(char[] binaryStr1, String op, char[] binaryStr2) {

		// this will be just two positive binary  together, so add them!
		if( op.equals("+") )
		{
			return addingExpressions( binaryStr1, binaryStr2 );
		}
		else if( op.equals("-") )
		{
			// put the second binary string through twosComplement
			binaryStr2 = twosComplementNegate( binaryStr2 );

			return addingExpressions( binaryStr1, binaryStr2 );
		}

		return binaryStr1;
	}

	/**
	 * This method will add expression together. It can be used for the
	 * twosComplement method to add one, or for the evaluateExpression method
	 * when you are adding two expressions together 
	 * 
	 * @param original The first expression that will be added
	 * @param added The second expression that will be added to the original
	 * @return answer the expression that correlates to original + added arrays 
	 */
	private char[] addingExpressions(char[] original, char[] added )
	{

		//Make sure you make a statement about checking the length. 
		//Especially if adding only 1 for two's complement you will 
		//use the extend sign bit for this to make sense. 

		if( added.length < original.length )
		{
			// make the added char array longer by using extend sign bit
			added = signExtend(added, original.length );
		}


		char[] answer = new char[original.length];
		char[] carried = new char[original.length+1];
		carried[original.length] = '0';


		for(int i = original.length-1; i >= 0;i--)
		{

			/*
			 * Getting the numeric values that correlate with the char
			 * values of 0 and 1
			 */
			int value = ( Character.getNumericValue( carried[i+1]) 
					+ Character.getNumericValue(original[i] )
					+ Character.getNumericValue( added[i] ) );
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
			if( value == 0 )
			{
				answer[i] = '0';
				carried[i] = '0';
			}
			else if( value == 1 )
			{
				answer[i] = '1';
				carried[i] = '0';
			}
			else if( value == 2 )
			{
				answer[i] = '0';
				carried[i] = '1';
			}
			else if( value == 3 )
			{
				answer[i] = '1';
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
		 */
		if( carried[0] == '1' )
		{
			char[] overflow = new char[answer.length+1];
			int count = 0;
			overflow[0]='*';
			for(int i = 1; i < overflow.length; i++ )
			{
				overflow[i] = answer[count];
				count++;
			}

			answer = overflow;
		}

		return answer;
	}

	/**
	 * Convert the int of 1 and 0 to the
	 * correlating char values
	 * 
	 * @param num int number that can be 0 or 1
	 * @return '0' or '1' 
	 */
	private char convertNumtoChar( int num )
	{
		if( num == 0 )
		{
			return '0';
		}

		return '1';
	}

	/**
	 * This converts the decimal number to the 
	 * bianry string
	 * 
	 * @param value decimal number 
	 * @return
	 */
	private char[] divisionMethod( int value )
	{
		// list of variables for this method
		// length will use the howManyBits method to know the length
		// of the answer
		int length = howManyBits(value);
		char[] returnArray = new char[length];
		char remainder;
		int num;

		/*
		 * while the value does not equal 0 it will continue to
		 *  Modulus by 2, which will give the remainder
		 *  convertNumtoChar will convert int to char and put into
		 * returnArray. The array will be backwards so we must switch
		 * the order 
		 */
		while( length !=  0 )
		{
			num = value%2;
			remainder = convertNumtoChar( num );
			returnArray[length-1] = remainder;
			length--;
			value = value/2;
		}

		return returnArray;

	}

}
