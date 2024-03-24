// specify the package
package model;

// system imports
import java.util.Properties; 
import java.util.Vector;

import javafx.scene.Scene;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;


/** The class containing the AccountCollection for the ATM application */
//==============================================================
public class PatronCollection  extends EntityBase implements IView
{
	private static final String myTableName = "Patron";

	private Vector<Patron> patronList;
	// GUI Components

	// constructor for this class
	//----------------------------------------------------------
	public PatronCollection() {
		super(myTableName);
		
		
		patronList = new Vector<>();
	}
	public PatronCollection(String zip) {
		super(myTableName);
		try {
			findPatronsAtZIpCode(zip);
		} catch (Exception e){
			patronList = new Vector<>();
		}
	}
	
	public Vector<Patron> findPatronsOlderThan(String year) throws InvalidPrimaryKeyException {
		String query = "Select FROM " + myTableName + " WHERE dateOfBirth < " + year;
		
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			patronList = new Vector<Patron>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextBookData = (Properties)allDataRetrieved.elementAt(cnt);

				Patron patron = new Patron(nextBookData);

				if (patron != null)
				{
					patronList.add(patron);
				}
			}

		}
		else
		{
			throw new InvalidPrimaryKeyException("No corresponding books found");
		}
		
		return patronList;

	}
	
	public Vector<Patron> findPatronsYoungerThan(String date) throws InvalidPrimaryKeyException {
		String query = "Select FROM " + myTableName + " WHERE dateOfBirth < '" + date + "'";
		
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			patronList = new Vector<Patron>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextBookData = (Properties)allDataRetrieved.elementAt(cnt);

				Patron patron = new Patron(nextBookData);

				if (patron != null)
				{
					patronList.add(patron);
				}
			}

		}
		else
		{
			throw new InvalidPrimaryKeyException("No corresponding books found");
		}
		
		return patronList;

	}
	public Vector<Patron> findPatronsAtZIpCode(String zip) throws InvalidPrimaryKeyException {
		String query = "Select FROM " + myTableName + " WHERE zip = '" + zip + "'ORDER BY name ASC";
		
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			patronList = new Vector<Patron>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextBookData = (Properties)allDataRetrieved.elementAt(cnt);

				Patron patron = new Patron(nextBookData);

				if (patron != null)
				{
					patronList.add(patron);
				}
			}

		}
		else
		{
			throw new InvalidPrimaryKeyException("No corresponding books found");
		}
		
		return patronList;

	}
	
	public Vector<Patron> findPatronsWithNameLike(String name) throws InvalidPrimaryKeyException {
		String query = "Select FROM " + myTableName + " WHERE name LIKE '%" + name + "%'";
		
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			patronList = new Vector<Patron>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextBookData = (Properties)allDataRetrieved.elementAt(cnt);

				Patron patron = new Patron(nextBookData);

				if (patron != null)
				{
					patronList.add(patron);
				}
			}

		}
		else
		{
			throw new InvalidPrimaryKeyException("No corresponding books found");
		}
		
		return patronList;

	}
	
	public void createAndShowPatronCollectionView()
	{
		Scene currentScene = (Scene)myViews.get("PatronCollectionView");

		View newView = ViewFactory.createView("PatronCollectionView", this); // USE VIEW FACTORY
		currentScene = new Scene(newView);
		myViews.put("PatronCollectionView", currentScene);
		
		swapToView(currentScene);
		
	}

	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("BookList"))
			return patronList;
		else
		if (key.equals("BookList"))
			return this;
		return null;
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		
		myRegistry.updateSubscribers(key, this);
	}

	//----------------------------------------------------------

	/** Called via the IView relationship */
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		stateChangeRequest(key, value);
	}

	//------------------------------------------------------

	//-----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}
}
