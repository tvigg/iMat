package iMat;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.*;

import java.lang.reflect.Array;
import java.util.List;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    @FXML private FlowPane orderHistoryFlowPane;
    @FXML private AnchorPane storeAnchorPane;
    @FXML private AnchorPane orderHistoryAnchorPane;
    @FXML private AnchorPane myPageAnchorPane;
    @FXML private AnchorPane orderHistoryLightbox;
    @FXML private AnchorPane categoryAnchorPane;
    @FXML private FlowPane orderHistoryDetailFlowPane;
    @FXML private AnchorPane orderItemHeader;
    @FXML private Label orderHistoryLightboxHeader;
    @FXML private VBox categoryListFlowPane;
    @FXML private GridPane myPageHome;
    @FXML private GridPane myPageRecords;

    @FXML private TextField myPageRecordsAddress;
    @FXML private TextField myPageRecordsEmail;
    @FXML private TextField myPageRecordsFirstName;
    @FXML private TextField myPageRecordsLastName;
    @FXML private TextField myPageRecordsPhoneNumber;
    @FXML private TextField myPageRecordsMobileNumber;
    @FXML private TextField myPageRecordsPostAddress;
    @FXML private TextField myPageRecordsPostCode;
    private final Map<Integer, List<OrderItem>> orderItemMap = new HashMap<>();

    private IMatDataHandler handler = IMatDataHandler.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeOrderHistory();
        updateCategoryList();
        initializeMyPageRecords();
    }

    private void updateCategoryList(){
        categoryListFlowPane.getChildren().clear();
        ProductCategory[] categoryList = getCategories();


        for (ProductCategory category : categoryList){
            var button = new IMatCategoryListItem(category, this);
            categoryListFlowPane.getChildren().add(button);
        }

    }

    private void initializeOrderHistory() {
        ObservableList<Order> orders = FXCollections.observableList(handler.getOrders());
        for (Order o : handler.getOrders()) {
            List<OrderItem> items = new ArrayList<OrderItem>();
            for (ShoppingItem i : o.getItems())
                items.add(new OrderItem(i,this));
            orderItemMap.put(o.getOrderNumber(), items);

            orderHistoryFlowPane.getChildren().add(new OrderHistoryItem(o, this));
        }

        orderHistoryFlowPane.setVgap(2);
        orderHistoryLightbox.toBack();
    }

    private void initializeMyPageRecords() {
        Customer customer = IMatDataHandler.getInstance().getCustomer();
        myPageRecordsAddress.setText(customer.getAddress());
        myPageRecordsEmail.setText(customer.getEmail());
        myPageRecordsFirstName.setText(customer.getFirstName());
        myPageRecordsLastName.setText(customer.getLastName());
        myPageRecordsPhoneNumber.setText(customer.getPhoneNumber());
        myPageRecordsMobileNumber.setText(customer.getMobilePhoneNumber());
        myPageRecordsPostAddress.setText(customer.getPostAddress());
        myPageRecordsPostCode.setText(customer.getPostCode());
    }

    public void onClickOrderHistory(Order order) {
        populateOrderHistoryLightbox(order);
        orderHistoryLightbox.toFront();
    }

    private void populateOrderHistoryLightbox(Order order) {
        orderHistoryDetailFlowPane.getChildren().clear();
        orderHistoryDetailFlowPane.getChildren().add(orderItemHeader);
        orderHistoryLightboxHeader.setText("Beställning " + order.getOrderNumber());
        orderHistoryDetailFlowPane.getChildren().addAll(orderItemMap.get(order.getOrderNumber()));
    }

    @FXML
    public void goToOrders(Event event) {
        orderHistoryAnchorPane.toFront();
        storeAnchorPane.toFront();
    }

    @FXML
    public void goToMyPage(Event event) {
        myPageAnchorPane.toFront();
        myPageHome.toFront();
        storeAnchorPane.toFront();
    }

    @FXML
    public void editUserRecords(Event event) {
        myPageRecords.toFront();
    }

    @FXML
    public void saveUserRecords(Event event) {
        Customer customer = IMatDataHandler.getInstance().getCustomer();
        customer.setAddress(myPageRecordsAddress.getText());
        customer.setEmail(myPageRecordsEmail.getText());
        customer.setFirstName(myPageRecordsFirstName.getText());
        customer.setLastName(myPageRecordsLastName.getText());
        customer.setPhoneNumber(myPageRecordsPhoneNumber.getText());
        customer.setMobilePhoneNumber(myPageRecordsMobileNumber.getText());
        customer.setPostAddress(myPageRecordsPostAddress.getText());
        customer.setPostCode(myPageRecordsPostCode.getText());
        System.out.println("Set the customer's records.");
    }

    @FXML
    public void mouseTrap(Event event) {
        event.consume();
    }

    @FXML
    public void onClickIMat(Event event) {
        categoryAnchorPane.toFront();
    }

    @FXML
    public void onClickOrderReplace(Event event) {
        IMatDataHandler.getInstance().getShoppingCart().clear();
        onClickOrderAdd(event);
    }

    @FXML
    public void onClickOrderAdd(Event event) {
        for (Node node : orderHistoryDetailFlowPane.getChildren()) {
            if (node instanceof OrderItem)
                IMatDataHandler.getInstance().getShoppingCart().addItem(((OrderItem) node).item);
        }
    }

    public ProductCategory[] getCategories(){
        return ProductCategory.values();
    }
}
