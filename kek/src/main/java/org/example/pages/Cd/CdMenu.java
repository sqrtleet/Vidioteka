package org.example.pages.Cd;

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
import org.example.entity.CdEntity;
import org.example.entity.FilmEntity;
import org.example.entity.GenreEntity;
import org.example.functions.CdFunc;
import org.example.functions.GenreFunc;
import org.example.pages.Genre.EditGenre;

public class CdMenu {

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
    private TableColumn<CdEntity, Integer> idColumn;

    @FXML
    private TableColumn<CdEntity, String> nameColumn;

    @FXML
    private TableColumn<CdEntity, String> priceColumn;

    @FXML
    private Button refreshButton;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<CdEntity> tableView;

    @FXML
    private TableColumn<FilmEntity, Integer> id2Column;

    @FXML
    private TableColumn<FilmEntity, String> name2Column;

    @FXML
    private TableView<FilmEntity> TableView2;

    private boolean isWindowOpen = false;

    @FXML
    void initialize() {
        employeeName.setText(employeeName.getText() + CurrentEmployeeName.loggedInEmployeeName);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableView.getColumns().setAll(idColumn, nameColumn, priceColumn);
        List<CdEntity> cds = CdFunc.getAllCds();
        ObservableList<CdEntity> observableCds= FXCollections.observableArrayList(cds);
        tableView.setItems(observableCds);
        id2Column.setCellValueFactory(new PropertyValueFactory<>("id"));
        name2Column.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableView2.getColumns().setAll(id2Column, name2Column);
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                List<FilmEntity> films = newValue.getFilms();
                ObservableList<FilmEntity> observableFilms = FXCollections.observableArrayList(films);
                TableView2.setItems(observableFilms);
            }
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<CdEntity> filteredList = new FilteredList<>(observableCds, p -> true);
            filteredList.setPredicate(cd-> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (cd.getName().toLowerCase().contains(lowerCaseFilter)) {
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/Cd/add_cd.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Add Cds");
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                } catch (Exception e) {
                    System.out.println("Исключение" + e);
                }
                isWindowOpen = false;
                refreshCds(observableCds);
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
                CdEntity selectedCd = tableView.getSelectionModel().getSelectedItem();
                if (selectedCd == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Выберите диск");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, выберите диск из таблицы");
                    alert.showAndWait();
                }
                else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/Cd/edit_cd.fxml"));
                        Parent root = loader.load();
                        EditCd ec = loader.getController();
                        ec.setCd(selectedCd);
                        Stage stage = new Stage();
                        stage.setTitle("Edit Film");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();
                        refreshCds(observableCds);
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
                CdEntity selectedCd = tableView.getSelectionModel().getSelectedItem();
                if (selectedCd == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Выберите диск");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, выберите диск из таблицы");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Подтвердите удаление");
                    alert.setHeaderText("Удаление диска из базы данных");
                    alert.setContentText("Вы точно хотите удалить этот диск?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        CdFunc.deleteCd(selectedCd.getId());
                        observableCds.remove(selectedCd);
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
            refreshCds(observableCds);
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
    private void refreshCds(ObservableList<CdEntity> observableCds) {
        observableCds.clear();
        List<CdEntity> genres = CdFunc.getAllCds();
        observableCds.setAll(genres);
    }
}
