import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Block genesisBlock = blockchain.lastBlock_MMO();

        String json = gson.toJson(genesisBlock);

        System.out.println(json);

        Block lastBlock =  blockchain.newBlock_MMO(blockchain.proofOfWork_MMO(genesisBlock.getProof_MMO()), Blockchain.hash_MMO(genesisBlock));

        json = gson.toJson(lastBlock);

        System.out.println(json);
    }
}
