/*********************************************************************
Problem: Box
Author: Adam Campbell
Cpp Solution: Nick Beato

This solution introduces some helper classes to store information
for easier reading.  I am emphasizing object-oriented programming.
I made this choice because this problem is more difficult than most
of the other problems in this problem set.  My original solution
was 2 functions and a lot of nested statements.  I decided it would
be much more clear to present the solution in an object-oriented
manner.  This solution is more lengthy, but does the exact same
thing as my original solution.  I made it longer to aid the readers
(you all) understand the idea of how this algorithm works.


Classes:
A Location is an x, y position.  It doesn't really have a lot of
functionality, but it is there to encapsulate the all of the
things like "startx, starty, endx, endy" into just "start, end".

A Cell is a square on the grid.  Each square knows its state, but not
its location.  All of the methods on a Cell are used to check whether
or not Ben can move from one Cell to another (questions such as:
Is the Cell a wall? Can Ben teleport? Where does he teleport to?) A
cell also monitors hte total number of steps to reach the cell so
far.

The Map is a collection of Cells.  The Map knows the information
regarding where the cells area and how they are connected.  The Map
is responsible for making Ben move around.

The algorithm:
I am solving this problem with a modifier implicit version of a very popular
search algorithm known as a "depth first search".  In a depth first search,
the idea is to start from a location and try different paths until we
find the goal.  Recursion is used to keep track of where we have been and
also ensures that we do not try the same path more than once.

So let's see how it works:
First we find out, where is Ben?  Once we find Ben, we simply make Ben
walk a LOT. We force him to walk everywhere he can.  Picture it this
way...

Let's look at the Map:
X..
...
..B

Ben can walk either left or up.  Let's consider that Ben goes left.  Now
Ben can go left, up, or right.  Going up puts him back in the same square, but
it costs him 2 steps.  This is bad because we want the lowest number of steps.
If we already got there in a shorter amount of steps, we can give up because
we have a better answer to that spot.  So then we have to try up and left.
Notice that after trying all of these recursively, we'll have to go back and
solve it assuming we stepped up originally.

So let's look at what we are storing...

X..
... move Ben left, he can get there in 1 step
.1B

X..
... move Ben left, he can get there in 2 steps
21B

X..
3.. move Ben up, he can get there in 3 steps
21B

4..
3.. move Ben up, he can get there in 4 steps
21B

So we found the box in 4 steps, however, there are a LOT more paths to try.
This solution exhausts EVERY path to ensure a shorter one does not exist.
So if Ben always moves left first, we eventually get a grid like this:

434
323
21B

Notice that Ben can move up and get there quicker, this is why we must
exhaust every path.  If we move up, we end up with a grid more like this one.

432
321
21B

In general, the idea is to fill in this matrix with the minimum number
of steps it takes to get to a cell on the map.  Now we have a few things
to consider:

1) redundandt paths
2) walls
3) teleporters

A redundant path can be seen like this...

....WW
.WW.WW
B....X

If we go up first, we get:
2345WW
1WW6WW
B98789

Now going right does this:
2345WW
1WW4WW
B12345

So in this case, we needed to traverse the entire path. However, consider
moving right first...

8765WW
9WW4WW
B12345

Now if we go up, we get: 
2345WW
1WW4WW
B12345

Notice that we can stop the path as soon as we get to the square marked
"5" at the top in the middle.  If we continue going down, we are producing
a worse answer than the known path.  So in general, Ben only uses a square
if he has not been there in an equal or fewer number of steps.

Walls can be addressed by setting the wall squares to -1 steps.  This will
make Ben think he was able to get there before he even started moving and
he doesn't even consider stepping on a wall.

Teleports are not too bad.  They allow Ben to move to another location
instantaneously.  Now a problem that can come up from this is that Ben
can teleport back to where he came from instantly!  So we can have a path
that teleports back and forth at no cost to Ben's walking!  Luckily we
solved this by making sure that Ben does not traverse a path unless it
takes strictly greater steps to get there (shew).


Now on to the solution!
*********************************************************************/

#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

/*********************************************************************
The Location class stores a grid location (x, y position) on the
Map.
*********************************************************************/

class Location {
	public:
		Location(int x = 0, int y = 0);

		bool operator !=(const Location &other) const;

		void set(int x, int y);
		int x() const;
		int y() const;
	
	private:
		int _x, _y;
};

