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

    @FXML private ImageView orderItemImage;
    @FXML private Label orderItemPrice;
    @FXML private Label orderItemCount;
    @FXML private Label orderItemSum;
    @FXML private Label orderItemName;

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
        updateAmount();
    }

    public IMatShoppingListItem(int i, ShoppingItem item, Controller iMatCategoryController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("iMat_orderitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }
        this.item = item;
        this.parentController = iMatCategoryController;

        orderItemImage.setImage(IMatDataHandler.getInstance().getFXImage(item.getProduct()));
        orderItemName.setText(item.getProduct().getName());
        orderItemPrice.setText(String.valueOf(item.getProduct().getPrice() + item.getProduct().getUnit()));

        orderItemCount.setText(String.valueOf(item.getAmount()));
        orderItemSum.setText("Summa");
        orderItemSum.setText(Controller.priceFormat(item.getProduct().getPrice() * item.getAmount()) + " kr");

        //updateAmount();
    }

    public void updateAmount() {
        productAmount.setText(String.valueOf((int)item.getAmount()) + " " + item.getProduct().getUnitSuffix());
        productPrice.setText(Controller.priceFormat(item.getProduct().getPrice() * item.getAmount()) + " kr");
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
