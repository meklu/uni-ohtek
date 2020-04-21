
import org.junit.Test;
import static org.junit.Assert.*;
import org.meklu.patkis.domain.Triple;

public class TripleTest {
    @Test
    public void triplesAreEqualWithThemselves() {
        Triple<String, String, String> a = new Triple<>("foo", "bar", "zot");
        assertTrue(a.equals(a));
    }

    @Test
    public void triplesArentNull() {
        Triple<String, String, String> a = new Triple<>("foo", "bar", "zot");
        assertFalse(a.equals(null));
    }

    @Test
    public void triplesArentEqualWithOtherDatatypes() {
        Triple<String, String, String> a = new Triple<>("foo", "bar", "zot");
        assertFalse(a.equals("(foo,bar)"));
    }

    @Test
    public void equalTriplesAreEqual() {
        Triple<String, String, String> a = new Triple<>("foo", "bar", "zot");
        Triple<String, String, String> b = new Triple<>("foo", "bar", "zot");
        assertTrue(a.equals(b));
    }

    @Test
    public void inEqualTriplesArentEqual() {
        Triple<String, String, String> a = new Triple<>("bar", "foo", "zot");
        Triple<String, String, String> b = new Triple<>("foo", "bar", "zot");
        assertFalse(a.equals(b));
    }

    @Test
    public void inEqualTriplesArentEqualPartDeux() {
        Triple<String, String, String> a = new Triple<>("foo", "foo", "zot");
        Triple<String, String, String> b = new Triple<>("foo", "bar", "zork");
        assertFalse(a.equals(b));
    }

    @Test
    public void inEqualTriplesArentEqualPartTroika() {
        Triple<String, String, String> a = new Triple<>("foo", "foo", "zot");
        Triple<String, String, String> b = new Triple<>("foo", "bar", "zot");
        assertFalse(a.equals(b));
    }

    @Test
    public void propsCanBeSet() {
        Triple<String, String, String> a = new Triple<>("bar", "foo", "zot");
        Triple<String, String, String> b = new Triple<>("foo", "bar", "zork");
        b.setA(a.getA());
        b.setB(a.getB());
        b.setC(a.getC());
        assertTrue(a.equals(b));
    }

    @Test
    public void stringRepresentationWorks() {
        Triple<Integer, Integer, Integer> p = new Triple<>(1, 0, -1);
        assertTrue("(1,0,-1)".equals(p.toString()));
    }
}
