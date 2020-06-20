package application;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.rmi.server.ServerNotActiveException;
import java.util.ResourceBundle;

import javax.security.auth.login.AccountNotFoundException;

import form.Account;
import form.Transaction;
import form.TransactionDAO;
import form.Enum.BankType;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WithdrawController implements Initializable {
    @FXML
    private TextField withdrawField;
    @FXML
    private TextField pwField;

    @FXML
    private Button okButton;
    @FXML
    private Button bankButton;
    @FXML
    private Label statusLabel;
    private TransactionDAO dao;

    /**
     * 출금 값 입력 후 OK입력 시 출금 결과화면 출력 이벤트이다. 출금하고자 하는 계좌로 부터 가짜 atm으로 돈을 송금시킴으로서 출금이
     * 완료된다.
     * 
     * @param event
     */
    public void OkAction(ActionEvent event) throws ServerNotActiveException, AccountNotFoundException {
        String amount = withdrawField.getText();
        // String pw = pw.getText();
        dao = TransactionDAO.getInstance();
        Account atm = dao.searchAccount("ATM", BankType.MDCBank);
        Account transactionAccount = dao.getAccount(dao.getSelectedAccount());// ** 계좌선택에서 받아온 계좌번호 가져오는 메소드 필요!!
        if (dao.checkPassword(pwField.getText())) {
            Transaction withdrawTransaction = new Transaction(TransactionType.WITHDRAWL, transactionAccount, atm,
                    BigInteger.valueOf(Integer.parseInt(amount)));

            dao.sendTransaction(withdrawTransaction); // 거래를 처리
        }

        Parent dere;
        try {
            dere = FXMLLoader.load(getClass().getResource("???.fxml"));
            Scene scene = new Scene(dere);
            Stage primaryStage = (Stage)okButton.getScene().getWindow();
            primaryStage.setScene(scene);   // 출금결과화면 연결
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Back 버튼 클릭 시 출금을 진행하지 않고 메인화면으로 이동
     * @param event
     * @throws Exception
     */
    public void BackAction(ActionEvent event) throws Exception{
        Parent main = FXMLLoader.load(getClass().getResource("???.fxml")); // 메인화면 연결
        Scene scene = new Scene(main);
        Stage primaryStage = (Stage)okButton.getScene().getWindow();
        primaryStage.setScene(scene);
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    OkAction(event);
                } catch (Exception e) {
                   statusLabel.setText("Withdraw Failed!");
                }
            }});
        
        bankButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    BackAction(event);
                }catch(Exception e) {
                    statusLabel.setText("메인화면으로 나갈 수 없습니다!");
                }
            }
        });
    }

}
