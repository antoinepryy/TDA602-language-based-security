# Language-based Security

## Lab 1 - TOCTOU

### Part 0 : About source code

- All our code is located in src folder 


### Part 1 : Exploit your program

- Shared resources are the wallet (wallet.txt) and our pocket (pocket.txt), that are shared between users that call our ShoppingCart.java file.
- The root of the problem is that our program verify in a first step that we have enough money in our wallet, but only withdraw it in a second step. If two or more users are calling the function at the same time, it can happen that both programs enter in the first step at the same time.
- To attack the system, you can run at the same time several instances of the program and when a data race error occurs, you will be able to have several items in your pocket.txt by paying only for one of them.

- In order to compile and run this program (Windows), you can simply use your command prompt and run the program run.bat located in src folder. It will compile you program (you have to ensure that Java is installed and configured on your machine) and then it will launch automatically two instances of the program in order to see if data races occur.

### Part 2 : Fix the API

- The safeWidthDraw function is implemented in the Wallet class.
- The Pocket class also suffers from possible race conditions, since it contains a method that is able to perform a write in a file. We have to ensure that this is done in a thread-safe manner.
- These protections are enough because Pocket & Wallet classes were the only ones that were allowed to perform any form of writing and since all other classes don't rely on data writes in order to run, we are sure that our program does not contain data races issues anymore.

The thread safe withdraw function implemented in Wallet class 
   
   ```java
   
   public void safeWithdraw(int valueToWithdraw) throws Exception {
           this.walletLock.lock();
           int balance = this.getBalance();
           if (balance >= valueToWithdraw) {
               this.setBalance(balance - valueToWithdraw);
           } else {
               throw new Exception("Not enough money in wallet");
           }
           this.walletLock.unlock();
       }
   
   ```

The thread safe pocket adding implemented in Pocket class

```java

public void safeAddProduct(String product) throws Exception {
        this.pocketLock.lock();
        this.addProduct(product);
        this.pocketLock.unlock();
    }

```

We also have to bring some changes to our main function in order to use safe-threaded functions

```java

        int currentBalance = wallet.getBalance();
        // Check if the amount of credit is enough, if not stop the execution
        if (currentBalance >= Store.getProductPrice(product)) {
            System.out.println("You just bought a " + product);
            Thread.sleep(1000);
            // Withdraw the price of the product from the wallet
            wallet.safeWithdraw(Store.getProductPrice(product));
            // add the name of the product to the pocket file
            pocket.safeAddProduct(product);
            // print the new balance.
            System.out.println("New balance : " + wallet.getBalance());
        } else {
            System.out.println("Not enough money in your wallet.. closing program !");
            System.exit(0);
        }

```


instead of 


```java

        int currentBalance = wallet.getBalance();
        // Check if the amount of credit is enough, if not stop the execution
        if (currentBalance >= Store.getProductPrice(product)) {
            System.out.println("You just bought a " + product);
            Thread.sleep(1000);
            // Withdraw the price of the product from the wallet
            wallet.setBalance(currentBalance - Store.getProductPrice(product));
            // add the name of the product to the pocket file
            pocket.addProduct(product);
            // print the new balance.
            System.out.println("New balance : " + wallet.getBalance());
        } else {
            System.out.println("Not enough money in your wallet.. closing program !");
            System.exit(0);
        }

```

