package org.apache.maven.archetypes;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Manager {
    private final List<Account> accounts;
    static final String ACCOUNTS_DIRECTORY = "data/accounts";

    // Constructor
    public Manager() {
        this.accounts = new ArrayList<>();
        
     // Load accounts and reservations from files on initialization
        loadAccountsFromFolders();
    }
 // Load accounts from account folders in the "data/accounts" directory
    private void loadAccountsFromFolders() {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Path.of(ACCOUNTS_DIRECTORY), "Acc-*")) {
            // Iterate through each account folder in the "data/accounts" directory
            for (Path accountFolderPath : directoryStream) {
                // Read the contents of the account file inside the account folder
                // The account file name is "acc-accountNumber.txt"
                String accountData = Files.readString(accountFolderPath.resolve(accountFolderPath.getFileName() + ".txt"));

                // Parse the account data and create an Account object
                Account account = parseAccountData(accountData);

                // If the account data is successfully parsed and an Account object is created
                if (account != null) {
                    // Load reservations for this account by reading reservation files inside the account folder
                    loadReservationsForAccount(account, accountFolderPath);

                    // Add the account to the list of accounts
                    accounts.add(account);
                }
            }
        } catch (IOException e) {
            // If an IOException occurs while reading the account folders, print an error message
            System.out.println("Failed to load accounts: " + e.getMessage());
        }
    }
 // Load reservations for a given account from reservation files in the account folder
    private void loadReservationsForAccount(Account account, Path accountFolderPath) {
        // Get the account number to be used in error messages if needed
        String accountNumber = account.getAccountNumber();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(accountFolderPath, "res-*.txt")) {
            // Iterate through each reservation file in the account folder
            for (Path reservationFilePath : directoryStream) {
                // Read the contents of the reservation file
                String reservationData = Files.readString(reservationFilePath);

                // Parse the reservation data and create a Reservation object
                Reservation reservation = parseReservationData(reservationData);

                // If the reservation data is successfully parsed and a Reservation object is created
                if (reservation != null) {
                    // Add the reservation to the account's list of reservations
                    account.addReservation(reservation);
                }
            }
        } catch (IOException e) {
            // If an IOException occurs while reading the reservation files, print an error message
            System.out.println("Failed to load reservations for account " + accountNumber + ": " + e.getMessage());
        }
    }
    // Parse account data from the file content
    private Account parseAccountData(String accountData) {
        String[] lines = accountData.split("\\n");
        String accountNumber = lines[0].substring("Account: ".length());
        String mailingAddress = lines[1].substring("Mailing Address: ".length());
        String emailAddress = lines[2].substring("Email Address: ".length());
        String phoneNumber = lines[3].substring("Phone Number: ".length());

        return new Account(accountNumber, mailingAddress, emailAddress, phoneNumber);
    }
    // Method to determine reservation type based on the content of the reservation file
    private int determineReservationType(String reservationData) {
        // Use regex to check for a specific line that exists only in House reservation files
        String housePattern = "Number of Floors: \\d+";
        Pattern pattern = Pattern.compile(housePattern);
        Matcher matcher = pattern.matcher(reservationData);

        if (matcher.find()) {
            return 3; // House Reservation
        } else {
            // If the line "Number of Floors" is not found, check for "kitchenette" line
            if (reservationData.contains("kitchenette")) {
                return 1; // Hotel Reservation
            } else {
                return 2; // Cabin Reservation
            }
        }
    }
    // Parse reservation data from the file content
    private Reservation parseReservationData(String reservationData) {
        // Determine the reservation type by parsing the input data
        int reservationType = determineReservationType(reservationData);

        // Split the input reservation data into lines
        String[] lines = reservationData.split("\\n");

        // Extract relevant information from each line
        String reservationNumber = lines[1].substring("Reservation Number: ".length());
        String accountNumber = lines[0].substring("Account Number: ".length());
        String physicalAddress = lines[2].substring("Physical Address: ".length());
        String mailingAddress = lines[3].substring("Mailing Address: ".length());
        String startDateStr = lines[4].substring("Start Date: ".length());
        Date startDate = parseDate(startDateStr);
        int numberOfNights = Integer.parseInt(lines[5].substring("Number of Nights: ".length()));
        int numberOfBeds = Integer.parseInt(lines[6].substring("Number of Beds: ".length()));
        int numberOfBedrooms = Integer.parseInt(lines[7].substring("Number of Bedrooms: ".length()));
        int numberOfBathrooms = Integer.parseInt(lines[8].substring("Number of Bathrooms: ".length()));
        int lodgingSize = Integer.parseInt(lines[9].substring("Lodging Size: ".length()));
        String statusStr = lines[11].substring("Status: ".length());
        ReservationStatus status = ReservationStatus.valueOf(statusStr);

        // Create the appropriate reservation object based on the reservation type
        switch (reservationType) {
            case 1 -> {
                // For HotelReservation, parse the boolean value for kitchenette from line 12
                boolean kitchenette = Boolean.parseBoolean(lines[12].substring("Kitchenette: ".length()));
                HotelReservation hotelReservation = new HotelReservation(accountNumber, reservationNumber, physicalAddress, mailingAddress,
                        startDate, numberOfNights, numberOfBeds, numberOfBedrooms, numberOfBathrooms, lodgingSize, kitchenette);
                hotelReservation.setStatus(status); // Set the status of the reservation
                return hotelReservation;
            }
            case 2 -> {
                // For CabinReservation, parse the boolean value for full kitchen from line 12
                boolean fullKitchen = Boolean.parseBoolean(lines[12].substring("Full Kitchen: ".length()));
                CabinReservation cabinReservation = new CabinReservation(accountNumber, reservationNumber, physicalAddress, mailingAddress,
                        startDate, numberOfNights, numberOfBeds, numberOfBedrooms, numberOfBathrooms, lodgingSize, fullKitchen);
                cabinReservation.setStatus(status); // Set the status of the reservation
                return cabinReservation;
            }
            case 3 -> {
                // For HouseReservation, parse the number of floors from line 12
                int numberOfFloors = Integer.parseInt(lines[12].substring("Number of Floors: ".length()));
                HouseReservation houseReservation = new HouseReservation(accountNumber, reservationNumber, physicalAddress, mailingAddress,
                        startDate, numberOfNights, numberOfBeds, numberOfBedrooms, numberOfBathrooms, lodgingSize, numberOfFloors);
                houseReservation.setStatus(status); // Set the status of the reservation
                return houseReservation;
            }
            default -> {
                // If an invalid reservation type is encountered, return null
                return null;
            }
        }
    }

    private static Date parseDate(String dateString) {
        // Create a SimpleDateFormat object with the input date format
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

        try {
            // Parse the input dateString using the specified format
            return inputFormat.parse(dateString);
        } catch (ParseException e) {
            // If there's a ParseException (invalid date format), catch the exception and print an error message
            System.out.println("Invalid date format. Please use the format: EEE MMM dd HH:mm:ss zzz yyyy");
            return null; // Return null to indicate that parsing failed
        }
    }
    // Retrieve an account by account number
    public Account getAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    // Add an account to the manager
    public void addAccount(Account account) {
        if (getAccount(account.getAccountNumber()) != null) {
            throw new DuplicateObjectException();
        }
        accounts.add(account);
    }
    // Add a reservation to an account
    public void addReservation(String accountNumber, Reservation reservation) {
        Account account = getAccount(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account with number " + accountNumber + " does not exist.");
        }

        if (reservationExists(reservation.getReservationNumber())) {
            throw new DuplicateObjectException();
        }

        account.addReservation(reservation);
    }
    // Complete a reservation
    public void completeReservation(String accountNumber, String reservationNumber) {
        Account account = getAccount(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account with number " + accountNumber + " does not exist.");
        }

        Reservation reservation = getReservation(reservationNumber);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation with number " + reservationNumber + " does not exist.");
        }

        if (reservation.getStatus() == ReservationStatus.DRAFT) {
            reservation.setStatus(ReservationStatus.COMPLETED);
            reservation.calculatePrice();
        } else {
            throw new IllegalOperationException("Reservation cannot be completed. Status: " + reservation.getStatus());
        }
    }

    // Cancel a reservation
    public void cancelReservation(String accountNumber, String reservationNumber) {
        Account account = getAccount(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account with number " + accountNumber + " does not exist.");
        }

        Reservation reservation = getReservation(reservationNumber);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation with number " + reservationNumber + " does not exist.");
        }

        if (reservation.getStatus() == ReservationStatus.DRAFT && reservation.getStartDate().after(new Date())) {
            reservation.setStatus(ReservationStatus.CANCELLED);
        } else {
            throw new IllegalOperationException("Reservation cannot be cancelled. Status: " + reservation.getStatus());
        }
    }
    // Calculate the price per night for a reservation
    public double calculatePricePerNight(String reservationNumber) {
        Reservation reservation = getReservation(reservationNumber);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation with number " + reservationNumber + " does not exist.");
        }

        reservation.calculatePrice();
        return reservation.getPrice();
    }
    // Calculate the total price for a reservation
    public double calculateTotalPrice(String reservationNumber) {
        Reservation reservation = getReservation(reservationNumber);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation with number " + reservationNumber + " does not exist.");
        }

        reservation.calculatePrice();
        return reservation.getPrice() * reservation.getNumberOfNights();
    }

    // Retrieve a reservation by reservation number
    public Reservation getReservation(String reservationNumber) {
        for (Account account : accounts) {
            for (Reservation reservation : account.getReservations()) {
                if (reservation.getReservationNumber().equals(reservationNumber)) {
                    return reservation;
                }
            }
        }
        return null;
    }
    // Check if a reservation already exists
    private boolean reservationExists(String reservationNumber) {
        return getReservation(reservationNumber) != null;
    }
    // Update a reservation
    public void updateReservation(String accountNumber, Reservation updatedReservation) {
        Account account = getAccount(accountNumber);
        if (account != null) {
            List<Reservation> reservations = account.getReservations();
            for (int i = 0; i < reservations.size(); i++) {
                Reservation reservation = reservations.get(i);
                if (reservation.getReservationNumber().equals(updatedReservation.getReservationNumber())) {
                    reservations.set(i, updatedReservation);
                    return;
                }
            }
        }
        System.out.println("Reservation not found.");
    }
    public List<Reservation> getAllReservationsForAccount(String accountNumber) {
        Account account = getAccount(accountNumber);
        if (account != null) {
            return account.getReservations();
        }
        return new ArrayList<>(); // Return an empty list if the account is not found
    }
}
