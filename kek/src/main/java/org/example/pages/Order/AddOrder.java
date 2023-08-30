package org.example.pages.Order;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import org.example.CurrentEmployeeName;
import org.example.entity.CdEntity;
import org.example.entity.ClientEntity;
import org.example.entity.EmployeeEntity;
import org.example.entity.FilmEntity;
import org.example.entity.enums.CollateralType;
import org.example.entity.enums.OrderStatus;
import org.example.functions.*;

public class AddOrder {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private ComboBox<String> cdColumn;

    @FXML
    private DatePicker dateColumn;

    @FXML
    private DatePicker returnDateColumn;

    @FXML
    private ComboBox<String> collateralColumn;

    @FXML
    private TextField valueColumn;

    @FXML
    private ComboBox<String> selectedCdColumn;

    @FXML
    private ComboBox<String> clientColumn;

    @FXML
    void initialize() {
        List<ClientEntity> clients = ClientFunc.getAllClients();
        List<CdEntity> selectedCds = new ArrayList<>();
        for(ClientEntity client : clients){
            clientColumn.getItems().add(client.getName());
        }
        List<CdEntity> cds = CdFunc.getAllCds();
        for(CdEntity cd: cds){
            if(cd.getOrder() == null){
                cdColumn.getItems().add(cd.getName());
            }
        }
        cdColumn.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (!selectedCdColumn.getItems().contains(newValue)) {
                    selectedCdColumn.getItems().add(newValue);
                }
            }
        });
        CollateralType[] collateralTypes = CollateralType.values();
        for (CollateralType type : collateralTypes) {
            collateralColumn.getItems().add(type.name());
        }
        addBtn.setOnAction(actionEvent -> {
            try{
                LocalDate date = dateColumn.getValue();
                if (date == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Неправильная дата");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, введите правильную дату.");
                    alert.showAndWait();
                    return;
                }
                LocalDate returnDate = returnDateColumn.getValue();
                if (returnDate == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Неправильная дата");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, введите правильную дату.");
                    alert.showAndWait();
                    return;
                }
                String collateralValue = valueColumn.getText();
                for(String cd : selectedCdColumn.getItems()){
                    selectedCds.add(CdFunc.getCdByName(cd));
                }
                if (collateralColumn.getValue() != null && cdColumn.getValue() != null && clientColumn.getValue() != null) {
                    OrderFunc.saveOrder(selectedCds, EmployeeFunc.getEmployeeByName(CurrentEmployeeName.loggedInEmployeeName), ClientFunc.getClientByName(clientColumn.getValue()), date, returnDate, OrderStatus.NEW, CollateralType.valueOf(collateralColumn.getValue()), collateralValue);
                    Stage stage = (Stage) addBtn.getScene().getWindow();
                    stage.close();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Заполните все поля");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, заполните все поля.");
                    alert.showAndWait();
                }
            } catch (Exception e){
                ErrorFunc.errorAlertData();
            }
        });
        cancelBtn.setOnAction(actionEvent -> {
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        });
    }
}
