package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.security.auth.login.LoginException;

import form.TransactionDAO;
import form.Enum.BankType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.scene.control.Toggle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class LoginController implements Initializable {
    @FXML
    private ToggleGroup group;
    @FXML
    private TextField Id;
    @FXML
    private PasswordField Pw;
    @FXML
    private Button sgin;
    @FXML
    private Label status;
    @FXML
    private RadioButton MDCBank;
    @FXML
    private RadioButton KAKAOBank;
    @FXML
    private RadioButton NHBank;
    @FXML
    private RadioButton AJOUBank;
    private int Type = 1;

    /**
     * 로그인 을 위한 이벤트이다. 입력한 Id,Pw를 String type으로 변환 후 TransactionDAO.login을 통해 서버에
     * 보낸다. 선택한 RadioButton에 따라 특정 BANK를 TransactionDAO.login을 통해 서버에 보낸다. 서버에 일치하는
     * 정보가 존재한다면 해당 객체에 대한 정보를 dao인스턴스를 통해 받아온다. 로그인에 성공하면 로그인 화면의 label을 "Login
     * Success"로 변경한다. 로그인 실패 시 에러를 throw하여 label을 "Login Failed!"로 변경한다.
     */
    public void LoginAction(ActionEvent event) throws LoginException {
        String ID = Id.getText();
        String PW = Pw.getText();
        String BANK = "";
        if (Type == 1) {
            BANK = "MDCBank";
        } else if (Type == 2) {
            BANK = "KAKAOBank";
        } else if (Type == 3) {
            BANK = "NHBank";
        } else if (Type == 4) {
            BANK = "AJOUBank";
        }
        TransactionDAO.login(ID, PW, BankType.valueOf(BANK));
        status.setText("Login Success");
        Parent login;
        try {
            login = FXMLLoader.load(getClass().getResource("fxml/Mainmenu.fxml"));   // 메인화면 연결
            Scene scene = new Scene(login);
            Stage primaryStage = (Stage)sgin.getScene().getWindow();
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
      
        sgin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    LoginAction(event);
                } catch (LoginException e) {
                   status.setText("Login Failed!");
                }
            }});
            
        /**
         * BANK 종류에 대한 RadioButton의 toggle group
         */
            group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
                @Override
                public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                    if(MDCBank.isSelected()) {
                        Type =1;
                    }else if(KAKAOBank.isSelected()){
                        Type =2;
                    }else if(NHBank.isSelected()){
                        Type =3;
                    }else if(AJOUBank.isSelected()){
                        Type =4;
                    }
                }

            });
    }
}
