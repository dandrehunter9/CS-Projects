// specify the package
package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

// project imports
import exception.InvalidPrimaryKeyException;
import database.*;

import impresario.IView;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the Account for the ATM application */
//==============================================================
public class Book extends EntityBase implements IView
{
	private static final String myTableName = "Book";

	protected Properties dependencies;

	// GUI Components

	private String updateStatusMessage = "";

	// constructor for this class
	//----------------------------------------------------------
	public Book(String bookId)
		throws InvalidPrimaryKeyException
	{
		super(myTableName);

		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (bookId = " + bookId + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one account at least
		if (allDataRetrieved != null)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one account. More than that is an error
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple accounts matching id : "
					+ bookId + " found.");
			}
			else
			{
				// copy all the retrieved data into persistent state
				Properties retrievedAccountData = allDataRetrieved.elementAt(0);
				persistentState = new Properties();

				Enumeration allKeys = retrievedAccountData.propertyNames();
				while (allKeys.hasMoreElements() == true)
				{
					String nextKey = (String)allKeys.nextElement();
					String nextValue = retrievedAccountData.getProperty(nextKey);
					// accountNumber = Integer.parseInt(retrievedAccountData.getProperty("accountNumber"));

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
			throw new InvalidPrimaryKeyException("No account matching id : "
				+ bookId + " found.");
		}
	}

	// Can also be used to create a NEW Account (if the system it is part of
	// allows for a new account to be set up)
	//----------------------------------------------------------
	public Book(Properties props)
	{
		super(myTableName);

		setDependencies();
		persistentState = new Properties();
		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements() == true)
		{
			String nextKey = (String)allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);

			if (nextValue != null)
			{
				persistentState.setProperty(nextKey, nextValue);
			}
		}
	}
	//-----------------------------------------------------------------------------------
	public Book() {
		super(myTableName);
		myViews = new Hashtable<String, Scene>();
		
	}
	//-----------------------------------------------------------------------------------

	public void processNewBook(Properties p) {
		persistentState.putAll(p);
	}
	//-----------------------------------------------------------------------------------

	
	public void createAndShowBookView()
	{
		Scene currentScene = (Scene)myViews.get("BookView");

		View newView = ViewFactory.createView("BookView", this); // USE VIEW FACTORY
		currentScene = new Scene(newView);
		myViews.put("BookView", currentScene);
		
		swapToView(currentScene);
		
	}


	//-----------------------------------------------------------------------------------
	private void setDependencies()
	{
		dependencies = new Properties();
	
		myRegistry.setDependencies(dependencies);
	}

	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("UpdateStatusMessage") == true)
			return updateStatusMessage;

		return persistentState.getProperty(key);
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{

		myRegistry.updateSubscribers(key, this);
	}

	/** Called via the IView relationship */
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		stateChangeRequest(key, value);
	}

	//---------------------------------------------------------
	//-----------------------------------------------------------------------------------
	public static int compare(Book a, Book b)
	{
		String aNum = (String)a.getState("bookId");
		String bNum = (String)b.getState("bookId");

		return aNum.compareTo(bNum);
	}

	//-----------------------------------------------------------------------------------
	public void update() // save()
	{
		updateStateInDatabase();
	}
	
	//-----------------------------------------------------------------------------------
	private void updateStateInDatabase() 
	{
		try
		{
			if (persistentState.getProperty("bookId") != null)
			{
				// update
				Properties whereClause = new Properties();
				whereClause.setProperty("bookID",
				persistentState.getProperty("bookID"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Book data for account number : " + persistentState.getProperty("bookId") + " updated successfully in database!";
			}
			else
			{
				// insert
				Integer bookId =
					insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("bookId", "" + bookId.intValue());
				updateStatusMessage = "Book data for new book : " +  persistentState.getProperty("bookId")
					+ "installed successfully in database!";
			}
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "Error in installing account data in database!";
		}
		//DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
	}

	//--------------------------------------------------------------------------
	public Vector<String> getEntryListView()
	{
		Vector<String> v = new Vector<String>();

		v.addElement(persistentState.getProperty("bookId"));
		v.addElement(persistentState.getProperty("bookTitle"));
		v.addElement(persistentState.getProperty("author"));
		v.addElement(persistentState.getProperty("pubYear"));
		v.addElement(persistentState.getProperty("status"));


		return v;
	}

	//-----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}
	public String toString()
	{
	return "Title: " + persistentState.getProperty("bookTitle") + "; Author: " + 
	persistentState.getProperty("author") + "; Year: " + persistentState.getProperty("pubYear") ;
	}
}
	

