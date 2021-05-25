package iMat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;


public class IMatProductListItem extends AnchorPane {
    private Controller parentController;
    private Product product;

    @FXML private ImageView productImage;
    @FXML private Label productText;
    @FXML private Label productPrice;
    @FXML private Label productPieces;
    @FXML private Label productAmount;
    @FXML private Button removeOne;
    @FXML private Button addOne;

    public IMatProductListItem(Product product, Controller iMatCategoryController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("iMatListProducts.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }
        this.product = product;
        this.parentController = iMatCategoryController;

        try {
            String imagePath = System.getProperty("user.home") + "/.dat215/imat/images/" + product.getImageName();
            Image image = new Image(new FileInputStream(imagePath));
            productImage.setImage(image);
        } catch (Exception e) {
            System.err.println("Failed to load image: " + e);
        }

        productText.setText(product.getName());
        productPrice.setText(String.valueOf(product.getPrice()));
        productPieces.setText(product.getUnit());
        productAmount.setText("1");
    }
}
