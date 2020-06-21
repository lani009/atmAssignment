package application;

import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;

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

public class WithdrawController implements Initializable{
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
     * 출금 값 입력 후 OK입력 시 출금 결과화면 출력 이벤트이다.
     * 출금하고자 하는 계좌로 부터 가짜 atm으로 돈을 송금시킴으로서 출금이 완료된다.
     * @param event
     * @throws Exception
     */
    public void OkAction(ActionEvent event) throws Exception{
        String amount=Withdraw.getText();
        String pw = Pw.getText();
        //dao.getInstance();
        Account 가짜atm = dao.searchAccount("ATM", BankType.MDCBank);
        Account transactionAccount = dao.getAccount(dao.getSelectedAccount());//** 계좌선택에서 받아온 계좌번호 가져오기
        if(dao.checkPassword(pw)) {// 입력한 비밀번호와 해당 ID에 대응되는 비밀번호를 비교
            Transaction 출금 = new Transaction(TransactionType.WITHDRAWL, transactionAccount, 가짜atm,
                BigInteger.valueOf(Integer.parseInt(amount)));

            dao.sendTransaction(출금); // 거래를 처리
        }
        
        Parent dere = FXMLLoader.load(getClass().getResource("fxml/Withdraw_ok.fxml")); // 출금결과화면 연결
        Scene scene = new Scene(dere);
        Stage primaryStage = (Stage)Ok.getScene().getWindow();
        primaryStage.setScene(scene);
    }
    
    /**
     * Back 버튼 클릭 시 출금을 진행하지 않고 메인화면으로 이동
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
    }

}
