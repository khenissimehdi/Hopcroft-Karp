import Algo.HopcroftKarp;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HopcroftKarpTests {
    private final String dir = "testData/";
    @Test
    public void graphOneTest() throws IOException {

        var algo = new HopcroftKarp(Path.of(dir+"g1.gr"));
        var res = algo.compute();
        assertEquals(res.size(), 7);
    }

    @Test
    public void graphTwoTest() throws IOException {
        var algo = new HopcroftKarp(Path.of(dir+"g2.gr"));
        var res = algo.compute();
        assertEquals(res.size(), 4);
    }

    @Test
    public void graphThreeTest() throws IOException {
        var algo = new HopcroftKarp(Path.of(dir+"g3.gr"));
        var res = algo.compute();
        assertEquals(res.size(), 9);
    }

    @Test
    public void graphFourTest() throws IOException {
        var algo = new HopcroftKarp(Path.of(dir+"g4.gr"));
        var res = algo.compute();
        assertEquals(res.size(), 8);
    }

    @Test
    public void graphFiveTest() throws IOException {
        var algo = new HopcroftKarp(Path.of(dir+"g5.gr"));
        var res = algo.compute();
        assertEquals(res.size(), 75);
    }

    @Test
    public void graphSixTest() throws IOException {
        var algo = new HopcroftKarp(Path.of(dir+"g6.gr"));
        var res = algo.compute();
        assertEquals(res.size(), 74);
    }

    @Test
    public void graphSevenTest() throws IOException {
        var algo = new HopcroftKarp(Path.of(dir+"g7.gr"));
        var res = algo.compute();
        assertEquals(res.size(), 838);
    }

    @Test
    public void graphEightTest() throws IOException {
        var algo = new HopcroftKarp(Path.of(dir+"g8.gr"));
        var res = algo.compute();
        assertEquals(res.size(), 831);
    }

    @Test
    public void graphNineTest() throws IOException {
        var algo = new HopcroftKarp(Path.of(dir+"g9.gr"));
        var res = algo.compute();
        assertEquals(res.size(), 985);
    }

    @Test
    public void graphTenTest() throws IOException {
        var algo = new HopcroftKarp(Path.of(dir+"g10.gr"));
        var res = algo.compute();
        assertEquals(res.size(), 979);
    }

    @Test
    public void graphElevenTest() throws IOException {
        var algo = new HopcroftKarp(Path.of(dir+"g11.gr"));
        var res = algo.compute();
        assertEquals(res.size(), 1000);
    }

    @Test
    public void graphTwelveTest() throws IOException {
        var algo = new HopcroftKarp(Path.of(dir+"g12.gr"));
        var res = algo.compute();
        assertEquals(res.size(), 1000);
    }


    // These test are really long to do
   /* @Test
    public void graphThirteenTest() throws IOException {
        var algo = new HopcroftKarp(Path.of(dir+"g13.gr"));
        var res = algo.compute();
        assertEquals(res.size(), 9264);
    }*/

   /* @Test
    public void graphFourteenTest() throws IOException {
        var algo = new HopcroftKarp(Path.of(dir+"g14.gr"));
        var res = algo.compute();
        assertEquals(res.size(), 9928);
    }*/

}
