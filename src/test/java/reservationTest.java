import org.apache.maven.archetypes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

public class reservationTest {
    @Test
    public void testAccountCreation() {
        // Test Case 1: Create an Account and verify the account details
        Account account = new Account("123456789", "123 Main St", "john@example.com", "123-456-7890");
        Assertions.assertEquals("123456789", account.getAccountNumber());
        Assertions.assertEquals("123 Main St", account.getMailingAddress());
        Assertions.assertEquals("john@example.com", account.getEmailAddress());
        Assertions.assertEquals("123-456-7890", account.getPhoneNumber());
    }

    @Test
    public void testReservationCreation() {
        // Test Case 2: Create a CabinReservation and verify the reservation details
        CabinReservation cabinReservation = new CabinReservation("123456789", "R123", "456 Oak St",
                "PO Box 789", new Date(), 3, 2, 1, 1, 800, true);
        Assertions.assertEquals("123456789", cabinReservation.getAccountNumber());
        Assertions.assertEquals("R123", cabinReservation.getReservationNumber());
        Assertions.assertEquals("456 Oak St", cabinReservation.getLodgingPhysicalAddress());
        Assertions.assertEquals("PO Box 789", cabinReservation.getLodgingMailingAddress());
        Assertions.assertTrue(cabinReservation.isFullKitchen());

        // Test Case 3: Create a HotelReservation and verify the reservation details
        HotelReservation hotelReservation = new HotelReservation("987654321", "R456", "789 Elm St",
                "PO Box 123", new Date(), 5, 1, 1, 1, 400, false);
        Assertions.assertEquals("987654321", hotelReservation.getAccountNumber());
        Assertions.assertEquals("R456", hotelReservation.getReservationNumber());
        Assertions.assertEquals("789 Elm St", hotelReservation.getLodgingPhysicalAddress());
        Assertions.assertEquals("PO Box 123", hotelReservation.getLodgingMailingAddress());
        Assertions.assertFalse(hotelReservation.hasKitchenette());
    }

    @Test
    public void testAccountManager() {
        // Test Case 4: Add an account to the manager and retrieve it
        Manager manager = new Manager();
        Account account = new Account("123456789", "123 Main St", "john@example.com", "123-456-7890");
        manager.addAccount(account);
        Assertions.assertEquals(account, manager.getAccount("123456789"));

        // Test Case 5: Add a reservation to an account
        Reservation reservation = new CabinReservation("123456789", "R123", "456 Oak St",
                "PO Box 789", new Date(), 3, 2, 1, 1, 800, true);
        manager.addReservation("123456789", reservation);
        Assertions.assertEquals(reservation, manager.getReservation("R123"));

        // Test Case 6: Complete a reservation and calculate the price
        manager.completeReservation("123456789", "R123");
        Assertions.assertEquals(145.0, manager.calculatePricePerNight("R123"));
    }

    @Test
    public void testAccountUpdate() {
        // Test Case 7: Update an account's mailing address and verify the change
        Account account = new Account("123456789", "123 Main St", "john@example.com", "123-456-7890");
        account.setMailingAddress("456 Oak St");
        Assertions.assertEquals("456 Oak St", account.getMailingAddress());
    }

    @Test
    public void testReservationCancellation() {
        // Test Case 8: Cancel a reservation and verify the status change
        Manager manager = new Manager();
        Account account = new Account("123456789", "123 Main St", "john@example.com", "123-456-7890");
        manager.addAccount(account);

        // Cancel the date to future date
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.AUGUST, 31); // Set the desired date: July 20, 2023
        Date startDate = calendar.getTime();

        Reservation reservation = new CabinReservation("123456789", "R123", "456 Oak St",
                "PO Box 789", startDate, 3, 2, 1, 1, 800, true);
        manager.addReservation("123456789", reservation);

