package iMat;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.io.IOException;


public class IMatCategoryList extends AnchorPane {
    private IMatCategoryController parentController;
    private ProductCategory productCategory;

    public IMatCategoryList(ProductCategory productCategory, IMatCategoryController iMatCategoryController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("iMatListCategories.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }
        this.productCategory = productCategory;
        this.parentController = iMatCategoryController;
    }

    //fxmlLoader.setRoot(this);

}
