package com.barak.lin.finalchat;

/**
 * Created by User on 03/01/2017.
 */

public class NewUser {
    private String firstName, lastName, email, phone, birthDate, userName, password;

    public NewUser(String firstName, String lastName, String email, String phone, String birthDate, String userName, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;

        this.userName = userName;
        this.password = password;
    }

    public NewUser(NewUser user) {
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.email = user.email;
        this.phone = user.phone;
        this.birthDate = user.birthDate;
        this.userName = user.userName;
        this.password = user.password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setUserName(String username) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String toString(){
        return "{'firstName:'" + firstName + "'lastName:'" + lastName + "'email:'" + email +
                "'phone:'" + phone + "'birthDate:'" + birthDate +
                "'userName:'" + userName + ", 'password:'" + password + "}";
    }

}
