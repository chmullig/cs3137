##Chris Mulligan 
clm2186@columbia.edu   <br />
COMSW3137 Data Structures & Algorithms   <br />
Hershkop, Fall 2013    <br />

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
two bonus commands to dump appropriate files for loading into graphviz and
Gephi. I found that trying to directly integrate the graphviz (and other)
libraries resulted in brittle, unportable code. Hopefully this solution is
sufficient, since `make test` does generate a png of the graph.

For graphviz, run command `j`, and give it an appropriate name, eg, `world`.
Then if you have graphviz installed, run `neato -Tpng world.dot -o world.png`
and see a beautiful rendering of the graph! Note that this will take a LONG time
on the full 9000 cities, and the graphs will be unreadable. However it works
with fict100 okish. You can see this by running `make fict100.png`.

My program also generates two CSV files suitable for loading into Gephi.

Note: for testing, I have set a static seed for generating the random flights.
This ensures that my graphs are identical each time I load them, greatly
simplifying my testing.


Files
=====

BinaryHeap.java
---------------
This is the book's implementation of a Binary Heap, which I'm using to represent
a priority queue for Djikstra's algorithm. As a heap it supports O(1) findMin,
O(log n) delete min, and O(log n) inserts. Since I'm storing cities, this is
n = V for my only use case of the heap.


City.java
---------
This is my node class. It stores name, latitude, longitude as basic properities,
so changing them is simple, just O(1).

It stores incoming and outgoing flights as separate LinkedLists, so appending
new flights is O(1). Returning a list of flights is also O(1), since we can just
return the list and let the consumer use it. However finding a specific flight
in the list is O(E), where E is the number of flights. However we know in this
specific instructions the maximum number of outgoing flights for a user is 8,
making iterating through the list of outgoing flights constant time. Incoming
flights is unbounded - potentially every other city could randomly choose to
link to us, making iterating through the incoming edges O(V).

For Djikstra's algorithm is keeps several temp variables - distance, visited,
parent city and parent flight. Accessing/modifying all these is O(1). 


Flight.java
-----------
This is my edge class. In general it does nothing interesting except store
origin and destination city, store a cost, and calculate the distance between
the cities. For calculating distance I use great circle distance in kilometers,
with implementation advice coming from wikipedia and
http://stackoverflow.com/a/837957/349931, which is just O(1).


MyGraphMap13.java
-----------------
This is the main graph class! It stores cities as an ArrayList, along with a
HashMap looking up city full name ("name, state") to the city. 

Loading a file iterates through the specified file the number of times specified
on the first line, creating city objects and adding them. AddCity first checks
to see if the city is already in the city list, costing O(V) for searching the
ArrayList. If not it adds it to the end, costing amortized constant time. It
then adds it to the hash table, again costing amortized constant time.

Adding a single flight is O(1) - the flight needs to be created, then it needs
to be added to the origin and destination's list of flights. However because
they're stored as LinkedLists, that's an O(1) operation. Adding random flights
to a single city is constant, although with a high multiplier. It generates a
random number for the # of outgoing edges, say r, then generates r random
cityIDs , then creates the flight. This is all constant, however we have to do
it V times, so adding random flights is overall O(V).

Listing cities based on state ("Denmark"), or city name ("Copenhagen"), are both
O(V) - we iterate through the entire list of cities and build up a linkedlist of
matching cities. Finding a city based on the full name ("Copenhagen, Denmark")
is O(1) because I have a precomputed hashmap. Finding a city by ID is also O(1),
because the ID corresponds to its index in the ArrayList.

Getting the n cheapest, n closest, or shortest path all result in running
Djikstra's algorithm from the current city and exploring the entire (reachable)
graph. The difference between using distance and cost is a single line of
whether we take flight.distance or flight.cost to calculate our weights for
Djikstra's.

