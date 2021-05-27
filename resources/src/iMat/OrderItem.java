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
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.FileInputStream;
import java.io.IOException;

public class OrderItem extends AnchorPane {
    public final ShoppingItem item;
    private Controller parentController;
    @FXML private ImageView orderItemImage;
    @FXML private Label orderItemName;
    @FXML private Label orderItemPrice;
    @FXML private Label orderItemCount;
    @FXML private Label orderItemSum;
    @FXML private Button orderAgainButton;

    public OrderItem(ShoppingItem item, Controller controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("iMat_orderitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.item = item;
        this.parentController = controller;
        this.orderItemName.setText(item.getProduct().getName());
        this.orderItemPrice.setText(Controller.priceFormat(item.getProduct().getPrice())+ " " + item.getProduct().getUnit());
        this.orderItemCount.setText(Controller.priceFormat(item.getAmount()) + " " + item.getProduct().getUnitSuffix());
        this.orderItemSum.setText(Controller.priceFormat(item.getTotal()) + " kr");

        try {
            String imagePath = System.getProperty("user.home") + "/.dat215/imat/images/" + item.getProduct().getImageName();

            Image image = new Image(new FileInputStream(imagePath));
            orderItemImage.setImage(image);
        } catch (Exception e) {
            System.err.println("Failed to load image: " + e);
        }
    }
}
