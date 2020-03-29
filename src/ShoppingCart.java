import backEnd.*;

import java.util.Scanner;

public class ShoppingCart {
    public static void main(String[] args) throws Exception {
        Wallet wallet = new Wallet();
        Pocket pocket = new Pocket();

        System.out.println("Your current balance is: " + wallet.getBalance() + " credits.");
        System.out.println(Store.asString());

        System.out.print("What do you want to buy? ");
        Scanner scan = new Scanner(System.in);
        String product = scan.nextLine();

        /* TODO:
           - check if the amount of credits is enough, if not stop the execution.
           - otherwise, withdraw the price of the product from the wallet.
           - add the name of the product to the pocket file.
           - print the new balance.
         */

        System.out.println(product);
        // Check if the amount of credit is enough, if not stop the execution
        if (wallet.getBalance() >= Store.getProductPrice(product)) {
            System.out.println("You just bought a " + product);
            // Withdraw the price of the product from the wallet
            wallet.setBalance(wallet.getBalance() - Store.getProductPrice(product));
            // add the name of the product to the pocket file
            pocket.addProduct(product);
            // print the new balance.
            System.out.println("New balance : " + wallet.getBalance());
        } else {
            System.out.println("Not enough money in your wallet.. closing program !");
            System.exit(0);
        }

    }
}
