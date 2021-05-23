package iMat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.ProductCategory;
import java.io.IOException;



public class IMatCategoryListItem extends AnchorPane {
    private Controller parentController;
    private ProductCategory productCategory;

    @FXML
    private Button categoryLabel;

    public IMatCategoryListItem(OurProductCategory category, Controller iMatCategoryController){
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

        categoryLabel.setText(category.getName());
    }
}
