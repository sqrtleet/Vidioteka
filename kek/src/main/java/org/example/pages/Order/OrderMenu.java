package org.example.pages.Order;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import jakarta.persistence.criteria.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.CurrentEmployeeName;
import org.example.entity.CdEntity;
import org.example.entity.ClientEntity;
import org.example.entity.FilmEntity;
import org.example.entity.OrderEntity;
import org.example.entity.enums.CollateralType;
import org.example.functions.ClientFunc;
import org.example.functions.EmployeeFunc;
import org.example.functions.OrderFunc;
import org.example.pages.Client.EditClient;
import org.hibernate.Hibernate;

public class OrderMenu {
    @FXML
    private Button addButton;

    @FXML
    private Button backBtn;

    @FXML
    private TableColumn<OrderEntity, String> collateralDataColumn;

    @FXML
    private TableColumn<OrderEntity, String> collateralValueColumn;

    @FXML
    private TableColumn<OrderEntity, LocalDate> dateColumn;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Label employeeName;

    @FXML
    private TableColumn<OrderEntity, Integer> idColumn;

    @FXML
    private Button refreshButton;

    @FXML
    private TableColumn<OrderEntity, LocalDate> returnDateColumn;

    @FXML
    private TextField searchField;

    @FXML
    private TableColumn<OrderEntity, String> statusColumn;

    @FXML
    private TableColumn<OrderEntity, Integer> sumColumn;

    @FXML
    private TableColumn<OrderEntity, String> clientColumn;

    @FXML
    private TableColumn<OrderEntity, String> employeeColumn;

    @FXML
    private TableColumn<OrderEntity, String> cdNameColumn;

    @FXML
    private TableView<OrderEntity> tableView;
    @FXML
    private TextField searchCdField;

    @FXML
    private TableColumn<CdEntity, Integer> id2Column;

    @FXML
    private TableColumn<CdEntity, String> name2Column;

    @FXML
    private TableView<CdEntity> TableView2;

    private boolean isWindowOpen = false;

    @FXML
    void initialize() {
        employeeName.setText(employeeName.getText() + CurrentEmployeeName.loggedInEmployeeName);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        collateralDataColumn.setCellValueFactory(new PropertyValueFactory<>("collateralData"));
        collateralValueColumn.setCellValueFactory(new PropertyValueFactory<>("collateralDataValue"));
        clientColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClient().getName()));
        employeeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmployee().getName()));
        cdNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCdList().get(0).getName()));
        tableView.getColumns().setAll(idColumn, clientColumn, cdNameColumn, sumColumn, statusColumn, dateColumn, returnDateColumn, collateralDataColumn, collateralValueColumn, employeeColumn);
        List<OrderEntity> orders = OrderFunc.getAllOrders();
        for (OrderEntity order : orders) {
            Hibernate.initialize(order.getCdList());
        }
        ObservableList<OrderEntity> observableOrders = FXCollections.observableArrayList(orders);
        tableView.setItems(observableOrders);
        id2Column.setCellValueFactory(new PropertyValueFactory<>("id"));
        name2Column.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableView2.getColumns().setAll(id2Column, name2Column);
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                List<CdEntity> cds = newValue.getCdList();
                ObservableList<CdEntity> observableCds = FXCollections.observableArrayList(cds);
                TableView2.setItems(observableCds);
            }
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<OrderEntity> filteredList = new FilteredList<>(observableOrders, p -> true);
            filteredList.setPredicate(order -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (order.getClient().getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else {
                    return false;
                }
            });
            tableView.setItems(filteredList);
        });
        searchCdField.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<OrderEntity> filteredList = new FilteredList<>(observableOrders, p -> true);
            filteredList.setPredicate(order -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (order.getCdList().get(0).getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else {
                    return false;
                }
            });
            tableView.setItems(filteredList);
        });

        // добавление клиента
        addButton.setOnAction(event -> {
            if (!isWindowOpen) {
                isWindowOpen = true;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/Order/add_order.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Add Orders");
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                } catch (Exception e) {
                    System.out.println("Исключение" + e);
                }
                isWindowOpen = false;
                refreshClients(observableOrders);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Закройте окна");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, закройте другие окна");
                alert.showAndWait();
            }
        });
        editButton.setOnAction(event -> {
            if (!isWindowOpen) {
                isWindowOpen = true;
                OrderEntity selectedOrder = tableView.getSelectionModel().getSelectedItem();
                if (selectedOrder == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Выберите заказ");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, выберите заказ из таблицы");
                    alert.showAndWait();
                }
                else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/Order/edit_order.fxml"));
                        Parent root = loader.load();
                        EditOrder eo = loader.getController();
                        eo.setOrder(selectedOrder);
                        Stage stage = new Stage();
                        stage.setTitle("Edit Order");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();
                        refreshClients(observableOrders);
                    } catch (Exception e) {
                        System.out.println("Исключение" + e);
                    }
                }
                isWindowOpen = false;
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Закройте окна");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, закройте другие окна");
                alert.showAndWait();
            }
        });
        deleteButton.setOnAction(event -> {
            if (!isWindowOpen) {
                isWindowOpen = true;
                OrderEntity selectedOrder = tableView.getSelectionModel().getSelectedItem();
                if (selectedOrder == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Выберите заказ");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, выберите заказ из таблицы");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Подтвердите удаление");
                    alert.setHeaderText("Удаление заказа из базы данных");
                    alert.setContentText("Вы точно хотите удалить этот заказ?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        OrderFunc.deleteOrder(selectedOrder.getId());
                        observableOrders.remove(selectedOrder);
                    }
                }
                isWindowOpen = false;
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Закройте окна");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, закройте другие окна");
                alert.showAndWait();
            }
        });
        // обновление списка
        refreshButton.setOnAction(event -> {
            refreshClients(observableOrders);
        });
        // назад в главное меню
        backBtn.setOnAction(event -> {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/MainMenu/MainMenu.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Parent root = loader.getRoot();
            stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        });
        // обновление списка
        refreshButton.setOnAction(event -> {
            refreshClients(observableOrders);
        });
    }
    private void refreshClients(ObservableList<OrderEntity> observableOrders) {
        observableOrders.clear();
        List<OrderEntity> orders = OrderFunc.getAllOrders();
        observableOrders.setAll(orders);
    }
}
