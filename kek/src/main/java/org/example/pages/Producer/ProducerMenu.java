package org.example.pages.Producer;

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
import org.example.entity.GenreEntity;
import org.example.entity.ProducerEntity;
import org.example.functions.GenreFunc;
import org.example.functions.ProducerFunc;
import org.example.pages.Genre.EditGenre;

public class ProducerMenu {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addButton;

    @FXML
    private Button backBtn;

    @FXML
    private TableColumn<ProducerEntity, String> bioColumn;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Label employeeName;

    @FXML
    private TableColumn<ProducerEntity, Integer> idColumn;

    @FXML
    private TableColumn<ProducerEntity, String> nameColumn;

    @FXML
    private Button refreshButton;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<ProducerEntity> tableView;

    private boolean isWindowOpen = false;

    @FXML
    void initialize() {
        employeeName.setText(employeeName.getText() + CurrentEmployeeName.loggedInEmployeeName);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        bioColumn.setCellValueFactory(new PropertyValueFactory<>("biographyData"));
        tableView.getColumns().setAll(idColumn, nameColumn, bioColumn);
        List<ProducerEntity> producers = ProducerFunc.getAllProducers();
        ObservableList<ProducerEntity> observableProducers= FXCollections.observableArrayList(producers);
        tableView.setItems(observableProducers);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<ProducerEntity> filteredList = new FilteredList<>(observableProducers, p -> true);
            filteredList.setPredicate(producer-> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (producer.getName().toLowerCase().contains(lowerCaseFilter)) {
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/Producer/add_producer.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Add Producers");
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                } catch (Exception e) {
                    System.out.println("Исключение" + e);
                }
                isWindowOpen = false;
                refreshFilms(observableProducers);
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
                ProducerEntity selectedProducer = tableView.getSelectionModel().getSelectedItem();
                if (selectedProducer == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Выберите продюсера");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, выберите продюсера из таблицы");
                    alert.showAndWait();
                }
                else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/Producer/edit_producer.fxml"));
                        Parent root = loader.load();
                        EditProducer ep = loader.getController();
                        ep.setProducer(selectedProducer);
                        Stage stage = new Stage();
                        stage.setTitle("Edit Film");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();
                        refreshFilms(observableProducers);
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
                ProducerEntity selectedProducer = tableView.getSelectionModel().getSelectedItem();
                if (selectedProducer == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Выберите продюсера");
                    alert.setHeaderText(null);
                    alert.setContentText("Пожалуйста, выберите продюсера из таблицы");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Подтвердите удаление");
                    alert.setHeaderText("Удаление продюсера из базы данных");
                    alert.setContentText("Вы точно хотите удалить этого продюсера?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        ProducerFunc.deleteProducer(selectedProducer.getId());
                        observableProducers.remove(selectedProducer);
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
            refreshFilms(observableProducers);
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
    private void refreshFilms(ObservableList<ProducerEntity> observableProducers) {
        observableProducers.clear();
        List<ProducerEntity> producers = ProducerFunc.getAllProducers();
        observableProducers.setAll(producers);
    }

}
