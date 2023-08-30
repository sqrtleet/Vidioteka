package org.example.pages.Genre;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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
import org.example.entity.GenreEntity;
import org.example.functions.FilmFunc;
import org.example.functions.GenreFunc;
import org.example.pages.Film.EditFilm;

public class GenreMenu {

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
    private TableColumn<GenreEntity, Integer> idColumn;

    @FXML
    private TableColumn<GenreEntity, String> nameColumn;

    @FXML
    private Button refreshButton;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<GenreEntity> tableView;

    private boolean isWindowOpen = false;

    @FXML
    void initialize() {
        employeeName.setText(employeeName.getText() + CurrentEmployeeName.loggedInEmployeeName);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableView.getColumns().setAll(idColumn, nameColumn);
        List<GenreEntity> genres = GenreFunc.getAllGenres();
        ObservableList<GenreEntity> observableGenres= FXCollections.observableArrayList(genres);
        tableView.setItems(observableGenres);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<GenreEntity> filteredList = new FilteredList<>(observableGenres, p -> true);
            filteredList.setPredicate(genre-> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (genre.getName().toLowerCase().contains(lowerCaseFilter)) {
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/Genre/add_genre.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Add Genres");
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                } catch (Exception e) {
                    System.out.println("Исключение" + e);
                }
                isWindowOpen = false;
                refreshGenres(observableGenres);
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
                GenreEntity selectedGenre = tableView.getSelectionModel().getSelectedItem();
                if (selectedGenre == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Выберите фильм");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, выберите фильм из таблицы");
                    alert.showAndWait();
                }
                else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/Genre/edit_genre.fxml"));
                        Parent root = loader.load();
                        EditGenre eg = loader.getController();
                        eg.setGenre(selectedGenre);
                        Stage stage = new Stage();
                        stage.setTitle("Edit Film");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();
                        refreshGenres(observableGenres);
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
                GenreEntity selectedGenre = tableView.getSelectionModel().getSelectedItem();
                if (selectedGenre == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Выберите жанр");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, выберите жанр из таблицы");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Подтвердите удаление");
                    alert.setHeaderText("Удаление жанра из базы данных");
                    alert.setContentText("Вы точно хотите удалить этот жанр?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        GenreFunc.deleteGenre(selectedGenre.getId());
                        observableGenres.remove(selectedGenre);
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
            refreshGenres(observableGenres);
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
    private void refreshGenres(ObservableList<GenreEntity> observableGenres) {
        observableGenres.clear();
        List<GenreEntity> genres = GenreFunc.getAllGenres();
        observableGenres.setAll(genres);
    }

}
