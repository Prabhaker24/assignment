package wiki;
import java.util.Arrays;
//import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import wiki.WikiWalker;

public class WikiWalkerTests {
    // =================================================
    // Test Configuration
    // =================================================

    WikiWalker ww,ww1,ww2;

    @Before
    public void init() {
        ww = new WikiWalker();
        ww1= new WikiWalker();
        ww2= new WikiWalker();
    }

    public static void main(String args[]) {

    }


    /**
     * Sets up the structure of the site map for the
     * WikiWalker used in each test
     *
     * @param ww The WikiWalker object to setup with the
     *           given
     */
    private static void setupWW (WikiWalker ww) {
        ww.addArticle("A", Arrays.asList("B", "C"));
        ww.addArticle("B", Arrays.asList("A", "C"));
        ww.addArticle("C", Arrays.asList("D", "E"));
        ww.addArticle("D", Arrays.asList("B", "F"));
        ww.addArticle("E", Arrays.asList("F"));
    }

    private static void setupWW1 (WikiWalker ww1) {
        ww1.addArticle("A", Arrays.asList("B", "C"));
        ww1.addArticle("B", Arrays.asList("A"));

    }

    private static void setupWW2 (WikiWalker ww2) {
        ww2.addArticle("J", Arrays.asList("L","K"));
        ww2.addArticle("K", Arrays.asList("M"));
        ww2.addArticle("L", Arrays.asList("P"));
        ww2.addArticle("M", Arrays.asList("K","N"));
    }



    // =================================================
    // Unit Tests
    // =================================================



    @Test
    public void testHasPath1() {

        setupWW1(ww1);

        assertTrue(ww1.hasPath("A", "A"));
        assertTrue(ww1.hasPath("A", "B"));
        assertTrue(ww1.hasPath("A", "C"));
        assertTrue(ww1.hasPath("B", "A"));
        ww1.print_links();
        System.out.println(ww1.hasPath("B", "C"));
        //assertTrue(ww1.hasPath("B", "C"));
        System.out.println(ww1.hasPath("C", "B"));
        assertFalse(ww1.hasPath("C", "B"));
    }
    @Test
    public void testHasPath2() {
        setupWW2(ww);

        assertTrue(ww.hasPath("J", "M"));
        assertTrue(ww.hasPath("J", "P"));
        ww.print_links();
        ww.hasPath("M", "K");
       System.out.println(ww.hasPath("K", "L"));
        //assertTrue(ww.hasPath("K", "L"));
        assertFalse(ww.hasPath("L", "N"));


    }





    //
    @Test
    public void testAddArticle() {
        setupWW(ww);
        setupWW1(ww1);
        setupWW2(ww2);
    }

    @Test
    public void testHasPath() {
        setupWW(ww);

        assertTrue(ww.hasPath("A", "A"));
        assertTrue(ww.hasPath("A", "B"));
        assertTrue(ww.hasPath("B", "A"));
        assertTrue(ww.hasPath("A", "F"));
        assertTrue(ww.hasPath("E", "F"));
        assertTrue(ww.hasPath("D", "E"));
        assertFalse(ww.hasPath("F", "E"));
        assertFalse(ww.hasPath("E", "D"));
    }

    @Test
    public void testClickthroughs() {
        setupWW(ww);
        assertEquals(0, ww.clickthroughs("A", "B"));
        assertEquals(0, ww.clickthroughs("B", "A"));
        assertEquals(-1, ww.clickthroughs("A", "A"));
        assertEquals(-1, ww.clickthroughs("A", "D"));

        //Additional test cases
        // assertThrows(IllegalArgumentException.class,()->{
        //   ww.clickthroughs("X", "D");
        //});
    }

    @Test
    public void testTrajectories() {
        setupWW(ww);

        ww.logTrajectory(Arrays.asList("A", "B", "C", "D"));
        ww.logTrajectory(Arrays.asList("A", "C", "D", "F"));
        assertEquals(1, ww.clickthroughs("A", "B"));
        assertEquals(1, ww.clickthroughs("A", "C"));
        assertEquals(1, ww.clickthroughs("B", "C"));
        assertEquals(2, ww.clickthroughs("C", "D"));

        ww.logTrajectory(Arrays.asList("A", "B", "A", "B", "C"));
        assertEquals(3, ww.clickthroughs("A", "B"));
        assertEquals(1, ww.clickthroughs("B", "A"));
        assertEquals(2, ww.clickthroughs("B", "C"));

        //Additional test cases
        ww.logTrajectory(Arrays.asList("B", "A", "B", "C", "E"));
        ww.logTrajectory(Arrays.asList("C", "E", "F"));
        assertEquals(-1, ww.clickthroughs("E", "C"));
        assertEquals(4, ww.clickthroughs("A", "B"));


    }


    @Test
    public void testMostLikelyTrajectory() {
        setupWW(ww);
        ww.print_links();
        System.out.println(ww.mostLikelyTrajectory("A", 1));
        assertEquals(Arrays.asList("B"),     ww.mostLikelyTrajectory("A", 1));
        System.out.println(ww.mostLikelyTrajectory("A", 2));
        assertEquals(Arrays.asList("B", "A"), ww.mostLikelyTrajectory("A", 2));
        System.out.println (ww.mostLikelyTrajectory("A", 3));
        assertEquals(Arrays.asList("B", "A", "B"), ww.mostLikelyTrajectory("A", 3));

        ww.logTrajectory(Arrays.asList("A", "B", "C", "D"));
        ww.logTrajectory(Arrays.asList("A", "C", "D", "F"));
        ww.logTrajectory(Arrays.asList("A", "B", "A", "B", "C"));
        System.out.println(ww.mostLikelyTrajectory("A", 1));
        assertEquals(Arrays.asList("B"), ww.mostLikelyTrajectory("A", 1));
        assertEquals(Arrays.asList("B", "C"), ww.mostLikelyTrajectory("A", 2));
        assertEquals(Arrays.asList("B", "C", "D"), ww.mostLikelyTrajectory("A", 3));
        assertEquals(Arrays.asList("B", "C", "D", "F"), ww.mostLikelyTrajectory("A", 4));
        assertEquals(Arrays.asList("B", "C", "D", "F"), ww.mostLikelyTrajectory("A", 5));
        assertEquals(Arrays.asList("B", "C", "D", "F"), ww.mostLikelyTrajectory("A", 100));

        //Additional testcases
        System.out.println((ww.mostLikelyTrajectory("A", 0)));
        assertEquals(Arrays.asList(""), ww.mostLikelyTrajectory("A", 0));
        assertEquals(Arrays.asList(""), ww.mostLikelyTrajectory("F", 1));
        assertEquals(Arrays.asList(""), ww.mostLikelyTrajectory("A", -1));

    }
}