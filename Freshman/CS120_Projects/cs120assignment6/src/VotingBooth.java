/**
 * This program will have three candidates for presidents and vice presidents. 
 * Each candidate must supply their name, political party, and a brief statement
 * of why they are running when they register for the election. You will be using the 
 * getCandidate() method to ask the user for information on each of the candidates.
 * After setting up the candidates you will start the voting process. Once the voting
 * process has been started you will process the votes to make sure they are using the 
 * correct numbers. Once a valid vote has been used you will record the vote. The program
 * will continue prompting the user to keep voting until it is false. After the user 
 * decides to not keep voting the program will display the final winner. 
 * 
 * Last Modified: October 29, 2014
 * @author Kala Arentz
 */
import java.util.Scanner;

public class VotingBooth {
	/** No class scoped variables (data members) are allowed */

	/**
	 * The main method, where the program starts
	 * @param args
	 */
	public static void main(String[] args) {
		VotingBooth driver = new VotingBooth();
		driver.runPollingStation();
		// No other Java statements should be in here.
	}

	/**
	 * Constructor for the Booth driver.
	 */
	public VotingBooth() {
		// Nothing to initialize in this class (no data members).
		// No other Java statements should be in here.
	}

	/**
	 * Run a voting booth for a polling station
	 * Note: This is where the program really starts
	 */
	public void runPollingStation() {
		boolean keepVoting = true;

		/*
		 * Candidates declare themselves
		 * Hint: Use the getCandidate() method here
		 */
		Scanner input = new Scanner(System.in);
		Candidate candidateP1, candidateP2, candidateP3; // Objects for Presidents
		Candidate candidateVP1, candidateVP2, candidateVP3;// Objects for Vice Presidents

		System.out.println("Input Candidates for President:");
		System.out.println("-------------------------------");

		/* call to the getCandidate method for President
		 * (asks user for input for name, blurb, and political party)
		 */
		candidateP1 = getCandidate( input, 1, "President" );
		candidateP2 = getCandidate( input, 2, "President" );
		candidateP3 = getCandidate( input, 3, "President" ); 

		System.out.println();
		System.out.println( "Input Candidates for Vice President:" );
		System.out.println( "------------------------------------" );

		/*call to the getCandidate method for Vice Presidents (asks user
		 * for input for name, blurb, and political party)
		 */
		candidateVP1 = getCandidate( input, 1, "Vice President" );
		candidateVP2 = getCandidate( input, 2, "Vice President" );
		candidateVP3 = getCandidate( input, 3, "Vice President" );

		/*
		 * Start the voting process
		 * Stop when user decides voting is over
		 */
		System.out.println();
		System.out.println("Begin voting...");
		System.out.println("---------------------------------");
		do {
			/*
			 * Vote for President
			 * Hint: Use the displayCandidate() and processVoteFromUser() methods here
			 */
			System.out.println("Your Presidential Candidates");
			// Will be using the displayCandidate method for presidents
			displayCandidate( 1, candidateP1 );
			displayCandidate( 2, candidateP2 );
			displayCandidate( 3, candidateP3 );

			processVoteFromUser(input, "President", candidateP1, candidateP2, candidateP3);

			/*
			 * Vote for Vice President
			 * Hint: Use the displayCandidate() and processVoteFromUser() methods here
			 */

			System.out.println("Your Vice Presidential Candidates");
			// will be using the displayCandidate method for vice presidents
			displayCandidate( 1, candidateVP1 );
			displayCandidate( 2, candidateVP2 );
			displayCandidate( 3, candidateVP3 );

			processVoteFromUser(input, "Vice President", candidateVP1,
					candidateVP2, candidateVP3);

			/*
			 * Continue voting
			 * Hint: Use the askIfShouldContinueVoting() method here
			 */
			System.out.println("");

			// use the askIfShouldContinueVoting method
			keepVoting = askIfShouldContinueVoting( input );

			System.out.println("");
			System.out.println("---------------------------------");

		} while( keepVoting );

		/*
		 * Display the winners
		 * Hint: Use the displayWinner() method here
		 */
		System.out.println("---------------------------------");
		// will use the displayWinner() method here to display the winner of the votes
		displayWinner( "President", candidateP1, candidateP2, candidateP3 );
		displayWinner( "Vice President", candidateVP1, candidateVP2, candidateVP3 );

	}

	/**
	 * Get all of the Candidate information from the user and construct a
	 * Candidate object containing all of that information.
	 * This method will prompt the user for the name, blurb, and political party after
	 * displaying the title banner. For example, this method will display the following
	 * template (replace INDEX and POSITION with the parameters provided):
	 * 
	 * Enter information for Candidate INDEX for POSITION 
	 *      Name :
	 *      Blurb:
	 *      Political Party:
	 * 
	 * @param input Scanner to use for user input
	 * @param index Position of the Candidate in the voting order
	 * @param position The position the user is voting for (e.g., President, Vice President)
	 * @return A new, valid Candidate object with all of the necessary information
	 */
	private Candidate getCandidate(Scanner input, int index, String position) {
		// Asks for the information each candidate (3 for President/VicePresident)
		String name, blurb, politicalParty;

		System.out.println("Enter information for Candidate " + index 
				+" for " + position);
		System.out.print('\t' + "Name  : ");
		name = input.nextLine();

		System.out.print('\t' + "Blurb : ");
		blurb = input.nextLine();

		System.out.print('\t' + "Political Party: ");
		politicalParty = input.nextLine();

		System.out.println();
		Candidate candidateObject = new Candidate(name, position);
		candidateObject.setBlurb(blurb);
		candidateObject.setPoliticalParty(politicalParty);

		return candidateObject;
	}

