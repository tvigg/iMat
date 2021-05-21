package iMat;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.FileInputStream;
import java.io.IOException;

public class OrderHistoryItem extends AnchorPane {
    private Order order;
    private Controller parentController;
    @FXML private ImageView orderItemImage;
    @FXML private Label orderHistoryItemDate;
    @FXML private Label orderHistoryItemId;
    @FXML private Label orderHistoryItemPrice;

    public OrderHistoryItem(Order order, Controller controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("iMat_orderhistoryitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.order = order;
        this.parentController = controller;
        this.orderHistoryItemDate.setText(order.getDate().toString());
        this.orderHistoryItemId.setText(String.valueOf(order.getOrderNumber()));
        double price = order.getItems().stream().map((i) -> i.getTotal()).reduce(0.0, (a, b) -> a + b);
        this.orderHistoryItemPrice.setText(price + " kr");
    }

    @FXML
    public void onClick(Event event) {
        parentController.onClickOrderHistory(order);
    }
}
