JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	HashTable.java \
	Virus.java \
	VirusChecker.java \
	VirusCollection.java \

default: classes
	jar cfe clm2186_programming_2.jar VirusChecker  $(CLASSES:.java=.class) lib/*.jar

classes: $(CLASSES:.java=.class)

docs: $(CLASSES)
	javadoc -private -d docs $(CLASSES)

.PHONY: clean all test
clean:
	$(RM) *.class *.jar

all: clean default docs

test: default
	java -jar clm2186_programming_2.jar words.txt

clm2186_programming_2.tar.gz: default docs
	tar czf clm2186_programming_2.tar.gz README.txt *.java clm2186_programming_2.jar docs
