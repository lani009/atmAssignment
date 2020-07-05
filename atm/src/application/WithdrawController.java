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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class WithdrawController implements Initializable {
    @FXML
    private TextField Withdraw;
    @FXML
    private TextField Pw;
    @FXML
    private Button Ok;
    @FXML
    private Button Back;
    @FXML
    private Label status;
    private TransactionDAO dao = TransactionDAO.getInstance();

    /**
     * 출금 값 입력 후 OK입력 시 출금 결과화면 출력 이벤트이다. 출금하고자 하는 계좌로 부터 가짜 atm으로 돈을 송금시킴으로서 출금이
     * 완료된다.
     * 
     * @param event
     * @throws IOException
     * @throws ServerNotActiveException
     * @throws AccountNotFoundException
     */
    public void OkAction(ActionEvent event) throws IOException, AccountNotFoundException, ServerNotActiveException {
        String amount=Withdraw.getText();
        String pw = Pw.getText();
        if(amount.isEmpty()) {
            status.setTextFill(Color.valueOf("#f00a0a"));
            status.setText("Please Input Amount!");
            return;
        }
        if(pw.isEmpty()) {
            status.setTextFill(Color.valueOf("#f00a0a"));
            status.setText("Please Input Password!");
            return;
        }
        Account fakeAtm = dao.searchAccount("ATM", BankType.MDCBank);
        Account transactionAccount = dao.getAccount(dao.getSelectedAccount());//** 계좌선택에서 받아온 계좌번호 가져오기
        if(dao.checkPassword(pw)) {// 입력한 비밀번호와 해당 ID에 대응되는 비밀번호를 비교
            if(transactionAccount.getBalance().compareTo(BigInteger.valueOf(Integer.parseInt(amount)))==-1) {
                status.setTextFill(Color.valueOf("#f00a0a"));
                status.setText("잔고가 부족합니다.");
                return ;
            }else {
                Transaction withdrawTransaction = new Transaction(TransactionType.WITHDRAWL, transactionAccount, fakeAtm,
                BigInteger.valueOf(Integer.parseInt(amount)));
                dao.sendTransaction(withdrawTransaction); // 거래를 처리
            }
        } else {
            status.setTextFill(Color.valueOf("#f00a0a"));
            status.setText("Wrong Password!");
            return;
        }
        
        Parent dere = FXMLLoader.load(getClass().getResource("fxml/Withdraw_ok.fxml")); // 출금결과화면 연결
        Scene scene = new Scene(dere);
        Stage primaryStage = (Stage)Ok.getScene().getWindow();
        primaryStage.setScene(scene);
    }
    
    /**
     * Back 버튼 클릭 시 출금을 진행하지 않고 메인화면으로 이동
     * @param event
     * @throws IOException
     */
    public void BackAction(ActionEvent event) throws IOException {
        Parent main = FXMLLoader.load(getClass().getResource("fxml/Mainmenu.fxml")); // 메인화면 연결
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
                   status.setText("Withdraw Failed!");
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

        Withdraw.textProperty().addListener((observable, oldVal, newVal) -> {
            InputUtil.detectString(Withdraw, oldVal, newVal);
		});
    }

}
