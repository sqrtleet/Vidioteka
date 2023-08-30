package org.example.functions;

import javafx.scene.control.Alert;

public class ErrorFunc {
    public static void errorAlertData(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Неверные данные");
        alert.setContentText("Пожалуйста, введите корректные данные для поля");
        alert.showAndWait();
    }
    public static void errorAlertForm(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Не удалось открыть форму");
        alert.setContentText("Пожалуйста, попробуйте заново");
        alert.showAndWait();
    }
}
