#include <stdlib.h>
#include <stdio.h>

#define MAX_S 256

void printInvalidDays(char daysWatered[MAX_S], char* validDays);

int main(){

	FILE* fin;
	char streetAddress[MAX_S];
	char daysWatered[MAX_S];
	int i, n, streetNumber;
	char* sundayThursday = "UR";
	char* wednesdaySaturday = "WS";
	
	/* open the water.in input file for reading
	 **/
	fin = fopen("water.in", "r");
	
	/* read in the number of input cases
	 * Notice the \n after the %d.  This is used so the newline
	 *   character is skipped over.
	 *
	 * Try removing the \n and see how it behaves differently.
	 **/
	fscanf(fin, "%d\n", &n);
	
	/* Loop through all of the input cases
	 * Notice how we never have to check for EOF
	 **/
	for(i = 0; i < n; i++){
		
		/* Reads in the street address line from the file
		 * Here, fgets is used because we have to read in the whole line
		 * We do not know ahead of time how many space seperated words
		 *   will be in this string
		 **/
		fgets(streetAddress, MAX_S, fin);
		
		/* Reads in the days watered line.  Agian, notice the \n
		 **/
		fscanf(fin, "%s\n", daysWatered);
		
		/* Grab the number off of the street address string.
		 * We don't care about the rest of the street address.
		 **/
		sscanf(streetAddress, "%d", &streetNumber);
		
		/* Determine whether or not the street number is odd or even,
		 *   and call the printInvalidDays function with the correct
		 *   parameters.
		 **/
		if(streetNumber % 2 == 0){
			printInvalidDays(daysWatered, sundayThursday);
		}else{
			printInvalidDays(daysWatered, wednesdaySaturday);
		}
		
	}
	
	return 0;
	
}

/* This function takes as its first parameter the days that the house was watered
 *   and the days that the house should be watered.
 * It will print out any characters in daysWatered that are not in validDays
 **/
void printInvalidDays(char daysWatered[MAX_S], char* validDays){

	char* dayWatered;
	char* validDay;
	int wateredOnOffDay, dayIsOK;
	
	/* We use this as a boolean value to indicate whether or not the 
	 *   house was watered on an invalid day.
	 **/
	wateredOnOffDay = 0;
	
	/* Loop through each one of the days the house was watered.
	 **/
	for(dayWatered = &daysWatered[0]; *dayWatered != '\0'; dayWatered++){

		/* This boolean valud indicates whether or not the current dayWatered
		 *   exists in the validDays string.
		 **/
		dayIsOK = 0;

		/* Loop through the validDays and if the dayWatered is equal to any of
		 *  the valid days, we set dayIsOK to true and break out of the loop.
		 **/
		for(validDay = &validDays[0]; *validDay != '\0'; validDay++){
			if(*dayWatered==*validDay){
				dayIsOK = 1;
				break;
			}
		}
		
		/* If the house was watered on an invalid day, print out the character,
		 *   and set wateredOnOffDay to true.
		 **/
		if(!dayIsOK){
			printf("%c", *dayWatered);
			wateredOnOffDay = 1;
		}
		
	}
	
	/* If the house was never watered on an off day, then we print "All safe!"
	 **/
	if(!wateredOnOffDay){
		printf("All safe!");
	}
	printf("\n");
	
}
