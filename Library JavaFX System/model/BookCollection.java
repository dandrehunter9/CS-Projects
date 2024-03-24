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
public class BookCollection  extends EntityBase implements IView
{
	private static final String myTableName = "Book";

	private Vector<Book> bookList;
	// GUI Components

	// constructor for this class
	//----------------------------------------------------------
	public BookCollection() {
		super(myTableName);
		
		
		bookList = new Vector<>();
	}
	public BookCollection(String title) {
		super(myTableName);
		try {
			findBooksWithTitleLike(title);
		} catch (Exception e){
			bookList = new Vector<>();
		}
	}
	
	public Vector<Book> findBooksOlderThanDate(String year) throws InvalidPrimaryKeyException {
		String query = "Select * FROM " + myTableName + " WHERE pubYear < " + year;
		
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			bookList = new Vector<Book>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextBookData = (Properties)allDataRetrieved.elementAt(cnt);

				Book book = new Book(nextBookData);

				if (book != null)
				{
					bookList.add(book);
				}
			}

		}
		else
		{
			throw new InvalidPrimaryKeyException("No corresponding books found");
		}
		
		return bookList;

	}
	
	public Vector<Book> findBooksNewerThanDate(String year) throws InvalidPrimaryKeyException {
		String query = "Select * FROM " + myTableName + " WHERE pubYear > " + year;
		
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			bookList = new Vector<Book>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextBookData = (Properties)allDataRetrieved.elementAt(cnt);

				Book book = new Book(nextBookData);

				if (book != null)
				{
					bookList.add(book);
				}
			}

		}
		else
		{
			throw new InvalidPrimaryKeyException("No corresponding books found");
		}
		
		return bookList;

	}
	public Vector<Book> findBooksWithTitleLike(String title) throws InvalidPrimaryKeyException {
		String query = "Select * FROM " + myTableName + " WHERE bookTitle LIKE '%" + title + "%' ORDER BY author ASC";
		
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			bookList = new Vector<Book>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextBookData = (Properties)allDataRetrieved.elementAt(cnt);

				Book book = new Book(nextBookData);

				if (book != null)
				{
					bookList.add(book);
				}
			}

		}
		else
		{
			throw new InvalidPrimaryKeyException("No corresponding books found");
		}
		
		return bookList;

	}
	
	public Vector<Book> findBooksWithAuthorLike(String author) throws InvalidPrimaryKeyException {
		String query = "Select * FROM " + myTableName + " WHERE author LIKE '%" + author + "%'";
		
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			bookList = new Vector<Book>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextBookData = (Properties)allDataRetrieved.elementAt(cnt);

				Book book = new Book(nextBookData);

				if (book != null)
				{
					bookList.add(book);
				}
			}

		}
		else
		{
			throw new InvalidPrimaryKeyException("No corresponding books found");
		}
		
		return bookList;

	}
	public Vector<Book> findBooksWithSameYear(String year) throws InvalidPrimaryKeyException {
		String query = "Select * FROM " + myTableName + " WHERE pubYear = " + year;
		
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{
			bookList = new Vector<Book>();

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextBookData = (Properties)allDataRetrieved.elementAt(cnt);

				Book book = new Book(nextBookData);

				if (book != null)
				{
					bookList.add(book);
				}
			}

		}
		else
		{
			throw new InvalidPrimaryKeyException("No corresponding books found");
		}
		
		return bookList;

	}


	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("BookList")) {
			return bookList;
		}
		return null;
	}

	//----------------------------------------------------------------
	public void createAndShowBookCollectionView()
	{
		Scene currentScene = (Scene)myViews.get("BookCollectionView");

		View newView = ViewFactory.createView("BookCollectionView", this); // USE VIEW FACTORY
		currentScene = new Scene(newView);
		myViews.put("BookCollectionView", currentScene);
		
		swapToView(currentScene);
		
	}
	//----------------------------------------------------------
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
