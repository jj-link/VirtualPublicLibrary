package com.revature.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@SequenceGenerator(name="seq", initialValue=52, allocationSize=100000)
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private int userId;

    @Column(name="email", unique = true, nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="checked_out",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="book_id")}
    )
    List<Book> checkedOut; // @JsonIgnore ??

    @Column(name="user_role", nullable = false)
    private int userRole; // 1 = user, 2 = owner

    public User(){
        this.checkedOut = new ArrayList<>();
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
        this.userId = 0;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
        this.checkedOut = new ArrayList<>();
    }
    //login object
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.checkedOut = new ArrayList<>();
    }

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = 1;
        this.checkedOut = new ArrayList<>();
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

    public void addCheckOut(Book book){
        this.checkedOut.add(book);
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