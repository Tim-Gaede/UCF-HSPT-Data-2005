/*********************************************************************
Problem: Necklace
Author: Walt Mundt
Java Solution: Nick Beato

This problem asks to find the number of circles tangant to the other
circles. The idea behind solving this is to look at each circle and
count how many circles are tangent to it.

Approach:
For a given circle, set a counter to 0. Loop over the remaining
circles and see if they are adjacent to the given circle.  Every
time one of them is, increment the counter.

So the question is: when are two circles tangent?  They must have at
least one point in common. There are two methods to find the
answer.  The first one infolves solving a system of equations for
the tangent point of 2 circles and hoping it is not an imaginary
number.  A more straightforward method is as follows:

If two circles are the same, they have infinitely many tangent points.
The problem specification does not allow this case!

If two circles are tangent such that the center of each circle lies
completely outside the other circle, then the distance between the
centers is equal to the sum of the radius of each circle!

In other words:
a.center.distance(b.center) = a.radius + b.radius

Draw some examples if you don't believe me!

Finally, if the circles are tangent and either circle has a center
inside the other, then at least one circle is completely enclosed by
the other.  The problem specification rules out this case as well.

This means it is suffice to check:
a.center.distance(b.center) = a.radius + b.radius

for every single possible pairing (a, b).  To hopefully remove some
floating point error, I am avoiding the square root by squaring each
side...

a.center.distanceSquared(b.center) = (a.radius + b.radius)^2

*********************************************************************/

import java.io.*;
import java.util.*;

public class necklace {

	/*****************************************************************
	The circle class stores the center, radius, and number of
	tangeant circles to a specific circle. it also has a method
	to compute the distance between two circles.
	*****************************************************************/
	public class circle {
		public circle(String line) {
			StringTokenizer st = new StringTokenizer(line);
			x = Double.parseDouble(st.nextToken());
			y = Double.parseDouble(st.nextToken());
			r = Double.parseDouble(st.nextToken()) / 2.0;

			counter = 0;
		}

		public double distanceSquared(circle other) {
			double dx = x - other.x;
			double dy = y - other.y;
			return dx * dx + dy * dy;
		}

		public double x, y, r;
		public int counter;
	}

	/*****************************************************************
	The main method creates a new necklace object for each in the
	file and let's the object solve the problem and print the result.
	*****************************************************************/

	public static void main(String []args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("necklace.in")));
		int number = Integer.parseInt(in.readLine().trim());

		for(int i = 0; i < number; i++) {
			necklace n = new necklace(in);
			n.solve();
			n.print(i+1);
		}

		in.close();
	}

	/*****************************************************************
	the necklace constructor does a lot of work.  it reads in the
	necklace along with all of the circles for that necklace. notice
	that the circle object initializes its counter to 0, so we
	are ready to go! 
	*****************************************************************/

	public necklace(BufferedReader in) throws IOException {
		int n = Integer.parseInt(in.readLine().trim());
		rings = new circle[n];
		for(int i = 0; i < n; i++) {
			rings[i] = new circle(in.readLine());
		}
	}

	/*****************************************************************
	solve takes a necklace and compares a circle (i) to every other
	circle (j). if they are tangent it increments i's counter.
	because the second loop starts at i+1, we need to increment j's
	counter as well because it is tangent to something it will not
	explictly check.
	*****************************************************************/

	public void solve() {
		for(int i = 0; i < rings.length; i++)
			for(int j = i + 1; j < rings.length; j++)
				if(equal(rings[i].distanceSquared(rings[j]), (rings[i].r + rings[j].r) * (rings[i].r + rings[j].r))) {
					rings[i].counter++;
					rings[j].counter++;
				}
				
	}

	/*****************************************************************
	finally, we print the result. the printing in this problem is a
	bit annoying. first, we need to see if there is any problems. a
	problem occurs if the counter is not 2 for any link. otherwise
	we can print a nice answer. if we find a problem, we just print
	the problem in order
	*****************************************************************/

	public void print(int x) {
		boolean foundOne = false;
		for(int i = 0; i < rings.length; i++)
			if(rings[i].counter != 2)
				foundOne = true;

		if(!foundOne) {
			System.out.println("Necklace " + x + " has no loose or tangled links.");
		}
		else {
			System.out.println("Necklace " + x + " has loose or tangled links:");
			for(int i = 0; i < rings.length; i++) {
				if(rings[i].counter < 2)
					System.out.println("  Link " + (i+1) + " is loose!");
				else if(rings[i].counter > 2)
					System.out.println("  Link " + (i+1) + " is tangled!");
			}
		}

		System.out.println();
	}

	
	/*****************************************************************
	finally, because we are comparing doubles for equality, we need
	to make sure we have a tolerance. this is because real numbers
	are infinite precision and a computer has to finitely represent
	the real number. so we can have rounding errors when we multiply.
	*****************************************************************/

	private static boolean equal(double a, double b) {
		return Math.abs(a - b) < 1e-6;
	}

	private circle [] rings;
}

/*********************************************************************
That's it, thanks for reading!
*********************************************************************/
