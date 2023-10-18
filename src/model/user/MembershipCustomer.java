package src.model.user;

public class MembershipCustomer extends Customer {
    private int poin;

    public MembershipCustomer(String username, String password, String profileName, String email, String phoneNumber, String address, int poin) {
        super(username, password, profileName, email, phoneNumber, address);
        this.poin = poin;
    }

    public int getPoin() {
        return this.poin;
    }
}
