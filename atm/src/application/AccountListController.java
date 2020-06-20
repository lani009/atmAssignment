package application;

import java.net.URL;
import java.util.ResourceBundle;

import form.Account;
import form.TransactionDAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AccountListController implements Initializable{

    @FXML
    private Button select1;
    @FXML
    private Label AccountNumber1;
    @FXML
    private Label balance1;
    
    @FXML
    private Button select2;
    @FXML
    private Label AccountNumber2;
    @FXML
    private Label balance2;
    
    @FXML
    private Button select3;
    @FXML
    private Label AccountNumber3;
    @FXML
    private Label balance3;
    
    @FXML
    private Button select4;
    @FXML
    private Label AccountNumber4;
    @FXML
    private Label balance4;
    
    @FXML
    private Button select5;
    @FXML
    private Label AccountNumber5;
    @FXML
    private Label balance5;
    
    @FXML
    private Button select6;
    @FXML
    private Label AccountNumber6;
    @FXML
    private Label balance6;
    
    private TransactionDAO dao = TransactionDAO.getInstance();
    //dao.getInstance();
  
   
    public void select1(ActionEvent event) throws Exception{
        dao.setSelectedAccount(AccountNumber1.getText());
        Parent main = FXMLLoader.load(getClass().getResource("Mainmenu.fxml")); // 1번 계좌에 대한 메인화면으로 이동(메인화면 fxml이랑 연결하면됨)
        Scene scene = new Scene(main);
        Stage primaryStage = (Stage)select1.getScene().getWindow();
        primaryStage.setScene(scene);
    }

    public void select2(ActionEvent event) throws Exception{
        dao.setSelectedAccount(AccountNumber2.getText());
        Parent main = FXMLLoader.load(getClass().getResource("Mainmenu.fxml")); // 2번 계좌에 대한 메인화면으로 이동(메인화면 fxml이랑 연결하면됨)
        Scene scene = new Scene(main);
        Stage primaryStage = (Stage)select2.getScene().getWindow();
        primaryStage.setScene(scene);
    }

    public void select3(ActionEvent event) throws Exception{
        dao.setSelectedAccount(AccountNumber3.getText());
        Parent main = FXMLLoader.load(getClass().getResource("Mainmenu.fxml")); // 3번 계좌에 대한 메인화면으로 이동(메인화면 fxml이랑 연결하면됨)
        Scene scene = new Scene(main);
        Stage primaryStage = (Stage)select3.getScene().getWindow();
        primaryStage.setScene(scene);
    }

    public void select4(ActionEvent event) throws Exception{
        dao.setSelectedAccount(AccountNumber4.getText());
        Parent main = FXMLLoader.load(getClass().getResource("Mainmenu.fxml")); // 4번 계좌에 대한 메인화면으로 이동(메인화면 fxml이랑 연결하면됨)
        Scene scene = new Scene(main);
        Stage primaryStage = (Stage)select4.getScene().getWindow();
        primaryStage.setScene(scene);
    }

    public void select5(ActionEvent event) throws Exception{
        dao.setSelectedAccount(AccountNumber5.getText());
        Parent main = FXMLLoader.load(getClass().getResource("Mainmenu.fxml")); // 5번 계좌에 대한 메인화면으로 이동(메인화면 fxml이랑 연결하면됨)
        Scene scene = new Scene(main);
        Stage primaryStage = (Stage)select5.getScene().getWindow();
        primaryStage.setScene(scene);
    }

    public void select6(ActionEvent event) throws Exception{
        dao.setSelectedAccount(AccountNumber6.getText());
        Parent main = FXMLLoader.load(getClass().getResource("Mainmenu.fxml")); // 1번 계좌에 대한 메인화면으로 이동(메인화면 fxml이랑 연결하면됨)
        Scene scene = new Scene(main);
        Stage primaryStage = (Stage)select6.getScene().getWindow();
        primaryStage.setScene(scene);
    }
    
    /**
     * 로그인한 ID에 포함되는 계좌의 개수마다 보여지는 계좌에대한 정보의 수가 달라집니다. (ex: 계좌1개 지닌 아이디->계좌 1번만 계좌번호, 잔액에 대한 정보가 바뀜, 나머지는 ???로 표시)
     * 각 계좌에 대한 버튼 클릭 시 해당 계좌에 대응되는 메인화면으로 이동합니다.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            Account[] myAccounts = dao.getAccountList(); // 나의 계좌 목록
            int length= myAccounts.length;
                if(length==1) {
                    AccountNumber1.setText(myAccounts[0].getAccountNumber());
                    balance1.setText(String.valueOf(myAccounts[0].getBalance()));
                }
                if(length==2) {
                    AccountNumber1.setText(myAccounts[0].getAccountNumber());
                    balance1.setText(String.valueOf(myAccounts[0].getBalance()));
                    AccountNumber2.setText(myAccounts[1].getAccountNumber());
                    balance2.setText(String.valueOf(myAccounts[1].getBalance()));
                }
                if(length==3) {
                    AccountNumber1.setText(myAccounts[0].getAccountNumber());
                    balance1.setText(String.valueOf(myAccounts[0].getBalance()));
                    AccountNumber2.setText(myAccounts[1].getAccountNumber());
                    balance2.setText(String.valueOf(myAccounts[1].getBalance()));
                    AccountNumber3.setText(myAccounts[2].getAccountNumber());
                    balance3.setText(String.valueOf(myAccounts[2].getBalance()));
                }
                if(length==4) {
                    AccountNumber1.setText(myAccounts[0].getAccountNumber());
                    balance1.setText(String.valueOf(myAccounts[0].getBalance()));
                    AccountNumber2.setText(myAccounts[1].getAccountNumber());
                    balance2.setText(String.valueOf(myAccounts[1].getBalance()));
                    AccountNumber3.setText(myAccounts[2].getAccountNumber());
                    balance3.setText(String.valueOf(myAccounts[2].getBalance()));
                    AccountNumber4.setText(myAccounts[3].getAccountNumber());
                    balance4.setText(String.valueOf(myAccounts[3].getBalance()));
                }
                if(length==5) {
                    AccountNumber1.setText(myAccounts[0].getAccountNumber());
                    balance1.setText(String.valueOf(myAccounts[0].getBalance()));
                    AccountNumber2.setText(myAccounts[1].getAccountNumber());
                    balance2.setText(String.valueOf(myAccounts[1].getBalance()));
                    AccountNumber3.setText(myAccounts[2].getAccountNumber());
                    balance3.setText(String.valueOf(myAccounts[2].getBalance()));
                    AccountNumber4.setText(myAccounts[3].getAccountNumber());
                    balance4.setText(String.valueOf(myAccounts[3].getBalance()));
                    AccountNumber5.setText(myAccounts[4].getAccountNumber());
                    balance5.setText(String.valueOf(myAccounts[4].getBalance()));
                }
                if(length==6) {
                    AccountNumber1.setText(myAccounts[0].getAccountNumber());
                    balance1.setText(String.valueOf(myAccounts[0].getBalance()));
                    AccountNumber2.setText(myAccounts[1].getAccountNumber());
                    balance2.setText(String.valueOf(myAccounts[1].getBalance()));
                    AccountNumber3.setText(myAccounts[2].getAccountNumber());
                    balance3.setText(String.valueOf(myAccounts[2].getBalance()));
                    AccountNumber4.setText(myAccounts[3].getAccountNumber());
                    balance4.setText(String.valueOf(myAccounts[3].getBalance()));
                    AccountNumber5.setText(myAccounts[4].getAccountNumber());
                    balance5.setText(String.valueOf(myAccounts[4].getBalance()));
                    AccountNumber6.setText(myAccounts[5].getAccountNumber());
                    balance6.setText(String.valueOf(myAccounts[5].getBalance()));
                }
        }catch(Exception e){
            e.getStackTrace();
        }
        select1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    select1(event);
                }catch(Exception e) {
                    e.getStackTrace();
                }
            }
        });
        select2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    select2(event);
                }catch(Exception e) {
                    e.getStackTrace();
                }
            }
        });
        select3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    select3(event);
                }catch(Exception e) {
                    e.getStackTrace();
                }
            }
        });
        select4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    select4(event);
                }catch(Exception e) {
                    e.getStackTrace();
                }
            }
        });
        select5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    select5(event);
                }catch(Exception e) {
                    e.getStackTrace();
                }
            }
        });
        select6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    select6(event);
                }catch(Exception e) {
                    e.getStackTrace();
                }
            }
        });
        
    }

}