/*********************************************************************
The Cell class is used to store information about a particular
location on the map.  The main information it stores is:
1) the fastest known path to this cell (the lowest number of steps
to get to the cell).
2) is the cell capable of teleporting (and which # is it)?
3) if it teleports, where is the corresponding teleporter?
*********************************************************************/

class Cell {
	public:
		// make a new cell that is walkable
		Cell();

		// return true if this cell can teleport
		bool teleports() const;

		// return true if this cell is a wall
		bool wall() const;

		// makes the cell into a wall
		void makeWall();

		// makes the cell into a teleporter
		// and assigns a teleporter id to the cell
		void makeTeleporter(char which);

		// this retrieves or changes information
		// about the target destination of a teleporter
		// note that these should ONLY be called if
		// this cell is a teleporter
		void setTarget(const Location &where);
		const Location &getTarget() const;

		// return true if the teleports of this and other
		// are the same identifier
		bool matches(const Cell &other) const;

		// manipute the number of steps on this Cell
		int getSteps() const;
		void setSteps(int steps);

	private:
		// the amount of steps to get to this cell is unknown.
		// anything greater than 20*20 works because at worst,
		// Ben walks on every square once, which is 400 steps.
		static const int UNKNOWN = 100000;

		// -1 is stored in bestAnswer meaning a smaller answer
		// will never be found.  this causes the program
		// to never search from a cell with WALL stored
		// in bestAnswer
		static const int WALL = -1;

		// because the teleporter is stored as a single digit
		// '0' - '9', the null character means it cannot teleport
		static const char CANNOT_TELEPORT = 0;

		// the teleporter id, CANNOT_TELEPORT
		// indicates that the cell cannot teleport
		char teleporterId;

		// if the cell teleports, the target location is here
		Location targetLocation;

		// the minimum number of steps to get here
		// if unknown, this is UNKNOWN
		int bestAnswer;
};

/*********************************************************************
The Map class is used to store information about the board
and solve the problem. It uses a bunch of cells and moves
Ben around the cells, updating as needed.
*********************************************************************/

class Map {
	public:
		// create a map of m by n Cells
		Map(int m, int n);

		// read the map in from a stream
		void read(std::istream &in);

		// solve the entire map
		void solve();

		// get the answer for this map
		int answer() const;

	private:
		// these are used to get the cell of at a specific
		// location. the use of & is a reference in C++. this
		// allows two variables to refer to the same data. they
		// are cheaper than copying the object and a bit more safe
		// than pointers (hence, java uses references)
		Cell &getCell(const Location &location);
		const Cell &getCell(const Location &location) const;

		// these functions look through the Cells and connect
		// target locations of teleporters for us
		void resolveTeleporters();
		const Location findMatchingTeleporter(const Location &source) const;

		// this makes Ben take a step through the map
		// to the specified location and records the total
		// number of steps taken so far
		void step(const Location &where, int stepsSoFar);

		// this returns true if the location is on the map
		// and is not a wall
		bool valid(const Location &where) const;

		// this is the map
		typedef vector <Cell> Array;
		typedef vector <Array> Matrix;
		Matrix cells;

		// this stores where Ben starts off and where the box is
		Location startCell;
		Location endCell;
};

/********************************************************************/

int main() {
	ifstream in("box.in");

	int m, n;
	in >> m >> n;

	// stop when BOTH are zero.  this
	// means that 5 0 is a VALID case
	while(!(m == 0 && n == 0)) {
		// make a new map of size m, n
		Map layout(m, n);
		
		// read the layout from the given stream in a format
		// that the map understands
		layout.read(in);

		// have the layout solve the current map
		layout.solve();

		// note, Ben always has a path, 
		// so solve should always produce an answer
		cout << "He got the Box in " << layout.answer() << " steps!" << std::endl;

		// read the next input case
		in >> m >> n;
	}

	in.close();
	return 0;
}

/*********************************************************************
Location implementations
*********************************************************************/

Location::Location(int x, int y) {
	set(x, y);
}

bool Location::operator !=(const Location &other) const {
	return _x != other._x || _y != other._y;
}

void Location::set(int x, int y) {
	_x = x;
	_y = y;
}

int Location::x() const {
	return _x;
}

int Location::y() const {
	return _y;
}

/*********************************************************************
Cell implementations
*********************************************************************/

Cell::Cell() {
	teleporterId = CANNOT_TELEPORT;
	bestAnswer = UNKNOWN;
}

bool Cell::teleports() const {
	return teleporterId != CANNOT_TELEPORT;
}

