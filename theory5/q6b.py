from unionfind import *

nodes = range(1,9)
links = [    (1, 2, 240), (1, 3, 210), (1, 4, 340), (1, 5, 280),
(1, 6, 200), (1, 7, 345), (1, 8, 120), (2, 3, 240), (2, 4, 120),
(2, 5, 215), (2, 6, 180), (2, 7, 190), (2, 8, 150), (3, 4, 260),
(3, 5, 110), (3, 6, 350), (3, 7, 430), (3, 8, 190), (4, 5, 160),
(4, 6, 330), (4, 7, 295), (4, 8, 200), (5, 6, 350), (5, 7, 400),
(5, 8, 180), (6, 7, 175), (6, 8, 205), (7, 8, 300), ]

uf = UnionFind()
uf.insert_objects(nodes)
mst = []

for l in sorted(links, key=lambda x: x[2]):
     if uf.find(l[0]) != uf.find(l[1]):
          uf.union(l[0], l[1])
          mst.append(l)

print "graph G {\noverlap=scalexy;"
for e in nodes:
    print "%s;" % e
for l in mst:
    print "\t %s -- %s [label=\"%s\"];" % l
print "}"