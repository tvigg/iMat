package iMat;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;



public class IMatCategoryListItem extends AnchorPane {
    private Controller parentController;
    private OurCategory category;

    @FXML private Button categoryLabel;

    public IMatCategoryListItem(OurCategory category, Controller iMatCategoryController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("iMatListCategories.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }
        this.category = category;
        this.parentController = iMatCategoryController;

        categoryLabel.setText(category.getName());

    }

    @FXML public void onClickCategory(Event event){
        parentController.onClickedCategory(category.getProductCategory());
        parentController.updateHeadline(category.getName());
    }
}
