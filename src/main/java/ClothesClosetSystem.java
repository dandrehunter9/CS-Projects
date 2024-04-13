//import exception.InvalidPrimaryKeyException;
//import model.Book;
//import model.Patron;
//import model.BookCollection;
//import model.PatronCollection;
//
//import java.util.Properties;
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) throws InvalidPrimaryKeyException {
//
//        Scanner scanner = new Scanner(System.in);
//        int choice;
//        do {
//
//            // Display options
//            System.out.println("Select an option:");
//            System.out.println("1. Insert Book");
//            System.out.println("2. Insert Patron");
//            System.out.println("3. Fetch Given Title");
//            System.out.println("4. Fetch Book Given Year");
//            System.out.println("5. Fetch Patron Given Date");
//            System.out.println("6. Fetch Patron Given Zip");
//            System.out.println("0. Exit");
//
//            // Get user input
//            System.out.print("Enter your choice: ");
//            choice = scanner.nextInt();
//
//            // Execute corresponding method based on user's choice
//            switch (choice) {
//                case 1:
//                    InsertBook();
//                    break;
//                case 2:
//                    InsertPatron();
//                    break;
//                case 3:
//                    givenBookTitle();
//                    break;
//                case 4:
//                    givenBookYear();
//                    break;
//                case 5:
//                    givenPatronDate();
//                    break;
//                case 6:
//                    givenPatronZip();
//                    break;
//                case 0:
//                    System.out.println("Exiting the program. Goodbye!");
//                    break;
//                default:
//                    System.out.println("Invalid choice. Please enter a valid option.");
//            }
//
//        } while (choice != 0);
//
//        scanner.close();
//
//    }
//
//    private static void givenBookTitle() throws InvalidPrimaryKeyException {
//        String title;
//        title = PrintStuff("Book Title Please: ");
//        BookCollection bookCollection = new BookCollection();
//        bookCollection.findBooksWithTitleLike(title);
//        bookCollection.display();
//    }
//
//    private static void givenBookYear() throws InvalidPrimaryKeyException {
//        String year;
//        year = PrintStuff("Book Year Please: ");
//        BookCollection bookCollection = new BookCollection();
//        bookCollection.findBooksOlderThanDate(year);
//        bookCollection.display();
//    }
//
//    private static void givenPatronDate() throws InvalidPrimaryKeyException {
//        String date;
//        date = PrintStuff("Patron Date Please: ");
//        PatronCollection patronCollection = new PatronCollection();
//        patronCollection.findPatronsYoungerThan(date);
//        patronCollection.display();
//    }
//
//    private static void givenPatronZip() throws InvalidPrimaryKeyException {
//        String zip;
//        zip = PrintStuff("Patron Zip Code Please: ");
//        PatronCollection patronCollection = new PatronCollection();
//        patronCollection.findPatronsAtZipCode(zip);
//        patronCollection.display();
//    }
//
//    public static void InsertBook() {
//        String bookTitle, author, pubYear, status = "";
//        bookTitle = PrintStuff("Book Title Please: ");
//        author = PrintStuff("Author Please: ");
//        pubYear = PrintStuff("Published Year Please: ");
//
//        // insert
//        Properties schema = new Properties();
//        schema.setProperty("TableName","Book");
//
//        Properties insertValues = new Properties();
//        insertValues.setProperty("bookTitle", bookTitle);
//        insertValues.setProperty("author", author);
//        insertValues.setProperty("pubYear", pubYear);
//        insertValues.setProperty("status", "Active");
//
//        Book book = new Book(insertValues);
//        book.save();
//    }
//
//    public static void InsertPatron() {
//        String name, address, city, stateCode, zip, email, dateOfBirth;
//        name = PrintStuff("Name please: ");
//        address = PrintStuff("Address please: ");
//        city = PrintStuff("City please: ");
//        stateCode = PrintStuff("State Code please: ");
//        zip = PrintStuff("Zip code please: ");
//        email = PrintStuff("Email please: ");
//        dateOfBirth = PrintStuff("Date of Birth please: ");
//
//        // insert
//        Properties schema = new Properties();
//        schema.setProperty("TableName","Patron");
//
//        Properties insertValues = new Properties();
//        insertValues.setProperty("name", name);
//        insertValues.setProperty("address", address);
//        insertValues.setProperty("city", city);
//        insertValues.setProperty("stateCode", stateCode);
//        insertValues.setProperty("zip", zip);
//        insertValues.setProperty("email", email);
//        insertValues.setProperty("dateOfBirth", dateOfBirth);
//        insertValues.setProperty("status", "Active");
//
//        Patron patron = new Patron(insertValues);
//        patron.save();
//    }
//
//    private static String PrintStuff(String message) {
//        System.out.println( "\n" + message);
//        Scanner scanner = new Scanner(System.in);
//        String input = scanner.nextLine();
//        return input;
//    }
//}
//

// specify the package

// system imports

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;

// project imports
import event.Event;

import model.Clerk;
import userinterface.MainStageContainer;
import userinterface.WindowPosition;

/** The class containing the main program  for the ATM application */
//==============================================================
public class ClothesClosetSystem extends Application
{
    private Clerk clerk;		// the main behavior for the application

    /** Main frame of the application */
    private Stage mainStage;

    // start method for this class, the main application object
    //----------------------------------------------------------
    public void start(Stage primaryStage)
    {
        System.out.println(" version 1.00");
        System.out.println("Copyright 2024 Alex Stalica, Ashley Allen, Mei Lamarre, D'Andre Hunter, Faizan Rafieuddin");

        MainStageContainer.setStage(primaryStage, "Clothes Closet System 1.00");
        mainStage = MainStageContainer.getInstance();

        // Finish setting up the stage (ENABLE THE GUI TO BE CLOSED USING THE TOP RIGHT
        // 'X' IN THE WINDOW), and show it.
        mainStage.setOnCloseRequest(new EventHandler <javafx.stage.WindowEvent>() {
            @Override
            public void handle(javafx.stage.WindowEvent event) {
                System.exit(0);
            }
        });

        try
        {
            clerk = new Clerk();
        }
        catch(Exception exc)
        {
            System.err.println("Main-Main - could not create Clerk!");
            new Event(Event.getLeafLevelClassName(this), "Main.<init>", "Unable to create a Clerk object", Event.ERROR);
            exc.printStackTrace();
        }


        WindowPosition.placeCenter(mainStage);

        mainStage.show();
    }


    /**
     * The "main" entry point for the application. Carries out actions to
     * set up the application
     */
    //----------------------------------------------------------
    public static void main(String[] args)
    {
        launch(args);
    }

}
