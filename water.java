import java.io.*;
import java.util.*;

public class water
{
/***********************************
 *   MAIN
 ***********************************/
    //Throw the IOException because it is unimportant and only an issue
    //if I handle the input incorrectly or the file doesn't exist
    public static void main(String[] args) throws IOException
    {
    	//create a new instance of the problem solving class
    	water w = new water();
    	
    	//Call the problem solving function of the class
    	w.run();
    }


/***********************************
 *   Class Variables
 ***********************************/

	//setup the needed class wide variables
	private BufferedReader in;
	
	//use these strings to make it easier to change if we had needed to
	private String odd = "WS";
	private String even = "RU";
	

/***********************************
 *   Class Functions
 ***********************************/
	//Constructor for the class
	//Throw the IOException the function creating this
	//so it doesn't clutter code here
	public water() throws IOException
	{
		in = new BufferedReader(new FileReader("water.in"));
	}
	
	
	public void run() throws IOException
	{
		/* Rules of the problem
		 *  odd numbered houses can water on Wednesdays and Saturdays (WS)
		 *  even numbered houses can water on Thursdays and Sundays (RU)
		 */
		 
		/* Special Cases
		 *  100Boo (Number and Address with no space separation)
		 *  What about street names that start with numbers (if no separation of spaces)
		 */
		 
		/* Input Specs
		 * Case count
		 * for each case count
		 *   1 line that contains the number first and then the street
		 *   1 line that contains the watering days
		 */ 
		 
		 //Get the number of Cases and change that to an integer
		 int cases = Integer.parseInt(in.readLine());
		 
		 //Loop through the number of cases and handle it
		 for(int i = 0; i < cases; i++)
		 {
		 	handleCase();
		 }
	}
	
	public void handleCase() throws IOException
	{
		//Read the street address and parse it
		//Under the assumption that there will be a space between the address number and street name
		//Break up string by spaces
		StringTokenizer st = new StringTokenizer(in.readLine()," ");
		
		//The first token is supposed to be a number
		//And is specified as a positive integer so we don't have to worry about
		//really really big numbers that can't be held by integers
		int houseNumber = Integer.parseInt(st.nextToken());
		
		//Find out if the house number is even or not.
		//If it were even then the remainder would not be 0 
		boolean evenNumber = (houseNumber % 2) == 0; 
		
		//Note that the rest of the tokens don't matter
		
		//Grab the days the person watered the lawn.
		String watered = in.readLine();
		
		String badDays = "";
		//Loop through the days this person watered
		//and check to see if they watered on the right day
		for(int i = 0; i < watered.length(); i++)
		{
			//Get the current day
			char theDay = watered.charAt(i);
			
			//if the house number was even and was watered on a day other than those specified
			//add it to the badDays list
			if(evenNumber && even.indexOf(theDay) == -1)
			{
				badDays += theDay;
			}
			//If the house number was odd and was watered on a day other than those specified
			//add it to the badDays list
			else if(!evenNumber && odd.indexOf(theDay) == -1)
			{
				badDays += theDay;
			}
		}
		
		//if badDays length is 0 then they watered on correct days only
		//so print out All safe!  otherwise print out the badDays string
		if(badDays.length() == 0)
		{
			System.out.println("All safe!");
		}
		else
		{
			System.out.println(badDays);
		}
	}
}