import random


V = 8
E = 16
nodes = range(V)
links = []

while len(links) < E:
    e1 = random.choice(nodes)
    e2 = random.choice(nodes)
    if e1 == e2:
        # no self loops
        continue
    if [True for o1, o2, w in links if ((e1 == o1 and e2 == o2) or (e1 == o2 and e2 == o1))]:
        #no multiedges
        continue
    links.append((e1, e2, random.randint(0, 9)))


print "graph G {" #\noverlap=scalexy;"
for e in nodes:
    print "%s;" % e
for e1, e2, w in sorted(links, key=lambda x: x[2]):
    print "\t %s -- %s [label=\"%s\"];" % (e1, e2, w)
print "}"