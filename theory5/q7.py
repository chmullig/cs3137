import sys
from collections import deque

adjacencies = {
1 : [2, 3, 4], 
2 : [1, 3, 4], 
3 : [1, 2, 4], 
4 : [1, 2, 3, 6],
5 : [6, 7, 8], 
6 : [4, 5, 7], 
7 : [5, 6, 8], 
8 : [5, 7],
}

sys.stdout.write("DFS: ")
stack = []
stack.append(1)
seen = set()
while stack:
    current = stack.pop()
    if current in seen:
        continue
    seen.add(current)
    sys.stdout.write("%s, " % current)
    for dest in adjacencies[current]:
        if dest not in seen:
            stack.append(dest)

print

sys.stdout.write("BFS: ")
queue = deque()
queue.append(1)
seen = set()
while queue:
    current = queue.popleft()
    if current in seen:
        continue
    seen.add(current)
    sys.stdout.write("%s, " % current)
    for dest in adjacencies[current]:
        if dest not in seen:
            queue.append(dest)

print
