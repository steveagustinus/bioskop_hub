package src.controller;

import java.sql.Date;

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
    private int membership_status;
    private Date membership_expiry_date;	
    private int membership_point;
    
    private UserDataSingleton() {
        this.id = 0;
        this.username = "";
        this.password = "";
        this.profile_name = "";
        this.email = "";
        this.phone_no = "";
        this.address = "";
        this.user_type = 0;
        this.membership_status = 0;
        this.membership_expiry_date = null;
        this.membership_point = 0;
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

    public int getMembership_status() {
        return membership_status;
    }

    public void setMembership_status(int membership_status) {
        this.membership_status = membership_status;
    }

    public int getMembership_point() {
        return membership_point;
    }

    public void setMembership_point(int membership_point) {
        this.membership_point = membership_point;
    }

    public Date getMembership_expiry_date() {
        return membership_expiry_date;
    }

    public void setMembership_expiry_date(Date membership_expiry_date) {
        this.membership_expiry_date = membership_expiry_date;
    }
}