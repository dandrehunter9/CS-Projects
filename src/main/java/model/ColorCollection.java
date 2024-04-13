package model;

import impresario.IView;

// system imports
import java.util.Collections;
import java.util.Properties;
import java.util.Vector;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

public class ColorCollection extends EntityBase implements IView {

    private static final String myTableName = "Color";

    private Vector<Color> colorList;

    public ColorCollection() {

        super(myTableName);

        colorList = new Vector();
    }

    //----------------------------------------------------------------------------------
    private void addColor(Color color)
    {
        //accounts.add(a);
        int index = findIndexToAdd(color);
        colorList.insertElementAt(color,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(Color color)
    {
        //users.add(u);
        int low=0;
        int high = colorList.size()-1;
        int middle;

        while (low <= high)
        {
            middle = (low+high)/2;

            Color midSession = colorList.elementAt(middle);

            int result = Color.compare(color,midSession);

            if (result ==0)
            {
                return middle;
            }
            else if (result<0)
            {
                high=middle-1;
            }
            else
            {
                low=middle+1;
            }


        }
        return low;
    }

    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    @Override
    public Object getState(String key) {
        if (key.equals("Colors"))
            return colorList;
        else
        if (key.equals("ColorList"))
            return this;
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }

    @Override
    protected void initializeSchema(String tableName) {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }

    // Custom implemented methods
    /*public void findBooksOlderThanDate(String year) throws InvalidPrimaryKeyException{

        String query = "SELECT * FROM " + myTableName + " WHERE pubYear < '" + year + "'";
        mapDbData(query);
    }

    public void findBooksNewerThanDate(String year) throws InvalidPrimaryKeyException{
        String query = "SELECT * FROM " + myTableName + " WHERE pubYear > '" + year + "'";
        mapDbData(query);
    }

    public void findBooksWithTitleLike(String title) throws InvalidPrimaryKeyException {

        String query = "SELECT * FROM " + myTableName + " WHERE bookTitle LIKE '%" + title + "%' ORDER BY author";
        mapDbData(query);
    }

    public void findBooksWithAuthorLike(String author) throws InvalidPrimaryKeyException {

        String query = "SELECT * FROM " + myTableName + " WHERE author LIKE '%" + author + "%'";
        mapDbData(query);
    }*/
    public void findColorWithAlphaCodeLike(String alphaCode) throws InvalidPrimaryKeyException {

        String query = "SELECT * FROM " + myTableName + " WHERE Alpha_Code LIKE '%" + alphaCode + "%' AND Status != 'Inactive'";
        mapDbData(query);
    }

    public void findColorWithDescriptionLike(String description) throws InvalidPrimaryKeyException {

        String query = "SELECT * FROM " + myTableName + " WHERE Description LIKE '%" + description + "%' AND Status != 'Inactive'";
        mapDbData(query);
    }

    public void display() {
        if ((colorList == null) || (colorList.size() == 0))
        {
            System.out.println("No colors matching criteria found!");
            return;
        }
        for(int i = 0; i < colorList.size(); i++) {
            colorList.elementAt(i).display();
        }
    }

    public void mapDbData(String query) throws InvalidPrimaryKeyException
    {
        Vector allDataRetrieved = getSelectQueryResult(query);
        if(allDataRetrieved != null)
        {
            for(int count = 0; count < allDataRetrieved.size(); count++)
            {
                Properties nextColorData = (Properties)allDataRetrieved.elementAt(count);

                Color color = new Color(nextColorData);

                if(color != null)
                {
                    addColor(color);
                }
            }
        }
        else
        {
            throw new InvalidPrimaryKeyException("No colors found!!!.");
        }
    }
}
