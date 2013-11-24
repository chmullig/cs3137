theory5.pdf: theory5.tex q6.png q6b.png
	pdflatex theory5.tex

q6.png: q6.dot
	dot -Tpng q6.dot -o q6.png

q6.dot: q6.py
	python q6.py > q6.dot

q6b.png: q6b.dot
	dot -Tpng q6b.dot -o q6b.png

q6b.dot: q6b.py
	python q6b.py > q6b.dot

q7.png: q7.dot
	neato -Tpng q7.dot -o q7.png

clean:
	rm -f *.aux *.log *.class *.fdb_latexmk *.synctex.gz