package org.apache.maven.archetypes;
import java.util.Date;

public class CabinReservation extends Reservation {
    private final boolean fullKitchen;

    // Constructor
    public CabinReservation(String accountNumber, String reservationNumber, String lodgingPhysicalAddress,
                            String lodgingMailingAddress, Date startDate, int numberOfNights,
                            int numberOfBeds, int numberOfBedrooms, int numberOfBathrooms,
                            int lodgingSize, boolean fullKitchen) {
        super(accountNumber, reservationNumber, lodgingPhysicalAddress, lodgingMailingAddress,
                startDate, numberOfNights, numberOfBeds, numberOfBedrooms, numberOfBathrooms,
                lodgingSize);
        this.fullKitchen = fullKitchen;
    }

    // Getter for fullKitchen
    public boolean isFullKitchen() {
        return fullKitchen;
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

        // Add additional fee if full kitchen is available
        if (isFullKitchen()) {
            additionalFee += 20.0;
        }

        // Add additional fee based on the number of bathrooms
        additionalFee += getNumberOfBathrooms() * 5.0;

        // Calculate and set the final price
        setPrice(basePrice + additionalFee);
    }

    // Override the toString method to provide a string representation of the object
    @Override
    public String toString() {
        return super.toString() + "\nFull Kitchen: " + fullKitchen;
    }
}
