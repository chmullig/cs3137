COMSW3137 Programming 2: Virus Checker
======================================

**Chris Mulligan <clm2186@columbia.edu>**

Professor Shlomo Hershkop, Fall 2013


What?
-----
This program implements a very simple Naïve Bayes-esque virus checker. Given a set
of known viruses and known benign files, it predicts whether the new files are
viruses or benign.

There are two interfaces:

`VirusChecker` is a command line program that uses the Apache Commons CLI library 
to parse command line parameters to flexibly run in command line mode. It can
load files or serialized VirusCollection objects, and test the files. It can be
run via the `clm2186_programming_2_cli.jar` jar file.

`VirusGUI` is a graphical interface that provides an interactive, GUI version of
the same features. It can be run via the `clm2186_programming_2_gui.jar` jar file.

Both require >=3g of heap for the given test files.


HashTable
---------
I implemented my own *HashTable*, because I felt like it was an important part
of this assignment, and it would be a waste of time to not bother actually
implementing it. It's implemented as a generic <K, V>, to be most flexible (I
primarily use it with <String, Integer> and <String, Double>. Internally
HashTable uses two arrays, one storing keys, one values. The backing arrays
capacities are always prime, using a list from
http://planetmath.org/goodhashtableprimes

The HashTable uses quadratic probing to deal with hash collisions. I use a
loadFactor of .66, and blindly grow if I exceed it. That means I don't account
for worst case or best case scenarios where the loadFactor could conceivably be
much larger or smaller. I use a single find(key) function to either locate the
key in the underlying arrays, or to locate where it could go. This is using
quadratic probing. I therefore use lazy deletion, by setting the Key to a
special sentinel object and checking for it later (by memory location).

I implement the Map interface, but not all functions specified by the Map<K,V>
interface are meaningful complete, only those I found necessary or interesting.
This means things like containsValue, and various Set, Map, iterator type things
are undone. They will throw UnsupportedOperationExceptions.

Structure
---------
The main class is `VirusCollection`. It stores hash tables of the n-grams for
viruses, and the n-grams for benign files. It additionally manages the parameters
n, alpha, and beta (see algorithm below). It has a number of helper functions for
loading various kinds of files, and then testing them. When it calculates results
it pre-caches all the logged probabilities for every word, making subsequent tests
very fast (as long as alpha/beta don't change). This class is unfortunately
somewhat of a god object in the program.

`VirusChecker` is the command line program. It uses the (bundled) Apache Commons
CLI to parse command line parameters. One interesting feature it implements is
hyperparameter probing mode, checking various values of alpha and beta, and
printing out the test results as a CSV. This can be useful for setting
reasonable defaults. Unfortunately it doesn't (currently) probe on n, due to the
length of time it would require. However it could be easily added. It can
(probably) be executed with reasonable parameters by running `make test`.

`VirusGUI` and `VirusPanel` implement a Java Swing powered interface for testing
files. Provides the expected buttons, including loading/saving serialized
`VirusCollection` objects (compatible with command line version). It doesn't
provide very clean or helpful output, but it does implement things like file
boxes well (for loading virus/benign files, and for testing, you can select 1
file, or n files, or a directory). Allows you to tune n (only before loading
files), alpha, beta.

Algorithm
---------
There were many ambiguities about exactly how the algorithm should perform. 
My Naïve Bayes approach is based on three resources:

* Paul Graham's spam essay at http://www.paulgraham.com/spam.html, 
* Wikipedia's articles on NB and Spam Filtering 
    http://en.wikipedia.org/wiki/Bayesian_spam_filtering
* Schutt & O'Neil, Doing Data Science, 2013. 

I calculate theta = P(Virus|Byte) = [(times in Virus) + alpha] / [(times in Virus) + (times in Benign) + beta].
alpha and beta determined experimentally to be 1 and 200, with n=8.
I then store log(theta) and log(1-theta) in hash tables for later use. To 
compute P(virus|bytes) I let aeta = sum(theta_i - (1-theta_i)), then the
overall probability is 1/(1+e^aeta).


ToDo
----
* Tune the hyperparameters in a meaningful way
* generally improve algorithm
* better GUI reports
* finish filling out hash table functions
