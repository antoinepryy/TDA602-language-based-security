# Language-based Security

## Lab 1 - TOCTOU

### Part 0 : About source code

All our code is located in the `src/` folder, it contains several elements :
- `ShoppingCart` class, where our main function is located.
- `backEnd/` folder, where classes that manage our pocket, our wallet and the store are located.
- a `Makefile`, that can automatically build our project or manage the wallet and pocket text files.
- `scripts/` folder, where we implemented some batch scripts to automate coherence checking, automatize products buying, etc.
- `run_candies.bat` and `run_car.bat` scripts, that you can run in order to check whether or not data races problems occur.
    
If you just want to compile and run the program, you can type the commands above in your shell, when you are in the `src` folder

```bash
make
java ShoppingCart
```

You will next been asked to choose what product you want top buy
For the two batch scripts `run_candies.bat` and `run_car.bat` (we will talk about them later in the report) you can simply type, located in the `src/` folder

```bash
run_candies.bat
run_car.bat 
```

located

### Part 1 : Exploit your program

- Shared resources are the wallet `wallet.txt` and our pocket `pocket.txt`, that are shared between users that call our `ShoppingCart.java` file.
- The root of the problem is that our program verifies in a first step that we have enough money in our wallet, but only withdraws it in a second step. If two or more users are calling the function **at the same time**, it can happen that these multiple programs enter the first step simultaneously.
- To attack the system, you can run at the same time several instances of the program and when a data race error occurs, you will be able to add several items in your `pocket.txt` that you might **not have payed for**.
- If the configuration below happens during execution, a problem will occur and will cause troubles in the accuracy of the program. This error is highlighted using the `run_car.bat` and `run_candies.bat` scripts with the **unpatched** version of the code. These scripts are respectively buying 2 cars or 14 candies at the same time and check whether or not a data race error occurs.

   
```

                                THREAD 1
                
--| Get Balance |-------------| Withdraw Money |---------------------->

-------------------| Get Balance |----------------| Withdraw Money|--->

                                THREAD 2

```

- Our `run_car.bat` script fills our wallet with 30000$, empties our pocket and then spawns 2 threads that will try to buy a car. Once the operation is finished, it checks if data race errors occurred by counting the number of items that were bought.
- Our `run_candies.bat` script fills our wallet with 30000$, empties our pocket and then spawns 14 threads that will try to buy candies. Once the operation is finished, it checks if data race errors occurred by checking the money in our wallet.

- In order to compile and run this program (Windows), you can simply use your command prompt and run the program `run.bat` located in src folder. It will compile you program (you have to ensure that Java is installed and configured on your machine) and then it will launch automatically two instances of the program in order to see if data races occur.

![No thread-Safe Version](/assets/lab1/no-thread-safe.PNG)

### Part 2 : Fix the API

- The `safeWithdraw` function is implemented in the `Wallet` class.
- The `Pocket` class also suffers from possible race conditions, since it contains a method that is able to perform a write in a file. We have to ensure that this is done in a thread-safe manner.
- These protections are enough because `Pocket` & `Wallet` classes were the only ones that were allowed to perform any form of writing and since all other classes don't rely on data writes in order to run, we are sure that our program does not contain data races issues anymore.
- To fix this program, we used a lock to perform operations in parallel without incoherence between our wallet and pocket files. A FileLock class is used for each file to ensure that critical functions are not executed at the same time. `FileLock lock = file.getChannel().lock();` is a blocking call, meaning that each thread will wait to obtain the lock to write or read the files.


![Thread-Safe Version](/assets/lab1/thread-safe.PNG)

The thread safe withdraw function implemented in `Wallet` class :
   
```java

public void safeWithdraw(int valueToWithdraw) throws Exception {
    FileLock lock = file.getChannel().lock();
    int balance = getBalance();
    if (balance >= valueToWithdraw) {
        this.setBalance(balance - valueToWithdraw);
        lock.release();
    } else {
        lock.release();
        throw new Exception("Not enough money in wallet");
        }
}

```

The `getBalance` function had to be rewritten since an error can occur it we don't use the FileLock class in this section :

```java


public int getBalanceThreadSafe() throws IOException {
        FileLock lock = file.getChannel().lock();
        this.file.seek(0);
        String line = this.file.readLine();
        lock.release();
        return Integer.parseInt(line);

    }

```

The thread safe pocket adding implemented in `Pocket` class :

```java

public void safeAddProduct(String product) throws Exception {
    FileLock lock = file.getChannel().lock();
    System.out.println("You just bought a " + product);
    this.addProduct(product);
    lock.release();
}

```

We also have to bring some changes to our main function in order to use safe-threaded functions :

```java

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

```


instead of :


```java

        public static void main(String[] args) throws Exception {
                Wallet wallet = new Wallet();
                Pocket pocket = new Pocket();
        
                System.out.println("Your current balance is: " + wallet.getBalance() + " credits.");
                System.out.println(Store.asString());
        
                System.out.println("What do you want to buy? ");
                Scanner scan = new Scanner(System.in);
                String product = scan.nextLine();
        
        
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
                wallet.close();
                pocket.close();
            }

```


