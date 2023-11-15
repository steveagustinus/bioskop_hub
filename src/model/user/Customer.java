package src.model.user;

import java.util.LinkedList;

import src.model.transaction.Transaction;

public class Customer extends User {
    private String idUser;
    private String profileName;
    private String email;
    private String phoneNumber;
    private String address;
    private LinkedList<Transaction> transaction ;

    public Customer(String idUser, String username, String password, String profileName, String email, String phoneNumber, String address, LinkedList<Transaction> transaction) {
        super(username, password);
        this.idUser = idUser;
        this.profileName = profileName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.transaction = transaction;
    }

    public String getIdUser() {
        return this.idUser;
    }
    
    public String getProfileName() {
        return this.profileName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public LinkedList<Transaction> getTransaction() {
        return transaction;
    }
}
