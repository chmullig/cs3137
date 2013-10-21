import pyavltree

t = pyavltree.AVLTree()

for k in [2, 1, 4, 5, 9, 10, 11]:
    t.insert(k)

print "First insert:"
print t.to_graph()

for k in [13, 3, 17, 33]:
    t.insert(k)

print t.to_graph()
