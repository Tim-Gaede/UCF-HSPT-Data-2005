
import java.io.*;
import java.util.*;

public class voyages
{
   // This flag, along with the following function, allow us to turn debug
   // outputs on and off. Set it to 'false' and recompile before submitting.
   private static final boolean debug = false;

   private static void debugPrint( String s )
   {
      if( debug ) System.out.println( s );
   }

   // This function calculates the time it takes to travel the given
   // distance at the provided warp factor.
   private static double travelTime( int warpFactor, double distance )
   {
      debugPrint( "warp factor: " + warpFactor );

      // Actual speed is the cube of the warp factor
      double speed = (double)warpFactor *
                     (double)warpFactor *
                     (double)warpFactor;
      debugPrint( "actual speed: " + speed );

      // Standard speed calculation
      double time = distance / speed * 1000.0;
      debugPrint( "travel time needed: " + time );

      return time;
   }

   public static void main( String[] args )
   {
      try
      {
         // We choose a BufferedReader so that we can read a line of text input
         // all at once.
         BufferedReader inputFile =
               new BufferedReader( new FileReader( "voyages.in" ) );

         // The first thing in the input is the number of scenarios.
         int totalScenarios = Integer.parseInt( inputFile.readLine() );
         debugPrint( "input parsed total # scenarios: " + totalScenarios );

         // Loop to read the scenarios, processing as we go.
         for( int numScenario = 0; numScenario < totalScenarios; ++numScenario )
         {
            // Use a StringTokenizer to parse the three numbers from each
            // input line. The problem statement specifies that there will be
            // three real numbers on each line.
            StringTokenizer scenario = new StringTokenizer( inputFile.readLine() );
            double currentStardate = Double.parseDouble( scenario.nextToken() );
            debugPrint( "input parsed current stardate: " + currentStardate );
            double weaponDeadline = Double.parseDouble( scenario.nextToken() );
            debugPrint( "input parsed weapon deadline: " + weaponDeadline );
            double distance = Double.parseDouble( scenario.nextToken() );
            debugPrint( "input parsed distance: " + distance  );

            // Figure out how much time we have left.
            double timeLeft = weaponDeadline - currentStardate;
            debugPrint( "time left is: " + timeLeft  );

            // Every scenario starts with the same output.
            System.out.println( "Captain's log: Stardate " + currentStardate );

            // Try the slower speeds first, then increase speed if we're too
            // slow. Since floating-point calculations sometimes have a small
            // error, be sure to account for travel times that are slightly
            // longer than the time left, according to the problem statement.
            if( (travelTime( 1, distance ) < timeLeft) ||
                (travelTime( 1, distance ) - timeLeft < 0.1) )
            {
               System.out.println( "Warp factor 1- " +
                     "Plenty of time to explore strange new worlds, too." );
            }
            else if( (travelTime( 2, distance ) < timeLeft) ||
                     (travelTime( 2, distance ) - timeLeft < 0.1) )
            {
               System.out.println( "Warp factor 2- Engage." );
            }
            else if( (travelTime( 3, distance ) < timeLeft) ||
                     (travelTime( 3, distance ) - timeLeft < 0.1) )
            {
               System.out.println( "Warp factor 3- Make it so." );
            }
            else if( (travelTime( 4, distance ) < timeLeft) ||
                     (travelTime( 4, distance ) - timeLeft < 0.1) )
            {
               System.out.println( "Warp factor 4- " +
                     "We had better get boldly going." );
            }
            else if( (travelTime( 5, distance ) < timeLeft) ||
                     (travelTime( 5, distance ) - timeLeft < 0.1) )
            {
               System.out.println( "Warp factor 5- Maximum warp!" );
            }
            else if( (travelTime( 6, distance ) < timeLeft) ||
                     (travelTime( 6, distance ) - timeLeft < 0.1) )
            {
               System.out.println( "Warp factor 6- " +
                     "If I give it any more, she's gonna blow!" );
            }
            else
            {
               // It would require a higher warp factor than 6 in order to travel
               // the distance in the time left. Evacuate everyone to Babylon 5!
               System.out.println( "I canna change the laws of physics!" );
            }

            // A blank line is required between scenario outputs.
            System.out.println( "" );
         }
      }
      catch( FileNotFoundException fnfEx )
      {
         System.out.println( "caught FileNotFoundException" );
      }
      catch( IOException ioEx )
      {
         System.out.println( "caught IOException" );
      }
   }
}

