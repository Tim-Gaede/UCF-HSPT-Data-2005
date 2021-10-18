/*
Solution to signs UCFHSPT2005

@author yiuyuho@yahoo.com
@date 2005-05-07
*/

#include <iostream>
#include <fstream>
#include <cmath>

using namespace std;

const double esp=0.01,PI=3.14159;

//equal function to compare floating point valus, 
//in order to avoid precision error
bool eq(double x,double y)
{
	return fabs(x-y)<esp;
}

//Does sin(A) where A is in degrees
double dSin(double A)
{
	return sin(A*PI/180);
}

//convert radian to degree
double deg(double R)
{
	return R*180/PI;
}

//Use angle sum formula to solve for an unknown angle, if the other 2 angles are known
void angleSum(double &A,double &B,double &C)
{
	if(A>0 && B>0 && C<0) C=180-A-B;
	else if(A>0 && C>0 && B<0) B=180-A-C;
	else if(B>0 && C>0 && A<0) A=180-B-C;
}

//use sin(sign) law to solve for one of the A,a,B,b
//given the other 3 are known
bool sinLaw(double &A,double &a,double &B,double &b)
{
	if(A>0 && a>0 && B>0 && b<0) b=a*dSin(B)/dSin(A);
	else if(B>0 && b>0 && A>0 && a<0) a=b*dSin(A)/dSin(B);
	else if(A>0 && a>0 && b>0 && B<0) B=deg(asin(b*dSin(A)/a));
	else if(B>0 && b>0 && a>0 && A<0) A=deg(asin(a*dSin(B)/b));
	else return 0; //no new information is found
	//some new information is found
	return 1;
}

bool invalidSin(double A,double a,double B,double b)
{
	double sin;
	if(A>0 && a>0 && b>0 && B<0)
	{
		sin=b*dSin(A)/a;
		if(sin<0 || sin>1) return 1;
	}
	if(B>0 && b>0 && a>0 && A<0)
	{
		sin=a*dSin(B)/b;
		if(sin<0 || sin>1) return 1;
	}
	return 0;
}

//checks to see if any measurement is missing
double miss(double A,double B,double C,double a,double b,double c)
{
	return A<0 || B<0 || C<0 || a<0 || b<0 || c<0;
}

//checks that all angles are acute and their sum is in fact 180.
bool checkAng(double A,double B,double C)
{
	return eq(A+B+C,180) && 0<A && A<90 && 0<B && B<90 && 0<C && C<90;
}

//checks that A,a,B,b obeys sin law
bool checkSin(double A,double a,double B,double b)
{
	return eq(a*dSin(B),b*dSin(A));
}

//check for ending condition (all negative)
bool end(double A,double B,double C,double a,double b,double c)
{
	return A<0 && B<0 && C<0 && a<0 && b<0 && c<0;
}

int main()
{
	ifstream cin("signs.in");
	double A,B,C,a,b,c;
	bool done;
	int cc;
	cc=1;
	//set output precision
	cout.precision(2);
	cout.setf(ios::fixed|ios::showpoint);
	//input
	cin>>A>>B>>C>>a>>b>>c;
	while(!end(A,B,C,a,b,c))
	{
		cout<<"Sign #"<<cc++<<": ";
		done=0;
		while(!done)
		{
			done=1;
			angleSum(A,B,C);
			//if we find any new information, then we might want to loop
			//back because the state of data has changed, thus set done=0;
			if(invalidSin(A,a,B,b) || invalidSin(A,a,C,c) || invalidSin(B,b,C,c))
			{
			 goto reject;
			}
			if(sinLaw(A,a,B,b)) done=0;
			if(sinLaw(A,a,C,c)) done=0;
			if(sinLaw(B,b,C,c)) done=0;
		}
		//if some measurement still missing, say so
		if(miss(A,a,B,b,C,c))
		{
			cout<<"Lost!"<<endl;
		}
		else if(checkAng(A,B,C) && checkSin(A,a,B,b) && checkSin(A,a,C,c))
		{
			//everything passed angle and sin law, thus output
			cout<<A<<' '<<B<<' '<<C<<' '<<a<<' '<<b<<' '<<c<<endl;
		}
		else
		{
			//some of the measurement did not pass the checking
reject:
			cout<<"Rejected!"<<endl;
		}
		cin>>A>>B>>C>>a>>b>>c;
	}
	return 0;
}