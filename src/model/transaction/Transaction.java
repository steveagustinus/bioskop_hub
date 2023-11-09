package src.model.transaction;

import java.time.LocalDateTime;

abstract public class Transaction {
    private String idTransaction;
    private LocalDateTime transactionDate;
    private String idUser;

    public Transaction(String idTransaction, LocalDateTime transactionDate, String idUser) {
        this.idTransaction = idTransaction;
        this.transactionDate = transactionDate;
        this.idUser = idUser;
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

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
