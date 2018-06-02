/**
 * Basic Shot Choice for a Rock-Paper-Scissors-Lizard-Spock Game
 * 
 * Last Modified Nov. 9, 2014
 * @author J. Hursey
 */
public class ShotChoice {
    /** Shot constant variable: Invalid */
    public final static int INVALID = -1;
    /** Shot constant variable: Rock */
    public final static int ROCK = 0;
    /** Shot constant variable: Paper */
    public final static int PAPER = 1;
    /** Shot constant variable: Scissors */
    public final static int SCISSORS = 2;
    /** Shot constant variable: Lizard */
    public final static int LIZARD = 3;
    /** Shot constant variable: Spock */
    public final static int SPOCK = 4;
    
    /** Current Choice */
    private int choice = INVALID;
    
    /**
     * Hold one shot from the user
     * 
     * @param choic Choice of shot (see static final variables)
     */
    public ShotChoice(int choice) {
        this.choice = choice;
    }

    /**
     * Return the shot as an integer for quick comparison
     * @return Shot represented as an integer
     */
    public int getShotAsInt() {
        return choice;
    }
    
    /**
     * Access the shot represented as a String
     * 
     * @return String representation of the shot
     */
    public String getShotAsString() {
        switch( choice ) {
            case ROCK:
                return "Rock";
            case PAPER:
                return "Paper";
            case SCISSORS:
                return "Scissors";
            case LIZARD:
                return "Lizard";
            case SPOCK:
                return "Spock";
            default:
                return "Unknown";
        }
    }
    
    /**
     * Compare the current shot and the peer's shot.
     * <pre>
     * Return values:
     * If > 0 then This wins over Peer
     * If   0 then is is a draw.
     * If < 0 then Peer wins over This
     * If -99 Invalid (either)
     * 
     *         0 = Draw
     *  (+/-)  1 = Scissors cut Paper
     *  (+/-)  2 = Scissors decapitate Lizard
     *  (+/-)  3 = Paper disproves Spock
     *  (+/-)  4 = Paper covers Rock
     *  (+/-)  5 = Rock crushes Scissors
     *  (+/-)  6 = Rock crushes Lizard
     *  (+/-)  7 = Lizard eats Paper
     *  (+/-)  8 = Lizard poisons Spock
     *  (+/-)  9 = Spock vaporizes Rock
     *  (+/-) 10 = Spock smashes Scissors
     * </pre>
     * 
     * @param peer The peer's shot to compare to
     * @return See above for comparison chart.
     */
    public int compareShotTo( ShotChoice peer ) {
        int p2Shot = peer.getShotAsInt();
        
        // Case: Draw
        if ( this.choice == p2Shot ) {
            return 0;
        }
        //  (+/-)  1 = Scissors cut Paper
        else if ( this.choice== SCISSORS && p2Shot == PAPER ) {
            return 1;
        }
        else if ( this.choice == PAPER && p2Shot == SCISSORS ) {
            return -1;
        }
        //  (+/-)  2 = Scissors decapitate Lizard
        else if ( this.choice== SCISSORS && p2Shot == LIZARD ) {
            return 2;
        }
        else if ( this.choice == LIZARD && p2Shot == SCISSORS ) {
            return -2;
        }
        //  (+/-)  3 = Paper disproves Spock
        else if ( this.choice== PAPER && p2Shot == SPOCK ) {
            return 3;
        }
        else if ( this.choice == SPOCK && p2Shot == PAPER ) {
            return -3;
        }
        //  (+/-)  4 = Paper covers Rock
        else if ( this.choice== PAPER && p2Shot == ROCK ) {
            return 4;
        }
        else if ( this.choice == ROCK && p2Shot == PAPER ) {
            return -4;
        }
        //  (+/-)  5 = Rock crushes Scissors
        else if ( this.choice== ROCK && p2Shot == SCISSORS ) {
            return 5;
        }
        else if ( this.choice == SCISSORS && p2Shot == ROCK ) {
            return -5;
        }
        //  (+/-)  6 = Rock crushes Lizard
        else if ( this.choice== ROCK && p2Shot == LIZARD ) {
            return 6;
        }
        else if ( this.choice == LIZARD && p2Shot == ROCK ) {
            return -6;
        }
        //  (+/-)  7 = Lizard eats Paper
        else if ( this.choice== LIZARD && p2Shot == PAPER ) {
            return 7;
        }
        else if ( this.choice == PAPER && p2Shot == LIZARD ) {
            return -7;
        }
        //  (+/-)  8 = Lizard poisons Spock
        else if ( this.choice== LIZARD && p2Shot == SPOCK ) {
            return 8;
        }
        else if ( this.choice == SPOCK && p2Shot == LIZARD ) {
            return -8;
        }
        //  (+/-)  9 = Spock vaporizes Rock
        else if ( this.choice== SPOCK && p2Shot == ROCK ) {
            return 9;
        }
        else if ( this.choice == ROCK && p2Shot == SPOCK ) {
            return -9;
        }
        //  (+/-) 10 = Spock smashes Scissors
        else if ( this.choice== SPOCK && p2Shot == SCISSORS ) {
            return 10;
        }
        else if ( this.choice == SCISSORS && p2Shot == SPOCK ) {
            return -10;
        }

        // Case: Invalid.
        return -99;
    }
    
