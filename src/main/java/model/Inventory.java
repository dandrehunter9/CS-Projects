package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

// project imports
import exception.InvalidPrimaryKeyException;
import database.*;

import impresario.IView;
public class Inventory extends EntityBase implements IView {
    private static final String myTableName = "Inventory";
    protected Properties dependencies;
    private String updateStatusMessage = " ";

    public Inventory(Properties props) {
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

    public Inventory() {
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
    }

    public Inventory(String barcode) throws InvalidPrimaryKeyException {
        super(myTableName);
        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE Barcode = " + barcode + "'";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {

            int size = allDataRetrieved.size();

            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple items matching id: " + barcode + " found.");
            } else {
                Properties retrievedInventoryData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedInventoryData.propertyNames();
                while (allKeys.hasMoreElements() == true) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedInventoryData.getProperty(nextKey);

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        } else {
            throw new InvalidPrimaryKeyException("No inventory matching id: " + barcode + " found.");
        }
    }

    private void setDependencies() {
        dependencies = new Properties();
        myRegistry.setDependencies(dependencies);
    }

    @Override
    public void updateState(String key, Object value) {

    }

    @Override
    public Object getState(String key) {
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
        if (key.equals("InsertInventory")) {
            Inventory inventory = new Inventory((Properties) value);
            inventory.save();
        }
    }

    public static int compare(Inventory a, Inventory b) {
        String aNum = (String)a.getState("inventoryID");
        String bNum = (String)b.getState("inventoryID");

        return aNum.compareTo(bNum);
    }

    public String toString() {
        return "Gender: " + persistentState.getProperty("gender") + "; Size: " +
                persistentState.getProperty("size") + "; Article Type " +
                persistentState.getProperty("articleType") + "; Color1: " +
                persistentState.getProperty("color1") + "; Color2: " +
                persistentState.getProperty("color2") + "; Brand: " +
                persistentState.getProperty("brand") + "; Notes: " +
                persistentState.getProperty("notes") + "; Status: " +
                persistentState.getProperty("status") + "; Donor Lastname: " +
                persistentState.getProperty("donorLastname") + "; Donor Firstname: " +
                persistentState.getProperty("donorFirstname") + "; Donor Phone: " +
                persistentState.getProperty("donorPhone") + "; Donor Email: " +
                persistentState.getProperty("donorEmail") + "; Receiver Netid: " +
                persistentState.getProperty("receiverNetID") + "; Receiver Lastname: " +
                persistentState.getProperty("receiverLastname") + "; Receiver Firstname: " +
                persistentState.getProperty("receiverFirstname") + "; Date Donated: " +
                persistentState.getProperty("dateDonated") + "; Date Taken: " +
                persistentState.getProperty("dateTaken");
    }

    public void display() {
        System.out.println(this.toString());
    }

    public void save() {
        updateStateInDatabase();
    }

    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase() {
        try {
            if (persistentState.getProperty("Barcode") != null) {
                // update
                Properties whereClause = new Properties();
                whereClause.setProperty("Barcode", persistentState.getProperty("Barcode"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Data for Barcode: " + persistentState.getProperty("Barcode") + " updated successfully in database!";
            } else {
                // insert
                //System.out.println(persistentState.getProperty("author"));
                //Integer aD =
                //insertAutoIncrementalPersistentState(mySchema, persistentState);
                //System.out.println(barcode);
                //persistentState.setProperty("inventory", "" + Barcode.intValue());
                updateStatusMessage = "Inventory data for new inventory : " + persistentState.getProperty("Barcode")
                        + "installed successfully in database!";
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error in installing article data in database!";
        }
    }

    @Override
    protected void initializeSchema(String tableName) {

    }
}


