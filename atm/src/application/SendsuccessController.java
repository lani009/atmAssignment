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
		Account transaction = dao.getAccount("");
		name.setText();
		Account.setText("account.accountnumber()");
		money.setText("Account.money()");
		balance.setText("account.balance()");
		backtoMainmenu.setOnAction(e -> {
			Parent login;
			try {
				login = FXMLLoader.load(getClass().getResource("Mainmenu.fxml"));
				Scene scene = new Scene(login);

				Stage primaryStage = (Stage) backtoMainmenu.getScene().getWindow(); // 현재 윈도우 가져오기

				primaryStage.setScene(scene);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

	}

}
