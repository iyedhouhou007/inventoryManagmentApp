package com.iyed_houhou.inventoryManagementApp.models;

public class User {
    private int userId;
    private String username;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;
    private Role role;


    // Constructor with all attributes (full details)
    public User(String username, String password, String email, String address, String phoneNumber, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }


    // Constructor with phone number and email, without address
    public User(String username, String password, String email, String phoneNumber, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // Constructor with only username, email, password, and role (simplified)
    public User(String username, String password, String email, Role role, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.address = address;
    }

    // Constructor with username, password, email, and role (basic)
    public User(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }




    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public Role getRole() {
        return role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
