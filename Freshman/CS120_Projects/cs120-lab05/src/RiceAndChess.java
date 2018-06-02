/*
 * This program will help the ruler understand what happened when the inventor asked for one 
 * grain of rice on the first square, and then 2 on the second square and 4 grains of rice on
 * the third square and etc. For each tile (64), it needs to show the number of grains of rice 
 * on that specific tile, and the total number of grains of rice calculated so far.
 * 
 * Date Last Modified <October 1, 2014)>
 * 
 * Author<Kala Arentz>
 * 
 * 1.) When using integers the numbers stopped outputed after the 32nd tile, and just showed 0, 
 * and the total of amount of grains showed up as -1 after tile 31. The total weight of the 
 * grain showed up as -0 metric tonnes.
 * 2.)  The same thing happened with longs, but he went much more farther, all the way to tile 64, but it showed up
 * as a negative number. In addition, the total amount for the tile 64 showed up as a -1. The 
 * total weight of the grains came out as an -0 metric tonnes.
 * 3.) With the doubles the numbers come out as accurate, but the tiles would come out as 
 * decimals, so the tile variable would need to stay in the integers type.   
 *  
 */

import java.text.DecimalFormat;

public class RiceAndChess { 

	public static void main(String[] args) {

		double  count, result, total, weight;
		int tile;
		DecimalFormat df;
		df = new DecimalFormat("#,###,##0");


		// Start out with 1 grain of rice on tile one
		// Can't be over 64 tiles
		// Print out the tile, the amount of grain on that specific tile and accumulated grain
		// Initialization
		tile = 0;
		count = 1;
		total = 0;
		weight = 0;

		while (tile <=63) { // Condition
			// Main work (code to executed)
			result = count;
			// Make progress
			tile++ ;
			count *= 2 ;
			total += result;
			if (result == 1) { // the sentence for the singular grain
				System.out.println("On tile " + tile + " there is " + df.format(result) + " grain. Total: " + df.format(total) );
			}
			else { // when the grains are over 1, make it plural form
				System.out.println("On tile " + tile + " there are " + df.format(result) + " grains. Total: " + df.format(total) );
			}

		} //compute the total weight of all the grains
		System.out.println("Total Weight of Grain: Metric Tonnes " + df.format(total * 0.00000002 ));
	}

}
