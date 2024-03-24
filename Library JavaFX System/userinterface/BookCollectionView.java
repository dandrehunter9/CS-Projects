// specify the package
package userinterface;

import java.util.Enumeration;
import java.util.Vector;

// project imports
import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Book;
import model.BookCollection;

/** The class containing the Account View  for the ATM application */
//==============================================================
public class BookCollectionView extends View
{

	// GUI components
	private TableView<BookTableModel> bookTable;
	private Button doneButton;

	// For showing error message
	private MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public BookCollectionView(IModel book) {
		super(book, "BookCollectionView");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// Add a title for this panel
		container.getChildren().add(createTitle());
		
		// create our GUI components, add them to this Container
		container.getChildren().add(createFormContent());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);

		populateFields();

		myModel.subscribe("UpdateStatusMessage", this);
	}


	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle() {
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);	

		Text titleText = new Text(" Library System ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
		container.getChildren().add(titleText);
		
		return container;
	}

	// Create the main form content
	//-------------------------------------------------------------
	private VBox createFormContent() {
		VBox vbox = new VBox(10);

		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
       	grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Text prompt = new Text("BOOK INFORMATION");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);
		
		bookTable = new TableView<>();
		
        bookTable.getColumns().addAll(createColumn("BookId", 45, "bookId"), createColumn("Author", 100, "author"),
        		createColumn("Title", 200, "bookTitle"), createColumn("Publication\nYear", 100, "pubYear"), createColumn("Status", 60, "status"));
		
		HBox doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);

		doneButton = createButton("Done", "Done");
		doneCont.getChildren().add(doneButton);
	
		vbox.getChildren().add(grid);
		vbox.getChildren().add(bookTable);
		vbox.getChildren().add(doneCont);

		return vbox;
	}
	
	private TableColumn createColumn(String title, int width, String propertyName) {
		TableColumn column = new TableColumn(title);
        column.setMinWidth(width);
        column.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>(propertyName));
		
		return column;
	}
	
	private Button createButton(String title, String state) {
		Button button = new Button(title);
		button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		button.setOnAction(new EventHandler<ActionEvent>() {
   		     @Override
   		     public void handle(ActionEvent e) {
	    		myModel.stateChangeRequest(state, null);
   		     }
		});
		
		return button; 
	}
	
	// Create the status log field
	//-------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage) {
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	//-------------------------------------------------------------
	public void populateFields() {
        ObservableList<BookTableModel> tableData = FXCollections.observableArrayList();
        try {

            Vector<?> entryList = (Vector<?>) myModel.getState("BookList");
            System.out.println(entryList);
            Enumeration<?> entries = entryList.elements();

            while (entries.hasMoreElements()) {
                Book nextBook = (Book) entries.nextElement();
                Vector<String> view = nextBook.getEntryListView();

                // add this list entry to the list
                BookTableModel nextTableRowData = new BookTableModel(view);
                tableData.add(nextTableRowData);

            }
            

            bookTable.setItems(tableData);
        } catch (Exception e) {//SQLException e) {
            // Need to handle this exception
        }
	}

	/**
	 * Update method
	 */
	//---------------------------------------------------------
	public void updateState(String key, Object value) {
		clearErrorMessage();
	}

	/**
	 * Display error message
	 */
	//----------------------------------------------------------
	public void displayErrorMessage(String message) {
		statusLog.displayErrorMessage(message);
	}

	/**
	 * Display info message
	 */
	//----------------------------------------------------------
	public void displayMessage(String message) {
		statusLog.displayMessage(message);
	}

	/**
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage() {
		statusLog.clearErrorMessage();
	}

}
