package project3.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import project3.form.Account;
import project3.form.Transaction;
import project3.form.TransactionDAO;

public class DepositOkController implements Initializable {
    @FXML
    private Label BankName;
    @FXML
    private Label AccountNumber;
    @FXML
    private Label Depositamount;
    @FXML
    private Label Balance;
    @FXML
    private Button Ok;
    private TransactionDAO dao = TransactionDAO.getInstance();
    private Transaction previous = dao.getPreviousTransaction(); // 이전 예금에서 사용한 Transaction객체 불러오기

    /**
     * 예금 처리결과 확인을 마친 후 메인화면으로 돌아가기 위해 사용되는 이벤트이다.
     * 
     * @param event
     * @throws IOException
     */
    public void OkAction(ActionEvent event) throws IOException {
        Parent main = FXMLLoader.load(getClass().getResource("fxml/Mainmenu.fxml")); // 메인화면 연결
        Scene scene = new Scene(main);
        Stage primaryStage = (Stage) Ok.getScene().getWindow();
        primaryStage.setScene(scene);
    }

    /**
     * 은행이름, 계좌번호, 잔액등을 화면을 통해 확인할 수 있다.
     * 
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        try {
            Account transactionAccount;
            transactionAccount = dao.getAccount(dao.getSelectedAccount());  // 계좌선택에서 받아온 계좌번호 가져오기
            BankName.setText(String.valueOf(transactionAccount.getBankType()));
            AccountNumber.setText(transactionAccount.getDashedAccountNumber());
            Depositamount.setText(String.valueOf(previous.getAmount()));    // 예금화면에서 얼마나 예금했는지 알려주기
            Balance.setText(String.valueOf(transactionAccount.getBalance()));
        } catch (Exception e) {
            e.getStackTrace();
        }

        Ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    OkAction(event);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        });
    }

}
