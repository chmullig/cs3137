JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	BinaryHeap.java \
	City.java \
	Flight.java \
	MyGraphMap13.java \
	MapApplication.java \
	mainf13.java

default: clm2186_programming_3_cli.jar

clm2186_programming_3_cli.jar: $(CLASSES:.java=.class)
	jar cfe clm2186_programming_3_cli.jar mainf13 $(CLASSES:.java=.class) 


docs: $(CLASSES)
	javadoc -private -d docs $(CLASSES)

.PHONY: clean all test
clean:
	$(RM) *.class *.jar

all: clean default docs

test: default
	java -Xmx2g -jar clm2186_programming_3_cli.jar fict100.txt

clm2186_programming_3.tar.gz: default docs
	tar czf clm2186_programming_3.tar.gz README.md *.java *.jar docs Makefile
