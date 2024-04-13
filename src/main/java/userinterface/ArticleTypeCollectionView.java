package userinterface;
import javax.swing.table.TableModel;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;


import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;



// project imports
import impresario.IModel;
import userinterface.ArticleTableModel;
import userinterface.MessageView;
import userinterface.View;

import java.util.Enumeration;
import java.util.Vector;
import model.ArticleTypeCollection;
import model.Article;


public class ArticleTypeCollectionView extends View {
    private MessageView statusLog;
    private TableView<ArticleTableModel> articleTable;
    private Button backButton;

    //-----------------------Constructor------------------------------
    public ArticleTypeCollectionView(IModel article){
        super(article, "ArticleTypeCollectionView");

        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());
        container.getChildren().add(createStatusLog("              "));

        getChildren().add(container);
        populateFields();
        myModel.subscribe("UpdateStateMessage", this);
    }
    private Node createTitle(){
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text("\t\t Professional Clothes Closet System");
        titleText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.BLACK);
        container.getChildren().add(titleText);

        return container;
    }
    private VBox createFormContent(){

        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text label = new Text("Article Information: ");
        label.setWrappingWidth(400);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFill(Color.BLACK);
        grid.add(label, 0, 0); //what are these numbers doing


        articleTable = new TableView<>();

        articleTable.getColumns().addAll(
                createColumn("Description", 200, "description"),
                createColumn("Barcode Prefix", 100, "barcodePrefix"),
                createColumn("AlphaCode", 100, "alphaCode"),
                createColumn("Status", 100, "status"));
        // Create context menu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem updateItem = new MenuItem("Update");
        MenuItem deleteItem = new MenuItem("Delete");

// Set actions for menu items
        updateItem.setOnAction(e -> processArticleModification());
        deleteItem.setOnAction(e -> processArticleDeletion());

// Add menu items to context menu
        contextMenu.getItems().addAll(updateItem, deleteItem);

// Set context menu to appear when right-clicking on a row
        articleTable.setContextMenu(contextMenu);
        //grid.add(articleTable, 0, 1);
        articleTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    contextMenu.show(articleTable, event.getScreenX(), event.getScreenY());
                }
            }
        });
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(115, 150);
        scrollPane.setContent(articleTable);
        grid.add(scrollPane, 0, 1);

        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);

        backButton = new Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                myModel.stateChangeRequest("done", null);
            }
        });
        buttonContainer.getChildren().add(backButton);
        vbox.getChildren().add(grid);
        vbox.getChildren().add(buttonContainer);
        return vbox;

    }
    private TableColumn createColumn(String title, int width, String propertyName){
        TableColumn column = new TableColumn(title);
        column.setMinWidth(width);
        column.setCellValueFactory(new PropertyValueFactory<TableModel, String>(propertyName));

        return column;
    }

    private Button createButton(String title, String state){
        Button button = new Button(title);
        button.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e){
                myModel.stateChangeRequest(state, null);
            }
        });
        return button;
    }

    protected MessageView createStatusLog(String initialMessage){
        statusLog = new MessageView(initialMessage);
        return statusLog;
    }

    //-----------------------Other Methods---------------------------------------------------
    public void populateFields(){
        getEntryTableModelValues();
    }

    public void updateState(String key, Object value){
        clearErrorMessage();
    }

    public void displayErrorMessage(String message) {
        statusLog.displayErrorMessage(message);
    }

    public void displayMessage(String message) {
        statusLog.displayMessage(message);
    }

    public void clearErrorMessage() {
        statusLog.clearErrorMessage();
    }

    public void getEntryTableModelValues(){
        ObservableList<ArticleTableModel> tableData = FXCollections.observableArrayList();
        try{
            ArticleTypeCollection articleList = (ArticleTypeCollection)myModel.getState("ArticleList");

            Vector<Article> entryList = (Vector<Article>) articleList.getState("ArticleTypes");
            Enumeration<?> entries = entryList.elements();

            while(entries.hasMoreElements()){
                Article nextArticle = (Article)entries.nextElement();
                Vector<String> view = nextArticle.getEntryListView();

                ArticleTableModel nextTableRowData = new ArticleTableModel(view);
                tableData.add(nextTableRowData);
            }
            articleTable.setItems(tableData);
        }   catch(Exception e){
            //SQL Exception
            e.toString();
            e.printStackTrace();
        }
    }

    protected void processArticleModification()
    {
        ArticleTableModel selectedItem = articleTable.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            String id = selectedItem.getId();
            myModel.stateChangeRequest("ModifySelectedArticle", id);
        }
    }

    protected void processArticleDeletion()
    {
        ArticleTableModel selectedItem = articleTable.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            String id = selectedItem.getId();
            myModel.stateChangeRequest("DeleteSelectedArticle", id);
        }
    }
}