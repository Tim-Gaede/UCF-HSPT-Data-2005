import java.io.*;
import java.util.*;


public class duckboot
{

    public void solve(int gameNum, ArrayList friends, int numRounds, 
                      int numDucks)
    {
        int round;
        int currentFriend;
        int i;
        Object[] friendsArray;

        // Keep track of the number of rounds remaining
        round = numRounds;

        // Start with Jimmy's best friend
        currentFriend = 0;

        // Play the next round
        while ((friends.size() > 0) && (round > 0))
        {
            // Decrement the number of rounds remaining
            round--;

            // Advance to the next "boot".  The modulo (%) operation here
            // will help us automatically start back at the front of the list
            // of friends if we run off of the end.  This essentially makes
            // the list circular.
            currentFriend = (currentFriend + numDucks) % friends.size();

            // Boot this friend
            friends.remove(currentFriend);
        }

        // Once we're done, print out the results
        System.out.println("Game " + gameNum + ":");

        // See if Jimmy is all out of friends
        if (friends.isEmpty())
        {
            // If there aren't any friends left, print the special message
            System.out.println("Jimmy has friends no more.");
        }
        else
        {
            // Convert the list of friends to an array, and sort it.
            friendsArray = friends.toArray();
            Arrays.sort(friendsArray);
            
            // Print the list of friends remaining
            for (i = 0; i < friends.size(); i++)
                System.out.println(friendsArray[i]);
        }
        System.out.println();
    }
    
    public static void main(String args[]) throws IOException
    {
        duckboot solution;
        BufferedReader reader;
        String line;
        ArrayList friends;
        StringTokenizer tokens;
        int numGames, numFriends, numRounds, numDucks;
        int i, j;

        // Create the solution object (the object that will actually do
        // the solving)
        solution = new duckboot();

        // Create the list of friends
        friends = new ArrayList();

        // Open the input file in a BufferedReader
        reader = new BufferedReader(new FileReader("duckboot.in"));

        // Get the number of games to play
        line = reader.readLine().trim();
        numGames = Integer.parseInt(line);

        // Get the data for each game, and play it out
        for (i = 0; i < numGames; i++)
        {
            // Clear out the list of friends
            friends.clear();

            // Get the number of friends in this game
            line = reader.readLine().trim();
            numFriends = Integer.parseInt(line);

            // Get each of the friend's names
            for (j = 0; j < numFriends; j++)
            {
                // Read the next friend's name
                line = reader.readLine().trim();
                
                // Add it to the list of friends
                friends.add(line);
            }

            // Finally, read the number of rounds and times Jimmy yells
            // "duck" between boots.  We'll need a StringTokenizer for this.
            line = reader.readLine().trim();
            tokens = new StringTokenizer(line);
            numRounds = Integer.parseInt(tokens.nextToken().trim());
            numDucks = Integer.parseInt(tokens.nextToken().trim());
            
         
            // Now that we have the data for this game, go ahead and
            // play it out
            solution.solve(i+1, friends, numRounds, numDucks);
        }
    }
}
