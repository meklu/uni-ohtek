
import org.junit.Test;
import static org.junit.Assert.*;
import org.meklu.patkis.domain.Pair;

public class PairTest {
    @Test
    public void pairsAreEqualWithThemselves() {
        Pair<String, String> a = new Pair<>("foo", "bar");
        assertTrue(a.equals(a));
    }

    @Test
    public void pairsArentNull() {
        Pair<String, String> a = new Pair<>("foo", "bar");
        assertFalse(a.equals(null));
    }

    @Test
    public void pairsArentEqualWithOtherDatatypes() {
        Pair<String, String> a = new Pair<>("foo", "bar");
        assertFalse(a.equals("(foo,bar)"));
    }

    @Test
    public void equalPairsAreEqual() {
        Pair<String, String> a = new Pair<>("foo", "bar");
        Pair<String, String> b = new Pair<>("foo", "bar");
        assertTrue(a.equals(b));
    }

    @Test
    public void inEqualPairsArentEqual() {
        Pair<String, String> a = new Pair<>("bar", "foo");
        Pair<String, String> b = new Pair<>("foo", "bar");
        assertFalse(a.equals(b));
    }

    @Test
    public void inEqualPairsArentEqualPartDeux() {
        Pair<String, String> a = new Pair<>("foo", "foo");
        Pair<String, String> b = new Pair<>("foo", "bar");
        assertFalse(a.equals(b));
    }

    @Test
    public void propsCanBeSet() {
        Pair<String, String> a = new Pair<>("bar", "foo");
        Pair<String, String> b = new Pair<>("foo", "bar");
        b.setA(a.getA());
        b.setB(a.getB());
        assertTrue(a.equals(b));
    }

    @Test
    public void stringRepresentationWorks() {
        Pair<Integer, Integer> p = new Pair<>(1, -1);
        assertTrue("(1,-1)".equals(p.toString()));
    }
}
