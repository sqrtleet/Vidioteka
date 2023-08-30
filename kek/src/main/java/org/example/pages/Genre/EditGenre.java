package org.example.pages.Genre;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.entity.GenreEntity;
import org.example.functions.GenreFunc;

public class EditGenre {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField nameColumn;

    private int id;

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
            GenreFunc.editGenre(id, nameColumn.getText());
            Stage stage = (Stage) addBtn.getScene().getWindow();
            stage.close();
        });
        cancelBtn.setOnAction(actionEvent -> {
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        });
    }

    public void setGenre(GenreEntity genre){
        id = genre.getId();
        nameColumn.setText(genre.getName());
    }
}
