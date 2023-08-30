package org.example.pages.MainMenu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.CurrentEmployeeName;
import org.example.entity.*;
import org.example.functions.*;
import org.example.pages.Client.ClientMenu;
import org.example.pages.Order.OrderMenu;

import java.io.IOException;

public class MainMenu {
    @FXML
    private Button cdBtn;
    @FXML
    private Button clientBtn;

    @FXML
    private Button countryBtn;

    @FXML
    private Label employeeName;

    @FXML
    private Button filmBtn;

    @FXML
    private Button genreBtn;

    @FXML
    private Button orderBtn;

    @FXML
    private Button producerBtn;

    @FXML
    private Button employeeBtn;

    @FXML
    void initialize() {
        employeeName.setText(employeeName.getText() + CurrentEmployeeName.loggedInEmployeeName);
        clientBtn.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/Client/ClientMenu.fxml"));
            try {
                loader.load();
                Stage stage = (Stage) clientBtn.getScene().getWindow();
                stage.close();
                Parent root = loader.getRoot();
                stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                ErrorFunc.errorAlertForm();
            }
        });
        orderBtn.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/Order/OrderMenu.fxml"));
            try {
                loader.load();
                Stage stage = (Stage) orderBtn.getScene().getWindow();
                stage.close();
                Parent root = loader.getRoot();
                stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                System.out.println("Исключение " + e);
                ErrorFunc.errorAlertForm();
            }
        });
        filmBtn.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/Film/FilmMenu.fxml"));
            try {
                loader.load();
                Stage stage = (Stage) filmBtn.getScene().getWindow();
                stage.close();
                Parent root = loader.getRoot();
                stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                System.out.println("Исключение " + e);
                ErrorFunc.errorAlertForm();
            }
        });
        genreBtn.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/Genre/GenreMenu.fxml"));
            try {
                loader.load();
                Stage stage = (Stage) genreBtn.getScene().getWindow();
                stage.close();
                Parent root = loader.getRoot();
                stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                System.out.println("Исключение " + e);
                ErrorFunc.errorAlertForm();
            }
        });
        producerBtn.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/Producer/ProducerMenu.fxml"));
            try {
                loader.load();
                Stage stage = (Stage) producerBtn.getScene().getWindow();
                stage.close();
                Parent root = loader.getRoot();
                stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                System.out.println("Исключение " + e);
                ErrorFunc.errorAlertForm();
            }
        });
        cdBtn.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/Cd/CdMenu.fxml"));
            try {
                loader.load();
                Stage stage = (Stage) cdBtn.getScene().getWindow();
                stage.close();
                Parent root = loader.getRoot();
                stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                System.out.println("Исключение " + e);
                ErrorFunc.errorAlertForm();
            }
        });
        employeeBtn.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/Employee/EmployeeMenu.fxml"));
            try {
                loader.load();
                Stage stage = (Stage) cdBtn.getScene().getWindow();
                stage.close();
                Parent root = loader.getRoot();
                stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                System.out.println("Исключение " + e);
                ErrorFunc.errorAlertForm();
            }
        });
    }


}
