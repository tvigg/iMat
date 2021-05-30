package iMat;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import se.chalmers.cse.dat216.project.*;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
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
    @FXML private ImageView imatLogga;
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
    @FXML private Label confirmOrderAddressLabel;
    @FXML private Label confirmOrderPostCodeLabel;
    @FXML private Label confirmOrderDeliveryDateLabel;
    @FXML private Label confirmOrderDeliveryTimeLabel;
    @FXML private Label confirmOrderCreditCardLabel;
    @FXML private Label paymentSumLabel;

    @FXML private Button goToDatesButton;
    @FXML private ComboBox dayComboBox;
    @FXML private ComboBox timeComboBox;

    //Orders
    @FXML private FlowPane orderHistoryDetailFlowPane;
    @FXML private AnchorPane orderItemHeader;
    @FXML private Label orderHistoryLightboxHeader;
    @FXML private VBox categoryListFlowPane;
    private Map<Integer, List<OrderItem>> orderItemMap = new HashMap<>();

    // Products
    @FXML private FlowPane productListFlowPane;

    // User page
    @FXML private AnchorPane myPageHome;
    @FXML private GridPane myPageRecords;
    @FXML private StackPane myPageStackPane;
    @FXML private Button myPageSaveButton;

    @FXML private TextField myPageRecordsAddress;
    @FXML private TextField myPageRecordsEmail;
    @FXML private TextField myPageRecordsFirstName;
    @FXML private TextField myPageRecordsLastName;
    @FXML private TextField myPageRecordsPhoneNumber;
    @FXML private TextField myPageRecordsMobileNumber;
    @FXML private TextField myPageRecordsPostAddress;
    @FXML private TextField myPageRecordsPostCode;


    // Account creation
    @FXML private AnchorPane accountCreationAnchorPane;
    @FXML private StackPane accountCreationStackPane;
    @FXML private AnchorPane accountCreationAccount;
    @FXML private AnchorPane accountCreationPersonal;
    @FXML private AnchorPane accountCreationAddress;
    @FXML private AnchorPane accountCreationOverview;
    @FXML private AnchorPane accountCreationCreditCard;
    @FXML private Button accountCreationPrevButton;
    @FXML private Button accountCreationNextButton;
    @FXML private TextField accountCreationUser;
    @FXML private TextField accountCreationPassword1;
    @FXML private TextField accountCreationPassword2;
    @FXML private TextField accountCreationFirstName;
    @FXML private TextField accountCreationLastName;
    @FXML private TextField accountCreationPhone;
    @FXML private TextField accountCreationMobile;
    @FXML private TextField accountCreationEmail;
    @FXML private TextField accountCreationHomeAddress;
    @FXML private TextField accountCreationPostAddress;
    @FXML private TextField accountCreationPostCode;
    @FXML private ComboBox accountCreationCardType;
    @FXML private TextField accountCreationCardNumber;
    @FXML private TextField accountCreationCardValidMonth;
    @FXML private TextField accountCreationCardValidYear;
    @FXML private TextField accountCreationCardValidCode;
    @FXML private TextArea accountCreationOverviewText;

    // Buttons
    @FXML private Button myPageButton;
    @FXML private Button ordersButton;

    // Shopping cart
    @FXML private FlowPane shoppingCartFlowPane;
    private Map<ShoppingItem, IMatShoppingListItem> shoppingListItemMap = new HashMap<>();
    private Map<Product, IMatProductListItem> productListItemMap = new HashMap<>();
    @FXML private Label shoppingCartTotalPrice;

    @FXML private FlowPane betalaAddresserFlowPane1;

    @FXML private AnchorPane categoryListStartPage;

    private IMatDataHandler handler = IMatDataHandler.getInstance();
    private DeliveryDate deliveryDate = new DeliveryDate();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeOrderHistory();
        updateCategoryList();
        initializeProductList();
        updateProductList(null);
        createUser();
        dayComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deliveryDate.setDate(newValue.toString());
        });
        timeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deliveryDate.setTime(newValue.toString());
        });
        populateDayComboBox();
        populateTimeComboBox();
        accountCreationCardType.getItems().add("MasterCard");
        accountCreationCardType.getItems().add("Visa");
        initializeAccountFieldListeners();
        loadUser();
        if (IMatDataHandler.getInstance().isFirstRun())
            setupAccount();
        else
            setupMyPage();
        setupShoppingList();
        Controller controller = this;
        handler.getShoppingCart().addShoppingCartListener(new ShoppingCartListener() {
            @Override
            public void shoppingCartChanged(CartEvent cartEvent) {
                ShoppingItem item = cartEvent.getShoppingItem();
                if (item == null) {
                    shoppingCartFlowPane.getChildren().clear();
                    shoppingListItemMap.clear();
                    for (Product product : handler.getProducts()) {
                        productListItemMap.get(product).updateAmount(null);
                    }
                    return;
                }
                IMatShoppingListItem shoppingListItem = shoppingListItemMap.get(item);
                IMatProductListItem productListItem = productListItemMap.get(item.getProduct());
                if (cartEvent.isAddEvent()) {
                    if (shoppingListItem != null) {
                        shoppingListItem.updateAmount();
                    } else {
                        shoppingListItem = new IMatShoppingListItem(item, controller);
                        shoppingListItemMap.put(item, shoppingListItem);
                        shoppingCartFlowPane.getChildren().add(shoppingListItem);
                    }
                    productListItem.updateAmount(item);
                } else {
                    if (shoppingListItem != null) {
                        if (shoppingListItem.getItem().getAmount() <= 0.0) {
                            shoppingListItemMap.remove(shoppingListItem);
                            shoppingCartFlowPane.getChildren().remove(shoppingListItem);
                        } else {
                            shoppingListItem.updateAmount();
                        }
                    }
                    productListItem.updateAmount(item);
                }
                double price = handler.getShoppingCart().getItems().stream().map((i) -> i.getTotal()).reduce(0.0, (a, b) -> a + b);
                shoppingCartTotalPrice.setText(Controller.priceFormat(price) + " kr");
            }
        });
    }

    private void setupShoppingList() {
        for (ShoppingItem item : handler.getShoppingCart().getItems()) {
            IMatShoppingListItem listItem = new IMatShoppingListItem(item, this);
            shoppingCartFlowPane.getChildren().add(listItem);
            shoppingListItemMap.put(item, listItem);
        }
        double price = handler.getShoppingCart().getItems().stream().map((i) -> i.getTotal()).reduce(0.0, (a, b) -> a + b);
        shoppingCartTotalPrice.setText(Controller.priceFormat(price) + " kr");
    }

    @FXML
    public void clearShoppingCart(Event event) {
        handler.getShoppingCart().clear();
    }

    public void removeShoppingItem(IMatShoppingListItem item) {
        shoppingCartFlowPane.getChildren().remove(item);
        shoppingListItemMap.remove(item.getItem());
    }

    private void setupMyPage() {
        myPageStackPane.getChildren().add(accountCreationPersonal);
        myPageStackPane.getChildren().add(accountCreationAccount);
        myPageStackPane.getChildren().add(accountCreationAddress);
        myPageStackPane.getChildren().add(accountCreationCreditCard);
        myPageHome.toFront();
    }

    private void loadUser() {
        User user = IMatDataHandler.getInstance().getUser();
        accountCreationUser.setText(user.getUserName());
        accountCreationPassword1.setText(user.getPassword());
        accountCreationPassword2.setText(user.getPassword());

        Customer customer = IMatDataHandler.getInstance().getCustomer();
        accountCreationFirstName.setText(customer.getFirstName());
        accountCreationLastName.setText(customer.getLastName());
        accountCreationPhone.setText(customer.getPhoneNumber());
        accountCreationMobile.setText(customer.getMobilePhoneNumber());
        accountCreationEmail.setText(customer.getEmail());
        accountCreationHomeAddress.setText(customer.getAddress());
        accountCreationPostAddress.setText(customer.getPostAddress());
        accountCreationPostCode.setText(customer.getPostCode());

        CreditCard card = IMatDataHandler.getInstance().getCreditCard();
        accountCreationCardNumber.setText(card.getCardNumber());
        accountCreationCardType.getSelectionModel().select(card.getCardType());
        accountCreationCardValidMonth.setText(String.valueOf(card.getValidMonth()));
        accountCreationCardValidYear.setText(String.valueOf(card.getValidYear()));
        accountCreationCardValidCode.setText(String.valueOf(card.getVerificationCode()));
    }

    @FXML
    public void goToMyPageAccount(Event event) {
        myPageSaveButton.setVisible(true);
        myPageSaveButton.setOnMouseClicked(mouseEvent -> {
            goToMyPage(null);
            saveUserAccount(null);
        });
        accountCreationAccount.toFront();
    }

    @FXML
    public void goToMyPagePersonal(Event event) {
        myPageSaveButton.setVisible(true);
        myPageSaveButton.setOnMouseClicked(mouseEvent -> {
            goToMyPage(null);
            saveUserPersonal(null);
        });
        accountCreationPersonal.toFront();
    }

    @FXML
    public void goToMyPageAddress(Event event) {
        myPageSaveButton.setVisible(true);
        myPageSaveButton.setOnMouseClicked(mouseEvent -> {
            goToMyPage(null);
            saveUserAddress(null);
        });
        accountCreationAddress.toFront();
    }

    @FXML
    public void goToMyPageCreditCard(Event event) {
        myPageSaveButton.setVisible(true);
        myPageSaveButton.setOnMouseClicked(mouseEvent -> {
            goToMyPage(null);
            saveUserCreditCard(null);
        });
        accountCreationCreditCard.toFront();
    }

    private void setupAccount() {
        accountCreationAnchorPane.toFront();
        accountCreationAccount.toFront();
        accountCreationPrevButton.setDisable(true);
        myPageButton.setVisible(false);
        ordersButton.setVisible(false);
        imatLogga.setDisable(true);
    }

    @FXML
    public void accountCreationPrev(Event event) {
        Node current = accountCreationStackPane.getChildren().get(accountCreationStackPane.getChildren().size() - 1);
        Node prev = null;
        if (current == accountCreationPersonal) {
            prev = accountCreationAccount;
            accountCreationPrevButton.setDisable(true);
        } else if (current == accountCreationAddress) {
            prev = accountCreationPersonal;
        } else if (current == accountCreationCreditCard) {
            prev = accountCreationAddress;
        } else if (current == accountCreationOverview) {
            prev = accountCreationCreditCard;
            accountCreationNextButton.setText("Gå till nästa steg");
        }
        if (prev != null)
            prev.toFront();
    }

    @FXML
    public void accountCreationNext(Event event) {
        Node current = accountCreationStackPane.getChildren().get(accountCreationStackPane.getChildren().size() - 1);
        Node next = null;
        if (current == accountCreationAccount) {
            if (!accountCreationUser.getText().isEmpty()
                    && !accountCreationPassword1.getText().isEmpty()
                    && accountCreationPassword1.getText().equals(accountCreationPassword2.getText())) {
                next = accountCreationPersonal;
                accountCreationPrevButton.setDisable(false);

            }
        } else if (current == accountCreationPersonal) {
            if (!accountCreationFirstName.getText().isEmpty()
                && !accountCreationLastName.getText().isEmpty()
                && (!accountCreationPhone.getText().isEmpty()
                || !accountCreationMobile.getText().isEmpty())
                && !accountCreationEmail.getText().isEmpty()) {
                next = accountCreationAddress;
            }
        } else if (current == accountCreationAddress) {
            if (!accountCreationHomeAddress.getText().isEmpty()
                && !accountCreationPostAddress.getText().isEmpty()
                && !accountCreationPostCode.getText().isEmpty()) {
                next = accountCreationCreditCard;
            }
        } else if (current == accountCreationCreditCard) {
            if (!accountCreationCardNumber.getText().isEmpty()
                && !accountCreationCardValidMonth.getText().isEmpty()
                && !accountCreationCardValidYear.getText().isEmpty()
                && !accountCreationCardValidCode.getText().isEmpty()
                && accountCreationCardType.getSelectionModel().getSelectedItem() != null) {
                next = accountCreationOverview;
                accountCreationNextButton.setText("Skapa konto");
                accountCreationOverviewText.setText(
                        "Användarnamn: " + accountCreationUser.getText() + "\n\n" +
                                "Förnamn: " + accountCreationFirstName.getText() + "\n\n" +
                                "Efternamn: " + accountCreationLastName.getText() + "\n\n" +
                                "Hemtelefonnummer: " + accountCreationPhone.getText() + "\n\n" +
                                "Mobiltelefonnummer: " + accountCreationMobile.getText() + "\n\n" +
                                "E-postadress: " + accountCreationEmail.getText() + "\n\n" +
                                "Gatuadress: " + accountCreationHomeAddress.getText() + "\n\n" +
                                "Ort: " + accountCreationPostAddress.getText() + "\n\n" +
                                "Postnummer: " + accountCreationPostCode.getText()
                );
            }
        } else if (current == accountCreationOverview) {
            saveUserAccount(null);
            saveUserPersonal(null);
            saveUserAddress(null);
            saveUserCreditCard(null);
            categoryAnchorPane.toFront();
            myPageButton.setVisible(true);
            ordersButton.setVisible(true);
            imatLogga.setDisable(false);
        }
        if (next != null)
            next.toFront();
    }

    private void updateCategoryList(){
        categoryListFlowPane.getChildren().clear();
        categoryListFlowPane.getChildren().add(categoryListStartPage);
        for (ProductCategory category : ProductCategory.values()){
            var button = new IMatCategoryListItem(new OurCategory(category), this);
            categoryListFlowPane.getChildren().add(button);
        }
    }

    private void initializeProductList() {
        List<Product> productList = handler.getProducts();
        for (Product product : productList) {
            IMatProductListItem listItem = new IMatProductListItem(product, this);
            productListItemMap.put(product,listItem);
        }
    }

    private void updateProductList(ProductCategory productCategory){
        productListFlowPane.getChildren().clear();
        List<Product> productList = productCategory != null
                ? handler.getProducts(productCategory)
                : handler.getProducts().subList(0,8);
        for (Product product : productList) {
            IMatProductListItem item = productListItemMap.get(product);
            item.setSale(productCategory == null);
            productListFlowPane.getChildren().add(item);
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

    private void populateCart() {
        //List<ShoppingItem> item = handler.getShoppingCart().getItems();
        betalaAddresserFlowPane1.getChildren().clear();
        for (ShoppingItem item : handler.getShoppingCart().getItems()) {
            IMatShoppingListItem listItem = new IMatShoppingListItem(1, item, this);
            betalaAddresserFlowPane1.getChildren().add(listItem);

            //shoppingListItemMap.put(item, listItem);
        }
        //ShoppingItem item = cartEvent.getShoppingItem();

        //betalaAddresserFlowPane1.getChildren().add(shoppingListItemMap.get(item));
        //TODO
    }

    @FXML
    public void goToOrders(Event event) {
        orderHistoryAnchorPane.toFront();
        storeAnchorPane.toFront();
        headline.setText("Beställningar");
    }

    @FXML
    public void goToMyPage(Event event) {
        myPageAnchorPane.toFront();
        myPageHome.toFront();
        storeAnchorPane.toFront();
        myPageSaveButton.setVisible(false);
        headline.setText("Min Sida");
    }

    @FXML
    public void saveUserAccount(Event event) {
        User user = IMatDataHandler.getInstance().getUser();
        user.setUserName(accountCreationUser.getText());
        user.setPassword(accountCreationPassword1.getText());
    }
    @FXML
    public void saveUserPersonal(Event event) {
        Customer customer = IMatDataHandler.getInstance().getCustomer();
        customer.setFirstName(accountCreationFirstName.getText());
        customer.setLastName(accountCreationLastName.getText());
        customer.setPhoneNumber(accountCreationPhone.getText());
        customer.setMobilePhoneNumber(accountCreationMobile.getText());
        customer.setEmail(accountCreationEmail.getText());
    }
    @FXML
    public void saveUserAddress(Event event) {
        Customer customer = IMatDataHandler.getInstance().getCustomer();
        customer.setAddress(accountCreationHomeAddress.getText());
        customer.setPostAddress(accountCreationPostAddress.getText());
        customer.setPostCode(accountCreationPostCode.getText());
    }
    @FXML
    public void saveUserCreditCard(Event event) {
        Customer customer = IMatDataHandler.getInstance().getCustomer();
        CreditCard card = IMatDataHandler.getInstance().getCreditCard();
        card.setCardNumber(accountCreationCardNumber.getText());
        card.setCardType((String)accountCreationCardType.getSelectionModel().getSelectedItem());
        card.setHoldersName(customer.getFirstName() + " " + customer.getLastName());
        card.setValidMonth(Integer.parseInt(accountCreationCardValidMonth.getText()));
        card.setValidYear(Integer.parseInt(accountCreationCardValidYear.getText()));
        card.setVerificationCode(Integer.parseInt(accountCreationCardValidCode.getText()));
    }

    @FXML
    public void mouseTrap(Event event) {
        event.consume();
    }

    @FXML
    public void onClickIMat(Event event) {
        categoryAnchorPane.toFront();
        updateProductList(null);
        headline.setText("Startsidan");
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
        headline.setText("Betala");
    }

    //===============Payments=================//
    @FXML public void createUser(){
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
            res[i] = i >= 27 ? i + " Maj" : i + " Juni";
        }
        return res;
    }

    @FXML private void populateTimeComboBox(){
        String[] times = new String[] {"08:00-11:00", "11:00-13:00", "13:00-16:00", "16:00-19:00"};
        timeComboBox.getItems().addAll(times);
    }

    @FXML
    public void confirmPurchase(Event event) {
        Order order = handler.placeOrder();
        OrderHistoryItem orderItem = new OrderHistoryItem(order,this);
        List<OrderItem> items = new ArrayList<OrderItem>();
        for (ShoppingItem i : order.getItems())
            items.add(new OrderItem(i,this));
        orderItemMap.put(order.getOrderNumber(), items);
        orderHistoryFlowPane.getChildren().add(1,orderItem);
        goToOrders(null);
    }

    @FXML public void onClickShowDeliveryDates(Event event){
        paymentDeliveryDateAnchorPane.toFront();
    }

    @FXML public void onClickShowCart(Event event){
        confirmOrderDeliveryDateLabel.setText(deliveryDate.getDate());
        confirmOrderDeliveryTimeLabel.setText(deliveryDate.getTime());
        double price = IMatDataHandler.getInstance().getShoppingCart().getTotal();
        paymentSumLabel.setText(priceFormat(price) + "kr");
        paymentCartAnchorPane.toFront();
        populateCart();
    }

    @FXML public void populateOrderConfirmation(){
        CreditCard creditCard = IMatDataHandler.getInstance().getCreditCard();
        confirmOrderCreditCardLabel.setText(creditCard.getCardNumber());
    }

    //TODO gör det omöjligt att skriva in bokstäver
    //TODO lägg till fält för namn
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

    private void initializeAccountFieldListeners() {
        //accountCreationUser.textProperty().;
        //accountCreationPassword1;
        //accountCreationPassword2;
        //accountCreationFirstName;
        //accountCreationLastName;
        accountCreationPhone.textProperty().addListener(new IntStringChangeListener(accountCreationPhone, 10));
        accountCreationMobile.textProperty().addListener(new IntStringChangeListener(accountCreationMobile, 10));
        //accountCreationEmail;
        //accountCreationHomeAddress;
        //accountCreationPostAddress;
        accountCreationPostCode.textProperty().addListener(new IntStringChangeListener(accountCreationPostCode, 5));
        accountCreationCardNumber.textProperty().addListener(new IntStringChangeListener(accountCreationCardNumber, 16));
        accountCreationCardValidMonth.textProperty().addListener(new IntStringChangeListener(accountCreationCardValidMonth, 2));
        accountCreationCardValidYear.textProperty().addListener(new IntStringChangeListener(accountCreationCardValidYear,2));
        accountCreationCardValidCode.textProperty().addListener(new IntStringChangeListener(accountCreationCardValidCode,3));
    }

    private class IntStringChangeListener implements ChangeListener<String> {
        TextField field;
        int maxLength = Integer.MAX_VALUE;
        public IntStringChangeListener(TextField field, int maxLength) {
            this.field = field;
            this.maxLength = maxLength;
        }
        public  IntStringChangeListener(TextField field) {
            this.field = field;
        }
        public void changed(ObservableValue<? extends String> observableValue, String from, String to) {
            if (!to.matches("\\d*") || to.length() > maxLength)
                field.setText(from);
        }
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

    public void onClickedCategory(ProductCategory category) {
        updateProductList(category);
    }

    @FXML private Label headline;
    public void updateHeadline(String name) {headline.setText(name);}

    public static String priceFormat(double price) {
        String str = String.format("%.2f", price);
        if (str.endsWith(".00"))
            str = String.valueOf((int)price);
        return str;
    }
}

