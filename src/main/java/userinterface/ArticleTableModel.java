package userinterface;

//system imports
import java.util.Vector;
import javafx.beans.property.SimpleStringProperty;

public class ArticleTableModel {
    private final SimpleStringProperty id;
    private final SimpleStringProperty description;
    private final SimpleStringProperty barcodePrefix;
    private final SimpleStringProperty alphaCode;
    private final SimpleStringProperty status;

    //----------------Constructor------------------------------
    public ArticleTableModel(Vector<String> article){
        id = new SimpleStringProperty(article.elementAt(0));
        description = new SimpleStringProperty(article.elementAt(1));
        barcodePrefix = new SimpleStringProperty(article.elementAt(2));
        alphaCode = new SimpleStringProperty(article.elementAt(3));
        status = new SimpleStringProperty(article.elementAt(4));
    }

    //-------------------Accessor Methods---------------------------
    public String getId(){return id.get();}
    public String getDescription(){
        return description.get();
    }

    public void setDescription(String desc){
        description.set(desc);
    }

    public String getBarcodePrefix(){
        return barcodePrefix.get();
    }

    public void setBarcodePrefix(String prefix){
        barcodePrefix.set(prefix);
    }

    public String getAlphaCode(){
        return alphaCode.get();
    }

    public void setAlphaCode(String code){
        alphaCode.set(code);
    }

    public String getStatus(){
        return status.get();
    }

    public void setStatus(String stat){
        status.set(stat);
    }
}
