import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.log4j.BasicConfigurator;
import spark.Spark;


import java.util.List;
import java.util.UUID;

import static spark.Spark.get;
import static spark.Spark.post;

public class Controller {

    public static void main(String[] args) {
        BasicConfigurator.configure();

        Spark.port(4567);
        Spark.port(4569);
        Blockchain blockchain = new Blockchain();
        Gson gson = new Gson();

            get("/mine", (req, res) -> {
             // отримуємо останній блок у блокчейні
             Block lastBlock = blockchain.lastBlock_MMO();
              // отримуємо останнє підтвердження роботи
             int lastProof = lastBlock.getProof_MMO();

             // отримуємо нове підтвердження роботи
             int proofOfWork = blockchain.proofOfWork_MMO(lastProof);

             // створюємо нову транзакцію
             blockchain.newTransaction_MMO("0", UUID.randomUUID().toString().replace("-", ""), 31);

             // отримуємо останній хеш у блокчейні
             String lastHash = Blockchain.hash_MMO(lastBlock);

             // створюємо новий блок
             Block newBlock = blockchain.newBlock_MMO(proofOfWork, lastHash);

             // створюємо рядок, що є відображенням нового блока
             String json = gson.toJson(newBlock);

             // ставимо статус відповіді 200
             res.status(200);

             // відправляюємо відомості про новий блок
             return json;
             });

         post("/transactions/new", (req, res) -> {
            try {
                // створюємо об'єкт транзакції за отриманими даними з запиту
                Transaction transaction = gson.fromJson(req.body(), Transaction.class);
                // додаємо нову транзакцію
                int index =blockchain.newTransaction_MMO(transaction.getSender_MMO(), transaction.getRecipient_MMO(),transaction.getAmount_MMO());
                res.status(201);
                return "Transaction will be added to Block " + index;
            } catch (JsonSyntaxException e) {
                res.status(400);

                return "Invalid JSON";
            }
        });

            get("/chain", (req, res) -> {
             List<Block> chain =  blockchain.chain_MMO;
            return gson.toJson(chain);
        });

        post("/nodes/register", (req, res) -> {
             try {
                 List<String> nodes = gson.fromJson(req.body(), NodesResponse.class).nodes;
                 for(String node : nodes) {
                     blockchain.registerNode(node);
                     }
                 return gson.toJson(blockchain.getNodes());
                 } catch (Exception e) {
                 res.status(400);
                 return "Incorrect host address";
                 }
             });

         get("/nodes/resolve", (req, res) -> {
             if(blockchain.resolveConflicts()) {
                 return gson.toJson(new ChainResponse(blockchain.getChain_MMO(), blockchain.getChain_MMO().size()));
                 }else {
                 return gson.toJson(new ChainResponse(blockchain.getChain_MMO(), blockchain.getChain_MMO().size()));
                 }

             });

        }
    }

