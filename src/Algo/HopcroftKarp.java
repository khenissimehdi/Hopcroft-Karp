package Algo;

import java.io.IOException;

import java.nio.file.Path;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * The Hopcroft-Karp algorithm is an algorithm that takes as input a bipartite graph and produces as output
 * a coupling of maximum cardinality,i.e.a set of edges as large as possible with
 * the property that tw o edges never share an end.
 */
public class HopcroftKarp {
    private final BipartiteGraph bGraph;
    private final List<Edge> matches = new ArrayList<>();
    private final Predicate<Integer> isEven = (num) -> num % 2 == 0;
    private final BiPredicate<Integer, Integer> notInM = (v, vPrime) -> !matches.contains(new Edge(v, vPrime));
    private final int[] level;
    private final boolean[] visited;

    public HopcroftKarp(Path pathToFile) throws IOException {
        this.bGraph = new BipartiteGraph();
        this.bGraph.feedFromMat(pathToFile);
        this.level = new int[this.bGraph.numberOfVertices()];
        this.visited = new boolean[this.bGraph.numberOfVertices()];

    }

    /**
     * Method that will compute the unMatched vertexes.
     *
     * @return LinkedList<Integer>
     */
    private LinkedList<Integer> unmatchedV1InM() {
        var queue = new LinkedList<Integer>();
        IntStream.range(0, this.bGraph.getLeft()).forEach(e -> {
            if (matches.stream().noneMatch(edge -> edge.getStart() == e)) {
                queue.add(e);
            }
        });
        return queue;
    }

    /**
     * Method that execute a BFS breadth-first and build an alternating level graph
     * rooted at unmatched vertices.
     *
     * @param level int[]
     * @return boolean
     */
    public boolean levelBFS(int[] level) {
        var queue = unmatchedV1InM();
        var newQueue = new LinkedList<Integer>();
        Arrays.fill(visited, false);
        Arrays.fill(level, 0);
        var clvl = 0;

        while (!queue.isEmpty()) {
            for (var v : queue) {
                var it = bGraph.edgeIterator(v);
                while (it.hasNext()) {
                    var cValue = it.next();
                    var vPrime = cValue.getEnd();
                    if (!visited[vPrime]) {
                        // Finding augmented path by alternating from even to odd.
                        if ((isEven.test(clvl) && (notInM.test(v, vPrime) && notInM.test(vPrime, v))) ||
                                (!isEven.test(clvl) && !(notInM.test(v, vPrime) && notInM.test(vPrime, v)))) {
                            visited[vPrime] = true;
                            newQueue.add(vPrime);
                            level[vPrime] = clvl + 1;
                        }
                    }
                }
            }
            clvl += 1;
            var oldQueue = queue;
            queue.clear();
            for (var v : newQueue) {
                if (matches.stream().noneMatch(edge -> (edge.getStart() == v) || (edge.getEnd() == v))) {
                    return true;
                }
            }
            // Switching between newQueue and oldQueue.
            queue = new LinkedList<>(newQueue);
            newQueue = new LinkedList<>(oldQueue);
        }
        return false;
    }

    /**
     * Method that apply DFS Depth-first to augment the current matching M
     * with maximal set vertex disjoint shortest-length path.
     *
     * @param v     int
     * @param level int[]
     * @param v1    ArrayList<Integer>
     * @return boolean
     */
    public boolean levelDFS(ArrayList<Integer> v1, int[] level, int v) {
        visited[v] = true;
        //var c = contains(v1,v);
        if ((!v1.contains(v)) && matches.stream().noneMatch(edge -> (edge.getStart() == v) || (edge.getEnd() == v))) {
            return true;
        }
        var it = bGraph.edgeIterator(v);
        while (it.hasNext()) {
            var cValue = it.next();
            var vPrime = cValue.getEnd();
            if (!visited[vPrime]) {
                if ((isEven.test(level[v])) && (notInM.test(v, vPrime) || notInM.test(vPrime, v)) ||
                        !(isEven.test(level[v])) && (!notInM.test(v, vPrime) || !notInM.test(vPrime, v))) {
                    if ((level[vPrime] == level[v] + 1) && (levelDFS(v1, level, vPrime))) {
                        // as the algorithm works in both sides means that v,v' == v',v
                        // i have to check when the level is even and when it's odd
                        if ((!notInM.test(v, vPrime) || !notInM.test(vPrime, v))) {
                            if (isEven.test(level[v])) {
                                var edge = getEdge(v, vPrime);
                                if (edge != null) {
                                    matches.remove(edge);
                                }
                            } else {
                                var edge = getEdge(vPrime, v);
                                if (edge != null) {
                                    matches.remove(edge);
                                }
                            }
                        } else {
                            if (isEven.test(level[v])) {
                                matches.add(new Edge(v, vPrime));
                            } else {
                                matches.add(new Edge(vPrime, v));
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Method that return an Edge using it coordinates.
     * Method may return null in case the value is not found.
     *
     * @param v      int
     * @param vPrime int
     * @return Edge
     */
    private Edge getEdge(int v, int vPrime) {
        return matches.stream().filter(edge -> edge.equals(new Edge(v, vPrime))).findFirst().orElse(null);
    }

    /**
     * Launcher method that will execute the algorithm until the BFS return false.
     * When the BFS return false that mean everything is matched and are good to go.
     *
     * @return List<Edge>
     */
    public List<Edge> compute() {
        Arrays.fill(visited, false);
        Arrays.fill(level, 0);

        var v1 =  this.bGraph.getLefts();

        while (levelBFS(level)) {
            Arrays.fill(visited, false);
            for (int i = 0; i < this.bGraph.getLeft(); i++) {
                int finalI = i;
                if (matches.stream().noneMatch(edge -> edge.getStart() == finalI)) {
                    levelDFS(v1, level, i);
                }
            }
            Arrays.fill(level, 0);
        }
        return matches;
    }

    public static void main(String[] args) throws IOException {
        var h = new HopcroftKarp(Path.of("g5.gr"));
        var c = h.compute();
        System.out.println(c.size());

    }
}
