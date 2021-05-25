package iMat;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.*;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    @FXML private FlowPane orderHistoryFlowPane;
    @FXML private AnchorPane storeAnchorPane;
    @FXML private AnchorPane orderHistoryAnchorPane;
    @FXML private AnchorPane myPageAnchorPane;
    @FXML private AnchorPane orderHistoryLightbox;
    @FXML private AnchorPane categoryAnchorPane;
    //Payments
    @FXML private AnchorPane betalaAnchorpane;
    @FXML private AnchorPane savedAddressAnchorPane;
    @FXML private AnchorPane paymentCartAnchorPane;
    @FXML private AnchorPane paymentCreditCardAnchorPane;
    @FXML private AnchorPane confirmPurchaseAnchorPane;
    @FXML private AnchorPane paymentDeliveryDateAnchorPane;
    @FXML private TextField addressTextField;
    @FXML private TextField postCodeTextField;
    @FXML private TextField paymentCardNumberTextField;
    @FXML private TextField paymentExpirationMonthTextField;
    @FXML private TextField paymentExpirationYearTextField;
    @FXML private TextField paymentCVCTextField;
    @FXML private Label displayAddressLabel;
    @FXML private Label displayPostCodeLabel;
    @FXML private Label noAddressEnteredLabel;
    @FXML private Label noPostCodeEnteredLabel;
    @FXML private Label  confirmOrderAddressLabel;
    @FXML private Label  confirmOrderPostCodeLabel;
    @FXML private Label  confirmOrderDeliveryDateLabel;
    @FXML private Label  confirmOrderDeliveryTimeLabel;
    @FXML private Label  confirmOrderCreditCardLabel;

    @FXML private Button goToDatesButton;
    @FXML private ComboBox dayComboBox;
    @FXML private ComboBox timeComboBox;

    //Orders
    @FXML private FlowPane orderHistoryDetailFlowPane;
    @FXML private AnchorPane orderItemHeader;
    @FXML private Label orderHistoryLightboxHeader;
    @FXML private VBox categoryListFlowPane;
    private final Map<Integer, List<OrderItem>> orderItemMap = new HashMap<>();

    // Products
    @FXML private FlowPane productListFlowPane;

    // User page
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

    private IMatDataHandler handler = IMatDataHandler.getInstance();
    private DeliveryDate deliveryDate = new DeliveryDate();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeOrderHistory();
        updateCategoryList();
        updateProductList();
        initializeMyPageRecords();
        createUser();
        dayComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deliveryDate.setDate(newValue.toString());
        });
        timeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deliveryDate.setTime(newValue.toString());
        });
    }

    private void updateCategoryList(){
        categoryListFlowPane.getChildren().clear();
        OurProductCategory[] categoryList = getCategories();

        for (OurProductCategory category : categoryList){
            var button = new IMatCategoryListItem(category, this);
            categoryListFlowPane.getChildren().add(button);
        }
    }

    private void updateProductList(){
        productListFlowPane.getChildren().clear();
        List<Product> productList = IMatDataHandler.getInstance().getProducts();
        System.out.println("productListLength " + productList.size()); // =0? List<Product>getProducts()

        for (Product product : productList){
            var button = new IMatProductListItem(product, this);
            productListFlowPane.getChildren().add(button);
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

    public void onClickPayments(Event event) {
        createUser();
        betalaAnchorpane.toFront();
    }

    public OurProductCategory[] getCategories(){
        return OurProductCategory.values();
    }

    //===============Payments=================//
    @FXML public void createUser(){
        populateDayComboBox();
        populateTimeComboBox();
        if(IMatDataHandler.getInstance().getCustomer().getAddress().isBlank() || IMatDataHandler.getInstance().getCustomer().getPostCode().isBlank()){
            savedAddressAnchorPane.setVisible(false);
        }
        else {
            displayAddressLabel.setText(IMatDataHandler.getInstance().getCustomer().getAddress());
            displayPostCodeLabel.setText(IMatDataHandler.getInstance().getCustomer().getPostCode());
            confirmOrderAddressLabel.setText(IMatDataHandler.getInstance().getCustomer().getAddress());
            confirmOrderPostCodeLabel.setText(IMatDataHandler.getInstance().getCustomer().getPostCode());
            savedAddressAnchorPane.setVisible(true);
        }
    }

    @FXML public void updateCustomerAddress(Event event){
        if (addressTextField.getText().isEmpty() || postCodeTextField.getText().isEmpty()){
            noAddressEnteredLabel.setText("Var vänlig fyll i fältet.");
            noPostCodeEnteredLabel.setText("Var vänlig fyll i fältet.");
            goToDatesButton.setDisable(true);
            if (!addressTextField.getText().isEmpty()) {
                noAddressEnteredLabel.setText("");
            }
            if(!postCodeTextField.getText().isEmpty()){
                noPostCodeEnteredLabel.setText("");
            }
        }
        else {
            savedAddressAnchorPane.setVisible(true);
            goToDatesButton.setDisable(false);
            //useCurrentAddressButton.setVisible(false);
            noAddressEnteredLabel.setText("");
            noPostCodeEnteredLabel.setText("");
            IMatDataHandler.getInstance().getCustomer().setAddress(addressTextField.getText());
            IMatDataHandler.getInstance().getCustomer().setPostCode(postCodeTextField.getText());
            displayAddressLabel.setText(IMatDataHandler.getInstance().getCustomer().getAddress());
            displayPostCodeLabel.setText(IMatDataHandler.getInstance().getCustomer().getPostCode());
            confirmOrderAddressLabel.setText(IMatDataHandler.getInstance().getCustomer().getAddress());
            confirmOrderPostCodeLabel.setText(IMatDataHandler.getInstance().getCustomer().getPostCode());
        }
    }

    @FXML
    public void confirmEmptyFields(Event event){
        if (!addressTextField.getText().isEmpty() || !postCodeTextField.getText().isEmpty()) {
            showAlert();
        }
    }

    @FXML public void showAlert(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bekräftelse");
        alert.setHeaderText("Tömma båda fälten");
        alert.setContentText("Är du säker på att du vill tömma fälten address och postnummer?");

        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.APPLY);
        ButtonType buttonTypeCancel = new ButtonType("Avbryt", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            emptyAddressFields();
        } else {
            // close alert
        }
    }

    @FXML public void emptyAddressFields(){
        addressTextField.setText("");
        postCodeTextField.setText("");
    }

    @FXML private void populateDayComboBox(){
        dayComboBox.getItems().addAll(generateDates(31));
    }

    private String[] generateDates(int limit){
        String[] res = new String[limit];
        for (int i = 1; i < res.length; i++) {
            if (i >= 27 ){
                res[i] = i + " Maj";
            }
            else{
                res[i] = i + " Juni";
            }
        }
        return res;
    }

    @FXML private void populateTimeComboBox(){
        String[] times = new String[] {"08:00-11:00", "11:00-13:00", "13:00-16:00", "16:00-19:00"};
        timeComboBox.getItems().addAll(times);
    }

    @FXML public void onClickShowDeliveryDates(Event event){
        paymentDeliveryDateAnchorPane.toFront();
    }

    @FXML public void onClickShowCart(Event event){
        System.out.println(deliveryDate.getDate() + " at " + deliveryDate.getTime() + " o'clock.");
        confirmOrderDeliveryDateLabel.setText(deliveryDate.getDate());
        confirmOrderDeliveryTimeLabel.setText(deliveryDate.getTime());
        paymentCartAnchorPane.toFront();
    }

    @FXML public void populateOrderConfirmation(){
        CreditCard creditCard = IMatDataHandler.getInstance().getCreditCard();
        confirmOrderCreditCardLabel.setText(creditCard.getCardNumber());
    }

    @FXML public void saveCreditCardInformation(){
        CreditCard creditCard = IMatDataHandler.getInstance().getCreditCard();
        creditCard.setCardNumber(paymentCardNumberTextField.getText());
        //hack
        creditCard.setValidMonth(Integer.parseInt(paymentExpirationMonthTextField.getText()));
        creditCard.setValidYear(Integer.parseInt(paymentExpirationYearTextField.getText()));
        creditCard.setVerificationCode(Integer.parseInt(paymentCVCTextField.getText()));
    }

    @FXML public void onClickShowCreditCard(Event event){
        paymentCreditCardAnchorPane.toFront();
    }

    @FXML public void onClickShowOrderConfirmation(Event event){
        populateOrderConfirmation();
        confirmPurchaseAnchorPane.toFront();

    }

    //Class for keeping track of current delivery date and time
    public class DeliveryDate{
        String date;
        String time;

        public String getDate(){
            return date;
        }
        public String getTime(){
            return time;
        }
        public void setDate(String newDate){
            date = newDate;
        }
        public void setTime(String newTime){
            time = newTime;
        }
    }

}

