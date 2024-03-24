// specify the package
package model;

// system imports
import java.util.Hashtable;
import java.util.Properties;

import javafx.stage.Stage;
import javafx.scene.Scene;

// project imports
import impresario.IModel;
import impresario.ISlideShow;
import impresario.IView;
import impresario.ModelRegistry;

import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import event.Event;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

/** The class containing the Teller  for the ATM application */
//==============================================================
public class Librarian implements IView, IModel
// This class implements all these interfaces (and does NOT extend 'EntityBase')
// because it does NOT play the role of accessing the back-end database tables.
// It only plays a front-end role. 'EntityBase' objects play both roles.
{
	// For Impresario
	private Properties dependencies;
	private ModelRegistry myRegistry;


	// GUI Components
	private Hashtable<String, Scene> myViews;
	private Stage	  	myStage;

	private String loginErrorMessage = "";

	// constructor for this class
	//----------------------------------------------------------
	public Librarian()
	{
		myStage = MainStageContainer.getInstance();
		myViews = new Hashtable<String, Scene>();

		// STEP 3.1: Create the Registry object - if you inherit from
		// EntityBase, this is done for you. Otherwise, you do it yourself
		myRegistry = new ModelRegistry("Librarian");
		if(myRegistry == null)
		{
			new Event(Event.getLeafLevelClassName(this), "Librarian",
				"Could not instantiate Registry", Event.ERROR);
		}

		// STEP 3.2: Be sure to set the dependencies correctly
		setDependencies();

		// Set up the initial view
		createAndShowLibrarianView();
	}

	//-----------------------------------------------------------------------------------
	private void setDependencies()
	{
		dependencies = new Properties();
		myRegistry.setDependencies(dependencies);
	}

	/**
	 * Method called from client to get the value of a particular field
	 * held by the objects encapsulated by this object.
	 *
	 * @param	key	Name of database column (field) for which the client wants the value
	 *
	 * @return	Value associated with the field
	 */
	//----------------------------------------------------------
	public Object getState(String key)
	{
		
			return null;
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value) {
	    switch (key) {
	        case "newBook": {
	            bookCreation();
	            break;
	        }
	        case "newPatron": {
	            newPatron();
	            break;
	        }
	        case "searchBooks": {
	            searchBooks();
	            break;
	        }
	        case "DisplayBooks": {
	            displayBooks((String) value);
	            break;
	        }
	        case "searchPatrons": {
	            searchPatrons();
	            break;
	        }
	        case "DisplayPatrons": {
	            displayPatrons((String) value);
	            break;
	        }
	        default:
	            break;
	    }
	}
		


	
	public void newPatron() {
		Patron patron = new Patron();
		patron.createAndShowPatronView();
	}
		
	public void displayPatrons(String zip) {
		PatronCollection patronCollection = new PatronCollection(zip);
		patronCollection.createAndShowPatronCollectionView();

		}
	
	private void searchBooks() {

		Scene currentScene = (Scene)myViews.get("BookSearchView");

		if (currentScene == null) {
		View newView = ViewFactory.createView("BookSearchView", this);
		currentScene = new Scene(newView);
		myViews.put("BookSearchView", currentScene);
		}
		swapToView(currentScene);
		}
	
	
	public void displayBooks(String title) {
		BookCollection bc = new BookCollection();
		bc.createAndShowBookCollectionView();
	}
	
	
	public void searchPatrons() {

		Scene currentScene = (Scene)myViews.get("PatronSearchView");




		if (currentScene == null) {
		View newView = ViewFactory.createView("PatronSearchView", this);
		currentScene = new Scene(newView);
		myViews.put("PatronSearchView", currentScene);
	}

		swapToView(currentScene);
		}



	/** Called via the IView relationship */
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		// DEBUG System.out.println("Teller.updateState: key: " + key);

		stateChangeRequest(key, value);
	}

	/**
	 * Login AccountHolder corresponding to user name and password.
	 */
	//----------------------------------------------------------
	

	/**
	 * Create a Transaction depending on the Transaction type (deposit,
	 * withdraw, transfer, etc.). Use the AccountHolder holder data to do the
	 * create.
	 */
	//----------------------------------------------------------
	public void bookCreation() {
		Book book = new Book();
		book.createAndShowBookView();
		
	}

	//----------------------------------------------------------


	//----------------------------------------------------------
	

	//------------------------------------------------------------
	private void createAndShowLibrarianView()
	{
		Scene currentScene = (Scene)myViews.get("LibrarianView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("LibrarianView", this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("LibrarianView", currentScene);
		}
				
		swapToView(currentScene);
		
	}


	/** Register objects to receive state updates. */
	//----------------------------------------------------------
	public void subscribe(String key, IView subscriber)
	{
		// DEBUG: System.out.println("Cager[" + myTableName + "].subscribe");
		// forward to our registry
		myRegistry.subscribe(key, subscriber);
	}

	/** Unregister previously registered objects. */
	//----------------------------------------------------------
	public void unSubscribe(String key, IView subscriber)
	{
		// DEBUG: System.out.println("Cager.unSubscribe");
		// forward to our registry
		myRegistry.unSubscribe(key, subscriber);
	}



	//-----------------------------------------------------------------------------
	public void swapToView(Scene newScene)
	{

		
		if (newScene == null)
		{
			System.out.println("Teller.swapToView(): Missing view for display");
			new Event(Event.getLeafLevelClassName(this), "swapToView",
				"Missing view for display ", Event.ERROR);
			return;
		}

		myStage.setScene(newScene);
		myStage.sizeToScene();
		
			
		//Place in center
		WindowPosition.placeCenter(myStage);

	}

}

