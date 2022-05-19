package Algo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * The Hopcroft-Karp algorithm is an algorithm that takes as input a bipartite graph and produces as output
 * a coupling of maximum cardinality,i.e.a set of edges as large as possible with
 * the property that two edges never share an end.
 */
public class HopcroftKarp {
    private BipartiteGraph bGraph;
    private final List<Edge> matches = new ArrayList<>();
    private final Predicate<Integer> isEven = (num) -> num % 2 == 0;
    private final BiPredicate<Integer, Integer> notInM = (v, vPrime) -> !matches.contains(new Edge(v, vPrime));
    private int[] level;
    private final String dir = "testData/";
    private boolean[] visited;
    private enum State {FINISHED, NOT_FINISHED}
    private State state = State.NOT_FINISHED;
    private static Charset UTF_8 = StandardCharsets.UTF_8;
    private static Logger logger  = Logger.getLogger(HopcroftKarp.class.getName());

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
     * @param fileName String
     * @return List<Edge>
     * @throws IOException
     */
    public List<Edge> compute(String fileName) throws IOException {

        var path = Path.of("./"+dir+fileName+".gr");
        this.bGraph = new BipartiteGraph();

        this.bGraph.feedFromMat(path);


        this.level = new int[this.bGraph.numberOfVertices()];
        this.visited = new boolean[this.bGraph.numberOfVertices()];

        Arrays.fill(visited, false);
        Arrays.fill(level, 0);

        var v1 =  this.bGraph.getLefts();
        // Number of iterations
        var it = 0;
        System.out.println("Computing...");
        System.out.println("============");
        while (levelBFS(level)) {
            Arrays.fill(visited, false);
            for (int i = 0; i < this.bGraph.getLeft(); i++) {
                int finalI = i;
                if (matches.stream().noneMatch(edge -> edge.getStart() == finalI)) {
                    levelDFS(v1, level, i);
                }
            }
            Arrays.fill(level, 0);
            it++;
        }
        state = State.FINISHED;

        System.out.println("File " + fileName +".gr");
        System.out.println("Matching with" + matches.size() + "edge(s)");
        System.out.println("Using "+ it + " iteration(s)");
        System.out.println("============");

        return matches;
    }

    /**
     * Method that return a .sol file that contain the data inside the matched list
     * 4
     * 0 0
     * 1 1
     * 2 2
     * 3 4
     * @param fileName String
     * */
    public void toSol(String fileName) throws IOException {
        if(!state.equals(State.FINISHED)){
            logger.info("Please run the command Compute first the state is still finished");
            return;
        }
        System.out.println("Generating your solution file...");
        var sb = new StringBuilder();
        sb.append(matches.size()).append("\n");
        for (Edge match : matches) {
            sb.append(match.toSol()).append("\n");
        }
        var filePath = Path.of("outFiles/"+fileName+".sol");
        Files.write(filePath,sb.toString().getBytes());
        System.out.println("Your file was generated please check the path : "+"outFiles/"+fileName+".sol");
    }



    /**
     * Method run the console
     * */
    private void consoleRun() {
        var aboutText = """
                This tool will help you compute HopcroftKarp algorithm.
                """;
        var commandsText = """
                --help,-h -> see help.
                --compute <fileName> -c <fileName> -> compute result.
                --genSol <fileName>, -gs <fileName> -> generate a file with the solution.
                --clean, -cl -> clean the console
                Note : 
                Please when giving a file name don't provide the extension for security,
                reasons we only allow .sol(output) .gr(input)
                """;
        var logo = """
                                  _  _                       __ _   _  __             \s
                                 | || |___ _ __  __ _ _ ___ / _| |_| |/ /__ _ _ _ _ __\s
                                 | __ / _ \\ '_ \\/ _| '_/ _ \\  _|  _| ' </ _` | '_| '_ \\
                                 |_||_\\___/ .__/\\__|_| \\___/_|  \\__|_|\\_\\__,_|_| | .__/
                                          |_|                                    |_|  \s   """;
        System.out.println(logo);
        System.out.println(aboutText);
        System.out.println(commandsText);
        try (var scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                var cmd = scanner.nextLine();

                var splits = cmd.split(" ",2);

                if(cmd.contains(".")) {
                    System.out.println("Please do not use file extention the only allowed one are (.gr, .sol)");
                } else {
                    switch (splits[0]) {
                        case "--help","-h" -> {
                            System.out.println(commandsText);
                        }
                        case "--compute", "-c" -> {
                            // Test if the user is giving a real file
                            if (isTheFileReal(splits))  {
                                System.out.println("Pleas provide a real file");
                                continue;
                            }
                            this.compute(splits[1]);
                        }
                        case "--genSol","-gs" -> {
                            this.toSol(splits[1]);
                        }
                        case "--clean","-cl" -> {
                            System.out.print("\033[H\033[2J");
                            System.out.flush();
                        }
                    }
                }
            }
        } catch (IOException ioException) {

            logger.severe(ioException.getMessage());
        }
        logger.info("Console stopped");
    }

    private boolean isTheFileReal(String[] splits) throws IOException {
        try(var s = Files.find(Path.of("./"+dir),1,(path, basicFileAttributes) -> {
            if (String.valueOf(path).equals("./"+dir+splits[1]+".gr")) {
                return true;
            }
            return false;
        })) {
           if(s.findAny().isEmpty()) {
               return true;
           }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        var h = new HopcroftKarp();
        h.consoleRun();
    }
}
