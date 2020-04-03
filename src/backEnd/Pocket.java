package backEnd;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;


public class Pocket {
    /**
     * The RandomAccessFile of the pocket file
     */
    private RandomAccessFile file;

    /**
     * Creates a Pocket object
     * <p>
     * A Pocket object interfaces with the pocket RandomAccessFile.
     */
    public Pocket() throws Exception {
        this.file = new RandomAccessFile(new File("backEnd/pocket.txt"), "rw");
    }

    /**
     * Adds a product to the pocket.
     *
     * @param product product name to add to the pocket (e.g. "car")
     */
    public void addProduct(String product) throws Exception {
        this.file.seek(this.file.length());
        this.file.writeBytes(product + '\n');
    }

    /**
     * Thread-safe wallet management function
     */
    public void safeAddProduct(String product) throws Exception {
        FileLock lock = file.getChannel().lock();
        System.out.println("You just bought a " + product);
        this.addProduct(product);
        lock.release();
    }

    /**
     * Closes the RandomAccessFile in this.file
     */
    public void close() throws Exception {
        this.file.close();
    }
}
