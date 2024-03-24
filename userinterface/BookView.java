// specify the package
package userinterface;

import java.util.Arrays;
import java.util.Properties;
import java.util.function.Supplier;

// project imports
import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;
import model.Book;

/** The class containing the Account View  for the ATM application */
//==============================================================
public class BookView extends View
{

	// GUI components
	protected TextField bookTitle;
	protected TextField author;
	protected TextField pubYear;
	protected ComboBox<String> status;

	protected Button cancelButton;
	protected Button submitButton;

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public BookView(IModel book)
	{
		super(book, "BookView");

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

		myModel.subscribe("ServiceCharge", this);
		myModel.subscribe("UpdateStatusMessage", this);
	}


	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle()
	{
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);	

		Text titleText = new Text(" Brockport Bank ATM ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
		container.getChildren().add(titleText);
		
		return container;
	}

	// Create the main form content
	//-------------------------------------------------------------
	private VBox createFormContent()
	{
		VBox vbox = new VBox(10);

		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
       	grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Text prompt = new Text("ACCOUNT INFORMATION");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

		Text accNumLabel = new Text(" Book Title : ");
		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
		accNumLabel.setFont(myFont);
		accNumLabel.setWrappingWidth(150);
		accNumLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(accNumLabel, 0, 1);

		bookTitle = new TextField();
		bookTitle.setEditable(true);
		grid.add(bookTitle, 1, 1);

		Text acctTypeLabel = new Text(" Author : ");
		acctTypeLabel.setFont(myFont);
		acctTypeLabel.setWrappingWidth(150);
		acctTypeLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(acctTypeLabel, 0, 2);

		author = new TextField();
		author.setEditable(true);
		grid.add(author, 1, 2);

		Text balLabel = new Text(" Publication Year : ");
		balLabel.setFont(myFont);
		balLabel.setWrappingWidth(150);
		balLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(balLabel, 0, 3);

		pubYear = new TextField();
		pubYear.setEditable(true);
		grid.add(pubYear, 1, 3);
		
		Text statLabel = new Text(" Status : ");
		statLabel.setFont(myFont);
		statLabel.setWrappingWidth(150);
		statLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(statLabel, 0, 4);
		
		status = new ComboBox<>(FXCollections.observableArrayList(Arrays.asList("Active", "Inactive")));
		status.setEditable(true);
		status.getSelectionModel().select("Active");
		grid.add(status, 1, 4);

		HBox doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
		submitButton = createButton("Submit", "BookSubmit", () -> {
			Pair<Boolean, String> message = checkValues();
			
			if (message.getKey()) {
				displayErrorMessage(message.getValue());
			} else {
				displayMessage(message.getValue());
				((Book)myModel).processNewBook(dataToProperties());
				((Book)myModel).update();
			}
			return false;
   	});
		cancelButton = new Button("Back");
		cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		    	clearErrorMessage();
       		    	myModel.stateChangeRequest("AccountCancelled", null);   
            	  }
        	});
		doneCont.getChildren().add(cancelButton);
		doneCont.getChildren().add(submitButton);
	
		vbox.getChildren().add(grid);
		vbox.getChildren().add(doneCont);

		return vbox;
	}
	private Properties dataToProperties() {
		Properties p = new Properties();
		
		p.setProperty("bookTitle", bookTitle.getText());;
		p.setProperty("author", author.getText());
		p.setProperty("pubYear", pubYear.getText());
		p.setProperty("status", status.getValue());
		return p;
	}
	
	private Button createButton(String title, String state, Supplier<Boolean> function) {
		Button button = new Button(title);
		button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		button.setOnAction(new EventHandler<ActionEvent>() {
       		     @Override
       		     public void handle(ActionEvent e) {
       		    	clearErrorMessage();
       		    	Boolean result = function.get();
       		    	
       		    	if (result) {
       		    		myModel.stateChangeRequest(state, null);
       		    	}
            	  }
		});
		
		return button;
	}
	
	private Text createText(String title) {
		Font font = Font.font("Helvetica", FontWeight.BOLD, 12);
		
		Text text = new Text(" " + title + " : ");
		text.setFont(font);
		text.setWrappingWidth(150);
		text.setTextAlignment(TextAlignment.RIGHT);
		
		return text;
	}
	
	private Pair<Boolean, String> checkValues() {
		if (bookTitle.getText() == null || bookTitle.getText().isEmpty()) {
			return new Pair<>(true, "Book Title field is empty.");
		}
		if (author.getText() == null || author.getText().isEmpty()) {
			return new Pair<>(true, "Author field is empty.");
		}
		if (pubYear.getText() == null || pubYear.getText().isEmpty()) {
			return new Pair<>(true, "Publication Year field is empty.");
		}
	
		try {
			int year = Integer.parseInt(pubYear.getText());
			
			if (year < 1800 || year > 2024) {
				return new Pair<>(true, "Publication Year should be between 1800 and 2024.");
			}
		} catch (Exception e) {
			return new Pair<>(true, "Publication Year is not a Year.");
		}
		
		return new Pair<>(false, "Book created.");
	}

	// Create the status log field
	//-------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	//-------------------------------------------------------------
	public void populateFields()
	{
		bookTitle.setText((String)myModel.getState("bookTitle"));
		author.setText((String)myModel.getState("author"));
		pubYear.setText((String)myModel.getState("pubYear"));
		status.setValue((String)myModel.getState("status"));
	}

	/**
	 * Update method
	 */
	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
		clearErrorMessage();

		if (key.equals("bookTitle") == true)
		{
			String val = (String)value;
			bookTitle.setText(val);
			displayMessage("Service Charge Imposed: $ " + val);
		}
		if (key.equals("author") == true)
		{
			String val = (String)value;
			author.setText(val);
			displayMessage("Service Charge Imposed: $ " + val);
		}
		if (key.equals("pubYear") == true)
		{
			String val = (String)value;
			pubYear.setText(val);
			displayMessage("Service Charge Imposed: $ " + val);
		}
		if (key.equals("status") == true)
		{
			String val = (String)value;
			status.setValue(val);
			displayMessage("Service Charge Imposed: $ " + val);
		}
	}

	/**
	 * Display error message
	 */
	//----------------------------------------------------------
	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
	}

	/**
	 * Display info message
	 */
	//----------------------------------------------------------
	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
	}

	/**
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}

}

//---------------------------------------------------------------
//	Revision History:
//


