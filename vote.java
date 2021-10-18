import java.io.*;
import java.util.*;

public class vote{

	/* Normally, it is bad practice to have the main function throw an Exception,
	 *   but because this is a programming competition and you want to complete
	 *   the problem as fast as possible, having main throw an Exception is 
	 *   a good idea.  If you didn't, you would have to put try/catch clauses
	 *   around all of the file I/O statements.
	 **/
	public static void main(String[] args) throws Exception{

		BufferedReader br = new BufferedReader(new FileReader("vote.in"));
		StringTokenizer tokenizer;
		String decision;
		int n, m, amendmentNumber, amendmentIndex;
		ArrayList amendmentList;
		Amendment[] amendmentArray;
		Amendment newAmendment;

		/* I call the trim() function just in case the judges are mean
		 *   and put spaces before or after the number
		 * If the judges did put spaces on the first line,
		 *   the Integer.parseInt() function would throw an exception
		 **/
		n = Integer.parseInt(br.readLine().trim());
		
		/* Loop through all of the districts
		 **/
		for(int district = 1; district <= n; district++){
		
			/* read in the number of Amendments for this district
			 **/
			m = Integer.parseInt(br.readLine().trim());
			
			/* We will use an amendment list to temporarily store all
			 *   of the Amendments.  The Amendments class is defined below.
			 **/
			amendmentList = new ArrayList();
			
			/* Loop through all of the amendments
			 **/
			for(int i = 0; i < m; i++){
			
				/* Read in Amendment
				 **/
				tokenizer = new StringTokenizer(br.readLine());
				
				/* skip over "Amendment" String
				 **/
				tokenizer.nextToken();
				
				/* Read in this Amendment's number and decision.
				 * The decision is either "for" or "against".
				 **/
				amendmentNumber = Integer.parseInt(tokenizer.nextToken());
				decision = tokenizer.nextToken();

				/* Create a new Amendment with the Amendment number
				 * Note: We have not inserted this into the ArrayList yet,
				 *   because we don't want to have Amendments in the ArrayList
				 *   with the same Amendment number.
				 **/
				newAmendment = new Amendment(amendmentNumber);
				
				/* If there is already an Amendment in the ArrayList that 
				 *   has the same Amendment number as the current Amendment,
				 *   then amendmentIndex will equal the index into the ArrayList
				 *   that the Amendment resides.
				 * Otherwise, amendmentIndex will be less than zero, and we have
				 *   to insert this new amendment into the ArrayList.
				 **/
				amendmentIndex = amendmentList.indexOf(newAmendment);
				
				/* The amendment we are currently reading in has a new Amendment
				 *   number, and we add it to the ArrayList.
				 * Also, the amendmentIndex is set appropriately.
				 **/
				if(amendmentIndex < 0){
					amendmentIndex = amendmentList.size();
					amendmentList.add(newAmendment);
				}

				/* Add a vote for or against this amendment based on the decision String
				 * Note: When we grab the Amendment from the ArrayList, it comes out as
				 *   type Object.  We have to first cast it to an Amendment type in order
				 *   to call the addVoteFor() and addVoteAgainst() functions.
				 **/
				if(decision.equals("for")){
					((Amendment)amendmentList.get(amendmentIndex)).addVoteFor();
				}else{
					((Amendment)amendmentList.get(amendmentIndex)).addVoteAgainst();
				}

			}
			
			/* The arrayList is now converted into an Amendment array.
			 * This allows us to sort the array and easily access the Amendments
			 *   in the array.
			 **/
			amendmentArray = (Amendment[])amendmentList.toArray(new Amendment[]{});
			
			/* Sort the array by Amendment number.
			 * For more info on this, see how the Amendment class is created below.
			 **/
			Arrays.sort(amendmentArray);
			
			/* Simply loop through all Amendments and print them out.
			 **/
			System.out.println("District " + district + ":");
			for(int i = 0; i < amendmentArray.length; i++){
				System.out.print("Amendment " + amendmentArray[i].number + " : ");
				System.out.print(amendmentArray[i].votesFor + " for, ");
				System.out.print(amendmentArray[i].votesAgainst + " against : ");
				
				if(amendmentArray[i].votesFor > amendmentArray[i].votesAgainst){
					System.out.println("YEA.");
				}else if(amendmentArray[i].votesFor < amendmentArray[i].votesAgainst){
					System.out.println("NAY.");
				}else{
					System.out.println("UND.");
				}
				
			}
			System.out.println();
			
		}
		
	}

	/* In order for us to use the Arrays.sort(Comparable[] objs) function,
	 *   we must define a class that implements Comparable.
	 * Also, we have to override the equals and compareTo functions.
	 **/
	private static class Amendment implements Comparable{
	
		/* Each amendment has a unique number, and we
		 *   also keep track of the number of votes
		 *   for and against the Amendment.
		 **/
		int number, votesFor, votesAgainst;
		
		/* Constructor that takes in an Amendment number and
		 *   initializes the votesFor and votesAgainst to zero.
		 **/
		public Amendment(int n){
		
			this.number = n;
			this.votesFor = 0;
			this.votesAgainst = 0;
		
		}
		
		/* Increment this Amendment's number of votesFor by one.
		 **/
		public void addVoteFor(){
			this.votesFor++;
		}
		
		/* Increment this Amendment's number of votesAgainst by one.
		 **/
		public void addVoteAgainst(){
			this.votesAgainst++;
		}
		
		/* Defining this function allows us to call the indexOf() function
		 *   in ArrayList.  The indexOf() function in ArrayList will search
		 *   through the ArrayList and return the index of the Amendment in it
		 *   that has the same Amendment number as the one passed to the indexOf()
		 *   function.  If that's confusing, just see how the indexOf function is 
		 *   used in the main function above.
		 **/
		public boolean equals(Object that){

			Amendment lhs = (Amendment)this;
			Amendment rhs = (Amendment)that;
			
			return (lhs.number == rhs.number);
			
		}
		
		/* The compareTo function needs to be defined in order to use the Arrays.sort() function.
		 * This function returns -1 if the left hand side Object (this) is less than the right
		 *   hand side Object (that).
		 * It returns 1 if the LHS is greater than the RHS.
		 * Returns 0 if they are equal.
		 *
		 * We use this to compare two Amendments' numbers.  We have to print the output in
		 *   ascending amendment number order, so this gives us an easy way to sort the
		 *   amendments.
		 **/
		public int compareTo(Object that){
		
			Amendment lhs = (Amendment)this;
			Amendment rhs = (Amendment)that;
			
			if(lhs.number < rhs.number) return -1;
			if(lhs.number > rhs.number) return 1;
			
			return 0;
			
		}
		
	}
	
}