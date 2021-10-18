import java.io.*;
import java.util.*;

public class signs
{
/************************************
 * MAIN
 ************************************/
	 //Throw the IOException because it is unimportant and only an issue
	 //if I handle the input incorrectly or the file doesn't exist
	 public static void main(String[] args) throws IOException
	 {
	 	//create an instance of the problem solving class
	 	signs s = new signs();
	 	
	 	//run the problem solving solution
	 	s.run();
	 }
 
/************************************
 * Class Variables
 ************************************/
 	private BufferedReader in;
 	private double PI = 3.14159;


/************************************
 * Class Funtions
 ************************************/

 	//Constructor for the class
	//Throw the IOException the function creating this
	//so it doesn't clutter code here
    public signs() throws IOException
    {
    	in = new BufferedReader(new FileReader("signs.in"));
    }
 
 	public void run() throws IOException
 	{
 		/* Rules of the problem
		 *  Use the A + B + C == 180 Rule
		 *  Use a / sin A = b / sin B = c / sin C
		 *  All angles should be less than 90
		 *  0.01 inch tolerance
		 */
		 
		/* Input Specs
		 * Each line will contain Angle Angle Angle side side side
		 *  Angles will not be between 0 and 90
		 *  if measurement is negative then it is missing
		 *  a line with all negative measurements will indicate end of problem
		 */ 
		 
		 int Counter = 1;
		 do
		 {
		 	//Read the input line and tokenize it by spaces
		 	StringTokenizer st = new StringTokenizer(in.readLine(), " ");
		 	
		 	//Grab all the measurements
		 	double[] measurements = new double[6];
		 	boolean[] empty = new boolean[6];
		 	
		 	for(int i = 0; i < 6; i++)
		 	{
		 		measurements[i] = Double.parseDouble(st.nextToken());
		 		empty[i] = measurements[i] < 0.0;
		 	}
		 	
		 	//If all of them are empty(negative) then exit as this is the terminating case
		 	if(empty[0] && empty[1] && empty[2] && empty[3] && empty[4] && empty[5])
		 	{
		 		return;
		 	}
		 	
		 	//See if you can fill out the angles real quick
		 	for(int i = 0; i < 3; i++)
 			{
 				int A = i;
 				int B = (i + 1) % 3;
 				int C = (i + 2) % 3;
 				if(!empty[A] && !empty[B] && empty[C])
 				{
 					measurements[C] = 180.0 - measurements[A] - measurements[B];
 					empty[C] = false;
 			
 				}
 				else if(!empty[A] && empty[B] && !empty[C])
 				{
 					empty[B] = false;
 					measurements[B] = 180.0 - measurements[A] - measurements[C];
 			
 				}
 			}
		 	
		 	//You wont be able to figure it out if you don't have a pair, so check
		 	int pair = -1;
		 	
		 	for(int i = 0; i < 3; i++)
		 	{
		 		if(!empty[i] && !empty[i+3])
		 		{
		 			pair = i;
		 			break;
		 		}
		 	}
		 	
		 	int emptyPairs = 0;
		 		
		 	//Won't be able to figure it out if a two groupings are empty either
	 		for(int i = 0; i < 3; i++)
	 		{
	 			if(empty[i] && empty[i + 3])
	 			{
	 				emptyPairs++;
	 			}
	 		}

	 		

			//If solvable then try to solve it	 		
		 	if(pair != -1 && emptyPairs <= 1)
		 	{
		 		boolean changed = false;
		 		
		 		//I didn't want to write a bunch of if's and loops so 
		 		//I just wrote two.
		 		//While the values are changing it'll keep doing the following
		 		//Checking to see if it can solve for a value
		 		//Solve for it
		 		//Solve for a missing angle
		 		do
		 		{
		 			changed = false;
		 			for(int i = 0; i < 3; i++)
		 			{
		 				//Skip over pair because we already know it's got both valueas
		 				if(i != pair)
		 				{
		 					
		 					//If the angle is missing but we have the side
		 					if(empty[i] && !empty[i + 3])
		 					{
		 						empty[i] = false;
		 						int side = i + 3;
		 						int angle = i;
		 						
		 						//Applying the law given in the problem spec
		 						//You have to multiply the angle by PI/180.0 to get it from degrees to radians
		 						//Multiply by 180.0 / PI to change it back to degrees
		 						double sinValue = measurements[side] * Math.sin((measurements[pair] * PI)/180.0);
		 						sinValue /= measurements[pair + 3];
		 						measurements[angle] = (Math.asin(sinValue) * 180.0) / PI;
		 						changed = true;
		 					}
		 					//If the side is missing but we have the angle
		 					else if(!empty[i] && empty[i + 3])
		 					{
		 						empty[i + 3] = false;
		 						
		 						int side = i + 3;
		 						int angle = i;
		 						
		 						//Applying the law given in the problem spec
		 						//You have to multiply the angle by PI/180.0 to get it from degrees to radians
		 						double sideValue = Math.sin(measurements[angle] * PI/180.0) * measurements[pair+3];
		 						sideValue /= Math.sin(measurements[pair] * PI/180.0);
		 						measurements[side] = sideValue;
		 						changed = true;
		 					}
		 				}
		 			}
		 			
		 			//Check the angles and fill in the blanks if possible
		 			//This uses the A + B + C = 180 rule
		 			for(int i = 0; i < 3; i++)
		 			{
		 				int A = i;
		 				int B = (i + 1) % 3;
		 				int C = (i + 2) % 3;
		 				if(!empty[A] && !empty[B] && empty[C])
		 				{
		 					measurements[C] = 180.0 - measurements[A] - measurements[B];
		 					empty[C] = false;
		 					changed = true;		 					
		 				}
		 				else if(!empty[A] && empty[B] && !empty[C])
		 				{
		 					empty[B] = false;
		 					measurements[B] = 180.0 - measurements[A] - measurements[C];
		 					changed = true;
		 				}
		 			}
		 		}while(changed);


				//Make sure it fits to all the rules
				//First Signs
				boolean lawOfSigns = true;
				
				//If the values for law of signs are within the tolerance of each other then the 
				//data abides by the lawOfSigns else it marks that it doesn't
				for(int i = 0; i < 3; i++)
				{
					double lawValue = measurements[i + 3] / Math.sin(measurements[i] * PI/180.0);

					for(int j = 0; j < 3; j++)
					{
						if(i == j) continue;

						double temp = measurements[j + 3] / Math.sin(measurements[j ] * PI/180.0);
						
						if(!equal(temp,lawValue))
						{
							lawOfSigns = false;
						}
					}
				}
				
				//Do the angles add up to 180 degrees?
				boolean angles = equal(measurements[0] + measurements[1] + measurements[2],180.0);

                                angles = angles && measurements[0] < 90.0 && measurements[1] < 90.0 && measurements[2] < 90.0;
				
				//Print the header
				System.out.print("Sign #" + Counter + ": ");
				
				//If the triangle passed print the data
				if(lawOfSigns && angles)
				{
			 		for (int i = 0; i < 6; i++)
			 		{
			 			System.out.print(format(measurements[i]) + " ");
			 		}
		 		}
		 		//Else print rejected
		 		else
		 		{
		 			System.out.print("Rejected!");
				
		 		}
		 		System.out.println("");
		 	}
		 	else
		 	{
		 		System.out.println("Sign #" + Counter + ": Lost!");
		 	}
		 	
		 	Counter++;

		 }while(true);
 	}
 	
 	private String format(double d)
 	{
 		//Since we are only rounding to two decimal places we can add 0.005 to do automatic rounding
 		d += 0.005;
 		
 		//make it a string
 		String value = d + "";
 		
 		//remove the excess garbage that we aren't using
 		//should return a number formatted to two decimal places
 		return value.substring(0, value.indexOf(".") + 3);
 	}
 	
 	private boolean equal(double a, double b)
 	{
 		//if the absolute difference between the two values is less than
 		//the tolerance then the values are the same
 		return Math.abs(a-b) <= 0.01	;
 	}
}
