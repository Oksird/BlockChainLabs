public class Transaction {
    private String sender_MMO;
    private String recipient_MMO;
    private int amount_MMO;

    public String getSender_MMO() {
        return sender_MMO;
    }

    public String getRecipient_MMO() {
        return recipient_MMO;
    }

    public int getAmount_MMO() {
        return amount_MMO;
    }

    public Transaction(String sender_MMO, String recipient_MMO, int amount_MMO){
        this.sender_MMO = sender_MMO;
        this.recipient_MMO = recipient_MMO;
        this.amount_MMO = amount_MMO;
    }


}
