public class DoritosHandler extends SnackDispenseHandler {
    public DoritosHandler(SnackDispenseHandler successor) {
        super(successor);
    }

    @Override
    public void handleRequest(VendingMachine vendingMachine, String itemName, double amountInserted) {
        if (itemName.equalsIgnoreCase("Doritos")) {
            Snack item = vendingMachine.getSnacks().get(itemName);
            if (item != null) {
                if (amountInserted >= item.getPrice()) {
                    if (item.getQuantity() > 0) {
                        item.reduceQuantity();
                        System.out.println("Dispensing Doritos");
                        vendingMachine.setInsertedMoney(0);
                        vendingMachine.setSelectedSnack(null);
                        vendingMachine.setState(new IdleState());
                        System.out.println("Your Doritos are ready. Thank you for your purchase!");
                    } else {
                        System.out.println("Doritos are currently unavailable.");
                        vendingMachine.returnMoney();
                        vendingMachine.setState(new IdleState());
                    }
                } else {
                    System.out.println("Additional funds required. Please add $" + (item.getPrice() - amountInserted) + ".");
                }
            } else {
                super.handleRequest(vendingMachine, itemName, amountInserted);
            }
        } else {
            super.handleRequest(vendingMachine, itemName, amountInserted);
        }
    }
}
