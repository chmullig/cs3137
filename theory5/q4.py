from unionfind import *

if __name__ == '__main__':
    print "Testing..."
    uf = UnionFind()
    uf.insert_objects(xrange(17))

    print uf
    uf.union(1,2)
    uf.union(3,4)
    uf.union(3,5)
    uf.union(1,7)
    uf.union(3,6)
    uf.union(8,9)
    uf.union (1,8)
    uf.union(3,10)
    uf.union(3,11)
    uf.union(3,12)
    uf.union(3,13)
    uf.union(14,15)
    uf.union(16,0)
    uf.union(14,16)
    uf.union(1,3)
    uf.union(1,14)

    print uf
    print [uf.find(x) for x in xrange(17)]