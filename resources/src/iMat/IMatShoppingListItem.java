package iMat;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingCart;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.FileInputStream;
import java.io.IOException;


public class IMatShoppingListItem extends AnchorPane {
    private Controller parentController;

    public ShoppingItem getItem() {
        return item;
    }

    private ShoppingItem item;

    @FXML private Label productText;
    @FXML private Label productPrice;
    @FXML private Label productAmount;
    @FXML private Button removeOne;
    @FXML private Button addOne;

    public IMatShoppingListItem(ShoppingItem item, Controller iMatCategoryController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("iMatShoppingItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }
        this.item = item;
        this.parentController = iMatCategoryController;

        productText.setText(item.getProduct().getName());
        productPrice.setText(String.valueOf(item.getProduct().getPrice()));
        updateAmount();
    }

    public void updateAmount() {
        productAmount.setText(String.valueOf((int)item.getAmount()) + " " + item.getProduct().getUnitSuffix());
    }

    @FXML
    public void addOne(Event event) {
        item.setAmount(item.getAmount() + 1.0);
        getCart().fireShoppingCartChanged(item,true);
        updateAmount();
    }
    @FXML
    public void removeOne(Event event) {
        item.setAmount(item.getAmount() - 1.0);
        updateAmount();
        getCart().fireShoppingCartChanged(item, false);
        if (item.getAmount() <= 0.0) {
            getCart().removeItem(item);
            parentController.removeShoppingItem(this);
        }
    }

    private ShoppingCart getCart() {
        return IMatDataHandler.getInstance().getShoppingCart();
    }
}
