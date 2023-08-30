package org.example.pages.Producer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.functions.GenreFunc;
import org.example.functions.ProducerFunc;

public class AddProducer {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addBtn;

    @FXML
    private TextArea bioColumn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField nameColumn;

    @FXML
    void initialize() {
        addBtn.setOnAction(actionEvent -> {
            if(nameColumn.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Пустое поле имени");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, введите имя.");
                alert.showAndWait();
                return;
            }
            ProducerFunc.addProducer(nameColumn.getText(), bioColumn.getText());
            Stage stage = (Stage) addBtn.getScene().getWindow();
            stage.close();
        });
        cancelBtn.setOnAction(actionEvent -> {
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        });
    }

}
