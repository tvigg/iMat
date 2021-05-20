package iMat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.io.IOException;
import java.util.Arrays;


public class IMatCategoryListItem extends AnchorPane {
    private IMatCategoryController parentController;
    private ProductCategory productCategory;

    @FXML
    private Label categoryLabel;

    public IMatCategoryListItem(ProductCategory category, IMatCategoryController iMatCategoryController){
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

        categoryLabel.setText(category.toString());
    }
}
