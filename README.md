# ReservationSystem
/**
 * OOP Project - Account and Accommodation Reservations Management
 * README File
 *
 * This README provides an overview of the project requirements and design for
 * managing accounts and associated accommodation reservations using Java.
 * The project includes various classes, methods, and functionalities to
 * handle data management and interactions between different entities.
 *
 * Project Description:
 * The goal of this project is to design and build prototype software for managing
 * accounts and associated accommodation reservations. The software is designed to
 * be a stand-alone application loaded on a local computer and will be called by a
 * User Interface (UI) that someone else will be implementing. The software's main
 * functions include loading and saving account and reservation data, performing
 * various operations on accounts and reservations, and calculating reservation
 * prices and totals.
 *
 * Classes and Functionalities:
 * 1. Manager: Responsible for loading and managing account and reservation data.
 * 2. Account: Represents an account with associated information.
 * 3. Reservation: Represents an accommodation reservation with various details.
 * 4. IllegalLoadException: Custom unchecked exception for loading data.
 * 5. DuplicateObjectException: Custom unchecked exception for duplicate objects.
 * 6. IllegalOperationException: Custom unchecked exception for invalid operations.
 * 7. TestManager: Test class with test cases to verify system functionality.
 *
 * Functionalities:
 * - Loading existing accounts and reservations from files
 * - Creating new accounts and adding them to the manager
 * - Updating account information
 * - Adding draft reservations to accounts
 * - Completing and cancelling reservations
 * - Changing reservation values (where applicable)
 * - Calculating reservation prices per night and total prices
 * - Handling exceptions for various scenarios
 * - Interaction with User Interface (UI) for authorization and control
 *
 * Important Notes:
 * - The project follows the separation of concerns principle for data reading/writing.
 * - Data is stored in human-readable text files using XML tags or JSON.
 * - Each class has a toString() method for output and file storage purposes.
 * - The UI handles login, authorization, and access control, and is not within the scope of this project.
 * - The system is designed with careful consideration of required functionality and constraints.
 *
 * Test Class:
 * The project includes a TestManager class with main() method and various test case methods.
 * Each test case method verifies different aspects of the system's functionality and handles
 * exceptions, data loading, and manipulation. The test class ensures the correct operation of
 * the designed classes and methods.
 *
 * Please refer to the project's source code for detailed implementation and comments.
 * For any further information or inquiries, please contact the project's author.
 *
 * Author: Syrone Robinson
 * Date: August 11th, 2023
 */
