package model;

import event.Event;
import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

import java.util.Hashtable;
import java.util.Properties;

public class Clerk implements IView, IModel {

    private Properties dependencies;
    private ModelRegistry myRegistry;
    private Hashtable<String, Scene> myViews;
    private Stage myStage;
    private String colorSavedMessage = "";
    private String patronSavedMessage = "";
    private String bookSavedMessage = "";
    private String errorMessage = "";
    private ColorCollection colorCollection;
    private ArticleTypeCollection articleTypeCollection;
    private Color selectedColor;
    private Article selectedArticle;


    public Clerk()
    {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();

        myRegistry = new ModelRegistry("Clerk");
        if(myRegistry == null)
        {
            new Event(Event.getLeafLevelClassName(this), "Clerk",
                    "Could not instantiate Registry", Event.ERROR);
        }

        setDependencies();
        createAndShowClerkView();
    }

    private void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("NewArticleData", "ArticleSavedMessage");
        dependencies.setProperty("NewColorData", "ColorSavedMessage");
        myRegistry.setDependencies(dependencies);
    }

    private void createAndShowClerkView()
    {
        Scene currentScene = (Scene)myViews.get("ClerkView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("ClerkView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("ClerkView", currentScene);
        }

        swapToView(currentScene);

    }

    public void swapToView(Scene newScene)
    {
        if (newScene == null)
        {
            System.out.println("Clerk.swapToView(): Missing view for display");
            new Event(Event.getLeafLevelClassName(this), "swapToView",
                    "Missing view for display ", Event.ERROR);
            return;
        }
        myStage.setScene(newScene);
        myStage.sizeToScene();
        WindowPosition.placeCenter(myStage); // centers the scene

    }
//----------------------------------------------------------------------------------------------------------
    public void createNewArticle() {
        createAndShowArticleView();
    }

    public void createNewColor() {
        createAndShowColorView();
    }

    protected void InsertColor(Properties value) {
        Color color = new Color();
        color.processNewColor(value);
        colorSavedMessage = "Color with ID: " + color.getState("Id") +
                " saved successfully!";
    }

    protected void InsertArticle(Properties value) {
        Article article = new Article();
        article.processNewArticle(value);
        bookSavedMessage = "Article with ID: " + article.getState("Id") +
                " saved successfully!";
    }
    //--------------------------------------------------------------------------------------------
    protected void createAndShowColorView()
    {
        View newView = ViewFactory.createView("ColorView", this);
        Scene newScene = new Scene(newView);
        swapToView(newScene);
    }
    protected void createAndShowArticleView()
    {
        View newView = ViewFactory.createView("ArticleView", this);
        Scene newScene = new Scene(newView);
        swapToView(newScene);
    }

    protected void createAndSearchColorView()
    {
        View newView = ViewFactory.createView("SearchColorsView", this);
        Scene newScene = new Scene(newView);
        swapToView(newScene);
    }

    protected void createAndSearchArticleView()
    {
        View newView = ViewFactory.createView("SearchArticleTypeView", this);
        Scene newScene = new Scene(newView);
        swapToView(newScene);
    }

    protected void createAndShowArticleCollectionView()
    {
        View newView = ViewFactory.createView("ArticleTypeCollectionView", this);
        Scene newScene = new Scene(newView);
        swapToView(newScene);
    }

    protected void createAndShowColorCollectionView()
    {
        View newView = ViewFactory.createView("ColorCollectionView", this);
        Scene newScene = new Scene(newView);
        swapToView(newScene);
    }

    protected void createAndShowModifyColorView()
    {
        View newView = ViewFactory.createView("ModifyColorView", this);
        Scene newScene = new Scene(newView);
        swapToView(newScene);
    }

    protected void createAndShowDeleteColorView()
    {
        View newView = ViewFactory.createView("DeleteColorView", this);
        Scene newScene = new Scene(newView);
        swapToView(newScene);
    }

    protected void createAndShowArticleTypeCollectionView()
    {
        View newView = ViewFactory.createView("ArticleTypeCollectionView", this);
        Scene newScene = new Scene(newView);
        swapToView(newScene);
    }

    protected void createAndShowDeleteArticleTypeView()
    {
        View newView = ViewFactory.createView("DeleteArticleTypeView", this);
        Scene newScene = new Scene(newView);
        swapToView(newScene);
    }

    protected void createAndShowModifyArticleTypeView()
    {
        View newView = ViewFactory.createView("ModifyArticleTypeView", this);
        Scene newScene = new Scene(newView);
        swapToView(newScene);
    }
    //-----------------------------------------------------------------------------------------------------
    @Override
    public Object getState(String key) {
        if(key.equals("ColorList")){
            return colorCollection;
        }
        if(key.equals("Selected Color")){
            return selectedColor;
        }
        if(key.equals("ArticleList")){
            return articleTypeCollection;
        }
        if(key.equals("Selected Article")){
            return selectedArticle;
        }
        return null;
    }
