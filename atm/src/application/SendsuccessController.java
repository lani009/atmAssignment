package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import form.Account;
import form.Transaction;
import form.TransactionDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SendsuccessController implements Initializable {
	@FXML
	private Label name;
	@FXML
	private Label Account;
	@FXML
	private Label money;
	@FXML
	private Label balance;
	@FXML
	private Button backtoMainmenu;
	private TransactionDAO dao;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		dao = TransactionDAO.getInstance();
		Account transaction = dao.getAccount(""); //TODO 계좌 정보를 불러오는 메소드 필요
		name.setText(dao.getUserId());//TODO 괄호 안에 id를 불러오는 메소드 필요 
		Account.setText(dao.getSelectedAccount());//TODO 괄호 안에 계좌번호를 불러오는 메소드 필요 
		SendController c = new SendController();
		money.setText(c.Money());//TODO 괄호 안에 송금액를 불러오는 메소드 필요 (sendController에서 불러와야 되는데 이렇게 해도 되는지 모르겠다)
		balance.setText("");//TODO 괄호 안에 id를 불러오는 메소드 필요 
		backtoMainmenu.setOnAction(e -> {
			Parent login;
			try {
				login = FXMLLoader.load(getClass().getResource("fxml/Mainmenu.fxml"));
				Scene scene = new Scene(login);

				Stage primaryStage = (Stage) backtoMainmenu.getScene().getWindow(); // 현재 윈도우 가져오기

				primaryStage.setScene(scene);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

	}

}
