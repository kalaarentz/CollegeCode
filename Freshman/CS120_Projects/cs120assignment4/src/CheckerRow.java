/*
 * This program will display a checker row in which the board is square (NxN cells), and 
 * each cell is square (MxM characters). The user will provide a positive integer value greater 
 * than 0. When displaying a cell on the board use capital X characters to represent dark 
 * spaces and (.) to represent light spaces. Before and after displaying the board there should 
 * be a border of (-) and it should be the same width of the board.
 * 
 * Date Last Modified <October 2, 2014>
 * 
 * Author<Kala Arentz>
 * 
 */
import java.util.Scanner;

public class CheckerRow {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int cellPerSide, cellSize, idx;

		do { // User enters the number of cells 
			System.out.print("Number of cells per side: ");
			cellPerSide = input.nextInt();
			if (cellPerSide<= 0) { // take into account no number below or equal to 0
				System.out.println("Error: Please enter a number greater than 0.");
			}

		} while (cellPerSide <= 0);
		do { // User enters the cell size
			System.out.print("Cell Size: ");
			cellSize = input.nextInt();
			if (cellSize <= 0 ) { // take into account no number below or equal to 0
				System.out.println("Error: Please enter a number greater than 0.");
			}

		} while (cellSize <= 0);

		for (idx =  0; (idx < (cellPerSide* cellSize)); idx++ ) { // make the border 
			System.out.print("-");

		}
		System.out.println();

		for ( idx = 0; (idx < (cellPerSide * cellSize)); idx++) { // alternate the dark and light checker boards
			if ((idx/cellSize)%2 == 0) {
				System.out.print(".");
			}
			else {
				System.out.print("X");
			}
		}
		System.out.println();

		for (idx =  0; (idx < (cellPerSide* cellSize)); idx++ ) { // make the border 
			System.out.print("-");

		}

	}

}
