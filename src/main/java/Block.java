import java.util.List;

public class Block {

    private int index_MMO;
    private long timestamp_MMO;
    private List<Transaction> transactions_MMO;
    private int proof;
    private String previousHash;

    public int getIndex_MMO() {
        return index_MMO;
    }

    public long getTimestamp_MMO() {
        return timestamp_MMO;
    }

    public List<Transaction> getTransactions_MMO() {
        return transactions_MMO;
    }

    public int getProof_MMO() {
        return proof;
    }

    public String getPreviousHash_MMO() {
        return previousHash;
    }

    public Block(int index_MMO, int proof, String previousHash, List<Transaction> transactions_MMO){
        this.index_MMO = index_MMO;
        this.proof = proof;
        this.previousHash = previousHash;
        this.transactions_MMO = transactions_MMO;
        this.timestamp_MMO = System.currentTimeMillis();
    }
}
