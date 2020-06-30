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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import form.Account;
import form.TransactionDAO;
import form.Enum.BankType;
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
    
    private TransactionDAO dao =TransactionDAO.getInstance();
    
    /**
     * 예금 값 입력 후 OK입력 시 예금 결과화면 출력 이벤트 이다.
     * 가짜atm으로 부터 예금하고자 하는 계좌로 돈이 송금된다.
     * 예금이 종료되면 예금 확인 화면이 출력된다.
     * @param event
     * @throws Exception
     */
    public void OkAction(ActionEvent event) throws Exception{
        String amount=Deposit.getText();
        String pw = Pw.getText();
        if(amount.isEmpty()) {
            status.setTextFill(Color.valueOf("#f00a0a"));
            status.setText("Pleas input Amount!");
            return;
        }
        if(pw.isEmpty()) {
            status.setTextFill(Color.valueOf("#f00a0a"));
            status.setText("Pleas input password!");
            return;
        }
        Account ATM = dao.searchAccount("ATM", BankType.MDCBank);   // 가짜 ATM 객체
        
        Account transactionAccount = dao.getAccount(dao.getSelectedAccount());  // 계좌선택에서 받아온 계좌번호 가져오기
        if(dao.checkPassword(pw)) {// 입력한 비밀번호와 해당 Id에 대응되는 비밀번호를 비교
            Transaction depositTransaction = new Transaction(TransactionType.DEPOSIT, ATM, transactionAccount,
                BigInteger.valueOf(Integer.parseInt(amount)));
            dao.sendTransaction(depositTransaction); // 거래를 처리
        } else {
            // 비밀번호를 틀린 경우임
            status.setTextFill(Color.valueOf("#f00a0a"));
            status.setText("Wrong Password!");
            return;
        }
        Parent dere = FXMLLoader.load(getClass().getResource("fxml/Deposit_ok.fxml")); // 예금결과화면 연결
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

        Deposit.textProperty().addListener((observable, oldVal, newVal) -> {
            InputUtil.detectString(Deposit, oldVal, newVal);
		});
    }
}
