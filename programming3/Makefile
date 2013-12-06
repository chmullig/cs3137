JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	GPS.java

default: classes
	jar cfe clm2186_programming_3_cli.jar VirusChecker  $(CLASSES:.java=.class) 

classes: $(CLASSES:.java=.class)

docs: $(CLASSES)
	javadoc -private -d docs $(CLASSES)

.PHONY: clean all test
clean:
	$(RM) *.class *.jar

all: clean default docs

test: default
	java -Xmx4g -jar clm2186_programming_3_cli.jar -b hwb/ -v hwv/ -t test -t hwb -t hwv  -save makefile.ser

clm2186_programming_3.tar.gz: default docs
	tar czf clm2186_programming_3.tar.gz README.md *.java *.jar docs Makefile
