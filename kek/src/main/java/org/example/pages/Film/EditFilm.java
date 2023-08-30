package org.example.pages.Film;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.entity.FilmEntity;
import org.example.entity.GenreEntity;
import org.example.entity.OrderEntity;
import org.example.entity.ProducerEntity;
import org.example.entity.enums.AgeRating;
import org.example.functions.FilmFunc;
import org.example.functions.GenreFunc;
import org.example.functions.ProducerFunc;

public class EditFilm {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addBtn;

    @FXML
    private ComboBox<String> ageColumn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField countryColumn;

    @FXML
    private DatePicker dateColumn;

    @FXML
    private ComboBox<String> genreColumn;

    @FXML
    private TextField nameColumn;

    @FXML
    private ComboBox<String> producerColumn;

    private int id;

    @FXML
    void initialize() {
        List<ProducerEntity> producers = ProducerFunc.getAllProducers();
        for(ProducerEntity producer: producers){
            producerColumn.getItems().add(producer.getName());
        }
        List<GenreEntity> genres = GenreFunc.getAllGenres();
        for(GenreEntity genre: genres){
            genreColumn.getItems().add(genre.getName());
        }
        AgeRating[] ages = AgeRating.values();
        for (AgeRating age : ages) {
            ageColumn.getItems().add(age.name());
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
            LocalDate date = dateColumn.getValue();
            if (date == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Неправильная дата");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, введите правильную дату.");
                alert.showAndWait();
                return;
            }
            if(countryColumn.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Пустое поле страны");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, введите страну.");
                alert.showAndWait();
                return;
            }
            if (producerColumn.getValue() != null && genreColumn.getValue() != null && ageColumn.getValue() != null) {
                FilmFunc.editFilm(id, nameColumn.getText(), dateColumn.getValue(), countryColumn.getText(), AgeRating.valueOf(ageColumn.getValue()), GenreFunc.getGenreByName(genreColumn.getValue()), ProducerFunc.getProducerByName(producerColumn.getValue()));
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

    public void setFilm(FilmEntity film) {
        id = film.getId();
        nameColumn.setText(film.getName());
        dateColumn.setValue(film.getReleaseDate());
        countryColumn.setText(film.getCountry());
        ageColumn.setValue(film.getAgeRating().name());
        producerColumn.setValue(film.getProducer().getName());
        genreColumn.setValue(film.getGenre().getName());
    }

}