	/**
	 * Display a single candidate. Example format below:
	 *   (1) John Doe [22] "I want to win. Vote for me!"
	 *   
	 * @param index Position of the Candidate in the voting order
	 * @param cand Candidate to display information about
	 */
	private void displayCandidate(int index, Candidate cand) {
		// Will display all the candidates before they start voting
		System.out.println( "(" + index + ") " + cand.getName() + " [" 
				+ cand.getPoliticalParty() + "] " + "\"" + cand.getBlurb() 
				+ "\"");
	}

	/**
	 * Get a vote from the user. Keep asking until it is a valid vote.
	 * Will display the following error if the number is not 1, 2, or 3
	 *    Error: Enter a value between 1 and 3!
	 * Once a valid vote has been entered then the vote is recorded for the candidate
	 * chosen by the user. Note that all votes are kept as part of the Candidate object.
	 *    
	 * @param input Scanner to use for user input
	 * @param position The position the user is voting for (e.g., President, Vice President)
	 * @param first First Candidate for this position (Index = 1)
	 * @param second Second Candidate for this position (Index = 2)
	 * @param third Third Candidate for this position (Index = 3)
	 */
	private void processVoteFromUser(Scanner input, String position,
			Candidate first, Candidate second, Candidate third) {
		// TODO Write me
		int answer;
		System.out.print( "Which " + position + " candidate are you voting for? ");
		answer = input.nextInt();

		//Check to make sure the votes are accurate numbers
		do {
			if ( answer != 1 && answer != 2 && answer != 3 ) {
				System.out.println("Error: Enter a value between 1 and 3!");
				System.out.print( "Which " + position + " candidate are you voting for? ");
				answer = input.nextInt();
			}
		} while ( answer != 1 && answer != 2 && answer != 3 );

		// adding votes to who won the votes
		if ( answer == 1 ){
			first.recordVote();
		}
		else if ( answer == 2 ){
			second.recordVote();
		}
		else if ( answer == 3 ){
			third.recordVote();
		}

	}

	/**
	 * Ask the user if they wish to continue voting.
	 * Keep asking until they enter either '1' for yes, or '0' for no.
	 * 
	 * @param input Scanner to use for user input
	 * @return true if they responded yes (1), and false if they responded no (0) 
	 */
	private boolean askIfShouldContinueVoting(Scanner input) {
		// use true or false for the do...while loop for keep voting/ process votes as well
		int answer;
		boolean keepVoting = true;

		System.out.print( "Continue voting? [yes=1, no=0] ");
		answer = input.nextInt();

		//check input to be a correct number
		do {
			if (answer != 1 && answer != 0) {
				System.out.println("Error: Please enter either 1 or 0!");
				System.out.print( "Continue voting? [yes=1, no=0] ");
				answer = input.nextInt();
			}
		} while (answer != 1 && answer != 0);

		// give specific number to the boolean expression
		if ( answer == 1 ){
			keepVoting = true;
		}
		else if ( answer == 0 ){
			keepVoting = false;
		}

		return keepVoting;
	}

	/**
	 * Display the winner of the election for this position.
	 * Display the position, candidate name, candidate party and number of votes:
	 *   POSITION: NAME of the PARTY with VOTES vote(s)
	 * For example:
	 *   President: Grace Hopper of the Automatic Languages party with 237 vote(s)
	 *   
	 * @param position The position the user is voting for (e.g., President, Vice President)
	 * @param first First Candidate (Index = 1)
	 * @param second Second Candidate (Index = 2)
	 * @param third Third Candidate (Index = 3)
	 */
	private void displayWinner(String position, Candidate first, Candidate second,
			Candidate third) {
		// Selection statement to display the winner for both candidates

		if ( first.getNumberOfVotes() >= second.getNumberOfVotes() 
				&& second.getNumberOfVotes() >= third.getNumberOfVotes()){
			System.out.println( position + ": " + first.getName() + " of the " 
					+ first.getPoliticalParty() + " party with " +
					first.getNumberOfVotes() + " vote(s)");
		}
		else if ( first.getNumberOfVotes() >= third.getNumberOfVotes() 
				&& third.getNumberOfVotes() >= second.getNumberOfVotes()){
			System.out.println( position + ": " + first.getName() + " of the " 
					+ first.getPoliticalParty() + " party with " +
					first.getNumberOfVotes() + " vote(s)");
		}
		else if ( second.getNumberOfVotes() >= first.getNumberOfVotes() 
				&& first.getNumberOfVotes() >= third.getNumberOfVotes()){
			System.out.println( position + ": " + second.getName() + " of the " 
					+ second.getPoliticalParty() + " party with " +
					second.getNumberOfVotes() + " vote(s)");
		}
		else if ( second.getNumberOfVotes() >= third.getNumberOfVotes() 
				&& third.getNumberOfVotes() >= first.getNumberOfVotes()){
			System.out.println( position + ": " + second.getName() + " of the " 
					+ second.getPoliticalParty() + " party with " +
					second.getNumberOfVotes() + " vote(s)");
		}
		else if ( third.getNumberOfVotes() >= second.getNumberOfVotes() 
				&& second.getNumberOfVotes() >= first.getNumberOfVotes()){
			System.out.println( position + ": " + third.getName() + " of the " 
					+ third.getPoliticalParty() + " party with " +
					third.getNumberOfVotes() + " vote(s)");
		}
		else if ( third.getNumberOfVotes() >= first.getNumberOfVotes() 
				&& first.getNumberOfVotes() >= second.getNumberOfVotes()){
			System.out.println( position + ": " + third.getName() + " of the " 
					+ third.getPoliticalParty() + " party with " +
					third.getNumberOfVotes() + " vote(s)");
		}
		
	}
}
