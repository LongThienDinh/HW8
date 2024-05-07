import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class CheetosHandlerTest {

    private VendingMachine machine;
    private Map<String, Snack> snacks;
    private CheetosHandler cheetosHandler;
    private IdleState idleState;

    @Before
    public void setUp() {
        snacks = new HashMap<>();
        snacks.put("Cheetos", new Snack("Cheetos", 1.75, 5));
        idleState = new IdleState();
        cheetosHandler = new CheetosHandler(null);
        machine = new VendingMachine(snacks, cheetosHandler);
    }

    @Test
    public void testHandleRequestCheetosWithExactFunds() {
        machine.selectSnack("Cheetos");
        machine.insertMoney(1.75);
        cheetosHandler.handleRequest(machine, "Cheetos", 1.75);
        assertEquals("All funds should be consumed, and no money should remain in the machine", 0, machine.getInsertedMoney(), 0.01);
        assertEquals("After dispensing Cheetos with exact funds, the machine should return to the IdleState", IdleState.class, machine.getState().getClass());
        assertEquals("Dispensing with exact funds should reduce Cheetos stock by one", 4, snacks.get("Cheetos").getQuantity());
    }

    @Test
    public void testHandleRequestCheetosWithInsufficientFunds() {
        machine.selectSnack("Cheetos");
        machine.insertMoney(1.00);
        cheetosHandler.handleRequest(machine, "Cheetos", 1.00);
        assertTrue("With insufficient funds, the machine should remain in a state awaiting additional funds", machine.getState() instanceof WaitingForMoneyState);
        assertEquals("The machine should retain the inserted amount when funds are insufficient", 1.00, machine.getInsertedMoney(), 0.01);
    }

    @Test
    public void testHandleRequestCheetosWhenOutOfStock() {
        snacks.put("Cheetos", new Snack("Cheetos", 1.75, 0));
        machine.selectSnack("Cheetos");
        machine.insertMoney(1.75);
        cheetosHandler.handleRequest(machine, "Cheetos", 1.75);
        assertEquals("When Cheetos are out of stock, the machine should automatically revert to the IdleState", IdleState.class, machine.getState().getClass());
        assertEquals("If Cheetos are out of stock, the machine should refund any money inserted", 0, machine.getInsertedMoney(), 0.01);
    }

    @Test
    public void testHandleRequestForNonCheetosItem() {
        machine.selectSnack("Lays");
        machine.insertMoney(1.25);
        cheetosHandler.handleRequest(machine, "Lays", 1.25);
        assertNotNull("When handling a non-Cheetos snack, the snack should remain selected and ready for another handler", machine.getSelectedSnack());
        assertEquals("Handling a non-Cheetos snack like Lays should not alter the selected snack", "Lays", machine.getSelectedSnack().getName());
    }
}
