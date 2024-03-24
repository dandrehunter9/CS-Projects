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
public class PatronView extends View
{

	// GUI components
	protected TextField name;
	protected TextField address;
	protected TextField city;
	protected TextField stateCode;
	protected TextField zip;
	protected TextField email;
	protected TextField dateOfBirth;
	protected ComboBox<String> status;

	protected Button cancelButton;
	protected Button submitButton;

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public PatronView(IModel book)
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

		Text nameLabel = new Text(" Name : ");
		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
		nameLabel.setFont(myFont);
		nameLabel.setWrappingWidth(150);
		nameLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(nameLabel, 0, 1);

		name = new TextField();
		name.setEditable(true);
		grid.add(name, 1, 1);

		Text addressLabel = new Text(" Address : ");
		addressLabel.setFont(myFont);
		addressLabel.setWrappingWidth(150);
		addressLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(addressLabel, 0, 2);

		address = new TextField();
		address.setEditable(true);
		grid.add(address, 1, 2);

		Text cityLabel = new Text(" City : ");
		cityLabel.setFont(myFont);
		cityLabel.setWrappingWidth(150);
		cityLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(cityLabel, 0, 3);

		city = new TextField();
		city.setEditable(true);
		grid.add(city, 1, 3);
		
		Text stateLabel = new Text(" State Code : ");
		stateLabel.setFont(myFont);
		stateLabel.setWrappingWidth(150);
		stateLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(stateLabel, 0, 4);

		stateCode = new TextField();
		stateCode.setEditable(true);
		grid.add(stateCode, 1, 4);

		Text zipLabel = new Text(" Zipcode : ");
		zipLabel.setFont(myFont);
		zipLabel.setWrappingWidth(150);
		zipLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(zipLabel, 0, 5);

		zip = new TextField();
		zip.setEditable(true);
		grid.add(zip, 1, 5);

		Text emailLabel = new Text(" Email : ");
		emailLabel.setFont(myFont);
		emailLabel.setWrappingWidth(150);
		emailLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(emailLabel, 0, 6);

		email = new TextField();
		email.setEditable(true);
		grid.add(email, 1, 6);
		
		Text dobLabel = new Text(" Date of Birth : ");
		dobLabel.setFont(myFont);
		dobLabel.setWrappingWidth(150);
		dobLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(dobLabel, 0, 7);

		dateOfBirth = new TextField();
		dateOfBirth.setEditable(true);
		grid.add(dateOfBirth, 1, 7);
		
		Text statLabel = new Text(" Status : ");
		statLabel.setFont(myFont);
		statLabel.setWrappingWidth(150);
		statLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(statLabel, 0, 8);
		
		status = new ComboBox<>(FXCollections.observableArrayList(Arrays.asList("Active", "Inactive")));
		status.setEditable(true);
		status.getSelectionModel().select("Active");
		grid.add(status, 1, 8);

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
		
		p.setProperty("name", name.getText());;
		p.setProperty("address", address.getText());
		p.setProperty("city", city.getText());
		p.setProperty("stateCode", stateCode.getText());;
		p.setProperty("zip", zip.getText());;
		p.setProperty("email", email.getText());;
		p.setProperty("dateOfBirth", dateOfBirth.getText());;
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
		if (name.getText() == null || name.getText().isEmpty()) {
			return new Pair<>(true, "Book Title field is empty.");
		}
		if (zip.getText() == null || zip.getText().isEmpty()) {
			return new Pair<>(true, "Author field is empty.");
		}
		if (city.getText() == null || city.getText().isEmpty()) {
			return new Pair<>(true, "Publication Year field is empty.");
		}
	
		try {
			int year = Integer.parseInt(city.getText());
			
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
		name.setText((String)myModel.getState("name"));
		address.setText((String)myModel.getState("address"));
		city.setText((String)myModel.getState("city"));
		stateCode.setText((String)myModel.getState("stateCode"));
		zip.setText((String)myModel.getState("zip"));
		email.setText((String)myModel.getState("email"));
		dateOfBirth.setText((String)myModel.getState("dateOfBirth"));
		status.setValue((String)myModel.getState("status"));
	}

	/**
	 * Update method
	 */
	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
		clearErrorMessage();

		if (key.equals("name") == true)
		{
			String val = (String)value;
			name.setText(val);
			displayMessage("Service Charge Imposed: $ " + val);
		}
		if (key.equals("city") == true)
		{
			String val = (String)value;
			city.setText(val);
			displayMessage("Service Charge Imposed: $ " + val);
		}
		if (key.equals("stateCOde") == true)
		{
			String val = (String)value;
			stateCode.setText(val);
			displayMessage("Service Charge Imposed: $ " + val);
		}
		if (key.equals("zip") == true)
		{
			String val = (String)value;
			zip.setText(val);
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


