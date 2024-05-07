public interface StateOfVendingMachine {
    void dispenseSnack(VendingMachine machine);
    void insertMoney(VendingMachine machine, double amount);
    void selectSnack(VendingMachine machine, String snackName);
}
