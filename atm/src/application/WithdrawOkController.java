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

public class WithdrawOkController implements Initializable {
    @FXML
    private Label bankNameLabel;
    @FXML
    private Label accountLabel;
    @FXML
    private Label withdrawAmountLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Button okButton;
    private TransactionDAO dao;

    /**
     * 출금 처리결과 확인을 마친 후 메인화면으로 돌아가기 위해 사용되는 이벤트이다.
     * 
     * @param event
     * @throws Exception
     */
    public void OkAction(ActionEvent event) throws Exception {
        Parent main = FXMLLoader.load(getClass().getResource("???.fxml")); // 메인화면 연결
        Scene scene = new Scene(main);
        Stage primaryStage = (Stage) okButton.getScene().getWindow();
        primaryStage.setScene(scene);
    }

    /**
     * 은행이름, 계좌번호, 잔액등을 화면을 통해 확인할 수 있다.
     * 
     * @param event
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        try {
            dao = TransactionDAO.getInstance();
            Account transactionAccount = dao.getAccount(dao.getSelectedAccount()); // 계좌선택에서 받아온 계좌번호 가져오는 메소드 필요!!
            bankNameLabel.setText(String.valueOf(transactionAccount.getBankType()));
            accountLabel.setText(transactionAccount.getAccountNumber());
            withdrawAmountLabel.setText(""); // 고치기 필요, 출금한 금액이 얼만지 가져오는 메소드 필요!!
            balanceLabel.setText(String.valueOf(transactionAccount.getBalance()));
        } catch (Exception e) {
            e.getStackTrace();
        }

        okButton.setOnAction(new EventHandler<ActionEvent>() {
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
