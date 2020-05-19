import backEnd.Pocket;
import backEnd.Store;
import backEnd.Wallet;

import java.util.Scanner;

public class ShoppingCart {
    public static void main(String[] args) throws Exception {
        Wallet wallet = new Wallet();
        Pocket pocket = new Pocket();

        System.out.println("Your current balance is: " + wallet.getBalanceThreadSafe() + " credits.");
        System.out.println(Store.asString());

        System.out.println("What do you want to buy? ");
        Scanner scan = new Scanner(System.in);
        String product = scan.nextLine();


        try {
            wallet.safeWithdraw(Store.getProductPrice(product));
            pocket.safeAddProduct(product);
            System.out.println(String.format("Your new balance is: %d", wallet.getBalanceThreadSafe()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        wallet.close();
        pocket.close();
        System.exit(0);


    }
}
