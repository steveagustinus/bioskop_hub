package src.model.transaction;

import java.time.LocalDateTime;

public class TransactionFnb extends Transaction{
    private String idFnb;
    private int quantity;

    public TransactionFnb(String idTransaction, LocalDateTime transactionDate, String idUser, String idFnb,
            int quantity) {
        super(idTransaction, transactionDate, idUser);
        this.idFnb = idFnb;
        this.quantity = quantity;
    }

    public String getIdFnb() {
        return idFnb;
    }

    public void setIdFnb(String idFnb) {
        this.idFnb = idFnb;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
