package src.model.transaction;

import java.time.LocalDateTime;

abstract public class transaction {
    private String idTransaction;
    private LocalDateTime transactionDate;

    public transaction (String idTransaction, LocalDateTime transactionDate){
        this.idTransaction = idTransaction;
        this.transactionDate = transactionDate;
    }

    public String getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
