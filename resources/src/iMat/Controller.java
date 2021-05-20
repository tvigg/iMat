package iMat;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
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
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    @FXML private TableView<Order> orderHistoryTable;
    @FXML private AnchorPane orderHistoryAnchorPane;
    @FXML private AnchorPane orderHistoryLightbox;
    @FXML private FlowPane orderHistoryFlowPane;
    private final Map<Integer, List<OrderItem>> orderItemMap = new HashMap<>();

    private IMatDataHandler handler = IMatDataHandler.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeOrderHistory();
    }

    private void initializeOrderHistory() {
        ObservableList<Order> orders = FXCollections.observableList(handler.getOrders());
        for (Order o : handler.getOrders()) {
            List<OrderItem> items = new ArrayList<OrderItem>();
            for (ShoppingItem i : o.getItems())
                items.add(new OrderItem(i,this));
            orderItemMap.put(o.getOrderNumber(), items);
        }
        orderHistoryTable.setEditable(false);
        orderHistoryTable.setItems(orders);
        TableColumn<Order,String> orderDateColumn = new TableColumn<>("Datum");
        orderDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                String s = p.getValue().getDate().toString();

                return new ReadOnlyStringWrapper(s);
            }
        });
        TableColumn<Order,String> orderNumberColumn = new TableColumn<>("Ordernummer");
        orderNumberColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                int n = p.getValue().getOrderNumber();

                return new ReadOnlyStringWrapper(String.valueOf(n));
            }
        });
        TableColumn<Order,String> orderPriceColumn = new TableColumn<>("Pris");
        orderPriceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                List<ShoppingItem> items = p.getValue().getItems();
                double price = items.stream().map((i) -> i.getTotal()).reduce(0.0, (a, b) -> a + b);

                return new ReadOnlyStringWrapper(String.valueOf(price));
            }
        });
        TableColumn<Order,String> orderItemsColumn = new TableColumn<>("Varor");
        orderItemsColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                List<ShoppingItem> items = p.getValue().getItems();
                String s = items.stream()
                        .map((i) -> (int)i.getAmount() + " " + i.getProduct().getName())
                        .collect(Collectors.joining(", "));

                return new ReadOnlyStringWrapper(s);
            }
        });
        orderHistoryTable.getColumns().setAll(orderDateColumn, orderNumberColumn, orderPriceColumn, orderItemsColumn);
        orderHistoryLightbox.toBack();
    }

    @FXML
    public void onClickOrder(Event event) {
        Order order = orderHistoryTable.getSelectionModel().getSelectedItem();
        if (order != null) {
            populateOrderHistoryLightbox(order);
            orderHistoryLightbox.toFront();
        }
    }

    private void populateOrderHistoryLightbox(Order order) {
        orderHistoryFlowPane.getChildren().clear();
        orderHistoryFlowPane.getChildren().addAll(orderItemMap.get(order.getOrderNumber()));
    }

    @FXML
    public void onClickOrderLightbox(Event event) {
        orderHistoryAnchorPane.toFront();
    }

    public void onClickOrderHistory() {
        ObservableList<Node> items = FXCollections.observableArrayList(orderHistoryFlowPane.getChildren());
        System.out.println("Sorting items!");
        items.sort((o1, o2) -> {
            double c1 = ((OrderItem)o1).item.getTotal(), c2 = ((OrderItem)o2).item.getTotal();
            System.out.println(c1 + " vs " + c2);
            if (c1 > c2)
                return 1;
            else if (c1 < c2)
                return -1;
            else
                return 0;
        });
        orderHistoryFlowPane.getChildren().setAll(items);
    }

    @FXML
    public void mouseTrap(Event event) {
        event.consume();
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
            IMatDataHandler.getInstance().getShoppingCart().addItem(((OrderItem)node).item);
        }
    }
}
