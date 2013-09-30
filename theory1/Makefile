theory1.pdf: theory1.tex fibtimes.png Fibonacci.java
	pdflatex theory1.tex

fibtimes.png: fibtimes.R fibonacci.cleantimes
	Rscript fibtimes.R

Fibonacci.class: Fibonacci.java
	javac Fibonacci.java

FibonacciTailess.class: FibonacciTailess.java
	javac FibonacciTailess.java

fibonacci.times: Fibonacci.class FibonacciTailess.class
	echo "" > fibonacci.times
	for((i=1;i<=51;i+=1)); do java Fibonacci $$i >> fibonacci.times; done
	for((i=1;i<=51;i+=1)); do java FibonacciTailess $$i >> fibonacci.times; done

fibonacci.cleantimes: fibonacci.times
	echo "type	n	time" > fibonacci.cleantimes
	cut -f 2,4,6 fibonacci.times >> fibonacci.cleantimes