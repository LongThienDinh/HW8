import java.util.Map;

public class VendingMachine {
    private StateOfVendingMachine currentState;
    private Map<String, Snack> availableSnacks;
    private Snack currentSnackSelection;
    private double currentMoneyInserted;
    private SnackDispenseHandler snackDispatchHandler;

    public VendingMachine(Map<String, Snack> snacks, SnackDispenseHandler snackDispenser) {
        this.availableSnacks = snacks;
        this.currentState = new IdleState();
        this.snackDispatchHandler = snackDispenser;
    }

    public StateOfVendingMachine getState() {
        return currentState;
    }

    public void setState(StateOfVendingMachine newState) {
        this.currentState = newState;
    }

    public void selectSnack(String snackName) {
        currentState.selectSnack(this, snackName);
    }

    public Snack getSelectedSnack() {
        return currentSnackSelection;
    }

    public void setSelectedSnack(Snack newSelection) {
        this.currentSnackSelection = newSelection;
    }

    public void insertMoney(double amount) {
        currentState.insertMoney(this, amount);
    }

    public double getInsertedMoney() {
        return currentMoneyInserted;
    }

    public void setInsertedMoney(double newAmount) {
        this.currentMoneyInserted = newAmount;
    }

    public void returnMoney() {
        System.out.println("Refunding $" + currentMoneyInserted + " to the customer.");
        this.currentMoneyInserted = 0;
    }

    public void dispenseSnack() {
        if (currentState != null) {
            currentState.dispenseSnack(this);
        } else {
            System.out.println("System error: State is not initialized.");
        }
    }

    public SnackDispenseHandler getSnackDispenser() {
        return snackDispatchHandler;
    }

    public Map<String, Snack> getSnacks() {
        return availableSnacks;
    }
}
