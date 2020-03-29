# Language-based Security

## Lab 1 - TOCTOU

### Part 1 : Exploit your program

- Shared resource are the wallet (wallet.txt) and our pocket (pocket.txt), that are shared between users that call our ShoppingCart file.
- The root of the problem is that our program firstly verify that we have enough money in our wallet, and THEN it withdraw it, but if two or more users are calling the function at the same time it can happen that both program enters in the first step at the same time

### Part 2 : Fix the API

