// Arup Guha
// Solution to 2005 UCF High School Contest Problem "Cents"
// 5/4/05

#include <iostream>
#include <fstream>

using namespace std;

void main() {

  // Open the input file.
  ifstream fin("cents.in");
  int numcases, dollars;

  // Read in the number of test cases.
  fin >> numcases;

  // Process each test case.
  for (int i=0; i<numcases; i++) {
    fin >> dollars;
    cout << 6*dollars << endl; // The tax in cents is 6 times the dollar
                               // amount of the purchase.
  }

  fin.close(); // close the input file.
}
