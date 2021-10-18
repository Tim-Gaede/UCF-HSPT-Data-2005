/*
2005 UCF High School Programming Contest
Problem: Vote
Solution: Nick Beato

This is a version of vote that uses the STL.  Hopefully you can see
the power that the STL offers with this solution.  I suggest looking
for some tutorials or references to learn other uses of the STL.
*/

#include <iostream>
#include <fstream>
#include <map>
#include <string>

using namespace std;

// keep track of the input data
struct amendment {
	amendment() {
		numfor = numagainst = 0;
	}
	int numfor, numagainst;
};

int main() {
	// open the file
	ifstream in("vote.in");

	// read in how many districts are in the file
	int districts;
	in >> districts;
	for(int i = 1; i <= districts; i++) {
		// votes keeps track of how many votes are in this district
		int votes;

		// the data keeps track of the votes and their for/against counts
		// an stl map lets you index with any type and only stores the things
		// that are explictly indexed 
		map <int, amendment> data;

		// read in the votes
		in >> votes;
		for(int j = 0; j < votes; j++) {
			// garbage stores "amendment", which is not needed
			// state reads in "for" or "against"
			// number indicates the amendment number
			string garbage, state;
			int number;
			in >> garbage >> number >> state;

			// increment the correct counter
			// initalized to 0 automatically in the amendment constructor
			if(state == "for")
				data[number].numfor++;
			else
				data[number].numagainst++;

		}

		// print the data
		// iterating a map iterates in sorted order for us
		// so we just need to print in order
		// it->first tells us the integer in the amendment number (the first template parameter)
		// it->second tells us the corresponding amendment struct

		cout << "District " << i << ":" << endl;
		for(map <int, amendment>::iterator it = data.begin(); it != data.end(); it++) {
			cout << "Amendment " << it->first << " : ";
			cout << it->second.numfor << " for, " << it->second.numagainst << " against : ";
			if(it->second.numagainst < it->second.numfor)
				cout << "YEA";
			else if(it->second.numagainst > it->second.numfor)
				cout << "NAY";
			else
				cout << "UND";
			cout << "." << endl;
		}
		cout << endl;
	}

	in.close();
	return 0;
}