    /**
     * Compare the current shot and the peer's shot.
     * Return a String representing the comparison.
     * <pre>
     * Return values:
     *         0 = Draw
     *  (+/-)  1 = Scissors cut Paper
     *  (+/-)  2 = Scissors decapitate Lizard
     *  (+/-)  3 = Paper disproves Spock
     *  (+/-)  4 = Paper covers Rock
     *  (+/-)  5 = Rock crushes Scissors
     *  (+/-)  6 = Rock crushes Lizard
     *  (+/-)  7 = Lizard eats Paper
     *  (+/-)  8 = Lizard poisons Spock
     *  (+/-)  9 = Spock vaporizes Rock
     *  (+/-) 10 = Spock smashes Scissors
     *       -99 = Invalid
     * </pre>
     * 
     * @param peer The peer's shot to compare to
     * @return See above for comparison chart.
     */
    public String compareShotToAsString( ShotChoice peer ) {
        int p2Shot = peer.getShotAsInt();
        
        // Case: Draw
        if ( this.choice == p2Shot ) {
            return "Draw";
        }
        //  (+/-)  1 = Scissors cut Paper
        else if ( (this.choice== SCISSORS && p2Shot == PAPER)  ||
                  (this.choice == PAPER && p2Shot == SCISSORS) ){
            return "Scissors cut Paper";
        }
        //  (+/-)  2 = Scissors decapitate Lizard
        else if ( ( this.choice== SCISSORS && p2Shot == LIZARD ) ||
                  ( this.choice == LIZARD && p2Shot == SCISSORS ) ){
            return "Scissors decapitate Lizard";
        }
        //  (+/-)  3 = Paper disproves Spock
        else if( ( this.choice== PAPER && p2Shot == SPOCK ) ||
                 ( this.choice == SPOCK && p2Shot == PAPER ) ){
            return "Paper disproves Spock";
        }
        //  (+/-)  4 = Paper covers Rock
        else if( ( this.choice== PAPER && p2Shot == ROCK ) ||
                 ( this.choice == ROCK && p2Shot == PAPER ) ) {
            return "Paper covers Rock";
        }
        //  (+/-)  5 = Rock crushes Scissors
        else if( ( this.choice== ROCK && p2Shot == SCISSORS ) ||
                 ( this.choice == SCISSORS && p2Shot == ROCK ) ) {
            return "Rock crushes Scissors";
        }
        //  (+/-)  6 = Rock crushes Lizard
        else if( ( this.choice== ROCK && p2Shot == LIZARD ) ||
                 ( this.choice == LIZARD && p2Shot == ROCK ) ) {
            return "Rock crushes Lizard";
        }
        //  (+/-)  7 = Lizard eats Paper
        else if( ( this.choice== LIZARD && p2Shot == PAPER ) ||
                 ( this.choice == PAPER && p2Shot == LIZARD ) ){
            return "Lizard eats Paper";
        }
        //  (+/-)  8 = Lizard poisons Spock
        else if( ( this.choice== LIZARD && p2Shot == SPOCK ) ||
                 ( this.choice == SPOCK && p2Shot == LIZARD ) ) {
            return "Lizard poisons Spock";
        }
        //  (+/-)  9 = Spock vaporizes Rock
        else if( ( this.choice== SPOCK && p2Shot == ROCK ) ||
                 ( this.choice == ROCK && p2Shot == SPOCK ) ) {
            return "Spock vaporizes Rock";
        }
        //  (+/-) 10 = Spock smashes Scissors
        else if( ( this.choice== SPOCK && p2Shot == SCISSORS ) ||
                 ( this.choice == SCISSORS && p2Shot == SPOCK ) ) {
            return "Spock smashes Scissors";
        }

        // Case: Invalid.
        return "Invalid Shot(s)";
    }
}
