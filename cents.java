import java.io.*;

public class cents
{
	public static void main(String args) throws Exception
	{
		int i;
		String s;

		int NumberOfCases;
		int ValueInDollars;
				
		// Open file for reading
		BufferedReader fin = new BufferedReader(new FileReader("cents.in"));
		
		// Read the number of cases	
		s = fin.readLine();
		NumberOfCases = Integer.parseInt(s);

		// Loop through each case
		for(i = 0; i < NumberOfCases; i++)
		{
			// Read the number of dollars being charged
			s = fin.readLine();
			ValueInDollars = Integer.parseInt(s);

			// Output the sales tax in cents
			System.out.println(6*ValueInDollars);
		} 
	}
}
