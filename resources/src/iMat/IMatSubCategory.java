package iMat;

import iMat.Controller;
import iMat.OurProductCategory;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.ProductCategory;

import javax.swing.text.html.ImageView;
import java.io.IOException;



public class IMatSubCategory extends AnchorPane {
    private Controller parentController;
    private ProductCategory productCategory;

    @FXML private Label subCategoryLabel;
//    @FXML private ImageView subCategoryImage;
    @FXML protected void onClick(Event event){ parentController.openRecipeView(productCategory); }

    public IMatSubCategory(ProductCategory category, Controller iMatCategoryController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("iMatSubCategory.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }
        this.productCategory = productCategory;
        this.parentController = iMatCategoryController;

        subCategoryLabel.setText(category.toString());
    }
}
