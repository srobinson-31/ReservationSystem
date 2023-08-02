package org.apache.maven.archetypes;

import java.util.Date;

public class HouseReservation extends Reservation {
    private final int numberOfFloors;

    // Constructor
    public HouseReservation(String accountNumber, String reservationNumber, String lodgingPhysicalAddress,
                            String lodgingMailingAddress, Date startDate, int numberOfNights,
                            int numberOfBeds, int numberOfBedrooms, int numberOfBathrooms,
                            int lodgingSize, int numberOfFloors) {
        super(accountNumber, reservationNumber, lodgingPhysicalAddress, lodgingMailingAddress,
                startDate, numberOfNights, numberOfBeds, numberOfBedrooms, numberOfBathrooms,
                lodgingSize);
        this.numberOfFloors = numberOfFloors;
    }

    // Getter for numberOfFloors
    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    // Override the calculatePrice method from the parent class
    @Override
    public void calculatePrice() {
        double basePrice = 120.0;
        double additionalFee = 0.0;

        // Calculate additional fee based on lodging size
        if (getLodgingSize() > 900) {
            additionalFee += 15.0;
        }

        // Add additional fee based on the number of floors
        additionalFee += getNumberOfFloors() * 25.0;

        // Calculate and set the final price
        setPrice(basePrice + additionalFee);
    }

    // Override the toString method to provide a string representation of the object
    @Override
    public String toString() {
        return super.toString() + "\nNumber of Floors: " + numberOfFloors;
    }
}
