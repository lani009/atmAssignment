package application;

import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;

import form.Transaction;
import form.Enum.TransactionType;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.math.BigInteger;
import java.rmi.server.ServerNotActiveException;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.LoginException;

import form.Account;
import form.Transaction;
import form.TransactionDAO;
import form.Enum.BankType;
import form.Enum.TransactionType;
public class DepositController implements Initializable{
    @FXML
    private TextField Deposit;
    @FXML
    private PasswordField Pw;
    @FXML
    private Button Ok;
    @FXML
    private Button Back;
    @FXML
    private Label status;
    
    private TransactionDAO dao;
    
    /**
     * 예금 값 입력 후 OK입력 시 예금 결과화면 출력 이벤트 이다.
     * 가짜atm으로 부터 예금하고자 하는 계좌로 돈이 송금된다.
     * 예금이 종료되면 예금 확인 화면이 출력된다.
     * @param event
     * @throws Exception
     */
    public void OkAction(ActionEvent event) throws Exception{
        String amount = Deposit.getText();
        String pw = Pw.getText();
        dao = TransactionDAO.getInstance();
        Account 가짜atm = dao.searchAccount("ATM", BankType.MDCBank);
        Account transactionAccount = dao.searchAccount(dao.getSelectedAccount(), dao.getSelectedBankType());  //** 계좌선택에서 받아온 계좌번호 가져오는 메소드 필요!!
        if(dao.checkPassword(pw)) {
            Transaction 예금 = new Transaction(TransactionType.DEPOSIT, transactionAccount, 가짜atm,
                BigInteger.valueOf(Integer.parseInt(amount)));

            dao.sendTransaction(예금);  // 거래를 처리
        }
        Parent dere = FXMLLoader.load(getClass().getResource("???.fxml")); // 예금결과화면 연결
        Scene scene = new Scene(dere);
        Stage primaryStage = (Stage)Ok.getScene().getWindow();
        primaryStage.setScene(scene);
    }
    
    /**
     * Back 버튼 클릭 시 예금을 진행하지 않고 메인화면으로 이동
     * @param event
     * @throws Exception
     */
    public void BackAction(ActionEvent event) throws Exception{
        Parent main = FXMLLoader.load(getClass().getResource("???.fxml")); // 메인화면 연결
        Scene scene = new Scene(main);
        Stage primaryStage = (Stage)Ok.getScene().getWindow();
        primaryStage.setScene(scene);
    }
    
    
    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        Ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    OkAction(event);
                } catch (Exception e) {
                   status.setText("Deposit Failed!");
                }
            }});
        
        Back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    BackAction(event);
                }catch(Exception e) {
                    status.setText("메인화면으로 나갈 수 없습니다!");
                }
            }
        });
    }
}
