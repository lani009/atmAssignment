package application;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.rmi.server.ServerNotActiveException;
import java.util.ResourceBundle;

import form.Account;
import form.TransactionDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
                    ((Label) pane.getChildren().get(1)).setText(temp.getBalance().toString() + "원");
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
        }catch(Exception e){
            e.getStackTrace();
        }
    }

}
