import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class DoritosHandlerTest {

    private VendingMachine machine;
    private Map<String, Snack> snacks;
    private DoritosHandler doritosHandler;
    private IdleState idleState;
    private MockSnackDispenseHandler mockHandler;

    @Before
    public void setUp() {
        snacks = new HashMap<>();
        snacks.put("Doritos", new Snack("Doritos", 1.25, 15));
        idleState = new IdleState();
        mockHandler = new MockSnackDispenseHandler();
        doritosHandler = new DoritosHandler(mockHandler);
        machine = new VendingMachine(snacks, doritosHandler);
    }

    @Test
    public void testHandleRequestWithDoritosSufficientFunds() {
        machine.selectSnack("Doritos");
        machine.insertMoney(1.25);
        doritosHandler.handleRequest(machine, "Doritos", 1.25);
        assertEquals("Doritos should be dispensed and quantity should decrease by 1", 14, snacks.get("Doritos").getQuantity());
        assertEquals("Machine should return to IdleState with zero inserted money", 0, machine.getInsertedMoney(), 0.0);
    }

    @Test
    public void testHandleRequestWithDoritosInsufficientFunds() {
        machine.selectSnack("Doritos");
        machine.insertMoney(1.00);
        doritosHandler.handleRequest(machine, "Doritos", 1.00);
        assertTrue("Machine should stay in a waiting state for more money", machine.getState() instanceof WaitingForMoneyState);
    }

    @Test
    public void testHandleRequestWithDoritosOutOfStock() {
        snacks.put("Doritos", new Snack("Doritos", 1.25, 0));
        machine.selectSnack("Doritos");
        machine.insertMoney(1.25);
        doritosHandler.handleRequest(machine, "Doritos", 1.25);
        assertEquals("Machine should return to IdleState and return inserted money", IdleState.class, machine.getState().getClass());
        assertEquals("Inserted money should be returned", 0, machine.getInsertedMoney(), 0.0);
    }

    @Test
    public void testNoActionTakenForNonDoritosItem() {
        machine.selectSnack("Lays");
        machine.insertMoney(1.00);
        doritosHandler.handleRequest(machine, "Lays", 1.00);
        assertTrue("Next handler should be called when the snack is not Doritos", mockHandler.isCalled());
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