//------------------------------------------------------------------------------------------------------------
    @Override
    public void subscribe(String key, IView subscriber) {
        myRegistry.subscribe(key, subscriber);
    }

    @Override
    public void unSubscribe(String key, IView subscriber) {
        myRegistry.unSubscribe(key, subscriber);
    }
//----------------------------------------------------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key){
            case "ColorView":
                createAndShowColorView();
                break;
            case "insert article":
                createAndShowArticleView();
                break;
            case "SearchArticleView":
                createAndSearchArticleView();
                break;
            case "SearchColorView":
                createAndSearchColorView();
                break;
            case "ObtainArticleCollectionAlpha":
                searchArticleTypeByAlphaCode((String) value);
                createAndShowArticleTypeCollectionView();
                break;
            case "ObtainArticleCollectionDesc":
                searchArticleTypeByDescription((String) value);
                createAndShowArticleTypeCollectionView();
                break;
            case "ObtainColorCollectionAlpha":
                searchColorByAlphaCode((String) value);
                createAndShowColorCollectionView();
                break;
            case "ObtainColorCollectionDesc":
                searchColorByDescription((String) value);
                createAndShowColorCollectionView();
                break;
            case "ModifySelectedColor":
                getSelectedColor((String) value);
                createAndShowModifyColorView();
                break;
            case "UpdateColor":
                selectedColor = new Color((Properties) value);
                selectedColor.updateStateInDatabase();
                break;
            case "DeleteSelectedColor":
                getSelectedColor((String) value);
                createAndShowDeleteColorView();
                break;
            case "DeleteColor":
                selectedColor = new Color((Properties) value);
                selectedColor.updateStateInDatabase();
                createAndShowClerkView();
                break;
            case "ModifySelectedArticle":
                getSelectedArticle((String) value);
                createAndShowModifyArticleTypeView();
                break;
            case "UpdateArticleType":
                selectedArticle = new Article((Properties) value);
                selectedArticle.updateStateInDatabase();
                break;
            case "DeleteSelectedArticle":
                getSelectedArticle((String) value);
                createAndShowDeleteArticleTypeView();
                break;
            case "DeleteArticleType":
                selectedArticle = new Article((Properties) value);
                selectedArticle.updateStateInDatabase();
                createAndShowClerkView();
                break;
            case "NewArticleData":
                InsertArticle((Properties) value);
                break;
            case "NewColorData":
                InsertColor((Properties) value);
                break;
            case "done":
                createAndShowClerkView();
                break;

        }

    }
//--------------------------------------------------------------------------------------------------------------
    private void searchColorByAlphaCode(String alphaCode)
    {
        colorCollection = new ColorCollection();
        try {
            colorCollection.findColorWithAlphaCodeLike(alphaCode);
        }catch(InvalidPrimaryKeyException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void searchColorByDescription(String desc)
    {
        colorCollection = new ColorCollection();
        try {
            colorCollection.findColorWithDescriptionLike(desc);
        }catch(InvalidPrimaryKeyException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void searchArticleTypeByAlphaCode(String alphaCode)
    {
        articleTypeCollection = new ArticleTypeCollection();
        try {
            articleTypeCollection.findArticleTypeWithAlphaCodeLike(alphaCode);
        }catch(InvalidPrimaryKeyException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void searchArticleTypeByDescription(String desc){
        articleTypeCollection = new ArticleTypeCollection();
        try{
            articleTypeCollection.findArticleTypeWithDescriptionLike(desc);
        }catch(InvalidPrimaryKeyException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void searchArticleTypeByDesc(String desc)
    {
        articleTypeCollection = new ArticleTypeCollection();
        try {
            articleTypeCollection.findArticleTypeWithDescriptionLike(desc);
        }catch(InvalidPrimaryKeyException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void getSelectedColor(String id){
        try {
            selectedColor = new Color(id);
        } catch (InvalidPrimaryKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public void getSelectedArticle(String id){
        try{
            selectedArticle = new Article(id);
        }catch(InvalidPrimaryKeyException e){
            throw new RuntimeException(e);
        }
    }
    //-------------------------------------------------------------------------------------------------------------
    @Override
    public void updateState(String key, Object value) {

    }
}
