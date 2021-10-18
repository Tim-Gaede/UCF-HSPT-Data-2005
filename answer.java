import java.io.*;
import java.util.*;

public class answer
{
   public answer(){}
   
   public static void main(String args[])
   {
      
      try
      {
		 // Create the BufferedReader to read in the file input.
         BufferedReader in = new BufferedReader(new FileReader("answer.in"));
         
		 // Get the number of data sets we need to process
         int numProducts = Integer.parseInt(in.readLine());
         
         for(int i = 0; i < numProducts; i++)
         {
		    // Get the two numbers we need by reading the line in and passing it
			// to the StringTokenizer.
            StringTokenizer st = new StringTokenizer(in.readLine());
            int firstNumber = Integer.parseInt(st.nextToken());
            int secondNumber = Integer.parseInt(st.nextToken());
            
			// See if the numbers are 6 or 9, and if so, we need to print
			// out 42.
            if((firstNumber == 6 && secondNumber == 9) ||
               (firstNumber == 9 && secondNumber == 6))
            {
               System.out.println(42);
            }
		    // Otherwise, just print out the product.
            else
            {
               System.out.println(firstNumber*secondNumber);
            }
         }
      }catch(Exception e){
         e.printStackTrace();
      }
   }
}
      
