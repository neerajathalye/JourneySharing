package com.group12.journeysharing.model;

import java.util.Date;

/**
 * Created by Neeraj Athalye on 18-Feb-19.
 */
public class User {

    private String firstName;
    private String lastName;
    private String fullName;
    private String dob;
    private String gender;
    private String phoneNumber;
    private String email;
    private String homeAddress;
    private String workAddress;
    private String emergencyName;
    private String emergencyPhoneNumber;
    private String emergencyEmail;
    private double rating;
    private String userId;

    public User(String firstName, String lastName, String dob, String gender, String phoneNumber, String email, String homeAddress, String workAddress, String emergencyName, String emergencyPhoneNumber, String emergencyEmail, double rating) {
        this.firstName = firstName;
        this.lastName = lastName;
        setFullName();
        this.dob = dob;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.homeAddress = homeAddress;
        this.workAddress = workAddress;
        this.emergencyName = emergencyName;
        this.emergencyPhoneNumber = emergencyPhoneNumber;
        this.emergencyEmail = emergencyEmail;
        this.rating = 0;
    }

    public User(String firstName, String lastName, String dob, String gender, String phoneNumber, String email, String emergencyName, String emergencyPhoneNumber, String emergencyEmail) {
        this.firstName = firstName;
        this.lastName = lastName;
        setFullName();
        this.dob = dob;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.emergencyName = emergencyName;
        this.emergencyPhoneNumber = emergencyPhoneNumber;
        this.emergencyEmail = emergencyEmail;
        this.rating = 0;
    }

    public User() {
        this.rating = 0;
    }

    public User(String firstName, String lastName, double rating) {
        this.firstName = firstName;
        this.lastName = lastName;
        setFullName();
        this.rating = rating;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName() {
        this.fullName = firstName + " " + lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public void setEmergencyName(String emergencyName) {
        this.emergencyName = emergencyName;
    }

    public String getEmergencyPhoneNumber() {
        return emergencyPhoneNumber;
    }

    public void setEmergencyPhoneNumber(String emergencyPhoneNumber) {
        this.emergencyPhoneNumber = emergencyPhoneNumber;
    }

    public String getEmergencyEmail() {
        return emergencyEmail;
    }

    public void setEmergencyEmail(String emergencyEmail) {
        this.emergencyEmail = emergencyEmail;
    }


    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
