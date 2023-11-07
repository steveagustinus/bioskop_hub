package src.model.user;

import java.util.LinkedList;

import src.model.transaction.Transaction;

public class MembershipCustomer extends Customer {
    private int poin;

    public MembershipCustomer(String username, String password, String profileName, String email, String phoneNumber,
            String address, LinkedList<Transaction> transaction, int poin) {
        super(username, password, profileName, email, phoneNumber, address, transaction);
        this.poin = poin;
    }

    public int getPoin() {
        return this.poin;
    }
}
