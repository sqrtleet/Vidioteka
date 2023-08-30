package org.example.pages.Employee;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.entity.AuthDataEntity;
import org.example.entity.ProducerEntity;
import org.example.entity.enums.AgeRating;
import org.example.functions.*;

public class AddEmployee {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private ComboBox<String> dataColumn;

    @FXML
    private TextField nameColumn;

    @FXML
    private TextField phoneColumn;

    @FXML
    void initialize() {
        List<AuthDataEntity> datas = AuthDataFunc.getAllData();
        for(AuthDataEntity data: datas){
            dataColumn.getItems().add(data.getLogin());
        }
        addBtn.setOnAction(actionEvent -> {
            if(nameColumn.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Пустое поле имени");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, введите имя.");
                alert.showAndWait();
                return;
            }
            String phone = phoneColumn.getText();
            try {
                long number = Long.parseLong(phone);
                if (number < 10000000000L || number >= 100000000000L) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Неправильный номер телефона");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, введите правильный номер телефона.");
                alert.showAndWait();
                return;
            }
            if (dataColumn.getValue() != null) {
                EmployeeFunc.addEmployee(nameColumn.getText(), phone, AuthDataFunc.getDataByLogin(dataColumn.getValue()));
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
        });
        cancelBtn.setOnAction(actionEvent -> {
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        });
    }

}

