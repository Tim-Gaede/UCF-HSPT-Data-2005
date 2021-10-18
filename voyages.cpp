/*
Solution to voyages, UCFHSPT2005

@author yiuyuho@yahoo.com
@date 2005-05-02
*/

#include <fstream>
#include <iostream>

using namespace std;

const double esp=0.1;

int main()
{
	ifstream cin("voyages.in");
	int N,warp;
	double now,then,dis;
	char *msg[7]={"","Plenty of time to explore strange new worlds, too.",
					 "Engage.",
					 "Make it so.",
					 "We had better get boldly going.",
					 "Maximum warp!",
					 "If I give it any more, she's gonna blow!"};
	cout.precision(1);
	cout.setf(ios::fixed|ios::showpoint);
	cin>>N;
	while(N>0)
	{
		cin>>now>>then>>dis;
		//go through each wrap factor and see we can arrive in time.
		for(warp=1;warp<=6;warp++)
		{
			//time = distance/speed, where speed is warp^3, and multiply by 1000 because
			//the time dis/warp^3 is in how many 1000 stardates.
			if(now+dis/(warp*warp*warp)*1000<then+esp) break;
		}
		//output
		cout<<"Captain's log: Stardate "<<now<<endl;
		if(warp<=6)
		{
			cout<<"Warp factor "<<warp<<"- "<<msg[warp]<<endl;
		}
		else
		{
			cout<<"I canna change the laws of physics!"<<endl;
		}
		cout<<endl;
		N--;
	}
	return 0;
}