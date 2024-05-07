import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class CokeHandlerTest {

    private VendingMachine machine;
    private Map<String, Snack> snacks;
    private CokeHandler cokeHandler;
    private IdleState idleState;
    private MockSnackDispenseHandler mockHandler;

    @Before
    public void setUp() {
        snacks = new HashMap<>();
        snacks.put("Coke", new Snack("Coke", 1.25, 15));
        idleState = new IdleState();
        mockHandler = new MockSnackDispenseHandler();
        cokeHandler = new CokeHandler(mockHandler);
        machine = new VendingMachine(snacks, cokeHandler);
    }

    @Test
    public void testHandleRequestWithCokeSufficientFunds() {
        machine.selectSnack("Coke");
        machine.insertMoney(1.25);
        cokeHandler.handleRequest(machine, "Coke", 1.25);
        assertEquals("Coke should be dispensed and quantity should decrease by 1", 14, snacks.get("Coke").getQuantity());
        assertEquals("Machine should return to IdleState with zero inserted money", 0, machine.getInsertedMoney(), 0.0);
    }

    @Test
    public void testHandleRequestWithCokeInsufficientFunds() {
        machine.selectSnack("Coke");
        machine.insertMoney(1.00);
        cokeHandler.handleRequest(machine, "Coke", 1.00);
        assertTrue("Machine should stay in a waiting state for more money", machine.getState() instanceof WaitingForMoneyState);
    }

    @Test
    public void testHandleRequestWithCokeOutOfStock() {
        snacks.put("Coke", new Snack("Coke", 1.25, 0));
        machine.selectSnack("Coke");
        machine.insertMoney(1.25);
        cokeHandler.handleRequest(machine, "Coke", 1.25);
        assertEquals("Machine should return to IdleState and return inserted money", IdleState.class, machine.getState().getClass());
        assertEquals("Inserted money should be returned", 0, machine.getInsertedMoney(), 0.0);
    }

    @Test
    public void testNoActionTakenForNonCokeItem() {
        machine.selectSnack("Pepsi");
        machine.insertMoney(1.00);
        cokeHandler.handleRequest(machine, "Pepsi", 1.00);
        assertTrue("Next handler should be called when the snack is not Coke", mockHandler.isCalled());
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
