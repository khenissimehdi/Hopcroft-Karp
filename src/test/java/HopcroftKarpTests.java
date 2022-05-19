import Algo.HopcroftKarp;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HopcroftKarpTests {
    @Test
    public void CustomGraphOne() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("csGr");
        assertEquals(res.size(), 4);
    }

    @Test
    public void CustomGraphTwo() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("csGr2");
        assertEquals(res.size(), 4);
    }

    @Test
    public void CustomGraphThree() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("csGr3");
        assertEquals(res.size(), 7);
    }

    @Test
    public void CustomGraphFoor() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("csGr4");
        assertEquals(res.size(), 70);
    }

    @Test
    public void graphOneTest() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("g1");
        assertEquals(res.size(), 7);
    }

    @Test
    public void graphTwoTest() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("g2");
        assertEquals(res.size(), 4);
    }

    @Test
    public void graphThreeTest() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("g3");
        assertEquals(res.size(), 9);
    }

    @Test
    public void graphFourTest() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("g4");
        assertEquals(res.size(), 8);
    }

    @Test
    public void graphFiveTest() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("g5");
        assertEquals(res.size(), 75);
    }

    @Test
    public void graphSixTest() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("g6");
        assertEquals(res.size(), 74);
    }

    @Test
    public void graphSevenTest() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("g7");
        assertEquals(res.size(), 838);
    }

    @Test
    public void graphEightTest() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("g8");
        assertEquals(res.size(), 831);
    }

    @Test
    public void graphNineTest() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("g9");
        assertEquals(res.size(), 985);
    }

    @Test
    public void graphTenTest() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("g10");
        assertEquals(res.size(), 979);
    }

    @Test
    public void graphElevenTest() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("g11");
        assertEquals(res.size(), 1000);
    }

    @Test
    public void graphTwelveTest() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("g12");
        assertEquals(res.size(), 1000);
    }


    // These test are really long to do
   /* @Test
    public void graphThirteenTest() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("g13");
        assertEquals(res.size(), 9264);
    }*/

   /* @Test
    public void graphFourteenTest() throws IOException {
        var algo = new HopcroftKarp();
        var res = algo.compute("g14");
        assertEquals(res.size(), 9928);
    }*/

}
