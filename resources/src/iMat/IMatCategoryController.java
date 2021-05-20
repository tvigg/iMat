package iMat;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.ProductCategory;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class IMatCategoryController implements Initializable{
    Controller backendController = new Controller();

    @FXML
    private VBox categoryListFlowPane;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateCategoryList();
    }



    private void updateCategoryList(){
        categoryListFlowPane.getChildren().clear();
        ProductCategory[] categoryList = backendController.getCategories();


        for (ProductCategory category : categoryList){
            var button = new IMatCategoryListItem(category, this);
            categoryListFlowPane.getChildren().add(button);
        }

    }


}
