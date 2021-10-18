#include <iostream>
#include <fstream>
#include <cmath>

using namespace std;

double sides[3], angles[3];
int numSidesKnown, numAnglesKnown;
double ratio;

// This function uses the "Law of Signs" to find any missing angles
// where we know the opposite side, or vice versa
bool checkLawOfSigns()
{
    if (ratio < 0) return true; // no matching side/angle pair
    for (int i = 0; i < 3; i++) {
	if (angles[i] < 0 && sides[i] >= 0) {
	    double sinAngle = sides[i] / ratio;
	    if (sinAngle < 0 || sinAngle > 1)
		return false;
	    angles[i] = asin(sinAngle);
	    numAnglesKnown++;
	} else if (angles[i] >= 0 && sides[i] < 0) {
	    sides[i] = ratio*sin(angles[i]);
	    numSidesKnown++;
	} else if (angles[i] >= 0 && sides[i] >= 0) {
	    if (fabs(ratio - sides[i]/sin(angles[i])) > 0.01)
		return false;
	}
    }
    return true;
}

const double PI = 3.14159;

int main(int argc, char *argv[])
{
    ifstream in("signs.in");
    cout.setf(ios::fixed);
    cout.precision(2);
    int signNumber = 0;
    while (in >> angles[0] >> angles[1] >> angles[2] >> sides[0] >> sides[1] >> sides[2])
    {
	signNumber++;
	numSidesKnown = numAnglesKnown = 3; // assume we know everything until we see negatives
	ratio = -1;
	for (int i = 0; i < 3; i++) {
	    if (angles[i] < 0)
		numAnglesKnown--;
	    else
		angles[i] *= PI/180.0; // convert to radians
	    if (sides[i] < 0)
		numSidesKnown--;
	    if (angles[i] >= 0 && sides[i] >= 0)
		ratio = sides[i] / sin(angles[i]); // matching side and angle, set LoS ratio
	}
	if (numAnglesKnown + numSidesKnown == 0)
	    return 0; // all negative, exit program
	cout << "Sign #" << signNumber << ":";
	if (!checkLawOfSigns()) {
	    cout << " Rejected!" << endl;
	    continue;
	}
	if (numAnglesKnown == 2) {
	    double angleSum = 0; int missingAngle = -1;
	    for (int i = 0; i < 3; i++) {
		if (angles[i] > 0) angleSum += angles[i];
		else missingAngle = i;
	    }
	    angles[missingAngle] = PI - angleSum;
	    numAnglesKnown++;
	    if (angles[missingAngle] >= PI/2 || angles[missingAngle] <= 0) {
		cout << " Rejected!" << endl;
		continue;
	    }
	    if (ratio < 0 && sides[missingAngle] >= 0) {
		ratio = sides[missingAngle]/sin(angles[missingAngle]);
	    }
	}
	if (!checkLawOfSigns()) {
	    cout << " Rejected!" << endl;
	    continue;
	}
	if (numAnglesKnown < 3 || numSidesKnown < 3) {
	    cout << " Lost!" << endl;
	    continue;
	}
	double angleSum = 0;
	for (int i = 0; i< 3; i++) angleSum += angles[i];
	if (fabs(180 - angleSum*180.0/PI) > 0.01) {
	    cout << " Rejected!" << endl;
	    continue;
	}
	for (int i = 0; i < 3; i++) cout << " " << angles[i]*180/PI;
	for (int i = 0; i < 3; i++) cout << " " << sides[i];
	cout << endl;
    }
    return 0;
}

