import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class IdleStateTest {

    private VendingMachine machine;
    private IdleState idleState;

    @Before
    public void setUp() {
        Map<String, Snack> snacks = new HashMap<>();
        snacks.put("Coke", new Snack("Coke", 1.25, 10));
        machine = new VendingMachine(snacks, null);
        idleState = new IdleState();
        machine.setState(idleState);
    }

    @Test
    public void testSelectValidSnack() {
        assertNull("Before selecting, no snack should be currently selected", machine.getSelectedSnack());
        idleState.selectSnack(machine, "Coke");
        assertNotNull("After selecting a valid snack, a snack object should be assigned", machine.getSelectedSnack());
        assertEquals("The name of the selected snack should match the valid input provided", "Coke", machine.getSelectedSnack().getName());
        assertEquals("After selecting a valid snack, the state should change to WaitingForMoneyState", WaitingForMoneyState.class, machine.getState().getClass());
    }

    @Test
    public void testSelectInvalidSnack() {
        idleState.selectSnack(machine, "InvalidSnack");
        assertNull("No snack should be selected on invalid input", machine.getSelectedSnack());
        assertEquals("Machine should remain in IdleState on invalid selection", IdleState.class, machine.getState().getClass());
        assertEquals("No money should be inserted", 0, machine.getInsertedMoney(), 0.01);
    }

    @Test
    public void testInsertMoneyDoesNothing() {
        double initialMoney = machine.getInsertedMoney();
        idleState.insertMoney(machine, 1.00);
        assertEquals("Inserting money should not change the inserted amount", initialMoney, machine.getInsertedMoney(), 0.01);
    }

    @Test
    public void testDispenseSnackDoesNothing() {
        Snack snack = new Snack("Coke", 1.25, 10);
        machine.setSelectedSnack(snack);
        machine.setInsertedMoney(1.25);
        idleState.dispenseSnack(machine);
        assertEquals("Dispensing snack should not change the selected snack", snack, machine.getSelectedSnack());
        assertEquals("Dispensing snack should not change the inserted money", 1.25, machine.getInsertedMoney(), 0.01);
        assertEquals("Machine should remain in IdleState", IdleState.class, machine.getState().getClass());
    }
}
