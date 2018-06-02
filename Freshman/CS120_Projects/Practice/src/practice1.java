
public class practice1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("----------- Loop 3 -----------");
		 int ret=25, var = 1, out = 10;
	        
	        while( ret >= 5 ) { 
	            var = out * 2;
	            do {
	                System.out.println("["+ret+","+var+"]");
	                var++;
	            } while( var < ret );
	            ret -= out;
	        }
	        
	        System.out.println("----------- Loop 6"	+ " -----------");
	        int limit = 6;
	        int result = 0;
	        int value = 1;
	        for(int idx = 0; idx < limit; ++idx) {
	            if( 0 == idx ) {
	                System.out.print(value + " ");
	                result += value;
	            }
	            else if( idx % 2 != 0 ) {
	                System.out.print("- " + value + " ");
	                result -= value;
	            }
	            else {
	                System.out.print("+ " + value + " ");
	                result += value;
	            }                
	            value += 2;
	        }
	        System.out.println();
	        System.out.println("Result : " + result);
	    }
	        
	}


