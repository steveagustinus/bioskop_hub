package src.model.user;

public class Customer extends User {
    private String profileName;
    private String email;
    private String phoneNumber;
    private String address;

    public Customer(String username, String password, String profileName, String email, String phoneNumber, String address) {
        super(username, password);
        this.profileName = profileName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
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
}
