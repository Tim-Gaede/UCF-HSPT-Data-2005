// Arup Guha
// C++ Solution to 2005 UCF High School Programming Contest Problem Necklace

#include <iostream>
#include <fstream>
#include <cmath>

using namespace std;

// Stores information for one single necklace link.
struct link {
  double x;
  double y;
  double radius;
};

// Error factor to test tangency between links.
const double epsilon = 0.000001;

void fillarray(struct link necklace[], int degreelink[], int size);
bool tangent(struct link necklace[], int i, int j);
void output(int degreelink[], int numlinks, int numnecklace);

void main() {

  // Open the input file.
  ifstream fin("necklace.in");
  int numnecklaces, numlinks;

  // Read in the number of necklaces.
  fin >> numnecklaces;

  // Process each necklace in the file.
  for (int i=1; i<=numnecklaces; i++) {

    // Read in the number of links in this necklace.
    fin >> numlinks;

    // Now, we can allocate space for the array to store the necklace info.
    struct link *necklace = new struct link[numlinks];

    // Read in all the information.
    for (int j=1; j<=numlinks; j++) {
      double tempd;
      fin >> necklace[j-1].x >> necklace[j-1].y >> tempd;
      necklace[j-1].radius = tempd/2; // Notice how radius is set!!!
    }
  
    // This array will store how many links each link is tangent to.
    int *degreelink = new int[numlinks];

    // The number of tangent links is calculated in this function.
    fillarray(necklace, degreelink, numlinks);

    // Produce the output for this necklace.
    output(degreelink, numlinks, i);
    cout << endl;

    delete [] degreelink;
    delete [] necklace;
  }

  fin.close();
}

// This function takes in information about a necklace and fills in the
// array degreelink so it stores the number of links each link is adjacent
// to.
void fillarray(struct link necklace[], int degreelink[], int size) {

  // Intialize the number of tangent links to 0 for each link.
  for (int i=0; i<size; i++)
    degreelink[i] = 0;

  // Loop through all combinations of two different links.
  for (int i=0; i<size; i++) {
    for (int j=i+1; j<size; j++) {

      // Check to see if links i and j are tangent to one another.
      if (tangent(necklace, i,j)) {

        // If so, increment BOTH counters for links i and j!
        degreelink[i]++;
        degreelink[j]++;
      }
    }
  }

}


// Returns true if links i and j are tangent in necklace.
bool tangent(struct link necklace[], int i, int j) {

  // This is the distance between the centers of the two links.
  double distance = sqrt(pow(necklace[i].x-necklace[j].x,2) +
                         pow(necklace[i].y-necklace[j].y,2));

  // This is the sum of the two radii of the two links.
  double correctd = necklace[i].radius+necklace[j].radius;

  // We return true only if the two values above are very close to one 
  // another in value. epsilon is used as a lower and upper error bound.
  if ((distance-epsilon < correctd) &&
      (distance+epsilon > correctd))
    return true;

  return false;
}

// Creates the output for necklace number numnecklace.
void output(int degreelink[], int numlinks, int numnecklace) {

  int check;

  // Loop through the number of tangent links for each link.
  for (check = 0; check<numlinks; check++)
    if (degreelink[check] != 2)
      break;

  // If we got all the way through, we have a good necklace!
  if (check == numlinks)
    cout << "Necklace " << numnecklace << " has no loose or tangled links." << endl;

  // We found a "bad" link.
  else {

    cout << "Necklace " << numnecklace << " has loose or tangled links:" << endl;

    // Loop through each link, printing out the defect with it, if necessary.
    for (int i=0; i<numlinks; i++) {

      if (degreelink[i] < 2)
        cout << "  Link " << (i+1) << " is loose!" << endl;
      else if (degreelink[i] > 2)
        cout << "  Link " << (i+1) << " is tangled!" << endl;
    }

  }
}
