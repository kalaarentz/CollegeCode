
public class OnlineTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println( "Count: " + stringMatch( "aabc", "aabcze") );
	}

	public int stringMatch(String a, String b) {

		  int length, count = 0;
		  
		  if ( a.length() > b.length()){
		  length = a.length();
		  }
		  else {
		  length = b.length();
		  }
		  
		  for (int idx = 0; idx < length-1; idx++){
		  
		  if ( a.charAt(idx) == b.charAt(idx) ){
		  count++;
		  }
		  else {
		  
		  }
		  
		  }
		  return count;
		}
}
