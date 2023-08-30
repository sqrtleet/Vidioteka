package org.example.pages.Client;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.transformation.FilteredList;
import org.example.CurrentEmployeeName;
import org.example.entity.*;
import org.example.functions.*;

public class ClientMenu {
    @FXML
    private Label employeeName;
    @FXML
    private Button backBtn;
    @FXML
    private Button excelBtn;
    @FXML
    private TableView<ClientEntity> tableView;
    @FXML
    private TableColumn<ClientEntity, Integer> idColumn;
    @FXML
    private TableColumn<ClientEntity, String> nameColumn;
    @FXML
    private TableColumn<ClientEntity, String> phoneColumn;
    @FXML
    private TableColumn<ClientEntity, Boolean> blacklistColumn;
    @FXML
    private TableColumn<ClientEntity, LocalDate> dobColumn;
    @FXML
    private Button addButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button refreshButton;
    private boolean isWindowOpen = false;

    @FXML
    public void initialize() {
        employeeName.setText(employeeName.getText() + CurrentEmployeeName.loggedInEmployeeName);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        blacklistColumn.setCellValueFactory(new PropertyValueFactory<>("blacklist"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        tableView.getColumns().setAll(idColumn, nameColumn, phoneColumn, blacklistColumn, dobColumn);
        List<ClientEntity> clients = ClientFunc.getAllClients();
        ObservableList<ClientEntity> observableClients = FXCollections.observableArrayList(clients);
        tableView.setItems(observableClients);
        // поиск клиента по имени
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<ClientEntity> filteredList = new FilteredList<>(observableClients, p -> true);
            filteredList.setPredicate(client -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (client.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/Client/add_client.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Add Client");
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                } catch (Exception e) {
                    System.out.println("Исключение" + e);
                }
                isWindowOpen = false;
                refreshClients(observableClients);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Закройте окна");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, закройте другие окна");
                alert.showAndWait();
            }
        });
        // обновление списка
        refreshButton.setOnAction(event -> {
            refreshClients(observableClients);
        });
        // редактирование клиента
        editButton.setOnAction(event -> {
            if (!isWindowOpen) {
                isWindowOpen = true;
                ClientEntity selectedClient = tableView.getSelectionModel().getSelectedItem();
                if (selectedClient == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Выберите клиента");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, выберите клиента из таблицы");
                    alert.showAndWait();
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/Client/edit_client.fxml"));
                        Parent root = loader.load();
                        EditClient ec = loader.getController();
                        ec.setClient(selectedClient);
                        Stage stage = new Stage();
                        stage.setTitle("Edit Client");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();
                        refreshClients(observableClients);
                    } catch (Exception e) {
                        System.out.println("Исключение" + e);
                    }
                }
                isWindowOpen = false;
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Закройте окна");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, закройте другие окна");
                alert.showAndWait();
            }
        });
        // удаление клиента
        deleteButton.setOnAction(event -> {
            if (!isWindowOpen) {
                isWindowOpen = true;
                ClientEntity selectedClient = tableView.getSelectionModel().getSelectedItem();
                if (selectedClient == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Выберите клиента");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, выберите клиента из таблицы");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Подтвердите удаление");
                    alert.setHeaderText("Удаление клиента из базы данных");
                    alert.setContentText("Вы точно хотите удалить этого клиента?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        ClientFunc.deleteClient(selectedClient.getId());
                        observableClients.remove(selectedClient);
                    }
                }
                isWindowOpen = false;
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Закройте окна");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, закройте другие окна");
                alert.showAndWait();
            }
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
        excelBtn.setOnAction(actionEvent -> {
            ClientFunc.generateReport();
        });
    }

    private void refreshClients(ObservableList<ClientEntity> observableClients) {
        observableClients.clear();
        List<ClientEntity> clients = ClientFunc.getAllClients();
        observableClients.setAll(clients);
    }
}