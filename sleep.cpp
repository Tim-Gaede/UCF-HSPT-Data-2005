/*
Solution to Problem sleep, UCFHSPT2005

@author yiuyuho@yahoo.com
@date 2005-05-02
*/

#include <fstream>
#include <iostream>

using namespace std;

/*
This function takes a time in HH:MM format and converts it to the number
of minutes from 00:00, so 00:01 will return 1, 12:00 will return 12*60=720
*/
int parse(char *s)
{
	int hr,min;
	//get hours and minute from the characters
	hr=(s[0]-'0')*10+s[1]-'0';
	min=(s[3]-'0')*10+s[4]-'0';
	//every hour has 60 minutes
	return hr*60+min;
}

int main()
{
	//open file
	ifstream cin("sleep.in");
	int N,cc,need,get,i,a,b,day;
	char time[256];
	//number of minutes in a day
	day=26*60;
	//get input size
	cin>>N;
	for(cc=1;cc<=N;cc++)
	{
		//read hours needed
		cin>>need;
		//convert need to minutes, everything will be handled in minutes format.
		need*=60;
		//go through the 7 lines and see how many minutes of sleep is obtained.
		get=0;
		for(i=0;i<7;i++)
		{
			cin>>time;
			a=parse(time);
			b=parse(time+6);
			//if b<=a, then we crossed over 00:00, and b is in fact the next day with reference to a
			//thus we add a day into b
			//keep in mind that you will always sleep, and at most for 26 hours.
			if(b<=a) b+=day;
			//the amount of minutes between a and b is how much we slept
			get+=b-a;
		}
		//output
		cout<<"Astronaut #"<<cc<<" ";
		if(get>=need)
		{
			cout<<"will sleep enough!"<<endl;
		}
		else
		{
			need-=get;
			cout<<"needs "<<need/60<<" hours and "<<need%60<<" minutes more sleep!"<<endl;
		}
	}
	return 0;
}