bool Cell::wall() const {
	return bestAnswer == WALL;
}

void Cell::makeWall() {
	bestAnswer = WALL;
}

void Cell::makeTeleporter(char which) {
	teleporterId = which;
}

void Cell::setTarget(const Location &where) {
	targetLocation = where;
}

const Location &Cell::getTarget() const {
	return targetLocation;
}

bool Cell::matches(const Cell &other) const {
	return teleporterId != CANNOT_TELEPORT && teleporterId == other.teleporterId;
}

int Cell::getSteps() const {
	return bestAnswer;
}

void Cell::setSteps(int steps) {
	bestAnswer = steps;
}

/*********************************************************************
Map implementations
*********************************************************************/

Map::Map(int m, int n) {
	cells.resize(m, Array(n));
}

void Map::read(std::istream &in) {
	char symbol;
	for(unsigned int row = 0; row < cells.size(); row++)
		for(unsigned int column = 0; column < cells[row].size(); column++) {
			in >> symbol;

			switch (symbol) {
				case 'B':
					startCell.set(column, row);
					break;

				case '.':
					break;

				case 'W':
					getCell(Location(column, row)).makeWall();
					break;

				case 'X':
					endCell.set(column, row);
					break;

				default:
					getCell(Location(column, row)).makeTeleporter(symbol);
					break;
			}
		}

	resolveTeleporters();
}

/*********************************************************************
to figure out how the teleports work, we look at each cell.
if one is a teleporter, we immediately find it's pairing (which must exist).

when searching for the pairing, a few things must be true...
first, they cannot be the same cell,
second, they both must be teleporters
third, they have to have the same number
*********************************************************************/

void Map::resolveTeleporters() {
	for(unsigned int row = 0; row < cells.size(); row++)
		for(unsigned int column = 0; column < cells[row].size(); column++) {
			const Location cellLocation(column, row);
			Cell &cell = getCell(cellLocation);

			if(cell.teleports()) {
				const Location loc = findMatchingTeleporter(cellLocation);
				cell.setTarget(loc);
			}
		}
}

const Location Map::findMatchingTeleporter(const Location &source) const {
	for(unsigned int row = 0; row < cells.size(); row++)
		for(unsigned int column = 0; column < cells[row].size(); column++) {
			const Location matchLocation(column, row);
			const Cell &match = getCell(matchLocation);

			if(source != matchLocation && match.teleports() && getCell(source).matches(match))
				return matchLocation;
		}

	// we should never get here because the problem gaurantees that a match
	// always exists
	return Location();
}

void Map::solve() {
	// just solve te entire map
	// this stops when there is no where to go "quicker"
	step(startCell, 0);
}

int Map::answer() const {
	// because we solved the whole map, we known the answer for every square...
	// neat!
	return getCell(endCell).getSteps();
}

Cell &Map::getCell(const Location &location) {
	return cells[location.y()][location.x()];
}

const Cell &Map::getCell(const Location &location) const {
	return cells[location.y()][location.x()];
}

void Map::step(const Location &location, int stepsSoFar) {
	// if we tried stpping in an invalid location, just act
	// like it never happened
	if(!valid(location) || getCell(location).wall())
		return;

	// get the current cell
	// the &currentCell creates a reference to the value
	// instead of copying the value.  this is slightly more
	// efficient
	Cell &currentCell = getCell(location);

	// make sure this is not worse than the current answer
	if(currentCell.getSteps() <= stepsSoFar)
		return;

	// update our answer
	currentCell.setSteps(stepsSoFar);

	// if we are at a teleporter, try teleporting
	// the cost of teleporting is 0, so do not change
	// the stepsSoFar argument
	if(currentCell.teleports())
		step(currentCell.getTarget(), stepsSoFar);

	// otherwise, walk around.... note that we will catch an
	// "off the board" above
	step(Location(location.x() - 1, location.y()), stepsSoFar + 1);
	step(Location(location.x(), location.y() - 1), stepsSoFar + 1);
	step(Location(location.x() + 1, location.y()), stepsSoFar + 1);
	step(Location(location.x(), location.y() + 1), stepsSoFar + 1);
}

bool Map::valid(const Location &location) const {
	const int x = location.x();
	const int y = location.y();
	return x >= 0 && y >= 0 && y < cells.size() && x < cells[y].size();
}

/*********************************************************************
That concludes the solution.  Thanks for reading!
*********************************************************************/
