package src.controller;

public class UserDataSingleton {
    private static UserDataSingleton instance;
    
    private int id;
    private String username;
    private String password;
    private String profile_name;
    private String email;
    private String phone_no;
    private String address;
    private int user_type;
    
    private UserDataSingleton() {
        this.id = 0;
        this.username = "";
        this.password = "";
        this.profile_name = "";
        this.email = "";
        this.phone_no = "";
        this.address = "";
        this.user_type = 0;
    }
    
    public static UserDataSingleton getInstance() {
        if (instance == null) {
            instance = new UserDataSingleton();
        }
        return instance;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public static void setInstance(UserDataSingleton instance) {
        UserDataSingleton.instance = instance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUser_type() {
        return user_type;
    }
    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }
}