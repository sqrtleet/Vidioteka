package org.example.pages.Employee;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.CurrentEmployeeName;
import org.example.entity.AuthDataEntity;
import org.example.entity.EmployeeEntity;
import org.example.entity.ProducerEntity;
import org.example.functions.EmployeeFunc;
import org.example.functions.ProducerFunc;
import org.example.pages.Producer.EditProducer;

public class EmployeeMenu {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addButton;

    @FXML
    private Button backBtn;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Label employeeName;

    @FXML
    private TableColumn<EmployeeEntity, String> idColumn;

    @FXML
    private TableColumn<EmployeeEntity, String> loginColumn;

    @FXML
    private TableColumn<EmployeeEntity, String> nameColumn;

    @FXML
    private TableColumn<EmployeeEntity, String> pasColumn;

    @FXML
    private TableColumn<EmployeeEntity, String> phoneColumn;

    @FXML
    private Button refreshButton;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<EmployeeEntity> tableView;

    private boolean isWindowOpen = false;

    @FXML
    void initialize() {
        employeeName.setText(employeeName.getText() + CurrentEmployeeName.loggedInEmployeeName);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        loginColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthData().getLogin()));
        pasColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthData().getPassword()));
        tableView.getColumns().setAll(idColumn, nameColumn, phoneColumn, loginColumn, pasColumn);
        List<EmployeeEntity> employees = EmployeeFunc.getAllEmployees();
        ObservableList<EmployeeEntity> observableEmployees = FXCollections.observableArrayList(employees);
        tableView.setItems(observableEmployees);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<EmployeeEntity> filteredList = new FilteredList<>(observableEmployees, p -> true);
            filteredList.setPredicate(employee-> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (employee.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else {
                    return false;
                }
            });
            tableView.setItems(filteredList);
        });
        addButton.setOnAction(actionEvent -> {
            if (!isWindowOpen) {
                isWindowOpen = true;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/Employee/add_employee.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Add Employee");
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                } catch (Exception e) {
                    System.out.println("Исключение" + e);
                }
                isWindowOpen = false;
                refreshEmployees(observableEmployees);
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
                EmployeeEntity selectedEmployee = tableView.getSelectionModel().getSelectedItem();
                if (selectedEmployee == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Выберите работника");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, выберите работника из таблицы");
                    alert.showAndWait();
                }
                else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/Employee/edit_employee.fxml"));
                        Parent root = loader.load();
                        EditEmployee ee = loader.getController();
                        ee.setEmployee(selectedEmployee);
                        Stage stage = new Stage();
                        stage.setTitle("Edit Film");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();
                        refreshEmployees(observableEmployees);
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
                EmployeeEntity selectedEmployee = tableView.getSelectionModel().getSelectedItem();
                if (selectedEmployee == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Выберите продюсера");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, выберите продюсера из таблицы");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Подтвердите удаление");
                    alert.setHeaderText("Удаление работника из базы данных");
                    alert.setContentText("Вы точно хотите удалить этого работника?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        EmployeeFunc.deleteEmployee(selectedEmployee.getId());
                        observableEmployees.remove(selectedEmployee);
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
        refreshButton.setOnAction(event -> {
            refreshEmployees(observableEmployees);
        });
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
    }
    private void refreshEmployees(ObservableList<EmployeeEntity> observableEmployees) {
        observableEmployees.clear();
        List<EmployeeEntity> employees = EmployeeFunc.getAllEmployees();
        observableEmployees.setAll(employees);
    }
}
