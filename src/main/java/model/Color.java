package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

// project imports
import exception.InvalidPrimaryKeyException;

import impresario.IView;

public class Color extends EntityBase implements IView {

    // Instance Variables
    private static final String myTableName = "Color";

    protected Properties dependencies;

    private String updateStatusMessage = "";

    // Constructors
    public Color(Properties props) {
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

    public Color() {
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
    }

    public Color(String colorId) throws InvalidPrimaryKeyException
    {
        super(myTableName);

        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE Id = '" + colorId + "'";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one account. More than that is an error
            if (size != 1)
            {
                throw new InvalidPrimaryKeyException("Multiple colors  matching id : "
                        + colorId + " found.");
            }
            else
            {
                // copy all the retrieved data into persistent state
                Properties retrievedBookData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedBookData.propertyNames();
                while (allKeys.hasMoreElements() == true)
                {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedBookData.getProperty(nextKey);

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
            throw new InvalidPrimaryKeyException("No color matching id : "
                    + colorId + " found.");
        }
    }

    private void setDependencies()
    {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }

    // Extended Methods
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
        if(key.equals("InsertColor")) {
            Color color = new Color((Properties) value);
            color.save();
        }
        if(key.equals("ModifyColor")){

        }
        if(key.equals("BookCancelled")) {

        }
    }

    public void processNewColor(Properties colorInfo) {
        persistentState = new Properties();
        Enumeration allKeys = colorInfo.propertyNames();
        while (allKeys.hasMoreElements()) {
            String nextKey = (String) allKeys.nextElement();
            String nextValue = colorInfo.getProperty(nextKey);

            if (nextValue != null) {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
        this.save();
    }

    @Override
    protected void initializeSchema(String tableName) {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }

    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    public void save() // save()
    {
        updateStateInDatabase();
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
                updateStatusMessage = "Color data for ID: " + persistentState.getProperty("Id") + " updated successfully in database!";
            }
            else
            {
                // insert
                //System.out.println(persistentState.getProperty("author"));
                Integer ColorId =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                //System.out.println(bookId);
                persistentState.setProperty("Id", "" + ColorId.intValue());
                updateStatusMessage = "Color data for new Color : " +  persistentState.getProperty("Id")
                        + "installed successfully in database!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing color data in database!";
        }
    }

    public static int compare(Color a, Color b)
    {
        String aNum = (String)a.getState("Description");
        String bNum = (String)b.getState("Description");

        return aNum.compareTo(bNum);
    }

    public String toString() {
        return "Description: " + persistentState.getProperty("Description") + "; Barcode Prefix: " +
                persistentState.getProperty("Barcode_Prefix") + "; Alpha Code: " +
                persistentState.getProperty("Alpha_Code") + "; Status: " +
                persistentState.getProperty("Status");
    }
    public void display() {
        System.out.println(toString());
    }

    /**
     * This method is needed solely to enable the Account information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
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
}
