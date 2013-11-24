theory5.pdf: theory5.tex q6.png
	pdflatex theory5.tex

q6.png: q6.dot
	dot -Tpng q6.dot -o q6.png

q6.dot: q6.py
	python q6.py > q6.dot


clean:
	rm -f *.aux *.log *.class *.fdb_latexmk *.synctex.gz