package org.example.pages.Client;

import java.math.BigInteger;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.entity.*;
import org.example.functions.*;

public class EditClient {
    private int id;
    @FXML
    private TextField blField;

    @FXML
    private DatePicker dobField;

    @FXML
    private Button editBtn;
    @FXML
    private Button cancelBtn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    void initialize() {
        editBtn.setOnAction(actionEvent -> {
            try {
                String name = nameField.getText();
                if (name.split(" ").length != 3) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Неправильный формат ФИО");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, введите ФИО с именем, отчеством и фамилией.");
                    alert.showAndWait();
                    return;
                }
                String phone = phoneField.getText();
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
                boolean bl = Boolean.parseBoolean(blField.getText());
                LocalDate dob = dobField.getValue();
                if (dob == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Неправильная дата рождения");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, введите правильную дату рождения.");
                    alert.showAndWait();
                    return;
                }
                ClientFunc.updateClient(id, name, phone, bl, dob);
                Stage stage = (Stage) editBtn.getScene().getWindow();
                stage.close();
            } catch (DateTimeParseException e) {
                ErrorFunc.errorAlertData();
            }
        });
        cancelBtn.setOnAction(actionEvent -> {
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        });
    }

    public void setClient(ClientEntity client) {
        id = client.getId();
        nameField.setText(client.getName());
        phoneField.setText(client.getPhoneNumber());
        blField.setText(Boolean.toString(client.isBlacklist()));
        dobField.setValue(client.getDateOfBirth());
    }

}

