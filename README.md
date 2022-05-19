# Hopcroft-Karp
The Hopcroft-Karp algorithm is an algorithm that takes as input a bipartite graph and produces as output a 
coupling of maximum cardinality,i.e.a set of edges as large as possible with  the property that two edges never share an end.


## Run the program
To run the code I provided a jar "HopJar.jar"
``
java -jar HopJar.jar
``
make sure to run the jar where the testData directory is available otherwise the code will not find where to searcch
when you run jar you will get this screen
```
| || |___ _ __  __ _ _ ___ / _| |_| |/ /__ _ _ _ _ __
| __ / _ \ '_ \/ _| '_/ _ \  _|  _| ' </ _` | '_| '_ \
|_||_\___/ .__/\__|_| \___/_|  \__|_|\_\__,_|_| | .__/
         |_|                                    |_|
This tool will help you compute HopcroftKarp algorithm.

--help,-h -> see help.
--compute <fileName> -c <fileName> -> compute result.
--genSol <fileName>, -gs <fileName> -> generate a file with the solution.
--clean, -cl -> clean the console
Note :
Please when giving a file name don't provide the extension for security,
reasons we only allow .sol(output) .gr(input)
```
### Run test 
To run test fire your IDE and launch them from the test file "/src/test/java/HopcroftKarpTests.java"


