package org.example.pages.Auth;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.CurrentEmployeeName;
import org.example.entity.ClientEntity;
import org.example.entity.EmployeeEntity;
import org.example.functions.EmployeeFunc;
import org.example.pages.MainMenu.MainMenu;

public class Auth extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxmls/Auth/Auth.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Authorization");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @FXML
    private Button authBtn;

    @FXML
    private TextField login;

    @FXML
    private Label alertLabel;

    @FXML
    private PasswordField password;

    @FXML
    private ComboBox<String> selectEmployee;

    @FXML
    void initialize() {
        List<EmployeeEntity> employees = EmployeeFunc.getAllEmployees();
        for(EmployeeEntity e : employees){
            selectEmployee.getItems().add(e.getName());
        }
        selectEmployee.setOnAction((event) -> {
            authBtn.setOnAction(actionEvent -> {
                if(!login.getText().equals("") && !password.getText().equals("")){
                    if(EmployeeFunc.checkAuth(EmployeeFunc.getEmployeeByName(selectEmployee.getValue()).getName(), login.getText(), password.getText())){
                        CurrentEmployeeName.loggedInEmployeeName = EmployeeFunc.getEmployeeByName(selectEmployee.getValue()).getName();
                        Stage stage = (Stage) authBtn.getScene().getWindow();
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
                    }
                    else {
                        alertLabel.setText("You have entered an invalid combination");
                        alertLabel.setVisible(true);
                    }
                }
                else {
                    alertLabel.setText("Please enter login, password and select employee");
                    alertLabel.setVisible(true);
                }
            });
        });
    }
}
