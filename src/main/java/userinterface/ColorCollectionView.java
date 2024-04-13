package userinterface;

//system imports

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
//import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

// project imports
import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ColorCollection;
import model.Color;

// project imports
import impresario.IModel;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;


public class ColorCollectionView extends View{
   // private TableView<ColorTableModel> colorTable;
    private MessageView statusLog;
    //private Button backButton;
    private TableView<ColorTableModel> colorTable;
    private Button backButton;

    //-----------------------Constructor------------------------------
    public ColorCollectionView(IModel color){
        super(color, "ColorCollectionView");

        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());
        container.getChildren().add(createStatusLog("              "));

        getChildren().add(container);
        populateFields();
        myModel.subscribe("UpdateStateMessage", this);
    }

    //-------------------Create Methods---------------------------------------------
    private Node createTitle(){
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text("\t\t Professional Clothes Closet System");
        titleText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        //titleText.setFill(Color.BLACK);
        container.getChildren().add(titleText);

        return container;
    }

    private VBox createFormContent(){
        //System.out.println("GETTING HERE");
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text label = new Text("Color Information: ");
        label.setWrappingWidth(400);
        label.setTextAlignment(TextAlignment.CENTER);
        //label.setFill(Color.BLACK);
        grid.add(label, 0, 0);



        colorTable = new TableView<>();

        colorTable.getColumns().addAll(
            createColumn("Description", 200, "description"),
            createColumn("Barcode Prefix", 100, "barcodePrefix"),
            createColumn("AlphaCode", 100, "alphaCode"),
            createColumn("Status", 100, "status"));
        // Create context menu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem updateItem = new MenuItem("Update");
        MenuItem deleteItem = new MenuItem("Delete");

// Set actions for menu items
        updateItem.setOnAction(e -> processColorModification());
        deleteItem.setOnAction(e -> processColorDeletion());

// Add menu items to context menu
        contextMenu.getItems().addAll(updateItem, deleteItem);

// Set context menu to appear when right-clicking on a row
        colorTable.setContextMenu(contextMenu);
        grid.add(colorTable, 0, 1);
        colorTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    contextMenu.show(colorTable, event.getScreenX(), event.getScreenY());
                }
            }
        });
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(115, 150);
        scrollPane.setContent(colorTable);

        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        backButton = new Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                myModel.stateChangeRequest("done", null);
            }
        });

        buttonContainer.getChildren().add(backButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(scrollPane);
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
        ObservableList<ColorTableModel> tableData = FXCollections.observableArrayList();
        try{
            ColorCollection colorList = (ColorCollection)myModel.getState("ColorList");

            Vector<Color> entryList = (Vector<Color>) colorList.getState("Colors");
            Enumeration<?> entries = entryList.elements();

            while(entries.hasMoreElements()){
                Color nextColor = (Color)entries.nextElement();
                Vector<String> view = nextColor.getEntryListView();

                ColorTableModel nextTableRowData = new ColorTableModel(view);
                tableData.add(nextTableRowData);
            }
            colorTable.setItems(tableData);
        }   catch(Exception e){
            //SQL Exception
            e.toString();
            e.printStackTrace();
        }
    }

    protected void processColorModification()
    {
        ColorTableModel selectedItem = colorTable.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            String id = selectedItem.getId();
            myModel.stateChangeRequest("ModifySelectedColor", id);
        }
    }

    protected void processColorDeletion()
    {
        ColorTableModel selectedItem = colorTable.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            String id = selectedItem.getId();
            myModel.stateChangeRequest("DeleteSelectedColor", id);
        }
    }



}
