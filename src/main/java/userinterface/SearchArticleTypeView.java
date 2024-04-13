package userinterface;

//GUI Imports
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import impresario.IModel;
import model.Clerk;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchArticleTypeView extends View{
    protected TextField searchCriteria;
    protected Button backButton;
    protected Button submitButton;
    protected MessageView statusLog;

    //--------------------Constructor----------------------------
    public SearchArticleTypeView(IModel article){
        super (article, "SearchArticleTypeView");

        //Creating Container
        VBox cont = new VBox(10);
        cont.setPadding(new Insets(15, 5, 5, 5));
        cont.getChildren().add(createTitle());
        cont.getChildren().add(createFormContent());
        cont.getChildren().add(createStatusLog("\t\t\t\t"));

        getChildren().add(cont);

        populateFields();
        myModel.subscribe("UpdateStatusMessage", this);
    }

    //----------------------Create Title Container----------------------------------------
    private Node createTitle(){
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Search Article Type ");
        titleText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.BLACK);
        container.getChildren().add(titleText);

        return container;
    }

    //---------------------Create Main Form Content---------------------------------------
    private VBox createFormContent(){
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label searchCriteriaLabel = new Label("Search by:");
        grid.add(searchCriteriaLabel, 0, 0);

        ArrayList<String> choices = new ArrayList<String>();
        choices.add("Alpha Code");
        choices.add("Description");
        ComboBox alphaOrDescr = new ComboBox(FXCollections.observableArrayList(choices));
        grid.add(alphaOrDescr, 1, 0);

        Label typeInfoLabel = new Label("Article Information: ");
        grid.add(typeInfoLabel, 0, 1);

        searchCriteria = new TextField();
        grid.add(searchCriteria, 1, 1);

        HBox buttonCont = new HBox(10);
        buttonCont.setAlignment(Pos.CENTER);
        backButton = new Button("Back");
        backButton.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e){
                myModel.stateChangeRequest("done", null);
            }
        });
        buttonCont.getChildren().add(backButton);

        submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        submitButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                clearErrorMessage();
                if(alphaOrDescr.getValue().equals("Alpha Code")){
                    myModel.stateChangeRequest("ObtainArticleCollectionAlpha", searchCriteria.getText());
                }else if(alphaOrDescr.getValue().equals("Description")){
                    //System.out.println("GETTING HERE");
                    myModel.stateChangeRequest("ObtainArticleCollectionDesc", searchCriteria.getText());
                }

                searchCriteria.clear();
            }
        });
        buttonCont.getChildren().add(submitButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(buttonCont);

        return vbox;
    }

    //--------------------Create Status Log Field-----------------------------------------
    protected MessageView createStatusLog(String initialMessage){
        statusLog = new MessageView(initialMessage);
        return statusLog;
    }

    //------------------------------------------------------------------------------------
    public void populateFields(){}

    public void updateState(String key, Object value){clearErrorMessage();}

    //---------------------Message Stuff--------------------------------------------------
    public void displayErrorMessage(String message){
        statusLog.displayErrorMessage(message);
    }

    public void displayMessage(String message){
        statusLog.displayMessage(message);
    }

    public void clearErrorMessage(){
        statusLog.clearErrorMessage();
    }

}
