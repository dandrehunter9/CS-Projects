package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class BookTableModel
{
	private final SimpleStringProperty bookId;
	private final SimpleStringProperty bookTitle;
	private final SimpleStringProperty author;
	private final SimpleStringProperty pubYear;
	private final SimpleStringProperty status;

	//----------------------------------------------------------------------------
	public BookTableModel(Vector<String> accountData)
	{
		bookId =  new SimpleStringProperty(accountData.elementAt(0));
		bookTitle =  new SimpleStringProperty(accountData.elementAt(1));
		author =  new SimpleStringProperty(accountData.elementAt(2));
		pubYear =  new SimpleStringProperty(accountData.elementAt(3));
		status =  new SimpleStringProperty(accountData.elementAt(4));

	}

	//----------------------------------------------------------------------------
	public String getAccountNumber() {
        return bookId.get();
    }

	//----------------------------------------------------------------------------
    public void setAccountNumber(String number) {
        bookId.set(number);
    }

    //----------------------------------------------------------------------------
    public String getAccountType() {
        return bookTitle.get();
    }

    //----------------------------------------------------------------------------
    public void setAccountType(String aType) {
        bookTitle.set(aType);
    }

    //----------------------------------------------------------------------------
    public String getBalance() {
        return author.get();
    }

    //----------------------------------------------------------------------------
    public void setBalance(String bal) {
        author.set(bal);
    }
    
    //----------------------------------------------------------------------------
    public String getServiceCharge() {
        return pubYear.get();
    }

    //----------------------------------------------------------------------------
    public void setServiceCharge(String charge)
    {
    	pubYear.set(charge);
    }
    //----------------------------------------------------------------------------
    public String getStatus() {
        return status.get();
    }

    //----------------------------------------------------------------------------
    public void setStatus(String charge)
    {
    	status.set(charge);
    }
}
