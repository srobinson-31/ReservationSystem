package org.apache.maven.archetypes;

import java.util.Date;

public abstract class Reservation {
    private final String accountNumber;
    private String reservationNumber;
    private String lodgingPhysicalAddress;
    private final String lodgingMailingAddress;
    private final Date startDate;
    private final int numberOfNights;
    private final int numberOfBeds;
    private final int numberOfBedrooms;
    private final int numberOfBathrooms;
    private final int lodgingSize;
    private double price;
    private ReservationStatus status;

    // Constructor
    public Reservation(String accountNumber, String reservationNumber, String lodgingPhysicalAddress,
                       String lodgingMailingAddress, Date startDate, int numberOfNights,
                       int numberOfBeds, int numberOfBedrooms, int numberOfBathrooms,
                       int lodgingSize) {
        this.accountNumber = accountNumber;
        this.reservationNumber = reservationNumber;
        this.lodgingPhysicalAddress = lodgingPhysicalAddress;
        this.lodgingMailingAddress = lodgingMailingAddress;
        this.startDate = startDate;
        this.numberOfNights = numberOfNights;
        this.numberOfBeds = numberOfBeds;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.lodgingSize = lodgingSize;
        this.price = 0.0;
        this.status = ReservationStatus.DRAFT;
    }

    // Getter for accountNumber
    public String getAccountNumber() {
        return accountNumber;
    }

    // Getter for reservationNumber
    public String getReservationNumber() {
        return reservationNumber;
    }

    // Setter for lodgingPhysicalAddress
    public void setLodgingPhysicalAddress(String lodgingPhysicalAddress)
    {
        this.lodgingPhysicalAddress = lodgingPhysicalAddress;
    }
    // Getter for lodgingPhysicalAddress
    public String getLodgingPhysicalAddress() {
        return lodgingPhysicalAddress;
    }

    // Setter for price
    public void setPrice(double price) {
        this.price = price;
    }

    // Getter for lodgingMailingAddress
    public String getLodgingMailingAddress() {
        return lodgingMailingAddress;
    }

    // Getter for startDate
    public Date getStartDate() {
        return startDate;
    }

    // Getter for numberOfNights
    public int getNumberOfNights() {
        return numberOfNights;
    }
    // Getter for numberOfBathrooms
    public int getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    // Getter for lodgingSize
    public int getLodgingSize() {
        return lodgingSize;
    }

    // Getter for price
    public double getPrice() {
        return price;
    }

    // Getter for status
    public ReservationStatus getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    // Abstract method to calculate the price
    public abstract void calculatePrice();

    // Override the toString method to provide a string representation of the object
    @Override
    public String toString() {
        return "Account Number: " + accountNumber +
                "\nReservation Number: " + reservationNumber +
                "\nPhysical Address: " + lodgingPhysicalAddress +
                "\nMailing Address: " + lodgingMailingAddress +
                "\nStart Date: " + startDate +
                "\nNumber of Nights: " + numberOfNights +
                "\nNumber of Beds: " + numberOfBeds +
                "\nNumber of Bedrooms: " + numberOfBedrooms +
                "\nNumber of Bathrooms: " + numberOfBathrooms +
                "\nLodging Size: " + lodgingSize +
                "\nPrice: $" + price +
                "\nStatus: " + status;
    }
        public void setReservationNumber(String newReservationNumber) {
            if (newReservationNumber == null || newReservationNumber.isEmpty()) {
                throw new IllegalArgumentException("Reservation number cannot be null or empty.");
            }

            // Extract the reservation number without the prefix (remove any prefix)
            String reservationNumberWithoutPrefix = newReservationNumber.replaceAll("[^0-9]", "");

            // Append the new prefix to the reservation number
            this.reservationNumber = newReservationNumber.charAt(0) + reservationNumberWithoutPrefix.substring(1);
        }
}

