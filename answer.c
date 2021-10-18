#include <stdio.h>

int main()
{
   FILE   *infile;
   char   line[80];
   int    numSets;
   int    i;
   int    num1;
   int    num2;

   /* Open the input file */
   infile = fopen("answer.in", "r");

   /* Get the first line and get the number of tests from it */
   fgets(line, sizeof(line), infile);
   sscanf(line, "%d", &numSets);

   /* Loop through the sets */
   for (i=0; i < numSets; i++)
   {
      /* Get the two numbers */
      fgets(line, sizeof(line), infile);
      sscanf(line, "%d %d", &num1, &num2);

      /* If they are 6 and 9, output the true Answer (otherwise, the product) */
      if ( ((num1 == 6) && (num2 == 9)) ||
           ((num1 == 9) && (num2 == 6)) )
         printf("42\n");
      else
         printf("%d\n", num1 * num2);
   }

   /* Exiting fine */
   return 0;
}