I use a relatively naive implementation of Djikstra's, based on my class notes
and Wikipedia. Because I'm using Weiss' Binary Heap this is O((E+V)logV) for all
cases. I do not cache (ie if the distance metric we're using, and the , so
subsequent runs experience no performance benefits. However in practice this is
sufficiently fast even with 9000 cities to be satisfactory for this use case.

Once Djikstra's has populated the distances (using the appropriate distance
metric): getting n closest is O(V+VlogV)=O(VlogV) - I copy the list of cities,
sort the list, and pull the first N out.

Getting the shortest path is the cost of running Djikstra's, plus worst case
O(V) - I start with the destination and ask for the parent until there is no
parent (indicating we're at the current node). That's appended to a LinkedList
in O(1), which at the end is reversed O(V).

Printing out full info about a node is O(V) absolute worst case, because it
prints out all incoming and outgoing links. Outgoing is bounded by 8, but
incoming could be V-1 if every other city links to this one.

Printing out the graphviz, gephi, and adjacency list are all O(V+E) because it
must touch every node and every edge.


MapApplication.java
-------------------
This is a wrapper around MyGraphMap13, as such it does very little work itself,
mostly calling functions in MyGraphMap13. It exists so that the CLI and GUI apps
can use the same underlying code more easily.

The exceptions are: when it loads a file it appends the file name to a linked list,
O(1). When you load another file it first checks that linked list, which is
O(num files), typically a very small number. Reseting is O(1) - it simply
creates a new map and linked list.

Searching by state, city are all O(V) from the underlying MyGraphMap13
implementation, with the additional linear cost of printing out the matches.

Finding the n closest is O(VlogV) from the underlying Djikstra's cost, plus a
linear cost on matches to print them out. Because I also print out the path there's
an additional O(V) linear cost each time, for a pathological worst case of O(V^2), but
average case the path will be MUCH shorter, generally approaching constant time.

Finding the shortest path is O(VlogV) from the underlying Djikstra's cost, plus a
linear cost on the number of nodes to print the path out.

mainf13.java
------------
CLI interface to the program. This program is just a wrapper around
MapApplication, taking user input and calling the appropriate MapApplication
function. It cleverly uses a single scanner to handle input, allowing the same
code to take commands from a file or interactively from a user. If it's taking
commands from a file it prints out the values that were entered, making it much
easier for a user to see what's going on. It does nothing itself, so see
MapApplication.java, and earlier, for all runtimes.

Can run this by running the `clm2186_programming_3_cli.jar` jar, or `make test`.


MapPanel.java
-------------
The core of the GUI application, it does the same things as mainf13.java, so it
has no interesting code from a data strucutre/runtime perspective - everything
it does is by calling into MapApplication.java based on the user input.

guif13.java
-----------
GUI application. All it does is setup the window and call MapPanel.

Can run this by running the `clm2186_programming_3_gui.jar` jar.


Miscellaneous Files
===================
world.dot
---------
Graphviz file created by the MyGraphMap13 makeGraphviz command - it's usable to
create an image representing the graph. It should be run with `neato`, for
example if you want an SVG `neato -Tsvg world.dot -o world.svg`. However you'll
have to have graphviz installed first to compile it.


world.png
---------
Created as above, in case you don't have graphviz installed. It's pretty
unreadable with 9000 cities, but works ok with a few hundred. Using gephi to
create an interactive visualization is much better.


docs/
-----
Javadoc created documentation! 


input.txt
---------
Test input, based on the example in the PDF, with some additional bonus testing.


worldcity.txt, worldcities.txt, fict100.txt, world100.txt
---------------------------------------------------------
These are the test files. NOTE, I swap the latitude/longitude from what the PDF
describes them as, because the PDF appears to be wrong.

Makefile
--------
Makefile that compiles the java, creates the two tarballs, creates the tgz I
submit (`make tgz`). Also, if you run `make test` it will create a png of the
graphviz file, as long as you have Graphviz installed.