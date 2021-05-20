package iMat;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
}
