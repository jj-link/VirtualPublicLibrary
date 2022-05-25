package com.revature.models;

import java.util.List;

public class User {

    private int userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<Book> checkedOut;
    private int userRole; // 1 = user, 2 = owner


    public User() {
    }

    public User(int userId, String email, String password, String firstName, String lastName, List<Book> checkedOut, int userRole) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.checkedOut = checkedOut;
        this.userRole = userRole;
    }

    public User(String email, String password, String firstName, String lastName, int userRole) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
    }
        //login object
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Book> getCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(List<Book> checkedOut) {
        this.checkedOut = checkedOut;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", checkedOut=" + checkedOut +
                ", userRole=" + userRole +
                '}';
    }
}
