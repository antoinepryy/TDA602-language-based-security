# Language-based Security

## Lab 1 - TOCTOU

### Part 1 : Exploit your program

- Shared resource are the wallet (wallet.txt) and our pocket (pocket.txt), that are shared between users that call our ShoppingCart file.
- The root of the problem is that our program firstly verify that we have enough money in our wallet, and THEN it withdraw it, but if two or more users are calling the function at the same time it can happen that both program enters in the first step at the same time.
- To attack the system you can run at the same time several instance of the program, and when data race error occurs you will be able to have several items in you wallet buy by paying just one of them.

- In order to compile and run this program (Windows), you can simply use your command prompt and run the program run.bat located in src folder, it will compile you program (you have to ensure that Java is installed and configured on your machine), then it will launch automatically two instances of the program in order to see if data races occurs

### Part 2 : Fix the API

- The safeWidthDraw function is implemented in the Wallet class.
- The Pocket class also suffers from possible races, since it contains a method that is able to perform a write in a file we have to ensure that this is done in a thread-safe manner.
- These protections are enough because Pocket & Wallet classes were the only ones that were allowed to perform any form of writing, and since all other classes don't rely on data writes in order to run, we are sure that our program does not contain data races issues anymore.

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