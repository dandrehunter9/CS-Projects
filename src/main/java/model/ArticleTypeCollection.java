package model;

import impresario.IView;

// system imports
import java.util.Properties;
import java.util.Vector;

// project imports
import exception.InvalidPrimaryKeyException;
//import EntityBase;


public class ArticleTypeCollection extends EntityBase implements IView {

    private static final String myTableName = "Article_Type";

    private Vector<Article> articleTypeList;

    public ArticleTypeCollection() {

        super(myTableName);

        articleTypeList = new Vector();
    }

    //----------------------------------------------------------------------------------
    private void addArticleType(Article articleType)
    {
        //accounts.add(a);
        int index = findIndexToAdd(articleType);
        articleTypeList.insertElementAt(articleType,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(Article articleType)
    {
        //users.add(u);
        int low=0;
        int high = articleTypeList.size()-1;
        int middle;

        while (low <= high)
        {
            middle = (low+high)/2;

            Article midSession = articleTypeList.elementAt(middle);

            int result = Article.compare(articleType,midSession);

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
        if (key.equals("ArticleTypes"))
            return articleTypeList;
        else
        if (key.equals("ArticleTypeList"))
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
    public void findArticleTypeWithAlphaCodeLike(String alphaCode) throws InvalidPrimaryKeyException {

        String query = "SELECT * FROM " + myTableName + " WHERE Alpha_Code LIKE '%" + alphaCode + "%' AND Status != 'Inactive'";
        mapDbData(query);
    }

    public void findArticleTypeWithDescriptionLike(String description) throws InvalidPrimaryKeyException {

        String query = "SELECT * FROM " + myTableName + " WHERE Description LIKE '%" + description + "%' AND Status != 'Inactive'";
        mapDbData(query);
    }

    public void display() {
        if ((articleTypeList == null) || (articleTypeList.size() == 0))
        {
            System.out.println("No article types matching criteria found!");
            return;
        }
        for(int i = 0; i < articleTypeList.size(); i++) {
            articleTypeList.elementAt(i).display();
        }
    }

    public void mapDbData(String query) throws InvalidPrimaryKeyException
    {
        Vector allDataRetrieved = getSelectQueryResult(query);
        if(allDataRetrieved != null)
        {
            for(int count = 0; count < allDataRetrieved.size(); count++)
            {
                Properties nextArticleTypeData = (Properties)allDataRetrieved.elementAt(count);

                Article articleType = new Article(nextArticleTypeData);

                if(articleType != null)
                {
                    addArticleType(articleType);
                }
            }
        }
        else
        {
            throw new InvalidPrimaryKeyException("No colors found!!!.");
        }
    }
}
