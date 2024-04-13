
// specify the package
package userinterface;

// system imports

import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

// project imports
import impresario.IModel;
import model.Article;
import model.Clerk;

/** The class containing the Teller View  for the ATM application */
//==============================================================
public class ClerkView extends View
{

    // GUI stuff
    private Button insertArticle;
    private Button insertColor;
    private Button modifyArticle;
    private Button modifyColor;
    private Clerk Clerk;
    private Button done;

    // For showing error message
    private MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public ClerkView(IModel librarian)
    {
        super(librarian, "ClerkView");

        // create a container for showing the contents
        VBox container = new VBox(10);

        container.setPadding(new Insets(15, 5, 5, 5));

        // create a Node (Text) for showing the title
        container.getChildren().add(createTitle());

        // create a Node (GridPane) for showing data entry fields
        container.getChildren().add(createFormContents());

        // Error message area
        container.getChildren().add(createStatusLog("                          "));

        getChildren().add(container);

        populateFields();

        myModel.subscribe("ArticleView", this);

        myModel.subscribe("ColorView", this);

        // STEP 0: Be sure you tell your model what keys you are interested in
//        myModel.subscribe("LoginError", this);
    }

    // Create the label (Text) for the title of the screen
    //-------------------------------------------------------------
    private Node createTitle()
    {
        Text titleText = new Text("        Clothes Closet System        ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKBLUE);

        return titleText;
    }

    // Create the main form contents
    //-------------------------------------------------------------
    private GridPane createFormContents()
    {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        insertArticle = new Button("INSERT NEW ARTICLE");
        insertArticle.setOnAction(e -> myModel.stateChangeRequest("insert article", null));
        grid.add(insertArticle, 0, 0);

        insertColor = new Button("INSERT NEW COLOR");
        insertColor.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e, "insert color");
            }
        });
        grid.add(insertColor, 0, 1);

        modifyArticle = new Button("MODIFY/DELETE ARTICLE TYPE");
        modifyArticle.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e, "modify article");
            }
        });
        grid.add(modifyArticle, 0, 2);

        modifyColor = new Button("MODIFY/DELETE COLOR");
        modifyColor.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e, "modify color");
            }
        });
        grid.add(modifyColor, 0, 3);

        done = new Button("DONE");
        done.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e, "done");
            }
        });
        Insets buttonPadding = new Insets(10); // You can adjust the values as needed
        done.setPadding(buttonPadding);

        // Add the button to the GridPane with a little margin to the right
        GridPane.setMargin(done, new Insets(0, 0, 0, 15)); // Adjust the right margin as needed
        grid.add(done, 0, 4);

        return grid;
    }


    // Create the status log field
    //-------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    public void populateFields()
    {
    }

    // This method processes events generated from our GUI components.
    // Make the ActionListeners delegate to this method
    //-------------------------------------------------------------
    public void processAction(Event evt, String type)
    {
        clearErrorMessage();
        if(type.equals("insert article")) {
            Clerk = new Clerk();
            Clerk.createNewArticle();
            myModel.stateChangeRequest("ArticleView", null);
        }
        if(type.equals("insert color")) {
            Clerk = new Clerk();
            Clerk.createNewColor();
            myModel.stateChangeRequest("ColorView", null);
        }
        if(type.equals("modify color")){
            myModel.stateChangeRequest("SearchColorView", null);
        }
        if(type.equals("modify article")){
            myModel.stateChangeRequest("SearchArticleView", null);
        }

        if(type.equals("done")){
            System.exit(0);
        }
    }

    //----------------------------------------------------------

    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {

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
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }

}

