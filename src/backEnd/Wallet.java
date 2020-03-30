package backEnd;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Wallet {
    /**
     * The RandomAccessFile of the wallet file
     */
    private RandomAccessFile file;
    private Lock walletLock;

    /**
     * Creates a Wallet object
     * <p>
     * A Wallet object interfaces with the wallet RandomAccessFile
     */
    public Wallet() throws Exception {
        this.file = new RandomAccessFile(new File("backEnd/wallet.txt"), "rw");
        this.walletLock = new ReentrantLock();
    }

    /**
     * Gets the wallet balance.
     *
     * @return The content of the wallet file as an integer
     */
    public int getBalance() throws IOException {
        this.file.seek(0);
        return Integer.parseInt(this.file.readLine());
    }

    /**
     * Sets a new balance in the wallet
     *
     * @param newBalance new balance to write in the wallet
     */
    public void setBalance(int newBalance) throws Exception {
        this.file.setLength(0);
        String str = Integer.valueOf(newBalance).toString() + '\n';
        this.file.writeBytes(str);
    }

    /**
     * Closes the RandomAccessFile in this.file
     */
    public void close() throws Exception {
        this.file.close();
    }

    /**
     * Thread-safe withdraw function
     */
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
}
