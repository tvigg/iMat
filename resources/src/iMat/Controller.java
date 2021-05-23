package iMat;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
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
    @FXML private AnchorPane orderHistoryLightbox;
    @FXML private AnchorPane categoryAnchorPane;
    //Payments
    @FXML private AnchorPane betalaAnchorpane;
    @FXML private AnchorPane savedAddressAnchorPane;
    @FXML private AnchorPane paymentCartAnchorPane;
    @FXML private AnchorPane paymentCreditCardAnchorPane;
    @FXML private TextField addressTextField;
    @FXML private TextField postCodeTextField;
    @FXML private Label displayAddressLabel;
    @FXML private Label displayPostCodeLabel;
    @FXML private Label noAddressEnteredLabel;
    @FXML private Label noPostCodeEnteredLabel;
    @FXML private Button saveAddressButton;
    @FXML private Button clearFieldsButton;
    @FXML private Button useCurrentAddressButton;
    @FXML private Button goToCartButton;
    @FXML private Button goToCreditCardButton;

    //Orders
    @FXML private FlowPane orderHistoryDetailFlowPane;
    @FXML private AnchorPane orderItemHeader;
    @FXML private Label orderHistoryLightboxHeader;
    @FXML private VBox categoryListFlowPane;
    private final Map<Integer, List<OrderItem>> orderItemMap = new HashMap<>();

    private IMatDataHandler handler = IMatDataHandler.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeOrderHistory();
        updateCategoryList();
        createUser();
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
    public void onClickOrderLightbox(Event event) {
        orderHistoryAnchorPane.toFront();
        storeAnchorPane.toFront();
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
    public void onClickPayments(Event event) {
        betalaAnchorpane.toFront();
    }

    @FXML
    public void onClickOrderAgain(Event event) {
        if (!IMatDataHandler.getInstance().getShoppingCart().getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Återbeställning");
            int count = IMatDataHandler.getInstance().getShoppingCart().getItems().size();
            alert.setHeaderText("Du har redan " + count + (count > 1 ? " varor" : " vara") + " i dramaten. Vill du tömma dramaten och lägga till varorna från beställningen eller lägga till alla varorna i dramaten?");
            ButtonType replace = new ButtonType("Ersätt dramatens varor med beställningens varor");
            ButtonType addAll = new ButtonType("Lägg till beställningens varor i dramaten");
            ButtonType cancel = new ButtonType("Avbryt", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(replace, addAll, cancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == replace) {
                IMatDataHandler.getInstance().getShoppingCart().clear();
            } else if (result.get() != addAll) {
                return;
            }
        }
        for (Node node : orderHistoryFlowPane.getChildren()) {
            IMatDataHandler.getInstance().getShoppingCart().addItem(((OrderItem) node).item);
        }
    }

    public ProductCategory[] getCategories(){
        return ProductCategory.values();
    }

    //===============Payments=================//
    public void createUser(){
        //IMatDataHandler.getInstance().getCustomer().setAddress("cool street 1337");
        //if there is an address, display it

        if(IMatDataHandler.getInstance().getCustomer().getAddress().isBlank() || IMatDataHandler.getInstance().getCustomer().getPostAddress().isBlank()){
            savedAddressAnchorPane.setVisible(false);
        }
        else {
            displayAddressLabel.setText(IMatDataHandler.getInstance().getCustomer().getAddress());
            displayPostCodeLabel.setText(IMatDataHandler.getInstance().getCustomer().getPostAddress());
            savedAddressAnchorPane.setVisible(true);
        }
    }

    public void updateCustomerAddress(Event event){
        if (addressTextField.getText().isEmpty() || postCodeTextField.getText().isEmpty()){
            noAddressEnteredLabel.setText("Var vänlig fyll i fältet.");
            noPostCodeEnteredLabel.setText("Var vänlig fyll i fältet.");
            if (!addressTextField.getText().isEmpty()) {
                noAddressEnteredLabel.setText("");
            }
            if(!postCodeTextField.getText().isEmpty()){
                noPostCodeEnteredLabel.setText("");
            }
        }
        else {
            savedAddressAnchorPane.setVisible(true);
            //useCurrentAddressButton.setVisible(false);
            noAddressEnteredLabel.setText("");
            noPostCodeEnteredLabel.setText("");
            IMatDataHandler.getInstance().getCustomer().setAddress(addressTextField.getText());
            IMatDataHandler.getInstance().getCustomer().setPostAddress(postCodeTextField.getText());
            displayAddressLabel.setText(IMatDataHandler.getInstance().getCustomer().getAddress());
            displayPostCodeLabel.setText(IMatDataHandler.getInstance().getCustomer().getPostAddress());
        }
    }

    @FXML
    public void confirmEmptyFields(Event event){
        if (!addressTextField.getText().isEmpty() || !postCodeTextField.getText().isEmpty()) {
            showAlert();
        }
    }

    public void showAlert(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bekräftelse");
        alert.setHeaderText("Tämma båda fälten");
        alert.setContentText("Är du säker på att du vill tömma fälten address och postnummer?");

        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.APPLY);
        ButtonType buttonTypeCancel = new ButtonType("Avbryt", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            emptyAddressFields();
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    public void emptyAddressFields(){
        addressTextField.setText("");
        postCodeTextField.setText("");
    }

    @FXML public void onClickShowCart(Event event){
        paymentCartAnchorPane.toFront();
    }

    @FXML public void onClickShowCreditCard(Event event){
        paymentCreditCardAnchorPane.toFront();
    }

}
