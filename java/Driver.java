import java.util.HashMap;
import java.util.Map;

public class Driver {
    public static void main(String[] args) {
        Map<String, Snack> snacks = new HashMap<>();
        snacks.put("Coke", new Snack("Coke", 1.10, 8));
        snacks.put("Pepsi", new Snack("Pepsi", 1.10, 8));
        snacks.put("KitKat", new Snack("KitKat", 0.50, 8));
        snacks.put("Doritos", new Snack("Doritos", 1.25, 8));
        snacks.put("Cheetos", new Snack("Cheetos", 1.25, 8));
        snacks.put("Snickers", new Snack("Snickers", 0.75, 8));

        SnackDispenseHandler cokeHandler = new CokeHandler(null);
        SnackDispenseHandler pepsiHandler = new PepsiHandler(cokeHandler);
        SnackDispenseHandler kitKatHandler = new KitkatHandler(pepsiHandler);
        SnackDispenseHandler cheetosHandler = new CheetosHandler(kitKatHandler);
        SnackDispenseHandler doritosHandler = new DoritosHandler(cheetosHandler);
        SnackDispenseHandler snickersHandler = new SnickersHandler(doritosHandler);
        VendingMachine vendingMachine = new VendingMachine(snacks, snickersHandler);


        System.out.println("\n+ First Test: Completing Doritos Purchase");
        vendingMachine.selectSnack("Doritos");
        vendingMachine.insertMoney(1.25);
        vendingMachine.dispenseSnack();

        System.out.println("\n+ Second Test: Completing KitKat Purchase with an Additional Fund");
        vendingMachine.selectSnack("KitKat");
        vendingMachine.insertMoney(0.20);
        vendingMachine.dispenseSnack();
        System.out.println("Customer inserted additional $0.30");
        vendingMachine.insertMoney(0.30);
        vendingMachine.dispenseSnack();

        System.out.println("\n+ Third Test: Completing Cheetos Purchase with 2 Additional Funds");
        vendingMachine.selectSnack("Cheetos");
        vendingMachine.insertMoney(1.00);
        vendingMachine.dispenseSnack();
        System.out.println("Insert another $0.20");
        vendingMachine.insertMoney(0.20);
        vendingMachine.dispenseSnack();
        System.out.println("Insert another $0.05");
        vendingMachine.insertMoney(0.05);
        vendingMachine.dispenseSnack();

        System.out.println("\n+ Fourt Test: Testing Purchase of Out-of-Stock Snickers");
        snacks.get("Snickers").setQuantity(0);
        vendingMachine.selectSnack("Snickers");
        vendingMachine.insertMoney(0.75);
        vendingMachine.dispenseSnack();
    }
}
