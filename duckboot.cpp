#include <stdio.h>
#include <string.h>

int main(void)
{
	char names[2048][32];
	int removed[2048];
	
	int game;
	int n;

	int m;
	int k;
	int l;

	int i;
	int j;
	int round;
	int pos;

	int lowest;
	int next;

	FILE *fin;

	// Open file for reading
	fin = fopen("duckboot.in","r");

	// Read the number of games to be played
	fscanf(fin,"%d",&n);

	// Process each game
	for(game = 1; game <= n; game++)
	{
		// Print the game header
		printf("Game %d:\n",game);

		// Read the number of friends
		fscanf(fin,"%d",&m);
		
		// Read the names of each friend
		for(i=0;i<m;i++)
		{
			fscanf(fin,"%s",names[i]);
			
			// Mark the friend as having not been eliminated
			removed[i] = 0;
		}		

		// Read the number of rounds and the number of "Ducks"
		fscanf(fin,"%d %d", &k,&l);

		// If the number of rounds is greater than the number of friends
		// Jimmy will boot all his friends
		if(k >= m)
		{
			printf("Jimmy has friends no more.\n\n");
			continue;
		}

		// Simulate the game round by round
		pos = 0;
		for(round=0;round<k;round++)
		{
			// Walk around the circle stopping at the lth remaining friend
			for(i=0; i<=l; pos = (pos+1)%m)
			{
				if(!removed[pos])
					i++;
				
				if(i == l+1)
					// Give the friend we stopped at the boot
					removed[pos]=1;
				
			}			
		}

		lowest = 0;
		// Loop for each remaining friend
		for(i=0;i<(m-k);i++)
		{
			// Find the lowest numbered remaining friend
			while(removed[lowest]) 
				lowest++;

			// Find the friend with the first name alphabetically
			next = lowest;
			for(j=lowest;j<m;j++)
			{
				if(!removed[j] && strcmp(names[j],names[next]) < 0)
					next = j;
			}

			// Print the name of the next friend and remove him/her from the circle
			removed[next] = 1;
			printf("%s\n",names[next]);
		}
		printf("\n");
		

	}
	return 0;


}