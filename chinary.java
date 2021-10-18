import java.io.*;
import java.util.*;

public class chinary
{
	
	public chinary(){}
	
	public static void main(String args[])
	{
		try
		{
		
			BufferedReader in = new BufferedReader(new FileReader("chinary.in"));
			StringTokenizer st;
			String temp;
			
			while(true)
			{
				// Read in the line of input and give it to the string tokenizer
				temp = in.readLine();
				st = new StringTokenizer(temp);
				
				// Use the string tokenizer to extract the three values.
				int initialPrice = Integer.parseInt(st.nextToken());
				int myPrice = Integer.parseInt(st.nextToken());
				double loweringAmount = Double.parseDouble(st.nextToken());
				
				if(myPrice == 0)
				{
					break;
				}
				
				int currentPrice = initialPrice;
				int iterations = 0;
				
				// Use integer division to keep the currentPrice going down
				// until it's less than the price we want to pay for it.
				while(currentPrice > myPrice)
				{
					currentPrice -= (currentPrice*loweringAmount);
					iterations++;
				}
				
				System.out.println(iterations);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}