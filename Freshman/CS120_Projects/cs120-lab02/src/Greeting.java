 /*
  * This program will prompt the user for their
  * name, and the user will input it into the 
  * program. In addition, the program will be polite
  * and ask for the user's favorite color
  * 
  * Last Modified <Sept. 9, 2014 at 1:13>
  * author <Kala Arentz>
  */

import java.util.Scanner;

public class Greeting {

	public static void main(String[] args) {
		Scanner userName = new Scanner(System.in);
		String name;
		Scanner askedColor = new Scanner(System.in);
		String color;
		
		// Program will say hello/ask for name
		System.out.print ("Hello! What is your name? ");
		// User will input name
		name = userName.nextLine();
		// Program will say a greeting, with person's name
		System.out.println("Awesome! It's a wonderful "
				+ "day today, ain't it " + name + "!");
		// Program will ask for favorite color
		System.out.print ("What is your favoirte color? ");
		//User will input color
		color = askedColor.nextLine();
		// Program will reply with color/sentence
		System.out.println(color + " is a cool color!");
		

	}

}
