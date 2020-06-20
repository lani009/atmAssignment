package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginMain extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
        primaryStage.setTitle("Well Come!!");
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene =new Scene(root);
        primaryStage.setScene(scene);  // Stage에 root.fxml를 load한 scene를 가지고 있음
        primaryStage.show();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
