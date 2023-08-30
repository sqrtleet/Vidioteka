package org.example.pages.Cd;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.entity.CdEntity;
import org.example.entity.ClientEntity;
import org.example.entity.FilmEntity;
import org.example.functions.CdFunc;
import org.example.functions.ClientFunc;
import org.example.functions.FilmFunc;

public class AddCd {

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

    @FXML
    private TextField priceColumn;

    @FXML
    private ComboBox<String> filmColumn;

    @FXML
    private ComboBox<String> selectedFilmColumn;

    @FXML
    private Button deleteBtn;

    @FXML
    void initialize() {
        List<FilmEntity> films = FilmFunc.getAllFilms();
        List<FilmEntity> selectedFilms = new ArrayList<>();
        for(FilmEntity film : films){
            filmColumn.getItems().add(film.getName());
        }
        filmColumn.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (!selectedFilmColumn.getItems().contains(newValue)) {
                    selectedFilmColumn.getItems().add(newValue);
                }
            }
        });
        deleteBtn.setOnAction(actionEvent -> {
            String selectedItem = selectedFilmColumn.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                selectedFilmColumn.getItems().remove(selectedItem);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Не выбран диск");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, выберите диск для удаления.");
                alert.showAndWait();
            }
        });
        addBtn.setOnAction(actionEvent -> {
            if(nameColumn.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Пустое поле имени");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, введите имя.");
                alert.showAndWait();
                return;
            }
            if(priceColumn.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Пустое поле цены");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, введите цену.");
                alert.showAndWait();
                return;
            }
            try {
                Integer.parseInt(priceColumn.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Неверная цена");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, введите цену.");
                alert.showAndWait();
                return;
            }
            for(String film : selectedFilmColumn.getItems()){
                selectedFilms.add(FilmFunc.getFilmByName(film));
            }
            CdFunc.addCd(nameColumn.getText(), Integer.parseInt(priceColumn.getText()), selectedFilms);
            Stage stage = (Stage) addBtn.getScene().getWindow();
            stage.close();
        });
        cancelBtn.setOnAction(actionEvent -> {
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        });
    }

}
