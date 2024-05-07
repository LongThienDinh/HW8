import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class WaitingForMoneyStateTest {

    private VendingMachine machine;
    private Map<String, Snack> snacks;

    @Before
    public void setUp() {
        snacks = new HashMap<>();
        snacks.put("Cheetos", new Snack("Cheetos", 1.75, 10));
        snacks.put("Pepsi", new Snack("Pepsi", 2.00, 5));
        machine = new VendingMachine(snacks, null);
    }

    @Test
    public void testInsertInsufficientMoneyForCheetos() {
        machine.selectSnack("Cheetos");
        machine.insertMoney(1.50);
        assertEquals("Money inserted should be recorded even if insufficient", 1.50, machine.getInsertedMoney(), 0.01);
        assertTrue("State should remain WaitingForMoneyState with insufficient funds for Cheetos", machine.getState() instanceof WaitingForMoneyState);
    }

    @Test
    public void testInsertExactAmountForPepsi() {
        machine.selectSnack("Pepsi");
        machine.setState(new WaitingForMoneyState());
        machine.insertMoney(2.00);
        assertEquals("Machine should record the exact amount needed for Pepsi", 2.00, machine.getInsertedMoney(), 0.01);
        assertTrue("Machine should transition to DispensingSnackState after exact payment for Pepsi", machine.getState() instanceof DispensingSnackState);
    }

    @Test
    public void testInsertMoneyWhenPepsiIsOutOfStock() {
        machine.selectSnack("Pepsi");
        snacks.get("Pepsi").setQuantity(0);
        machine.insertMoney(2.00);
        assertEquals("All money should be returned if Pepsi is out of stock", 0, machine.getInsertedMoney(), 0.01);
        assertTrue("Machine should revert to IdleState when Pepsi is out of stock", machine.getState() instanceof IdleState);
    }
}
