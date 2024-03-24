// specify the package
package userinterface;

// project imports
import impresario.IModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/** The class containing the Account View  for the ATM application */
//==============================================================
public class BookSearchView extends View
{

	// GUI components
	private TextField title;

	private Button submitButton;
	private Button doneButton;

	// For showing error message
	private MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public BookSearchView(IModel book) {
		super(book, "BookSearchView");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// Add a title for this panel
		container.getChildren().add(createTitle());
		
		// create our GUI components, add them to this Container
		container.getChildren().add(createFormContent());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);

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
        
        Text prompt = new Text("SEARCH BOOK");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

		grid.add(createText("Title"), 0, 2);
		title = new TextField();
		title.setEditable(true);
		grid.add(title, 1, 2);
		
		HBox doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);

		submitButton = createButton("Submit", "DisplayBooks", true);
		doneButton = createButton("Done", "Done", false);

		doneCont.getChildren().add(submitButton);
		doneCont.getChildren().add(doneButton);
		vbox.getChildren().add(grid);
		vbox.getChildren().add(doneCont);

		return vbox;
	}
	
	private Button createButton(String title, String state, boolean value) {
	    Font myFont = Font.font("Arial", FontWeight.BOLD, 14);
	    Button button = new Button(title);
	    button.setFont(myFont);
	    button.setOnAction(e -> {
	        clearErrorMessage();
	        myModel.stateChangeRequest(state, value == true ? this.title.getText() : null);
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

	// Create the status log field
	//-------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage) {
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	//-------------------------------------------------------------
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
