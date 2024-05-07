public class WaitingForMoneyState implements StateOfVendingMachine {
    @Override
    public void dispenseSnack(VendingMachine vendingMachine) {}
    @Override
    public void selectSnack(VendingMachine vendingMachine, String itemName) {}

    @Override
    public void insertMoney(VendingMachine vendingMachine, double amountInserted) {
        Snack currentSnack = vendingMachine.getSelectedSnack();
        double cumulativeAmount = vendingMachine.getInsertedMoney() + amountInserted;
        vendingMachine.setInsertedMoney(cumulativeAmount);

        if (currentSnack == null) {
            System.out.println("Selection error: No snack chosen.");
            return;
        }

        if (cumulativeAmount < currentSnack.getPrice()) {
            System.out.println("Additional amount required: Please add $" +
                    String.format("%.2f", (currentSnack.getPrice() - cumulativeAmount)) + " to complete the purchase of " + currentSnack.getName());
        } else {
            if (currentSnack.getQuantity() > 0) {
                System.out.println("Payment complete. Preparing to dispense " + currentSnack.getName());
                vendingMachine.setState(new DispensingSnackState());
            } else {
                System.out.println("Out of stock: " + currentSnack.getName() + " is not available.");
                vendingMachine.returnMoney();
                vendingMachine.setState(new IdleState());
            }
        }
    }
}
