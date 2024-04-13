package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model)
	{
		if(viewName.equals("ClerkView") == true)
		{
			return new ClerkView(model);
		}
		else if(viewName.equals("ColorCollectionView") == true){
			return new ColorCollectionView(model);
		}
		else if(viewName.equals("SearchArticleTypeView") == true){
			return new SearchArticleTypeView(model);
		}
		else if(viewName.equals("ArticleTypeCollectionView")){
			return new ArticleTypeCollectionView(model);
		}
		else if(viewName.equals("DeleteArticleTypeView")){
			return new DeleteArticleTypeView(model);
		}
		else if(viewName.equals("ModifyArticleTypeView")){
			return new ModifyArticleTypeView(model);
		}
		else if(viewName.equals("SearchColorsView")){
			return new SearchColorsView(model);
		}
		else if(viewName.equals("ModifyColorView")){
			return new ModifyColorView(model);
		}
		else if(viewName.equals("DeleteColorView")){
			return new DeleteColorView(model);
		}
		else if(viewName.equals("ColorView")){
			return new ColorView(model);
		}
		else if (viewName.equals("ArticleView")) {
			return new ArticleView(model);
		}

		/*
		else if(viewName.equals("SearchBooksView") == true)
		{
			return new SearchBooksView(model);
		}
		else if(viewName.equals("BookCollectionView") == true)
		{
			return new BookCollectionView(model);
		}
		else if(viewName.equals("SearchPatronsView") == true)
		{
			return new SearchPatronsView(model);
		}
		else if(viewName.equals("PatronCollectionView") == true)
		{
			return new PatronCollectionView(model);
		}
		else if(viewName.equals("BookView") == true)
		{
			return new BookView(model);
		}
		else if(viewName.equals("PatronView") == true)
		{
			return new PatronView(model);
		}
		else
		{
			return null;
		}*/
		return null;
	}


	/*
	public static Vector createVectorView(String viewName, IModel model)
	{
		if(viewName.equals("SOME VIEW NAME") == true)
		{
			//return [A NEW VECTOR VIEW OF THAT NAME TYPE]
		}
		else
			return null;
	}
	*/

}
