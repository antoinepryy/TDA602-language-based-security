package backEnd;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

public class Wallet {
    /**
     * The RandomAccessFile of the wallet file
     */
    private RandomAccessFile file;

    /**
     * Creates a Wallet object
     * <p>
     * A Wallet object interfaces with the wallet RandomAccessFile
     */
    public Wallet() throws Exception {
        this.file = new RandomAccessFile(new File("backEnd/wallet.txt"), "rw");
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
     * Gets the wallet balance.
     *
     * @return The content of the wallet file as an integer, this version is thread-safe
     */
    public int getBalanceThreadSafe() throws IOException {
        FileLock lock = file.getChannel().lock();
        this.file.seek(0);
        String line = this.file.readLine();
        lock.release();
        return Integer.parseInt(line);

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
}
