package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

// project imports
import exception.InvalidPrimaryKeyException;

import impresario.IView;

public class Article extends EntityBase implements IView {
    private static final String myTableName = "Article_Type";

    protected Properties dependencies;

    private String updateStatusMessage = "";

    // Constructors
    public Article(Properties props) {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements() == true) {
            String nextKey = (String) allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null) {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }

    public Article() {
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
    }

    public Article(String Id) throws InvalidPrimaryKeyException
    {
        super(myTableName);
        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE Id = '" + Id + "'";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one account. More than that is an error
            if (size != 1)
            {
                throw new InvalidPrimaryKeyException("Multiple articles matching id : "
                        + Id + " found.");
            }
            else
            {
                // copy all the retrieved data into persistent state
                Properties retrievedArticleData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedArticleData.propertyNames();
                while (allKeys.hasMoreElements() == true)
                {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedArticleData.getProperty(nextKey);

                    if (nextValue != null)
                    {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
        }
        // If no account found for this user name, throw an exception
        else
        {
            throw new InvalidPrimaryKeyException("No article matching id : "
                    + Id + " found.");
        }
    }

    private void setDependencies()
    {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }

    @Override
    public void updateState(String key, Object value) {

    }

    @Override
    public Object getState(String key) {
        if(key.equals("UpdateStatusMessage") == true) {
            return updateStatusMessage;
        }

        return persistentState.getProperty(key);
    }

    @Override
    public void stateChangeRequest(String key, Object value) {

        myRegistry.updateSubscribers(key, this);
        if(key.equals("InsertArticle")) {
            Article article = new Article((Properties) value);
            article.save();
        }
    }

    public static int compare(Article a, Article b)
    {
        String aNum = (String)a.getState("Barcode_Prefix");
        String bNum = (String)b.getState("Barcode_Prefix");

        return aNum.compareTo(bNum);
    }

    public String toString() {
        return "Description: " + persistentState.getProperty("Description") + "; Barcode Prefix: " +
                persistentState.getProperty("Barcode_Prefix") + "; Alpha Code: " +
                persistentState.getProperty("Alpha_Code") + "; Status: " +
                persistentState.getProperty("Status");
    }

    public void display() {
        System.out.println(this.toString());
    }

    public void save()
    {
        updateStateInDatabase();
    }

    public void processNewArticle(Properties articleInfo) {
        persistentState = new Properties();
        Enumeration allKeys = articleInfo.propertyNames();
        while (allKeys.hasMoreElements()) {
            String nextKey = (String) allKeys.nextElement();
            String nextValue = articleInfo.getProperty(nextKey);

            if (nextValue != null) {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
        this.save();
    }
    //-----------------------------------------------------------------------------------
    public void updateStateInDatabase()
    {
        try
        {
            if (persistentState.getProperty("Id") != null)
            {
                // update
                Properties whereClause = new Properties();
                whereClause.setProperty("Id",
                        persistentState.getProperty("Id"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Book data for Article ID: " + persistentState.getProperty("Id") + " updated successfully in database!";
            }
            else
            {
                // insert
                Integer Id =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                System.out.println(Id);
                persistentState.setProperty("Id", "" + Id.intValue());
                updateStatusMessage = "Article data for new Article : " +  persistentState.getProperty("Id")
                        + "installed successfully in database!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing article data in database!";
        }
    }

    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();
        v.addElement(persistentState.getProperty("Id"));
        v.addElement(persistentState.getProperty("Description"));
        v.addElement(persistentState.getProperty("Barcode_Prefix"));
        v.addElement(persistentState.getProperty("Alpha_Code"));
        v.addElement(persistentState.getProperty("Status"));

        return v;
    }

    @Override
    protected void initializeSchema(String tableName) {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}
