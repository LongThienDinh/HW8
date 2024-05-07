public class IdleState implements StateOfVendingMachine {

    @Override
    public void insertMoney(VendingMachine vendingMachine, double amountInserted) {}

    @Override
    public void dispenseSnack(VendingMachine vendingMachine) {}

    @Override
    public void selectSnack(VendingMachine vendingMachine, String itemName) {
        Snack item = vendingMachine.getSnacks().get(itemName);
        if (item != null) {
            vendingMachine.setSelectedSnack(item);
            vendingMachine.setState(new WaitingForMoneyState());
            System.out.println("You have selected: " + itemName + ". Please insert $" + item.getPrice());
        } else {
            System.out.println("Selection invalid. Please choose a valid snack.");
            if (vendingMachine.getInsertedMoney() > 0) {
                vendingMachine.returnMoney();
            }
        }
    }

}
