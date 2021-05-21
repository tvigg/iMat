package iMat;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.ProductCategory;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class IMatCategoryController {

    @FXML
    private FlowPane categoryListFlowPane;
    public void cat(){
        // Bara testar att skriva ut kategorierna. Kan tas bort
        System.out.println(Arrays.toString(ProductCategory.values()));
    }

    private void updateCategoryList(){
        categoryListFlowPane.getChildren().clear();

//        List<Recipe> recipeList = backendController.getRecipes();
//        RecipeListItem recipeListItem;
//        for (ProductCategory productCategory : ProductCategory.values()) {

//            recipeListItem = recipeListItemMap.get(recipe.getName());
//            recipeListFlowPane.getChildren().add(recipeListItem);
        }
//        for (ProductCategory productCategory : ProductCategory.values()) {

            //recipeListItem = recipeListItemMap.get(recipe.getName());
            //recipeListFlowPane.getChildren().add(recipeListItem);
//        }
    
}

