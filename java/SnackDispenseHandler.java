public abstract class SnackDispenseHandler {
    protected SnackDispenseHandler next_handler;
    public void handleRequest(VendingMachine machine, String snackName, double moneyInserted) {
        if (next_handler != null) {
            next_handler.handleRequest(machine, snackName, moneyInserted);
        }
    }
    public SnackDispenseHandler(SnackDispenseHandler nextHandler) {
        this.next_handler = nextHandler;
    }

}
