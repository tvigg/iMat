package iMat;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;


public class IMatProductListItem extends AnchorPane {
    private Controller parentController;
    private Product product;

    @FXML private ImageView productImage;
    @FXML private Label productText;
    @FXML private Label productPrice;
    @FXML private Label productAmount;
    @FXML private Button removeOne;
    @FXML private Button addOne;
    @FXML private Label sale;

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
            productImage.setImage(IMatDataHandler.getInstance().getFXImage(product, 221, 165));
        } catch (Exception e) {
            System.err.println("Failed to load image: " + e);
        }

        productText.setText(product.getName());
        productPrice.setText(Controller.priceFormat(product.getPrice()) + " " + product.getUnit());
        ShoppingItem item = getShoppingItem();
        double amount = item != null ? item.getAmount() : 0.0;
        sale.setVisible(false);

        updateAmount(item);
    }

    public void updateAmount(ShoppingItem item) {
        removeOne.setDisable(item == null ? true : !(item.getAmount() > 0));
        double value = item == null ? 0.0 : item.getAmount();
        productAmount.setText(Controller.priceFormat(value) + " " + product.getUnitSuffix());
    }

    private ShoppingItem getShoppingItem() {
        for (ShoppingItem item : IMatDataHandler.getInstance().getShoppingCart().getItems()) {
            if (item.getProduct().equals(product))
                return item;
        }
        return null;
    }

    @FXML
    public void addOne(Event event) {
        ShoppingItem item = getShoppingItem();
        if (item != null) {
            item.setAmount(item.getAmount() + 1.0);
            IMatDataHandler.getInstance().getShoppingCart().fireShoppingCartChanged(item,true);
        } else {
            item = new ShoppingItem(product, 1.0);
            IMatDataHandler.getInstance().getShoppingCart().addItem(item);
        }
        updateAmount(item);
    }
    @FXML
    public void removeOne(Event event) {
        ShoppingCart cart = IMatDataHandler.getInstance().getShoppingCart();
        ShoppingItem item = getShoppingItem();
        if (item != null) {
            item.setAmount(item.getAmount() - 1.0);
            if (item.getAmount() <= 0.0) {
                cart.removeItem(item);
            }
            updateAmount(item);
            IMatDataHandler.getInstance().getShoppingCart().fireShoppingCartChanged(item,false);
        }
    }

    private ShoppingCart getCart() {
        return IMatDataHandler.getInstance().getShoppingCart();
    }

    public void setSale(boolean isSale) {
        sale.setVisible(isSale);
    }
}
