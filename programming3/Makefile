JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $<

SRC = \
	BinaryHeap.java \
	City.java \
	Flight.java \
	MyGraphMap13.java \
	MapApplication.java \
	guif13.java \
	MapPanel.java \
	mainf13.java

CLASSES = $(SRC:.java=.class)

default: clm2186_programming_3_cli.jar clm2186_programming_3_gui.jar

clm2186_programming_3_cli.jar: $(CLASSES)
	jar cfe clm2186_programming_3_cli.jar mainf13 $(SRC:.java=.class) *.class

clm2186_programming_3_gui.jar: $(CLASSES)
	jar cfe clm2186_programming_3_gui.jar guif13 $(SRC:.java=.class) *.class



docs: $(SRC)
	javadoc -private -d docs $(SRC)

.PHONY: clean all test
clean:
	$(RM) *.class *.jar

all: clean default docs

test: default
	java -Xmx2g -jar clm2186_programming_3_cli.jar input.txt

world.png: test
	neato -Tpng world.dot -o world.png

README.html: README.md
	markdown README.md > README.html

clm2186_programming_3.tar.gz: default docs world.png README.html
	tar czf clm2186_programming_3.tar.gz README.md README.html *.java *.jar docs Makefile world.dot world.png input.txt worldcity.txt worldcities.txt

tgz: clm2186_programming_3.tar.gz
