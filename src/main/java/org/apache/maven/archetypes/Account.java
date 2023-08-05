package org.apache.maven.archetypes;

import java.util.ArrayList;
import java.util.List;



public class Account {
    private final String accountNumber;
    private String mailingAddress;
    private final List<Reservation> reservations;
    private String emailAddress;
    private String phoneNumber;

    public Account(String accountNumber, String mailingAddress, String emailAddress, String phoneNumber) {
        this.accountNumber = accountNumber;
        this.mailingAddress = mailingAddress;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.reservations = new ArrayList<>();
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public String getMailingAddress() {
        return mailingAddress;
    }
    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }
    public List<Reservation> getReservations() {
        return reservations;
    }
    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }
    @Override
    public String toString() {
        // Implement the string representation of the Account object
        // Format the data to be written to the file
        return "Account: " + accountNumber +
                "\nMailing Address: " + mailingAddress +
                "\nEmail Address: " + emailAddress +
                "\nPhone Number: " + phoneNumber;
    }
}