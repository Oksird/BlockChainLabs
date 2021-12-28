import com.google.gson.Gson;
import org.sparkproject.guava.hash.Hashing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Blockchain {

    public List<Block> getChain_MMO() {
        return chain_MMO;
    }

    public List<Block> chain_MMO = new ArrayList<>();
    private List<Transaction> currentTransactions_MMO = new ArrayList<>();

    public Set<String> getNodes() {
        return nodes;
    }

    private Set<String> nodes = new HashSet<>();

    /**
     *
     * Нарвавляет новую транзакцию в следующий блок
     *
     * @param sender_MMO Адрес отправителя
     * @param recipient_MMO Адрес получателя
     * @param amount_MMO Сумма
     * @return Индексы блока, который будет хранить эту транзакцию
     */

    public int newTransaction_MMO(String sender_MMO, String recipient_MMO, int amount_MMO){
    this.currentTransactions_MMO.add(new Transaction(sender_MMO, recipient_MMO, amount_MMO));
    return this.chain_MMO.size();
    }

    /**
     *
     * @param proof_MMO Доказательства проведённой работы
     * @param previousHash_MMO Хеш предыдущего блока
     * @return Новый блок
     */

    public Block newBlock_MMO(int proof_MMO, String previousHash_MMO){
        // Создаём копию списка
        List<Transaction> transactions_MMO = this.currentTransactions_MMO.stream().collect(Collectors.toList());

        // Создаём новый обьект блока
        Block newBlock_MMO = new Block(this.chain_MMO.size(), proof_MMO, previousHash_MMO, transactions_MMO);

        // Очищаем список транзакций
        this.currentTransactions_MMO.clear();

        // Добавляем новый блок в цепочку
        this.chain_MMO.add(newBlock_MMO);

        // Возвращаем новый блок
        return newBlock_MMO;
    }

    public static String hash_MMO(Block block){
        StringBuilder hashingInputBuilder_MMO = new StringBuilder();

        // Добавляем параметры блока в переменную в точном неизменном порядке
        hashingInputBuilder_MMO.append(block.getIndex_MMO())
                .append(block.getTimestamp_MMO())
                .append(block.getProof_MMO())
                .append(block.getPreviousHash_MMO());

        String hashingInput_MMO = hashingInputBuilder_MMO.toString();

        // Генерируем хеш блока на основе его полей с помощью переменной
        return Hashing.sha256().hashString(hashingInput_MMO, StandardCharsets.UTF_8).toString();
    }

    public Block lastBlock_MMO(){
        return this.chain_MMO.size()>0?this.chain_MMO.get(this.chain_MMO.size()-1):null;

    }

    /**
     * Простая проверка алгоритма: Поиск числа p', так как hash(pp')
     * содержит 4 заглавных нуля, где р - являеться предыдущим
     * доказательством? а p' - новым
     *
     * @param lastProofOfWork_MMO  z
     * @return int
     */

    public int proofOfWork_MMO(int lastProofOfWork_MMO){
        int proof_MMO = 0;
        while(!isProofValid_MMO(lastProofOfWork_MMO, proof_MMO)){
            proof_MMO++;
        }
        return proof_MMO;
    }

    /**
     * Подтверждение доказательства: Содержит ли hash(lastProof, proof) заголовок нуля
     *
     * @param lastProof_MMO
     * @param proof_MMO
     * @return
     */

    private boolean isProofValid_MMO(int lastProof_MMO, int proof_MMO){
        String guessString_MMO = Integer.toString(lastProof_MMO) + Integer.toString(proof_MMO);
        String guessHash_MMO = Hashing.sha256().hashString(guessString_MMO, StandardCharsets.UTF_8).toString();
        return guessHash_MMO.endsWith("0000");
    }

    public void registerNode(String netloc) {
        nodes.add(netloc);
    }

     public boolean validChain(List<Block> chain) {
         for (int i = 1; i < chain.size(); i++) {
             Block lastBlock = chain.get(i - 1);
             Block currentBlock = chain.get(i);
             if (!currentBlock.getPreviousHash_MMO().equals(hash_MMO(lastBlock))) {
                 System.out.println("Hash don't match");
                 return false;
                 }
             if (!isProofValid_MMO(lastBlock.getProof_MMO(), currentBlock.getProof_MMO())) {
                 System.out.println("Proof is not valid");
                 return false;
                 }
             }
         return true;
         }
 public boolean resolveConflicts() {
     Gson gson = new Gson();
     int maxLen = this.chain_MMO.size();
     List<Block> newChain = this.chain_MMO;
     try {
         for (String host : this.nodes) {
             URL url;
             url = new URL(host + "/chain");
             HttpURLConnection con = (HttpURLConnection)
                     url.openConnection();
             con.setRequestMethod("GET");
             int status = con.getResponseCode();
             if (status == 200) {
                 BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                 String inputLine;
                 StringBuffer content = new StringBuffer();
                 while ((inputLine = in.readLine()) != null) {
                     content.append(inputLine);
                 }
                 in.close();
                 con.disconnect();
                 ChainResponse response =
                         gson.fromJson(content.toString(), ChainResponse.class);
                 if (response.length > maxLen && validChain(response.chain)) {
                     maxLen = response.length;
                     newChain = response.chain;
                 }
             }
         }
     } catch (IOException e) {
         e.printStackTrace();
     }
     if (newChain != this.chain_MMO) {
         this.chain_MMO = newChain;
     }
     return newChain == this.chain_MMO;
 }


    public Blockchain() {
        newBlock_MMO(31052002, "Muzychenko");
    }
}
