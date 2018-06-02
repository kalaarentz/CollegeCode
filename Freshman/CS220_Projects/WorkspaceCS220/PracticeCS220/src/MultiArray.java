
public class MultiArray {

	public static void main( String[] args ){
		
	String[][] names = new String[3][];
	
	names[0] = new String[2];
	names[1] = new String[3];
	names[2] = new String[1];
	
	names[0][0] = "David";
	names[0][1] = "Jessica";
	
	names[1][0] = "Rachel";
	names[1][1] = "Batman";
	names[1][2] = "Harley";
	
	names[2][0] = "Cambria";
	
	for( int i = 0; i < names.length; i++ ){
		
		for( int j = 0; j < names[i].length; j++ ){
			
			System.out.println( "Names["+i+"] and ["+j+"] "+names[i][j] );
		}
	}
	
	}
}
