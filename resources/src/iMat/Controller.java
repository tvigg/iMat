package iMat;
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
    @FXML private Label displayAddressLabel;
    @FXML private Label displayPostCodeLabel;
    @FXML private Label noAddressEnteredLabel;
    @FXML private Label noPostCodeEnteredLabel;
    @FXML private Button saveAddressButton;
    @FXML private Button clearFieldsButton;
    @FXML private Button useCurrentAddressButton;
    @FXML private Button goToCartButton;
    @FXML private Button goToCreditCardButton;
    @FXML private ComboBox dayComboBox;
    @FXML private ComboBox timeComboBox;
    @FXML private ComboBox monthComboBox;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeOrderHistory();
        updateCategoryList();
        //ProductCategory.BREAD //+ ProductCategory.BERRY;
        //updateSubCategory(ProductCategory.BERRY);
        updateProductList(ProductCategory.FRUIT);
        initializeMyPageRecords();
        createUser();
        populateDayComboBox();
    }

    private void updateCategoryList(){
        categoryListFlowPane.getChildren().clear();
        OurProductCategory.OurCategory[] categoryList = getCategories();

        for (OurProductCategory.OurCategory category : categoryList){
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



    private void updateProductList(ProductCategory productCategory){
        productListFlowPane.getChildren().clear();
        List<Product> productList = IMatDataHandler.getInstance().getProducts(productCategory);
        System.out.println("productListLength " + productList.size()); // =0? List<Product>getProducts()

        for (Product product : productList){
            var button = new IMatProductListItem(product, this);
            productListFlowPane.getChildren().add(button);
        }

    }

//    private void updateSubCategory(ProductCategory category){
//        productListFlowPane.getChildren().clear();
//        List<Product> productList = IMatDataHandler.getInstance().getProducts(category);
//        //System.out.println("productListLength " + productList.size()); // =0? List<Product>getProducts()
//
//        for (Product product : productList){
//            var button = new IMatSubCategory(category, this);
//            productListFlowPane.getChildren().add(button);
//        }
//
//    }

    public void openRecipeView(ProductCategory category) {
        updateProductList(category);
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
        betalaAnchorpane.toFront();
    }

    public OurProductCategory.OurCategory[] getCategories(){
        return OurProductCategory.OurCategory.values();
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
            IMatDataHandler.getInstance().getCustomer().setPostCode(postCodeTextField.getText());
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

    private void populateDayComboBox(){
        dayComboBox.getItems().addAll(generateDates(31));
    }

    private Integer[] generateDates(int limit){
        Integer[] res = new Integer[limit];
        for (int i = 1; i < res.length; i++) {
            res[i] = i;
        }
        return res;
    }

    @FXML public void onClickShowDeliveryDates(Event event){
        paymentDeliveryDateAnchorPane.toFront();
    }

    @FXML public void onClickShowCart(Event event){
        paymentCartAnchorPane.toFront();
    }

    @FXML public void onClickShowCreditCard(Event event){
        paymentCreditCardAnchorPane.toFront();
    }

    @FXML public void onClickShowOrderConfirmation(Event event){
        confirmPurchaseAnchorPane.toFront();
    }

}