        manager.cancelReservation("123456789", "R123");
        Assertions.assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
    }

    @Test
    public void testReservationUpdate() {
        // Test Case 9: Update a reservation's physical address and verify the change
        Manager manager = new Manager();
        Account account = new Account("123456789", "123 Main St", "john@example.com", "123-456-7890");
        manager.addAccount(account);
        Reservation reservation = new CabinReservation("123456789", "R123", "456 Oak St",
                "PO Box 789", new Date(), 3, 2, 1, 1, 800, true);
        manager.addReservation("123456789", reservation);

        reservation.setLodgingPhysicalAddress("789 Elm St");
        Assertions.assertEquals("789 Elm St", reservation.getLodgingPhysicalAddress());
    }

    @Test
    public void testReservationCompletion() {
        // Test Case 11: Complete a reservation and verify the status change and calculated price
        Manager manager = new Manager();
        Account account = new Account("987654321", "789 Elm St", "jane@example.com", "987-654-3210");
        manager.addAccount(account);
        Reservation reservation = new HotelReservation("987654321", "R456", "456 Oak St",
                "PO Box 789", new Date(), 5, 2, 1, 1, 800, true);
        manager.addReservation("987654321", reservation);

        manager.completeReservation("987654321", "R456");
        Assertions.assertEquals(ReservationStatus.COMPLETED, reservation.getStatus());
        Assertions.assertEquals(180.0, reservation.getPrice());
    }

    @Test
    public void testReservationCancellationNotAllowed() {
        // Test Case 12: Attempt to cancel a reservation that is not in the DRAFT status
        Manager manager = new Manager();
        Account account = new Account("987654321", "789 Elm St", "jane@example.com", "987-654-3210");
        manager.addAccount(account);
        Reservation reservation = new HouseReservation("987654321", "R789", "789 Elm St",
                "PO Box 789", new Date(), 7, 3, 2, 2, 1000, 2);
        manager.addReservation("987654321", reservation);

        Assertions.assertThrows(IllegalOperationException.class, () -> manager.cancelReservation("987654321", "R789"));
    }

    @Test
    public void testReservationPricePerNightCalculation() {
        // Test Case 13: Calculate the price per night for a reservation and verify the result
        Manager manager = new Manager();
        Account account = new Account("987654321", "789 Elm St", "jane@example.com", "987-654-3210");
        manager.addAccount(account);
        Reservation reservation = new CabinReservation("987654321", "R123", "456 Oak St",
                "PO Box 789", new Date(), 4, 2, 1, 1, 800, true);
        manager.addReservation("987654321", reservation);

        double pricePerNight = manager.calculatePricePerNight("R123");
        Assertions.assertEquals(145.0, pricePerNight);
    }

    @Test
    public void testReservationTotalPriceCalculation() {
        // Test Case 14: Calculate the total price for a reservation and verify the result
        Manager manager = new Manager();
        Account account = new Account("987654321", "789 Elm St", "jane@example.com", "987-654-3210");
        manager.addAccount(account);
        Reservation reservation = new HotelReservation("987654321", "R456", "456 Oak St",
                "PO Box 789", new Date(), 7, 2, 1, 1, 800, true);
        manager.addReservation("987654321", reservation);

        double totalPrice = manager.calculateTotalPrice("R456");
        Assertions.assertEquals(1260.0, totalPrice);
    }

    @Test
    public void testInvalidAccountRetrieval() {
        // Test Case 15: Attempt to retrieve a non-existent account and verify the result
        Manager manager = new Manager();
        Account account = manager.getAccount("123456789");
        Assertions.assertNull(account);
    }

    @Test
    public void testInvalidReservationRetrieval() {
        // Test Case 16: Attempt to retrieve a non-existent reservation and verify the result
        Manager manager = new Manager();
        Reservation reservation = manager.getReservation("R123");
        Assertions.assertNull(reservation);
    }

    @Test
    public void testDuplicateAccountCreation() {
        // Test Case 17: Attempt to create a duplicate account and verify the exception
        Manager manager = new Manager();
        Account account1 = new Account("987654321", "789 Elm St", "jane@example.com", "987-654-3210");
        Account account2 = new Account("987654321", "456 Oak St", "john@example.com", "123-456-7890");

        manager.addAccount(account1);
        Assertions.assertThrows(DuplicateObjectException.class, () -> manager.addAccount(account2));
    }

    @Test
    public void testDuplicateReservationCreation() {
        // Test Case 18: Attempt to create a duplicate reservation and verify the exception
        Manager manager = new Manager();
        Account account = new Account("987654321", "789 Elm St", "jane@example.com", "987-654-3210");
        manager.addAccount(account);
        Reservation reservation1 = new CabinReservation("987654321", "R123", "456 Oak St",
                "PO Box 789", new Date(), 4, 2, 1, 1, 800, true);
        Reservation reservation2 = new CabinReservation("987654321", "R123", "789 Elm St",
                "PO Box 789", new Date(), 3, 1, 1, 1, 600, false);

        manager.addReservation("987654321", reservation1);
        Assertions.assertThrows(DuplicateObjectException.class, () -> manager.addReservation("987654321", reservation2));
    }

}

