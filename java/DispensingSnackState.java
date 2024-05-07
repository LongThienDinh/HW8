public class DispensingSnackState implements StateOfVendingMachine {

    @Override
    public void selectSnack(VendingMachine vendingMachine, String itemName) {}

    @Override
    public void insertMoney(VendingMachine vendingMachine, double amountInserted) {}

    @Override
    public void dispenseSnack(VendingMachine vendingMachine) {
        if (vendingMachine.getSelectedSnack() != null) {
            vendingMachine.getSnackDispenser().handleRequest(vendingMachine, vendingMachine.getSelectedSnack().getName(), vendingMachine.getInsertedMoney());
        } else {
            System.out.println("No snack has been selected. Please select a snack.");
            vendingMachine.setState(new IdleState());
        }
    }
}
