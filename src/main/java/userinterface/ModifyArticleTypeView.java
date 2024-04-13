package userinterface;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Article;
import model.Color;

import java.util.Properties;

public class ModifyArticleTypeView extends View {

    protected TextField description;
    protected TextField barcodePrefix;
    protected TextField alphaCode;
    protected Button submitButton;
    protected Button doneButton;
    protected MessageView statusLog;
    private Article articleType;

    public ModifyArticleTypeView(IModel model) {
        super(model, "ModifyArticleTypeView");
        articleType = (Article) myModel.getState("Selected Article");
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

        Text titleText = new Text(" Modify Article Type ");
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

        //ArrayList<String> choices = new ArrayList<String>();
        //choices.add("Alpha Code");
        //choices.add("Description");
        //ComboBox alphaOrDescr = new ComboBox(FXCollections.observableArrayList(choices));
        //grid.add(alphaOrDescr, 1, 0);

        Label descLabel = new Label("Description:");
        grid.add(descLabel, 0, 0);

        description = new TextField();
        grid.add(description, 1, 0);

        Label barcodeLabel = new Label("Barcode Prefix:");
        grid.add(barcodeLabel, 0, 1);

        barcodePrefix = new TextField();
        grid.add(barcodePrefix, 1, 1);

        Label alphaLabel = new Label("Alpha Code:");
        grid.add(alphaLabel, 0, 2);

        alphaCode = new TextField();
        grid.add(alphaCode, 1, 2);


        description.setText((String)articleType.getState("Description"));
        barcodePrefix.setText((String) articleType.getState("Barcode_Prefix"));
        alphaCode.setText((String) articleType.getState("Alpha_Code"));

        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        doneButton = new Button("Done");
        doneButton.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        doneButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                myModel.stateChangeRequest("done", null);
            }
        });
        doneCont.getChildren().add(doneButton);
        submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                char[] prefix = barcodePrefix.getText().toCharArray();
                if(prefix[0] == '1' || prefix[0] == '0') {
                    Properties p = setProperties();
                    myModel.stateChangeRequest("UpdateArticleType", p);
                }else{
                    displayErrorMessage("Invalid barcode prefix");
                }
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

    public Properties setProperties(){
        Properties p = new Properties();
        p.setProperty("Id", (String)articleType.getState("Id"));
        p.setProperty("Description", description.getText());
        p.setProperty("Barcode_Prefix", barcodePrefix.getText());
        p.setProperty("Alpha_Code", alphaCode.getText());
        return p;
    }
}


