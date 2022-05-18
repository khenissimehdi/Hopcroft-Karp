package Algo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;


/**
 * BipartiteGraph In the mathematical field of graph theory, a bipartite graph is a graph whose vertices can be divided
 * into two disjoint and independent sets, namely U and V, i.e. each edge connects a vertex
 * of U to a vertex of V. The sets of vertices U and V are usually called the parts of the graph.
 * THIS IS  A LIGHT, WEIGHT IMPLEMENTATION I ONLY USE  ONE ARRAY THAT HAVE THE LEFT ELEMENTS
 */
public class BipartiteGraph implements Graph {
    private ArrayList<LinkedList<Edge>> bipartiteAdj;
    private int left;
    private int right;
    private int links;
    private ArrayList<Integer> lefts;

    /**
     * Return the number of vertexes on the left side
     *
     * @return int
     */
    public int getLeft() {
        return left;
    }

    /**
     * Return the number of vertexes on the right side
     */
    public int getRight() {
        return right;
    }

    /**
     * Return the elements on the left side
     *
     * @return ArrayList<Integer>
     */
    public ArrayList<Integer> getLefts() {
        return lefts;
    }

    /**
     * Return the number of edges.
     * @return int
     * */
    @Override
    public int numberOfEdges() {
        return links;
    }

    /**
     * Return numbers of vertices.
     * @return int
     * */
    @Override
    public int numberOfVertices() {
        return left + right;
    }

    /**
     * Add an edge in the graph
     * */
    @Override
    public void addEdge(int i, int j, int value) {
        var linkedList = bipartiteAdj.get(i);
        var newEdge = new Edge(i, j, value);
        linkedList.add(newEdge);
    }

    /**
     * Tell if the coordinate gives an edge
     * @param i int
     * @param j int
     * @return boolean
     * */
    @Override
    public boolean isEdge(int i, int j) {
        return bipartiteAdj.get(i).stream().anyMatch(edge -> edge.getEnd() == j);
    }

    /**
     * Gives the weight on the vertex.
     * @param i int
     * @param j int
     * @return int
     * */
    @Override
    public int getWeight(int i, int j) {
        var edge = bipartiteAdj.get(i).stream().filter(e -> e.getEnd() == j).findFirst();
        if (edge.isEmpty()) {
            throw new IllegalArgumentException("The edge i not here");
        }
        return edge.get().getValue();
    }

    /**
     * Return and edge iterator of a given vertex.
     * @param i int
     * @return Iterator<Edge>
     * */
    @Override
    public Iterator<Edge> edgeIterator(int i) {
        return bipartiteAdj.get(i).iterator();
    }

    /**
     * Method that gives access to a foreach loop.
     * @param i int
     * @param consumer Consumer<Edge>
     * */
    public void forEachEdge(int i, Consumer<Edge> consumer) {
        bipartiteAdj.get(i).forEach(consumer);
    }

    /**
     * Return a Graphviz as a string
     * @return String
     * */
    @Override
    public String toGraphviz() {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("digraph G {").append("\n");
        IntStream.range(0, bipartiteAdj.size()).forEach(i -> {
            stringBuilder.append(i).append(";").append("\n");
            forEachEdge(i, edge -> stringBuilder.append(i).append(" -> ").
                    append(edge.getEnd()).append(" [ ").append("label=\"").
                    append(edge.getValue()).append("\"").append(" ]").
                    append(" ;").append("\n"));
        });
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /**
     * To String method
     * @return String
     * */
    @Override
    public String toString() {
        return bipartiteAdj.toString();
    }

    /**
     * Method that take a filePath and fill the BipartiteGraph
     * @param pathFile Path
     * */
    public void feedFromMat(Path pathFile) throws IOException {
        var lines = Files.readAllLines(pathFile, StandardCharsets.UTF_8);
        var tabLR = lines.remove(0).split(" ");
        left = Integer.parseInt(tabLR[0]);
        right = Integer.parseInt(tabLR[1]);
        lefts = new ArrayList<>();
        links = Integer.parseInt(lines.remove(0));
        this.bipartiteAdj = new ArrayList<>(right + left);
        IntStream.range(0, right + left).forEach(e -> bipartiteAdj.add(new LinkedList<>()));
        for (int i = 0; i < links; i++) {
            var parserArray = lines.get(i).split(" ");
            var l = Integer.parseInt(parserArray[0]);
            var r = Integer.parseInt(parserArray[1]) + left;
            this.addEdge(l, r, 0);
            this.addEdge(r, l, 0);
            this.lefts.add(l);
        }
    }
}