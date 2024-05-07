import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class VendingMachineTest {
    private VendingMachine machine;
    private Map<String, Snack> snacks;
    private MockSnackDispenseHandler dispenseHandler;

    @Before
    public void setUp() {
        snacks = new HashMap<>();
        snacks.put("Snickers", new Snack("Snickers", 1.50, 10));
        dispenseHandler = new MockSnackDispenseHandler();
        machine = new VendingMachine(snacks, dispenseHandler);
    }

    @Test
    public void testInsertMoney() {
        machine.insertMoney(1.00);
        assertEquals("0.00 should be inserted and remain until explicitly changed", 0.00, machine.getInsertedMoney(), 0.0);
    }

    @Test
    public void testReturnMoney() {
        machine.insertMoney(1.00);
        machine.returnMoney();
        assertEquals("All money should be returned, setting inserted money to 0.0", 0.0, machine.getInsertedMoney(), 0.0);
    }

    @Test
    public void testDispenseSnackWithSufficientFunds() {
        machine.selectSnack("Snickers");
        machine.insertMoney(1.50);
        machine.dispenseSnack();
        assertEquals("Quantity of Snickers should decrease by 1 after dispensing", 9, snacks.get("Snickers").getQuantity());
        assertEquals("All money should be used for the transaction, setting inserted money to 0.0 after dispensing", 0.0, machine.getInsertedMoney(), 0.0);
    }

    @Test
    public void testDispenseSnackWithInsufficientFunds() {
        machine.selectSnack("Snickers");
        machine.insertMoney(1.00);
        machine.dispenseSnack();
        assertEquals("Insufficient funds should not allow snack dispensing, quantity should remain unchanged", 10, snacks.get("Snickers").getQuantity());
        assertEquals("Inserted money should remain unchanged as snack was not dispensed", 1.00, machine.getInsertedMoney(), 0.0);
    }

    private class MockSnackDispenseHandler extends SnackDispenseHandler {
        public MockSnackDispenseHandler() {
            super(null);
        }

        @Override
        public void handleRequest(VendingMachine machine, String snackName, double moneyInserted) {
            Snack snack = machine.getSnacks().get(snackName);
            if (moneyInserted >= snack.getPrice()) {
                snack.setQuantity(snack.getQuantity() - 1);
                machine.setInsertedMoney(0);
            }
        }
    }
}
