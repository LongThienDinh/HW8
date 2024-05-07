import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class SnickersHandlerTest {

    private VendingMachine machine;
    private Map<String, Snack> snacks;
    private SnickersHandler snickersHandler;
    private IdleState idleState;
    private MockSnackDispenseHandler mockHandler;

    @Before
    public void setUp() {
        snacks = new HashMap<>();
        snacks.put("Snickers", new Snack("Snickers", 1.25, 15));
        idleState = new IdleState();
        mockHandler = new MockSnackDispenseHandler();
        snickersHandler = new SnickersHandler(mockHandler);
        machine = new VendingMachine(snacks, snickersHandler);
    }

    @Test
    public void testHandleRequestWithSnickersSufficientFunds() {
        machine.selectSnack("Snickers");
        machine.insertMoney(1.25);
        snickersHandler.handleRequest(machine, "Snickers", 1.25);
        assertEquals("Snickers should be dispensed and quantity should decrease by 1", 14, snacks.get("Snickers").getQuantity());
        assertEquals("Machine should return to IdleState with zero inserted money", 0, machine.getInsertedMoney(), 0.0);
    }

    @Test
    public void testHandleRequestWithSnickersInsufficientFunds() {
        machine.selectSnack("Snickers");
        machine.insertMoney(1.00);
        snickersHandler.handleRequest(machine, "Snickers", 1.00);
        assertTrue("Machine should stay in a waiting state for more money", machine.getState() instanceof WaitingForMoneyState);
    }

    @Test
    public void testHandleRequestWithSnickersOutOfStock() {
        snacks.put("Snickers", new Snack("Snickers", 1.25, 0));
        machine.selectSnack("Snickers");
        machine.insertMoney(1.25);
        snickersHandler.handleRequest(machine, "Snickers", 1.25);
        assertEquals("Machine should return to IdleState and return inserted money", IdleState.class, machine.getState().getClass());
        assertEquals("Inserted money should be returned", 0, machine.getInsertedMoney(), 0.0);
    }

    @Test
    public void testNoActionTakenForNonSnickersItem() {
        machine.selectSnack("Mars");
        machine.insertMoney(1.00);
        snickersHandler.handleRequest(machine, "Mars", 1.00);
        assertTrue("Next handler should be called when the snack is not Snickers", mockHandler.isCalled());
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
