package org.example.pages.Film;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import org.example.entity.FilmEntity;
import org.example.entity.OrderEntity;
import org.example.entity.enums.AgeRating;
import org.example.functions.FilmFunc;
import org.example.functions.OrderFunc;
import org.example.pages.Order.EditOrder;
import org.hibernate.Hibernate;

public class FilmMenu {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<FilmEntity, String> ageColumn;

    @FXML
    private Button backBtn;

    @FXML
    private TableColumn<FilmEntity, String> countryColumn;

    @FXML
    private TableColumn<FilmEntity, LocalDate> dateColumn;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Label employeeName;

    @FXML
    private TableColumn<FilmEntity, String> genreColumn;

    @FXML
    private TableColumn<FilmEntity, Integer> idColumn;

    @FXML
    private TableColumn<FilmEntity, String> nameColumn;

    @FXML
    private TableColumn<FilmEntity, String> producerColumn;

    @FXML
    private Button refreshButton;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<FilmEntity> tableView;

    private boolean isWindowOpen = false;

    @FXML
    void initialize() {
        employeeName.setText(employeeName.getText() + CurrentEmployeeName.loggedInEmployeeName);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        ageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAgeRating().toString()));
        producerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProducer().getName()));
        genreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenre().getName()));
        tableView.getColumns().setAll(idColumn, nameColumn, dateColumn, countryColumn, ageColumn, producerColumn, genreColumn);
        List<FilmEntity> films = FilmFunc.getAllFilms();
        ObservableList<FilmEntity> observableFilms = FXCollections.observableArrayList(films);
        tableView.setItems(observableFilms);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<FilmEntity> filteredList = new FilteredList<>(observableFilms, p -> true);
            filteredList.setPredicate(film -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (film.getName().toLowerCase().contains(lowerCaseFilter)) {
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/Film/add_film.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Add Films");
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                } catch (Exception e) {
                    System.out.println("Исключение" + e);
                }
                isWindowOpen = false;
                refreshFilms(observableFilms);
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
                FilmEntity selectedFilm = tableView.getSelectionModel().getSelectedItem();
                if (selectedFilm == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Выберите фильм");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, выберите фильм из таблицы");
                    alert.showAndWait();
                }
                else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/Film/edit_film.fxml"));
                        Parent root = loader.load();
                        EditFilm ef = loader.getController();
                        ef.setFilm(selectedFilm);
                        Stage stage = new Stage();
                        stage.setTitle("Edit Film");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();
                        refreshFilms(observableFilms);
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
                FilmEntity selectedFilm = tableView.getSelectionModel().getSelectedItem();
                if (selectedFilm == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Выберите фильм");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, выберите фильм из таблицы");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Подтвердите удаление");
                    alert.setHeaderText("Удаление фильма из базы данных");
                    alert.setContentText("Вы точно хотите удалить этот фильм?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        FilmFunc.deleteFilm(selectedFilm.getId());
                        observableFilms.remove(selectedFilm);
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
            refreshFilms(observableFilms);
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
    private void refreshFilms(ObservableList<FilmEntity> observableFilms) {
        observableFilms.clear();
        List<FilmEntity> films = FilmFunc.getAllFilms();
        observableFilms.setAll(films);
    }

}
