Chris Mulligan
clm2186

COMS3137 Data Structures & Algorithms
# Programming 1 #


## Classes ##
### HWTest.java ###
This tester class exercises the SDLL class. It creates has 3 stages:

First it uses a small list of words and phrases to test basic creating, deleting,
incrementing, etc. It prints out the list at several points in the process to
aid debugging.

Second it fulfills steps 5-7 by creating a list of 1000 integers in [0, 30]. It
then prints them with percentages, prints the top 3 most frequent entries,
and prints reverses the list. It prints timing information in milliseconds for
adding the 1000 integers (the time includes generating the random numbers) and
for reversing the list.

Finally it looks to the command line arguments for a single file name. It attempts
to load the file, and then uses Scanner to read each line and add the lines to
the linked list. It sorts, and then prints, the list.


### LinkNode.java ### 
The node stored in the SDLL. The node is a generic, doubly linked node. It has a
Data field, a count, and next and previous LinkNodes.

If the next is null it should be considered the tail of the list.

Equally if the previous is null it should be considered the head.

Internally it keeps a count for the numbers of times this element should be
considered in the list. Deleting probably wants to decrement the count until
its zero, but the node doesn't know anything about that.

### SuperDuperLinkedLists.java ###
A doubly linked list of LinkNodes. A generic, it implements the iterator
interface.

Elements can be inserted by value, in which case they either increment an existing
node with the same value (as determined by .equals()) or adds a new node to the
end of the list.

Find returns the node whose data .equals() the given input.

Delete by node decrements the node's count. If the count is 0 it will route around
the node, remove it from the list.

Delete by value uses find to find a node, and then delete by node to delete it.

Sort uses a mergesort (based on
http://www.chiark.greenend.org.uk/~sgtatham/algorithms/listsort.html ) to sort
the linked list by count. It currently doesn't allow sorting by value.

Reverse reverses the list by beginning at the head and swapping every elements
next and previous pointers. It then swaps the head and tail pointers. The result
is the same list, in reverse order.

print will print out the list prettily. Shows frequency as either a count or a
percent of the total items in the list. If our list contains "hello" 5 times,
followed by "world" once it would print with the count argument:

    6: [head] -> hello:5 -> world:1 <- [tail]

printN will print the n most frequent elements in the list. It does this in a
roundabout manner by cloning the list, sorting the clone, and printing the last
n entries in the clone (only showing percentages).

clone makes a copy of the linked list, using the same data values but new nodes.
Useful for sorting a copy, for example.

Maintains an internal (read only) size equal to the sum of the counts of every
node.

SDLL implements a basic iterator that permits only forward iteration. Does not
support removing. It returns the node itself so the caller can access the count,
even though that's not necessarily ideal in all circumstances.

### SDLException.java ###
An exception class in case you attempt to delete an element that doesn't exist.


## Runtime Analysis ##

Insert: O(n). We have to first find in the list to see if the element is already there,
which is O(n). If we find it the best case is just incrementing an int. Worst case
we don't find it and we have to create a node, point tail's next to it, and
repoint the tail, which are all constant operations.

Find: O(n) because it must search each node individually and cannot take advantage
of any sorting, etc. 

Delete by node: O(1) If the count is >1 then we just decrement the count, a
constant operation. It the count is 1 it's a matter of updating the previous and
next nodes to bypass it (and possibly head/tail), all constant time operations.

Delete by value: O(n) - it must first find the node, which is O(n), then delete
it by node which is O(1), so overall O(n).

getHead/Tail/Size are all O(1) operations because they're stored internally and
updated as the list changes. 

print: O(n) because it has to touch every element once to print it out.

reverse: O(n) it touches every element swapping its previous and next pointers, 
plus updating the head and tail in constant time.

sort: O(n log n) uses an in place mergesort, which is relatively efficient. It
does log n passes over n items, for n logn total work. 

clone: O(n) makes a new node for every existing node, but no worse.

printN: O(n logn), although actual performance is worse than that suggests. 
It clones in O(n) and then sorts in O(n log n), before printing the last i elements
in O(i) where i is the argument value. PrintN uses substantially more memory,
although not pathologically more, since it duplicates the linked list and nodes,
but not the actual values.
