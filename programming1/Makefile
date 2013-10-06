JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	LinkNode.java \
	SDLException.java \
	SuperDuperLinkedLists.java \
	HWTest.java

default: classes
	jar cfe clm2186_programming_1.jar HWTest $(CLASSES:.java=.class) SuperDuperLinkedLists*.class

classes: $(CLASSES:.java=.class)

docs: $(CLASSES)
	javadoc -private -d docs $(CLASSES)

.PHONY: clean all test
clean:
	$(RM) *.class *.jar

all: clean default docs

test: default
	java -jar clm2186_programming_1.jar words.txt

tgz:

clm2186_programming_1.tar.gz: default docs
	tar czf clm2186_programming_1.tar.gz README.txt *.java clm2186_programming_1.jar docs