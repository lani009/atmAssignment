package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * 이건 심플 컨트롤러
 * 
 * @author 정의철
 *
 */
public class Fcon implements Initializable {
    @FXML
    private Button d;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        d.setOnAction(e -> {
            Parent login;
            try {
                login = FXMLLoader.load(getClass().getResource("Sample.fxml"));
                Scene scene = new Scene(login);
        
                Stage primaryStage = (Stage)d.getScene().getWindow(); // 현재 윈도우 가져오기
            
                primaryStage.setScene(scene);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

    }



}
