// specify the package
package userinterface;

// system imports
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

// project imports
import impresario.IModel;

import java.util.Properties;

/** The class containing the Account View  for the ATM application */
//==============================================================
public class ColorView extends View {

    // GUI components
    protected TextField description;
    protected TextField barcodePrefix;
    protected TextField alphaCode;

    protected ComboBox<String> status;
    protected Button doneButton;

    protected Button submitButton;

    // For showing error message
    protected MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public ColorView(IModel book) {
        super(book, "ColorView");

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
        //myModel.subscribe("ColorView", this);
        myModel.subscribe("ColorSavedMessage", this);
    }

    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text("       Clothes Closet System          ");
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

        Text prompt = new Text("COLOR INFORMATION");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Text authorLabel = new Text(" Description : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        authorLabel.setFont(myFont);
        authorLabel.setWrappingWidth(150);
        authorLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(authorLabel, 0, 1);

        ColorView.this.description = new TextField();
        ColorView.this.description.setEditable(true);
        grid.add(ColorView.this.description, 1, 1);

        Text titleLabel = new Text(" Barcode Prefix : ");
        titleLabel.setFont(myFont);
        titleLabel.setWrappingWidth(150);
        titleLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(titleLabel, 0, 2);

        ColorView.this.barcodePrefix = new TextField();
        ColorView.this.barcodePrefix.setEditable(true);
        grid.add(ColorView.this.barcodePrefix, 1, 2);

        Text pubYearLabel = new Text(" Alpha Code : ");
        pubYearLabel.setFont(myFont);
        pubYearLabel.setWrappingWidth(150);
        pubYearLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(pubYearLabel, 0, 3);

        ColorView.this.alphaCode = new TextField();
        ColorView.this.alphaCode.setEditable(true);
        grid.add(ColorView.this.alphaCode, 1, 3);

        Text statusLabel = new Text(" Status : ");
        statusLabel.setFont(myFont);
        statusLabel.setWrappingWidth(150);
        statusLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(statusLabel, 0, 4);

        status = new ComboBox<>();
        status.getItems().addAll("Active", "Inactive");
        // Set a default value if needed
        status.setValue("Active");
        grid.add(status, 1, 4);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);

        doneButton = new Button("Done");
        doneButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        doneButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                myModel.stateChangeRequest("done", null);
            }
        });
        doneCont.getChildren().add(doneButton);

        submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitButton.setOnAction(this::ProcessAction);
        doneCont.getChildren().add(submitButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(doneCont);

        return vbox;
    }

    private void ProcessAction(ActionEvent actionEvent) {
        clearErrorMessage();

        String descriptionEntered = description.getText();
        String prefixEntered = barcodePrefix.getText();
        String alphaEntered = alphaCode.getText();
        String statusSelected = (String) status.getValue();

        //Pattern yearRegex = Pattern.compile("^(18\\d\\d|19\\d\\d|200\\d|201\\d|202[0-9])$");

        if ((descriptionEntered == null) || (descriptionEntered.length() == 0)) {
            displayErrorMessage("!!!Please enter a description!!!!");
            description.requestFocus();
        } else if ((prefixEntered == null) || (prefixEntered.length() == 0)) {
            displayErrorMessage("!!!Please enter a barcode prefix!!!");
            barcodePrefix.requestFocus();
        } else if ((prefixEntered == null) || (prefixEntered.length() == 0)) {
            displayErrorMessage("!!!Please enter an aplha code!!!");
            alphaCode.requestFocus();
        } else {
            Properties props = new Properties();
            props.setProperty("Description", descriptionEntered);
            props.setProperty("Barcode_Prefix", prefixEntered);
            props.setProperty("Alpha_Code", alphaEntered);
            props.setProperty("Status", statusSelected);
            System.out.println(descriptionEntered + "" + prefixEntered + "" + alphaEntered+ "" + statusSelected);
            myModel.stateChangeRequest("NewColorData", props);
        }
    }

    // Create the status log field
    //-------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);
        return statusLog;
    }

    //-------------------------------------------------------------
    public void populateFields() {
        ColorView.this.description.setText((String) myModel.getState("Description"));
        ColorView.this.barcodePrefix.setText((String) myModel.getState("Barcode Prefix"));
        ColorView.this.alphaCode.setText((String) myModel.getState("Alpha Code"));
    }

    /**
     * Update method
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value) {
        clearErrorMessage();
        if (key.equals("ColorSavedMessage") == true)
        {
            String msg = (String)value;
            if ((msg.startsWith("ERR")) || (msg.startsWith("Err")))
            {
                displayErrorMessage(msg);
            }
            else
            {
                displayMessage(msg);
            }
        }

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



