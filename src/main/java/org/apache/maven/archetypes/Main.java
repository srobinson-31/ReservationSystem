package org.apache.maven.archetypes;

import java.io.IOException;
import java.nio.file.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * The main class that handles the user interface and interactions.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Manager manager = new Manager();


    /**
     * The entry point of the program.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            System.out.println("========== Main Menu ==========");
            System.out.println("1. Create Account");
            System.out.println("2. View Account Information");
            System.out.println("3. All reservations for Account Information");
            System.out.println("4. Create Reservation");
            System.out.println("5. Update Account");
            System.out.println("6. Update Reservation");
            System.out.println("7. Cancel Reservation");
            System.out.println("8. Exit");
            System.out.println("===============================");

            int choice = getChoice();

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> viewAccountInformation();
                case 3 -> viewAllReservationsForAccount();
                case 4 -> createReservation();
                case 5 -> updateAccount();
                case 6 -> updateReservation();
                case 7 -> cancelReservation();
                case 8 -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Reads and returns the user's choice from the console.
     *
     * @return The user's choice.
     */
    private static int getChoice() {
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        return choice;
    }

    /**
     * Creates a new account by collecting user input and invoking the manager's addAccount method.
     */
    private static void createAccount() {
        System.out.println("========== Create Account ==========");

        // Generate a random 8-digit account number
        Random random = new Random();
        String accountNumber = String.format("%08d", random.nextInt(100000000));

        System.out.println("Enter Account INFO Below");

        System.out.print("Enter mailing address: ");
        String mailingAddress = scanner.nextLine();

        System.out.print("Enter email address: ");
        String emailAddress = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();

        Account account = new Account(accountNumber, mailingAddress, emailAddress, phoneNumber);
        manager.addAccount(account);

        // Specify the account directory path
        String accountDirectoryPath = "data/accounts/Acc-" + accountNumber;

        // Create the account directory
        Path accountDirectory = Path.of(accountDirectoryPath);
        try {
            Files.createDirectories(accountDirectory);
        } catch (IOException e) {
            System.out.println("Failed to create account directory: " + e.getMessage());
            return;
        }

        // Save the account data in the account file
        String accountFileName = "acc-" + accountNumber + ".txt";
        String accountFilePath = accountDirectoryPath + "/" + accountFileName;
        try {
            Files.write(Path.of(accountFilePath), account.toString().getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.out.println("Failed to save account: " + e.getMessage());
            return;
        }

        System.out.println("Account created successfully.");
        System.out.println(account);
    }

    private static void viewAccountInformation() {
        System.out.println("========== View Account Information ==========");
        System.out.print("Enter account number: ");
        String accountNumber = scanner.next();

        // Check if the account folder exists
        String accountDirectoryPath = "data/accounts/Acc-" + accountNumber;
        if (!Files.exists(Path.of(accountDirectoryPath))) {
            System.out.println("Account not found 1.");
            return;
        }

        // Check if the account file exists
        String accountFilePath = accountDirectoryPath + "/acc-" + accountNumber + ".txt";
        if (!Files.exists(Path.of(accountFilePath))) {
            System.out.println("Account not found 2.");
            return;
        }

        Account account = manager.getAccount(accountNumber);
        if (account != null) {
            System.out.println(account);
        } else {
            System.out.println("Account not found 3.");
        }
    }

    private static void viewAllReservationsForAccount() {
        System.out.println("========== View All Reservations for Account ==========");
        System.out.print("Enter account number: ");
        String accountNumber = scanner.next();

        // Check if the account folder exists
        String accountDirectoryPath = "data/accounts/Acc-" + accountNumber;
        if (!Files.exists(Path.of(accountDirectoryPath))) {
            System.out.println("Account not found.");
            return;
        }

        Manager manager = new Manager();
        List<Reservation> reservations = manager.getAllReservationsForAccount(accountNumber);

        if (reservations.isEmpty()) {
            System.out.println("No reservations found for the account.");
        } else {
            System.out.println("Reservations for Account: " + accountNumber);
            for (Reservation reservation : reservations) {
                // Load the reservation data from the file and print it directly, including the price
                String reservationNumber = reservation.getReservationNumber();
                String reservationFilePath = "data/accounts/Acc-" + accountNumber + "/res-" + reservationNumber + ".txt";
                try {
                    String reservationData = Files.readString(Path.of(reservationFilePath));
                    System.out.println(reservationData);
                    System.out.println("==============");
                } catch (IOException e) {
                    System.out.println("Failed to read reservation data: " + e.getMessage());
                }
            }
        }
    }

    private static void createReservation() {
        System.out.println("========== Create Reservation ==========");
        System.out.print("Enter account number: ");
        String accountNumber = scanner.next();
        scanner.nextLine(); // Consume the newline character

        // Remove the "acc-" prefix if present
        if (accountNumber.startsWith("acc-")) {
            accountNumber = accountNumber.substring(4);
        }

        // Check if the account file exists
        String accountFilePath = "data/accounts/Acc-" + accountNumber + "/acc-" + accountNumber + ".txt";
        if (!Files.exists(Path.of(accountFilePath))) {
            System.out.println("Account not found.");
            return;
        }

        Account account = manager.getAccount(accountNumber);
        if (account != null) {
            System.out.print("Enter lodging physical address: ");
            String lodgingPhysicalAddress = scanner.nextLine();

            System.out.print("Enter lodging mailing address (leave empty if same as physical address): ");
            String lodgingMailingAddress = scanner.nextLine();

            System.out.print("Enter start date (YYYY-MM-DD): ");
            String startDateStr = scanner.next();
            Date startDate = parseDate(startDateStr);

            if (startDate != null) {
                System.out.print("Enter number of nights: ");
                int numberOfNights = scanner.nextInt();

                System.out.print("Enter number of beds: ");
                int numberOfBeds = scanner.nextInt();

                System.out.print("Enter number of bedrooms: ");
                int numberOfBedrooms = scanner.nextInt();

                System.out.print("Enter number of bathrooms: ");
                int numberOfBathrooms = scanner.nextInt();

                System.out.print("Enter lodging size in square feet: ");
                int lodgingSize = scanner.nextInt();

                System.out.print("Enter reservation type (1. Hotel, 2. Cabin, 3. House): ");
                int reservationType = scanner.nextInt();

                // Generate a random 8-digit reservation number prefix based on the reservation type
                String reservationNumberPrefix;
                switch (reservationType) {
                    case 1 -> reservationNumberPrefix = "H"; // Hotel
                    case 2 -> reservationNumberPrefix = "C"; // Cabin
                    case 3 -> reservationNumberPrefix = "O"; // House
                    default -> {
                        System.out.println("Invalid reservation type.");
                        return;
                    }
                }

                // Generate a random 10-digit reservation number suffix
                Random random = new Random();
                String reservationNumberSuffix = String.format("%08d", random.nextInt(100000000));
                String reservationNumber = reservationNumberPrefix + reservationNumberSuffix;

                Reservation reservation;
                switch (reservationType) {
                    case 1 -> {
                        System.out.print("Does the hotel room have a kitchenette? (true/false): ");
                        boolean kitchenette = scanner.nextBoolean();
                        reservation = new HotelReservation(accountNumber, reservationNumber, lodgingPhysicalAddress,
                                lodgingMailingAddress, startDate, numberOfNights, numberOfBeds, numberOfBedrooms,
                                numberOfBathrooms, lodgingSize, kitchenette);
                    }
                    case 2 -> {
                        System.out.print("Does the cabin have a full kitchen? (true/false): ");
                        boolean fullKitchen = scanner.nextBoolean();
                        reservation = new CabinReservation(accountNumber, reservationNumber, lodgingPhysicalAddress,
                                lodgingMailingAddress, startDate, numberOfNights, numberOfBeds, numberOfBedrooms,
                                numberOfBathrooms, lodgingSize, fullKitchen);
                    }
                    case 3 -> {
                        System.out.print("Enter number of floors: ");
                        int numberOfFloors = scanner.nextInt();
                        reservation = new HouseReservation(accountNumber, reservationNumber, lodgingPhysicalAddress,
                                lodgingMailingAddress, startDate, numberOfNights, numberOfBeds, numberOfBedrooms,
                                numberOfBathrooms, lodgingSize, numberOfFloors);
                    }
                    default -> {
                        System.out.println("Invalid reservation type.");
                        return;
                    }
                }

                reservation.calculatePrice(); // Calculate the price for the reservation
                account.addReservation(reservation); // Add the reservation to the account's reservation list

                // Specify the reservation file path
                String reservationFileName = "res-" + reservationNumber + ".txt";
                String reservationDirectoryPath = "data/accounts/Acc-" + accountNumber;
                String reservationFilePath = reservationDirectoryPath + "/" + reservationFileName;

                // Create the account directory if it does not exist
                Path reservationDirectory = Path.of(reservationDirectoryPath);
                if (!Files.exists(reservationDirectory)) {
                    try {
                        Files.createDirectories(reservationDirectory);
                    } catch (IOException e) {
                        System.out.println("Failed to create reservation directory: " + e.getMessage());
                        return;
                    }
                }

                // Save the reservation data in the reservation file
                try {
                    Files.write(Path.of(reservationFilePath), reservation.toString().getBytes(), StandardOpenOption.CREATE);
                } catch (IOException e) {
                    System.out.println("Failed to save reservation: " + e.getMessage());
                    return;
                }

                System.out.println("Reservation created successfully.");
                System.out.println(reservation);
            } else {
                System.out.println("Invalid start date format.");
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void updateAccount() {
        System.out.println("========== Update Account ==========");
        System.out.print("Enter account number: ");
        String accountNumber = scanner.next();

        Account account = manager.getAccount(accountNumber);
        if (account != null) {
            System.out.print("Enter new mailing address: ");
            String newMailingAddress = scanner.next();
            scanner.nextLine(); // Consume the newline character

            System.out.print("Enter new email address: ");
            String newEmailAddress = scanner.next();
            scanner.nextLine(); // Consume the newline character

            System.out.print("Enter new phone number: ");
            String newPhoneNumber = scanner.next();
            scanner.nextLine(); // Consume the newline character

            // Update the account object
            account.setMailingAddress(newMailingAddress);
            account.setEmailAddress(newEmailAddress);
            account.setPhoneNumber(newPhoneNumber);

            // Save the updated account object to the file
            String accountFilePath = "data/accounts/Acc-" + accountNumber + "/acc-" + accountNumber + ".txt";
            try {
                Files.write(Path.of(accountFilePath), account.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                System.out.println("Account updated successfully.");
                System.out.println(account);
            } catch (IOException e) {
                System.out.println("Failed to update account: " + e.getMessage());
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void updateReservation() {
        System.out.println("========== Update Reservation ==========");
        System.out.print("Enter account number: ");
        String accountNumber = scanner.next();
        scanner.nextLine(); // Consume the newline character

        Account account = manager.getAccount(accountNumber);
        if (account != null) {
            System.out.print("Enter reservation number: ");
            String reservationNumber = scanner.next();
            scanner.nextLine(); // Consume the newline character

            Reservation reservation = manager.getReservation(reservationNumber);
            if (reservation != null) {
                if (reservation.getStatus() == ReservationStatus.DRAFT) {
                    System.out.print("Enter lodging physical address: ");
                    String lodgingPhysicalAddress = scanner.nextLine();

                    System.out.print("Enter lodging mailing address (leave empty if same as physical address): ");
                    String lodgingMailingAddress = scanner.nextLine();

                    System.out.print("Enter start date (YYYY-MM-DD): ");
                    String startDateStr = scanner.next();
                    Date startDate = parseDate(startDateStr);

                    if (startDate != null) {
                        System.out.print("Enter number of nights: ");
                        int numberOfNights = scanner.nextInt();

                        System.out.print("Enter number of beds: ");
                        int numberOfBeds = scanner.nextInt();

                        System.out.print("Enter number of bedrooms: ");
                        int numberOfBedrooms = scanner.nextInt();

                        System.out.print("Enter number of bathrooms: ");
                        int numberOfBathrooms = scanner.nextInt();

                        System.out.print("Enter lodging size in square feet: ");
                        int lodgingSize = scanner.nextInt();

                        System.out.print("Enter reservation type (1. Hotel, 2. Cabin, 3. House): ");
                        int reservationType = scanner.nextInt();

                        Reservation updatedReservation = null;
                        if (reservationType == 1) {
                            System.out.print("Does the hotel room have a kitchenette? (true/false): ");
                            boolean kitchenette = scanner.nextBoolean();
                            updatedReservation = new HotelReservation(accountNumber, reservationNumber, lodgingPhysicalAddress,
                                    lodgingMailingAddress, startDate, numberOfNights, numberOfBeds, numberOfBedrooms,
                                    numberOfBathrooms, lodgingSize, kitchenette);
                        } else if (reservationType == 2) {
                            System.out.print("Does the cabin have a full kitchen? (true/false): ");
                            boolean fullKitchen = scanner.nextBoolean();
                            updatedReservation = new CabinReservation(accountNumber, reservationNumber, lodgingPhysicalAddress,
                                    lodgingMailingAddress, startDate, numberOfNights, numberOfBeds, numberOfBedrooms,
                                    numberOfBathrooms, lodgingSize, fullKitchen);
                        } else if (reservationType == 3) {
                            System.out.print("Enter number of floors: ");
                            int numberOfFloors = scanner.nextInt();
                            updatedReservation = new HouseReservation(accountNumber, reservationNumber, lodgingPhysicalAddress,
                                    lodgingMailingAddress, startDate, numberOfNights, numberOfBeds, numberOfBedrooms,
                                    numberOfBathrooms, lodgingSize, numberOfFloors);
                        }

                        if (updatedReservation != null) {
                            updatedReservation.calculatePrice(); // Calculate price for the updated reservation
                            updatedReservation.setStatus(ReservationStatus.COMPLETED); // Mark the reservation as completed

                            // Generate new file name prefix based on reservation type
                            String newFilePrefix = "";
                            if (reservationType == 1) {
                                newFilePrefix = "H";
                            } else if (reservationType == 2) {
                                newFilePrefix = "C";
                            } else if (reservationType == 3) {
                                newFilePrefix = "O";
                            }

                            // Update the reservation number inside the reservation object to match the new file name format
                            String newReservationNumber = newFilePrefix + reservationNumber;
                            updatedReservation.setReservationNumber(newReservationNumber);

                            // Get the old file name and construct the new file name
                            String oldFileName = "data/accounts/Acc-" + accountNumber + "/res-" + reservationNumber + ".txt";
                            String newFileName = "data/accounts/Acc-" + accountNumber + "/res-" + newReservationNumber + ".txt";

                            // Rename the old file to the new file name
                            try {
                                Path oldFilePath = Paths.get(oldFileName);
                                Path newFilePath = Paths.get(newFileName);
                                Files.move(oldFilePath, newFilePath, StandardCopyOption.REPLACE_EXISTING);

                                // Update the existing reservation object in the account's reservations list
                                manager.updateReservation(accountNumber, updatedReservation);

                                // Save the updated reservation object to the file
                                try {
                                    String reservationFilePath = "data/accounts/Acc-" + accountNumber + "/res-" + newReservationNumber + ".txt";
                                    Files.write(Path.of(reservationFilePath), updatedReservation.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                                    System.out.println("Reservation updated successfully and marked as completed.");
                                    System.out.println(updatedReservation);
                                } catch (IOException e) {
                                    System.out.println("Failed to update reservation: " + e.getMessage());
                                }
                            } catch (IOException e) {
                                System.out.println("Failed to rename file: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Invalid reservation type.");
                        }
                    } else {
                        System.out.println("Invalid start date format.");
                    }
                } else {
                    System.out.println("Reservation cannot be updated. Status: " + reservation.getStatus());
                }
            } else {
                System.out.println("Reservation not found.");
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void cancelReservation() {
        System.out.println("========== Cancel Reservation ==========");
        System.out.print("Enter account number: ");
        String accountNumber = scanner.next();

        Account account = manager.getAccount(accountNumber);
        if (account != null) {
            System.out.print("Enter reservation number: ");
            String reservationNumber = scanner.next();

            Reservation reservation = manager.getReservation(reservationNumber);
            if (reservation != null) {
                if (reservation.getStatus().equals(ReservationStatus.DRAFT)) {
                    // Calculate the price before canceling the reservation
                    reservation.calculatePrice();

                    // Update the status of the reservation to "CANCELLED" in the in-memory object
                    reservation.setStatus(ReservationStatus.CANCELLED);

                    // Update the status of the reservation in the corresponding file
                    String reservationFilePath = "data/accounts/Acc-" + accountNumber + "/res-" + reservationNumber + ".txt";
                    try {
                        Files.write(Path.of(reservationFilePath), reservation.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                        System.out.println("Reservation cancelled successfully.");
                    } catch (IOException e) {
                        System.out.println("Failed to cancel reservation: " + e.getMessage());
                    }
                } else {
                    System.out.println("Reservation cannot be cancelled. Status: " + reservation.getStatus());
                }
            } else {
                System.out.println("Reservation not found.");
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    private static Date parseDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use the format YYYY-MM-DD.");
            return null;
        }
    }
}

