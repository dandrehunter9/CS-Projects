package userinterface;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

// project imports
import impresario.IModel;
import model.Color;

import java.util.ArrayList;

public class SearchColorsView extends View {

    protected TextField color;
    protected Button submitButton;
    protected Button doneButton;
    protected MessageView statusLog;

    public SearchColorsView(IModel color) {
        super(color, "SearchColorsView");

        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        //container.getChildren().add();
        // Add a title for this panel
        container.getChildren().add(createTitle());

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContent());

        container.getChildren().add(createStatusLog("             "));

        getChildren().add(container);

        //populateFields();

        //myModel.subscribe("ServiceCharge", this);
        myModel.subscribe("UpdateStatusMessage", this);
    }

    //create the title container
    private Node createTitle() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Search Colors ");
        titleText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        //titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }

    //create the main form content
    private VBox createFormContent() {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label searchLabel = new Label("Search by:");
        grid.add(searchLabel, 0, 0);
        ArrayList<String> choices = new ArrayList<String>();
        choices.add("Alpha Code");
        choices.add("Description");
        ComboBox alphaOrDescr = new ComboBox(FXCollections.observableArrayList(choices));
        grid.add(alphaOrDescr, 1, 0);

        Label colorLabel = new Label("Color information:");
        grid.add(colorLabel, 0, 1);

        color = new TextField();
        grid.add(color, 1, 1);

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        doneButton = new Button("Back");
        doneButton.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        doneButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                myModel.stateChangeRequest("done", null);
            }
        });
        doneCont.getChildren().add(doneButton);
        submitButton = new Button("Search");
        submitButton.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                //Obtain Color Collection
                if(alphaOrDescr.getValue() != null) {
                    if (alphaOrDescr.getValue().equals("Alpha Code")) {
                        myModel.stateChangeRequest("ObtainColorCollectionAlpha", color.getText());
                    } else if (alphaOrDescr.getValue().equals("Description")) {
                        myModel.stateChangeRequest("ObtainColorCollectionDesc", color.getText());
                    }
                }else{
                    displayErrorMessage("Please select a search by value!");
                }
                color.clear();
            }
        });
        doneCont.getChildren().add(submitButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(doneCont);

        return vbox;
    }
    protected MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    public void populateFields() {
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


