
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;


public class box
{
   private static final boolean debug = false;

   public static void main( String[] args )
   {
      try
      {
         BufferedReader inputFile = new BufferedReader( new FileReader( "box.in" ) );

         boolean done = false;
         while( !done )
         {
            StringTokenizer line = new StringTokenizer( inputFile.readLine() );

            // height of the galaxy (y-axis) is the number of rows
            int height = Integer.parseInt( line.nextToken() );

            // width of the galaxy (x-axis) is the number of columns
            int width = Integer.parseInt( line.nextToken() );

            if( height != 0 && width != 0 )
            {
               Galaxy galaxy = new Galaxy( width, height );
               galaxy.loadPositions( inputFile );
               galaxy.debugPrintPositions();
               galaxy.findAdjacentPositions();
               galaxy.debugPrintMoveConnections();
               galaxy.findTeleporterConnections();
               galaxy.debugPrintTeleportConnections();
               galaxy.debugPrintMoveConnections();
               int numSteps = galaxy.doBreadthFirstSearch();

               // output result
               System.out.println( "He got the Box in " + numSteps + " steps!" );
            }
            else
            {
               // be sure to stop processing input!
               done = true;
            }
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

   /**
    * @class Galaxy
    * The Galaxy class represents Ben's map of a galaxy. Each Galaxy contains
    * a list of Positions where Ben can be--pretty much anywhere except a wall.
    * It also has a list of pairs of Positions that Ben can move between.
    */
   public static class Galaxy
   {
      // 20 by 20 means up to 400 positions per galaxy, so 400 is big enough
      // for a maximum value
      public static final int MAX_POSITIONS = 400;

      private int m_width = 0;
      private int m_height = 0;

      private Position[] m_positions = new Position[MAX_POSITIONS];
      private int m_numPositions = 0;

      private boolean[][] m_canMoveBetween =
            new boolean[MAX_POSITIONS][MAX_POSITIONS];
      private boolean[][] m_canTeleportBetween =
            new boolean[MAX_POSITIONS][MAX_POSITIONS];

      private int m_start = -1;
      private int m_goal = -1;

      public Galaxy( int width, int height )
      {
         // remember the width and height
         m_width = width;
         m_height = height;

         // initialize all move possibilities to false (no movement)
         for( int i = 0; i < MAX_POSITIONS; ++i )
         {
            for( int j = 0; j < MAX_POSITIONS; ++j )
            {
               m_canMoveBetween[i][j] = false;
               m_canTeleportBetween[i][j] = false;
            }
         }
      }

      public void loadPositions( BufferedReader r ) throws IOException
      {
         // for each line in the galaxy
         for( int y = 0; y < m_height; ++y )
         {
            // read the line
            String line = r.readLine();

            // for each position on the line
            for( int x = 0; x < m_width; ++x )
            {
               // get one character from the line
               char c = line.charAt( x );

               // skip walls; remember only the positions where Ben can go
               if( c == 'W' ) continue;

               // if starting position, remember its index
               if( c == 'B' ) m_start = m_numPositions;

               // if goal position, remember its index
               if( c == 'X' ) m_goal = m_numPositions;

               // store the new position
               m_positions[m_numPositions] = new Position();

               // remember the position's x,y coordinates
               m_positions[m_numPositions].setCoordinates( x, y );

               // mark position if it's a teleporter
               if( Character.isDigit( c ) )
               {
                  m_positions[m_numPositions].markTeleporter( c );
                  m_positions[m_numPositions].debugPrint( "Teleporter '" + c
                        + "' found, index=" + m_numPositions );
               }

               // increment the total number of positions
               ++m_numPositions;
            }
         }
      }

      public void findAdjacentPositions()
      {
         // for each position in the galaxy
         for( int i = 0; i < m_numPositions; ++i )
         {
            // look at all the positions after it
            for( int j = i + 1; j < m_numPositions; ++j )
            {
               // if positions are adjacent, remember it
               if( m_positions[i].isAdjacentTo( m_positions[j] ) )
               {
                  // Ben can move from i to j or vice versa
                  m_canMoveBetween[i][j] = true;
                  m_canMoveBetween[j][i] = true;
               }
            }
         }
      }

      public void findTeleporterConnections()
      {
         // for each position in the galaxy
         for( int i = 0; i < m_numPositions; ++i )
         {
            // if it's a teleporter
            if( m_positions[i].isTeleporter() )
            {
               // look at all the positions after it
               for( int j = i + 1; j < m_numPositions; ++j )
               {
                  // if it's also a teleporter, and has the same number
                  if( m_positions[j].isTeleporter()
                      && (m_positions[i].getTeleporterNumber()
                          == m_positions[j].getTeleporterNumber()) )
                  {
                     // we have both ends of the teleporter!
                     m_positions[i].debugPrint( "Teleporter pair '" +
                           m_positions[i].getTeleporterNumber() +
                           "', index=" + i );
                     m_positions[j].debugPrint( "Teleporter pair '" +
                           m_positions[j].getTeleporterNumber() +
                           "', index=" + j );

                     // let's look at the positions adjacent to each:
                     int[] iNeighbors = whereBenCanMoveFrom( i );
                     int[] jNeighbors = whereBenCanMoveFrom( j );

                     int k;

                     // since the teleporter is instantaneous, Ben can move
                     // from i directly to any of the positions adjacent to
                     // j, and vice versa
                     if( jNeighbors != null )
                     {
                        for( k = 0; k < jNeighbors.length; ++k )
                        {
                           int n = jNeighbors[k];
                           m_canTeleportBetween[n][i] = true;
                           m_canTeleportBetween[i][n] = true;
                        }
                     }

                     // similarly, Ben can move from j directly to any of the
                     // positions adjacent to i, and vice versa
                     if( iNeighbors != null )
                     {
                        for( k = 0; k < iNeighbors.length; ++k )
                        {
                           int n = iNeighbors[k];
                           m_canTeleportBetween[n][j] = true;
                           m_canTeleportBetween[j][n] = true;
                        }
                     }
                  }
               }
            }
         }
         for( int i = 0; i < MAX_POSITIONS; ++i )
         {
            for( int j = 0; j < MAX_POSITIONS; ++j )
            {
               if( m_canTeleportBetween[i][j] )
               {
                  m_canMoveBetween[i][j] = true;
               }
            }
         }
      }

      private int[] whereBenCanMoveFrom( int positionNumber )
      {
         int count;
         int i;

         // get a quick count of how many
         count = 0;
         for( i = 0; i < MAX_POSITIONS; ++i )
         {
            if( m_canMoveBetween[positionNumber][i] ) ++count;
         }

         // allocate a perfectly-sized array for the results
         int[] list = new int[count]; ////NOTE: what if count is zero??

         // now store the position numbers
         count = 0;
         for( i = 0; i < MAX_POSITIONS; ++i )
         {
            if( m_canMoveBetween[positionNumber][i] )
            {
               list[count] = i;
               ++count;
            }
         }

         return list;
      }

      public void debugPrintPositions()
      {
         if( !debug ) return;
         for( int i = 0; i < m_numPositions; ++i )
         {
            m_positions[i].debugPrint( "index " + i );
         }
      }

      public void debugPrintMoveConnections()
      {
         if( !debug ) return;

         System.out.print( "------------------------------" );
         System.out.println( "------------------------------" );
         System.out.print( "       " );
         for( int k = 0; k < m_numPositions; ++k )
         {
            System.out.print( " " + (k % 10) );
         }
         System.out.println( "" );

         for( int i = 0; i < m_numPositions; ++i )
         {
            System.out.print( ">>> " + ((i < 10)? " " : "") + i + ": " );
            for( int j = 0; j < m_numPositions; ++j )
            {
               if( m_canMoveBetween[i][j] )
               {
                  System.out.print( "O " );
               }
               else
               {
                  System.out.print( "- " );
               }
            }
            System.out.println( "" );
         }
         System.out.print( "------------------------------" );
         System.out.println( "------------------------------" );
      }

      public void debugPrintTeleportConnections()
      {
         if( !debug ) return;

         System.out.print( "------------------------------" );
         System.out.println( "------------------------------" );
         System.out.print( "       " );
         for( int k = 0; k < m_numPositions; ++k )
         {
            System.out.print( " " + (k % 10) );
         }
         System.out.println( "" );

         for( int i = 0; i < m_numPositions; ++i )
         {
            System.out.print( ">>> " + ((i < 10)? " " : "") + i + ": " );
            for( int j = 0; j < m_numPositions; ++j )
            {
               if( m_canTeleportBetween[i][j] )
               {
                  System.out.print( "O " );
               }
               else
               {
                  System.out.print( "- " );
               }
            }
            System.out.println( "" );
         }
         System.out.print( "------------------------------" );
         System.out.println( "------------------------------" );
      }

      public int doBreadthFirstSearch()
      {
         // breadth-first search algorithm finds shortest path length
         Queue q = new Queue();
         m_positions[m_start].setNumberOfSteps( 0 );
         m_positions[m_start].markQueued();
         q.enqueue( m_start );
         int currentPosition = -1;

         while( !q.isEmpty() && currentPosition != m_goal )
         {
            // get the next Position to process from the queue
            currentPosition = q.dequeue();
            int numSteps = m_positions[currentPosition].getNumberOfSteps();

            // look at all Positions where Ben can move from currentPosition
            int[] nextPositions = whereBenCanMoveFrom( currentPosition );
            if( nextPositions != null )
            {
               for( int k = 0; k < nextPositions.length; ++k )
               {
                  int iNext = nextPositions[k];

                  // don't queue up the iNext Position if we already did
                  if( !m_positions[iNext].wasQueued() )
                  {
                     // the number of steps to reach Position iNext is one more
                     // than the number of steps to reach currentPosition
                     m_positions[iNext].setNumberOfSteps( numSteps + 1 );

                     // when Ben gets to Position iNext, the previous Position
                     // will be currentPosition; remember it
                     m_positions[iNext].setPreviousPosition( currentPosition );

                     // Queue up Position iNext for processing
                     m_positions[iNext].markQueued();
                     q.enqueue( iNext );
                  }
               }
            }
         }

         if( debug )
         {
            Position p = null;
            for( int index = m_goal; index > 0; index = p.getPreviousPosition() )
            {
               p = m_positions[index];
               p.debugPrint( "backwards route, step=" + p.getNumberOfSteps() );
            }
         }

         return m_positions[m_goal].getNumberOfSteps();
      }
   }

   /**
    * @class Position
    * The Position class represents a single position where Ben can go within
    * a galaxy. It has x and y coordinates, and it may or may not contain one
    * part of a teleporter pair. It also will track some more info for Ben,
    * such as the number of steps it takes to get to the Position from the
    * start, the index of the previous Position, and whether the Position has
    * been queued up as a part of the search algorithm.
    */
   public static class Position
   {
      private int m_x = -1;
      private int m_y = -1;
      private char m_teleporterNumber = '_';
      private boolean m_queued = false;
      private int m_numSteps = -1;
      private int m_previous = -1;

      public void setCoordinates( int x, int y )
      {
         m_x = x;
         m_y = y;
      }

      public boolean isTeleporter()
      {
         if( Character.isDigit( m_teleporterNumber ) ) return true;
         return false;
      }

      public char getTeleporterNumber()
      {
         return m_teleporterNumber;
      }

      public void markTeleporter( char number )
      {
         m_teleporterNumber = number;
      }

      public boolean isAdjacentTo( Position other )
      {
         if( m_y == other.m_y )
         {
            if( m_x == other.m_x + 1 ) return true;
            if( m_x == other.m_x - 1 ) return true;
            return false;
         }
         if( m_x == other.m_x )
         {
            if( m_y == other.m_y + 1 ) return true;
            if( m_y == other.m_y - 1 ) return true;
            return false;
         }
         return false;
      }

      public boolean wasQueued()
      {
         return m_queued;
      }

      public void markQueued()
      {
         m_queued = true;
      }

      public int getNumberOfSteps()
      {
         return m_numSteps;
      }

      public void setNumberOfSteps( int n )
      {
         m_numSteps = n;
      }

      public int getPreviousPosition()
      {
         return m_previous;
      }

      public void setPreviousPosition( int index )
      {
         m_previous = index;
      }

      public void debugPrint( String s )
      {
         if(debug) System.out.println( "Position " + m_x + ", " + m_y + ": " + s );
      }
   }

   /**
    * @class Queue
    * The Queue class implements the standard data type. Basically, a queue
    * is a list, where the order you put things on the list is always the same
    * as the order you take them off the list. (Also called FIFO, for "first
    * in, first out")
    */
   public static class Queue
   {
      private int[] m_list = new int[Galaxy.MAX_POSITIONS];
      private int m_head = 0;
      private int m_tail = 0;

      public void enqueue( int value )
      {
         m_list[m_tail] = value;
         ++m_tail;
         if( m_tail == m_list.length ) m_tail = 0;
      }

      public int dequeue()
      {
         int value = m_list[m_head];
         ++m_head;
         if( m_head == m_list.length ) m_head = 0;
         return value;
      }

      public boolean isEmpty()
      {
         if( m_head == m_tail ) return true;
         return false;
      }
   }
}

