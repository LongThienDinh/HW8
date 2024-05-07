import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class DispensingSnackStateTest {

    private VendingMachine machine;
    private DispensingSnackState state;
    private Map<String, Snack> snacks;
    private SnackDispenseHandler testHandler;

    @Before
    public void setUp() {
        snacks = new HashMap<>();
        snacks.put("Doritos", new Snack("Doritos", 1.25, 10));
        testHandler = createTestHandler();
        machine = new VendingMachine(snacks, testHandler);
        state = new DispensingSnackState();
        machine.setState(state);
    }

    private SnackDispenseHandler createTestHandler() {
        return new SnackDispenseHandler(null) {
            @Override
            public void handleRequest(VendingMachine machine, String snackName, double moneyInserted) {
                System.out.println("Processing request for " + snackName);
                Snack snack = machine.getSnacks().get(snackName);
                if (snack != null && snack.getQuantity() > 0) {
                    snack.reduceQuantity();
                    machine.setInsertedMoney(0);
                    machine.setSelectedSnack(null);
                }
                System.out.println("Thank you for your purchase!");
                machine.setState(new IdleState());
            }
        };
    }

    @Test
    public void testDispenseSnackWithSelectedSnack() {
        machine.setSelectedSnack(new Snack("Doritos", 1.25, 10));
        machine.setInsertedMoney(1.25);
        state.dispenseSnack(machine);
        assertEquals("Doritos quantity should be reduced to 9", 9, snacks.get("Doritos").getQuantity());  // Reflecting the change
        assertTrue("Machine should transition to IdleState after dispensing", machine.getState() instanceof IdleState);
    }

    @Test
    public void testDispenseSnackWithNoSnackSelected() {
        machine.setSelectedSnack(null);
        state.dispenseSnack(machine);
        assertTrue("Machine should transition to IdleState if no snack is selected", machine.getState() instanceof IdleState);
    }
}
