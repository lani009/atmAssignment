package application;

import java.io.IOException;
import java.net.URL;
import java.rmi.server.ServerNotActiveException;
import java.util.ResourceBundle;

import form.Account;
import form.TransactionDAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AccountListController implements Initializable {
    private int controll = 0;
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

    @FXML
    private GridPane grid;

    private TransactionDAO dao = TransactionDAO.getInstance();

    // public void select1(ActionEvent event) throws Exception {
    //     goToMainMenu(AccountNumber1.getText());
    // }

    // public void select2(ActionEvent event) throws Exception {
    //     goToMainMenu(AccountNumber2.getText());
    // }

    // public void select3(ActionEvent event) throws Exception {
    //     goToMainMenu(AccountNumber3.getText());
    // }

    // public void select4(ActionEvent event) throws Exception {
    //     goToMainMenu(AccountNumber4.getText());
    // }

    // public void select5(ActionEvent event) throws Exception {
    //     goToMainMenu(AccountNumber5.getText());
    // }

    // public void select6(ActionEvent event) throws Exception {
    //     goToMainMenu(AccountNumber6.getText());
    // }

    /**
     * 메인 메뉴로 화면전환
     * 
     * @throws IOException
     * @throws ServerNotActiveException
     */
    public void goToMainMenu(Pane pane) throws IOException, ServerNotActiveException {
        String selectedAccount = deleteDash(((Label) pane.getChildren().get(4)).getText());
        dao.setSelectedAccount(selectedAccount);
        Parent main = FXMLLoader.load(getClass().getResource("fxml/Mainmenu.fxml")); // 1번 계좌에 대한 메인화면으로 이동(메인화면 fxml이랑 연결하면됨)
        Scene scene = new Scene(main);
        Stage primaryStage = (Stage)select1.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * 계좌번호에 붙은 하이픈 제거
     * @param accountNumber 하이픈 붙은 계좌번호
     * @return
     */
    private String deleteDash(String accountNumber) {
        char[] temp = new char[accountNumber.length() - 2];
        int j = 0;
        for (char i : accountNumber.toCharArray()) {
            if(i =='-') {
                continue;
            } else {
                temp[j++] = i;
            }
        }
        return String.valueOf(temp);
    }
    
    /**
     * 로그인한 ID에 포함되는 계좌의 개수마다 보여지는 계좌에대한 정보의 수가 달라집니다. (ex: 계좌1개 지닌 아이디->계좌 1번만 계좌번호, 잔액에 대한 정보가 바뀜, 나머지는 ???로 표시)
     * 각 계좌에 대한 버튼 클릭 시 해당 계좌에 대응되는 메인화면으로 이동합니다.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            Account[] myAccounts = dao.getAccountList();
            Pane[] list = new Pane[6];
            grid.getChildren().toArray(list);
            int i = 0;
            for (Pane pane : list) {
                if(myAccounts.length > i) {
                    Account temp = myAccounts[i++];
                    ((Label) pane.getChildren().get(1)).setText(temp.getBalance().toString());
                    ((Label) pane.getChildren().get(4)).setText(temp.getDashedAccountNumber());
                    ((Button) pane.getChildren().get(5)).setOnAction(e -> {
                        try {
                            goToMainMenu(pane);
                        } catch (IOException | ServerNotActiveException e1) {
                            e1.printStackTrace();
                        }
                    });
                } else {
                    ((Label) pane.getChildren().get(1)).setText("-");
                    ((Label) pane.getChildren().get(4)).setText("-");
                    ((Button) pane.getChildren().get(5)).setOnAction(e -> {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("경고");
                        alert.setHeaderText("없는 계좌입니다.");
                        alert.setContentText("다른 계좌를 선택해주세요.");
                        
                        alert.showAndWait();
                    });
                }
            }
            // Account[] myAccounts = dao.getAccountList(); // 나의 계좌 목록
            // int length= myAccounts.length;
            //     if(length==1) {
            //         AccountNumber1.setText(myAccounts[0].getAccountNumber());
            //         balance1.setText(String.valueOf(myAccounts[0].getBalance()));
            //         controll=1;
            //     }
            //     if(length==2) {
            //         AccountNumber1.setText(myAccounts[0].getAccountNumber());
            //         balance1.setText(String.valueOf(myAccounts[0].getBalance()));
            //         AccountNumber2.setText(myAccounts[1].getAccountNumber());
            //         balance2.setText(String.valueOf(myAccounts[1].getBalance()));
            //         controll=2;
            //     }
            //     if(length==3) {
            //         AccountNumber1.setText(myAccounts[0].getAccountNumber());
            //         balance1.setText(String.valueOf(myAccounts[0].getBalance()));
            //         AccountNumber2.setText(myAccounts[1].getAccountNumber());
            //         balance2.setText(String.valueOf(myAccounts[1].getBalance()));
            //         AccountNumber3.setText(myAccounts[2].getAccountNumber());
            //         balance3.setText(String.valueOf(myAccounts[2].getBalance()));
            //         controll=3;
            //     }
            //     if(length==4) {
            //         AccountNumber1.setText(myAccounts[0].getAccountNumber());
            //         balance1.setText(String.valueOf(myAccounts[0].getBalance()));
            //         AccountNumber2.setText(myAccounts[1].getAccountNumber());
            //         balance2.setText(String.valueOf(myAccounts[1].getBalance()));
            //         AccountNumber3.setText(myAccounts[2].getAccountNumber());
            //         balance3.setText(String.valueOf(myAccounts[2].getBalance()));
            //         AccountNumber4.setText(myAccounts[3].getAccountNumber());
            //         balance4.setText(String.valueOf(myAccounts[3].getBalance()));
            //         controll=4;
            //     }
            //     if(length==5) {
            //         AccountNumber1.setText(myAccounts[0].getAccountNumber());
            //         balance1.setText(String.valueOf(myAccounts[0].getBalance()));
            //         AccountNumber2.setText(myAccounts[1].getAccountNumber());
            //         balance2.setText(String.valueOf(myAccounts[1].getBalance()));
            //         AccountNumber3.setText(myAccounts[2].getAccountNumber());
            //         balance3.setText(String.valueOf(myAccounts[2].getBalance()));
            //         AccountNumber4.setText(myAccounts[3].getAccountNumber());
            //         balance4.setText(String.valueOf(myAccounts[3].getBalance()));
            //         AccountNumber5.setText(myAccounts[4].getAccountNumber());
            //         balance5.setText(String.valueOf(myAccounts[4].getBalance()));
            //         controll=5;
            //     }
            //     if(length==6) {
            //         AccountNumber1.setText(myAccounts[0].getAccountNumber());
            //         balance1.setText(String.valueOf(myAccounts[0].getBalance()));
            //         AccountNumber2.setText(myAccounts[1].getAccountNumber());
            //         balance2.setText(String.valueOf(myAccounts[1].getBalance()));
            //         AccountNumber3.setText(myAccounts[2].getAccountNumber());
            //         balance3.setText(String.valueOf(myAccounts[2].getBalance()));
            //         AccountNumber4.setText(myAccounts[3].getAccountNumber());
            //         balance4.setText(String.valueOf(myAccounts[3].getBalance()));
            //         AccountNumber5.setText(myAccounts[4].getAccountNumber());
            //         balance5.setText(String.valueOf(myAccounts[4].getBalance()));
            //         AccountNumber6.setText(myAccounts[5].getAccountNumber());
            //         balance6.setText(String.valueOf(myAccounts[5].getBalance()));
            //         controll=6;
            //     }
        }catch(Exception e){
            e.getStackTrace();
        }
        // select1.setOnAction(new EventHandler<ActionEvent>() {
        //     @Override
        //     public void handle(ActionEvent event) {
        //         try {
        //             if(controll>0) {
        //                 select1(event);
        //             }
        //         }catch(Exception e) {
        //             e.getStackTrace();
        //         }
        //     }
        // });
        // select2.setOnAction(new EventHandler<ActionEvent>() {
        //     @Override
        //     public void handle(ActionEvent event) {
        //         try {
        //             if(controll>1) {
        //                 select2(event);
        //             }
        //         }catch(Exception e) {
        //             e.getStackTrace();
        //         }
        //     }
        // });
        // select3.setOnAction(new EventHandler<ActionEvent>() {
        //     @Override
        //     public void handle(ActionEvent event) {
        //         try {
        //             if(controll>2) {
        //                 select3(event);
        //             }
        //         }catch(Exception e) {
        //             e.getStackTrace();
        //         }
        //     }
        // });
        // select4.setOnAction(new EventHandler<ActionEvent>() {
        //     @Override
        //     public void handle(ActionEvent event) {
        //         try {
        //             if(controll>3) {        
        //                 select4(event);
        //             }
        //         }catch(Exception e) {
        //             e.getStackTrace();
        //         }
        //     }
        // });
        // select5.setOnAction(new EventHandler<ActionEvent>() {
        //     @Override
        //     public void handle(ActionEvent event) {
        //         try {
        //             if(controll>4) {
        //                 select5(event);
        //             }
        //         }catch(Exception e) {
        //             e.getStackTrace();
        //         }
        //     }
        // });
        // select6.setOnAction(new EventHandler<ActionEvent>() {
        //     @Override
        //     public void handle(ActionEvent event) {
        //         try {
        //             if(controll>5) {
        //                 select6(event);
        //             }
        //         }catch(Exception e) {
        //             e.getStackTrace();
        //         }
        //     }
        // });
        
    }

}
