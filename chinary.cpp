#include <stdio.h>

const double esp=1e-9;

int main(void)
{
	int n;
	int p;
	double r;
	int iteration;

	FILE *fin;

	// Open the input file for reading
	fin = fopen("chinary.in","r");

	while(1)
	{
		// Read the line of input
		fscanf(fin,"%d %d %lf ", &n, &p, &r);

		// Input terminates when p is 0
		if(p == 0)
			return 0;

		// Simulate the haggling process
		iteration = 0;
		while(n > p)
		{
			// Since the price is reduced by r each iteration, 
			// this is the same as multiplying by 1-r
			n = (int)(n*(1.0 - r)+esp);
			iteration++;
		}

		// Print the number of iterations required
		printf("%d\n",iteration);

	}

	return 0;
}