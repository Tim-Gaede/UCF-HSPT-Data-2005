import java.lang.*;
import java.io.*;
import java.util.StringTokenizer;

public class sleep
{
   public static void main(String args[]) throws Exception
   {
      FileInputStream   infile;
      BufferedReader    inreader;
      String            line;
      int               numPlans;
      int               i;
      int               minSleepInMinutes;
      int               totalSleepInMinutes;
      int               j;
      StringTokenizer   st;
      int               startHour;
      int               startMinute;
      int               endHour;
      int               endMinute;
      int               startTime;
      int               endTime;
      int               diffSleep;

      // Open the input file into a buffered reader
      infile = new FileInputStream("sleep.in");
      inreader = new BufferedReader(new InputStreamReader(infile));

      // Read the first line and get the number of sleep plans
      line = inreader.readLine();
      numPlans = Integer.parseInt(line.trim());

      // Loop through the sleep plans
      for (i=0; i < numPlans; i++)
      {
         // Read the next line and get the minimum number of hours sleep
         // per week
         line = inreader.readLine();
         minSleepInMinutes = Integer.parseInt(line.trim()) * 60;

         // Initialize the total sleep
         totalSleepInMinutes = 0;

         // Go through the 7 days of the week
         for (j=0; j < 7; j++)
         {
            // Read the next line that has the sleep that night
            line = inreader.readLine();

            // Set-up a tokenizer to process the line
            st = new StringTokenizer(line, ":-");

            // Get start hour and minute
            startHour = Integer.parseInt(st.nextToken().trim());
            startMinute = Integer.parseInt(st.nextToken().trim());

            // Get end hour and minute
            endHour = Integer.parseInt(st.nextToken().trim());
            endMinute = Integer.parseInt(st.nextToken().trim());

            // Compute start time in minutes
            startTime = startHour * 60 + startMinute;

            // Compute end time in minutes
            endTime = endHour * 60 + endMinute;

            // If the end time seems to be earlier than the start time, then 
            // it must be on the next day so add a day's worth of minutes to it
            if (endTime <= startTime)
               endTime = endTime + (26 * 60);

            // Add this sleep shift to the total
            totalSleepInMinutes = totalSleepInMinutes + (endTime - startTime);
         }

         // Print the astronaut number
         System.out.print("Astronaut #" + (i+1) + " ");

         // Check whether we got enough sleep and output accordingly
         if (totalSleepInMinutes < minSleepInMinutes)
         {
            // We need more sleep, uh oh!

            // Figure out how much more we need
            diffSleep = minSleepInMinutes - totalSleepInMinutes;

            // Output how many hours and minutes we need
            System.out.println("needs " + (diffSleep / 60) + " hours and " +
                               (diffSleep % 60) + " minutes more sleep!");
         }
         else
         {
            // We got enough sleep!
            System.out.println("will sleep enough!");
         }
      }
   }
}

