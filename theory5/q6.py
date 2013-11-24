import random


V = 8
E = 16
nodes = range(V)
links = []
for i in xrange(E):
    e1 = random.choice(nodes)
    e2 = random.choice(nodes)
    if e1 == e2:
        # no self loops
        continue
    if [True for o1, o2, w in links if ((e1 == o1 and e2 == o2) or (e1 == o2 and e2 == o1))]:
        #no multiedges
        continue
    links.append((e1, e2, random.randint(0, 9)))


print "graph G {\noverlap=scalexy;"
for e1, e2, w in links:
    print "\t %s -- %s [label=\"%s\"];" % (e1, e2, w)
print "}"