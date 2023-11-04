package src.model.transaction;

import java.time.LocalDateTime;

public class transactionFnb extends transaction{
    private String idFnb;
    private int quantity;

    public transactionFnb(String idTransaction, LocalDateTime transactionDate, String idFnb, int quantity) {
        super(idTransaction, transactionDate);
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
