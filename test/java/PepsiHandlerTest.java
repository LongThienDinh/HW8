import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class PepsiHandlerTest {

    private VendingMachine machine;
    private Map<String, Snack> snacks;
    private PepsiHandler pepsiHandler;
    private IdleState idleState;
    private MockSnackDispenseHandler mockHandler;

    @Before
    public void setUp() {
        snacks = new HashMap<>();
        snacks.put("Pepsi", new Snack("Pepsi", 1.25, 15));
        idleState = new IdleState();
        mockHandler = new MockSnackDispenseHandler();
        pepsiHandler = new PepsiHandler(mockHandler);
        machine = new VendingMachine(snacks, pepsiHandler);
    }

    @Test
    public void testHandleRequestWithPepsiSufficientFunds() {
        machine.selectSnack("Pepsi");
        machine.insertMoney(1.25);
        pepsiHandler.handleRequest(machine, "Pepsi", 1.25);
        assertEquals("Pepsi should be dispensed and quantity should decrease by 1", 14, snacks.get("Pepsi").getQuantity());
        assertEquals("Machine should return to IdleState with zero inserted money", 0, machine.getInsertedMoney(), 0.0);
    }

    @Test
    public void testHandleRequestWithPepsiInsufficientFunds() {
        machine.selectSnack("Pepsi");
        machine.insertMoney(1.00);
        pepsiHandler.handleRequest(machine, "Pepsi", 1.00);
        assertTrue("Machine should stay in a waiting state for more money", machine.getState() instanceof WaitingForMoneyState);
    }

    @Test
    public void testHandleRequestWithPepsiOutOfStock() {
        snacks.put("Pepsi", new Snack("Pepsi", 1.25, 0));
        machine.selectSnack("Pepsi");
        machine.insertMoney(1.25);
        pepsiHandler.handleRequest(machine, "Pepsi", 1.25);
        assertEquals("Machine should return to IdleState and return inserted money", IdleState.class, machine.getState().getClass());
        assertEquals("Inserted money should be returned", 0, machine.getInsertedMoney(), 0.0);
    }

    @Test
    public void testNoActionTakenForNonPepsiItem() {
        machine.selectSnack("Coca-Cola");
        machine.insertMoney(1.00);
        pepsiHandler.handleRequest(machine, "Coca-Cola", 1.00);
        assertTrue("Next handler should be called when the snack is not Pepsi", mockHandler.isCalled());
    }

    private class MockSnackDispenseHandler extends SnackDispenseHandler {
        private boolean called = false;

        public MockSnackDispenseHandler() {
            super(null);
        }

        @Override
        public void handleRequest(VendingMachine vendingMachine, String itemName, double amountInserted) {
            called = true;
        }

        public boolean isCalled() {
            return called;
        }
    }
}
