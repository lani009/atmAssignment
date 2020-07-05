package application;

import java.io.IOException;

import form.TransactionDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            primaryStage.setTitle("Well Come!!");
            Parent root = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene); // Stage에 root.fxml를 load한 scene를 가지고 있음
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        launch(args);
        if(TransactionDAO.isLoggedIn()) {
            TransactionDAO.logout();    // 창 종료시, 로그아웃.
        }
    }
}
