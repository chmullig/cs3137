Chris Mulligan
clm2186@columbia.edu
COMSW3137 Data Structures & Algorithms
Hershkop, Fall 2013

Programming 3
=============
Fully implemented with a command line interface. Structurally it's mediocre, but
it's pretty reasonable.

I created a City class, and a Flight class to represent edges between cities. I
then create a map class, MyGraphMap13, that manages the list of cities, finding
distances, etc, etc. There's then a MapApplication that contains functions for
each of the commands a-i. The MapApplication is used by `mainf13` to implement a
command line interface.

It is also used by `guif13` to implement a graphical interface.

The data structures I use are Java's List (both ArrayList and LinkedList),
HashMap, and Arrays. I also use Weiss' BinaryHeap for Dijkstra's priority queue
(the instructions suggest we should only use the basic structures I mentioned,
structures we make, or ones we take from the book). 

For finding n Closest, and finding cheapest/shortest paths I implemented 
Dijkstra's algorithm, based on my class notes and the wikipedia article.

I implemented a basic graphical interface. For drawing the graph, I've created
two bonus commands to dump appropriate files for loading into graphviz and Gephi.

For graphviz, run command `j`, and give it an appropriate name, eg, `world`. Then
if you have graphviz installed, run `neato -Tpng world.dot -o world.png` and see
a beautiful rendering of the graph! Note that this will take a LONG time on the
full 9000 cities, and the graphs will be unreadable. However it works with
fict100 okish. You can see this by running `make fict100.png`.

My program also generates two CSV files suitable for loading into Gephi.

Note: for testing, I have set a static seed for generating the random flights.
This ensures that my graphs are identical each time I load them, greatly simplifying
my testing.