package org.apache.maven.archetypes;

import java.util.Date;

public class HotelReservation extends Reservation {
    private final boolean kitchenette;

    // Constructor
    public HotelReservation(String accountNumber, String reservationNumber, String lodgingPhysicalAddress,
                            String lodgingMailingAddress, Date startDate, int numberOfNights,
                            int numberOfBeds, int numberOfBedrooms, int numberOfBathrooms,
                            int lodgingSize, boolean kitchenette) {
        super(accountNumber, reservationNumber, lodgingPhysicalAddress, lodgingMailingAddress,
                startDate, numberOfNights, numberOfBeds, numberOfBedrooms, numberOfBathrooms,
                lodgingSize);
        this.kitchenette = kitchenette;
    }
    // Getter for kitchenette
    public boolean hasKitchenette() {
        return kitchenette;
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

        // Add fixed additional fee
        additionalFee += 50.0;

        // Add additional fee if kitchenette is available
        if (hasKitchenette()) {
            additionalFee += 10.0;
        }

        // Calculate and set the final price
        setPrice(basePrice + additionalFee);
    }

    // Override the toString method to provide a string representation of the object
    @Override
    public String toString() {
        return super.toString() + "\nKitchenette: " + kitchenette;
    }
}
