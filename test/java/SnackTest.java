import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SnackTest {
    private Snack snack;

    @Before
    public void setUp() {
        snack = new Snack("Snickers", 1.25, 20);
    }

    @Test
    public void testGetName() {
        assertEquals("Snickers", snack.getName());
    }

    @Test
    public void testGetPrice() {
        assertEquals(1.25, snack.getPrice(), 0.0);
    }

    @Test
    public void testGetQuantity() {
        assertEquals(20, snack.getQuantity());
    }

    @Test
    public void testSetName() {
        snack.setName("Mars");
        assertEquals("Mars", snack.getName());
    }

    @Test
    public void testSetPrice() {
        snack.setPrice(1.50);
        assertEquals(1.50, snack.getPrice(), 0.0);
    }

    @Test
    public void testSetQuantity() {
        snack.setQuantity(25);
        assertEquals(25, snack.getQuantity());
    }

    @Test
    public void testReduceQuantity() {
        snack.reduceQuantity();
        assertEquals(19, snack.getQuantity());
    }

    @Test
    public void testReduceQuantityAtZero() {
        snack.setQuantity(0);
        snack.reduceQuantity();
        assertEquals(0, snack.getQuantity());
    }
}
