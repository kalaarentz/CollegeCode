/*
Author: Kala Arentz

Date: May 11, 2015

Purpose:

*/
package finalExam;

import java.util.Iterator;

public class WrtingCode {

	public static void main(String[] args) {
		
		
	}

	private double  iteratesObject( Iterable< Integer > input )
	{
		
		double sums = 0.0;
		for( Integer num : input  )
		{
			sums += num;
		}
		
		return (double) sums;
	}

}
