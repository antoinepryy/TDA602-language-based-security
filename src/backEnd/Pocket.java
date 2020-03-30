package backEnd;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.ReentrantLock;


public class Pocket {
    /**
     * The RandomAccessFile of the pocket file
     */
    private RandomAccessFile file;
    private ReentrantLock pocketLock;

    /**
     * Creates a Pocket object
     * 
     * A Pocket object interfaces with the pocket RandomAccessFile.
     */
    public Pocket () throws Exception {
        this.file = new RandomAccessFile(new File("backEnd/pocket.txt"), "rw");
        this.pocketLock = new ReentrantLock();
    }

    /**
     * Adds a product to the pocket. 
     *
     * @param  product           product name to add to the pocket (e.g. "car")
     */
    public void addProduct(String product) throws Exception {
        this.file.seek(this.file.length());
        this.file.writeBytes(product+'\n'); 
    }

    /**
     * Thread-safe wallet management function
     */
    public void safeAddProduct(String product) throws Exception {
        this.pocketLock.lock();
        this.addProduct(product);
        this.pocketLock.unlock();
    }

    /**
     * Closes the RandomAccessFile in this.file
     */
    public void close() throws Exception {
        this.file.close();
    }
}
