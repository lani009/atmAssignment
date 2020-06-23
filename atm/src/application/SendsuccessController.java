package application;

import java.io.IOException;
import java.net.URL;
import java.rmi.server.ServerNotActiveException;
import java.util.ResourceBundle;

import javax.security.auth.login.AccountNotFoundException;

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
	private Label Account;	// TODO 이름 수정
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
		Transaction previousTransaction = dao.getPreviousTransaction();

		name.setText(previousTransaction.getTo().getBankType().toString());
		Account.setText(previousTransaction.getTo().getAccountNumber());
		// SendController c = new SendController();

		money.setText(previousTransaction.getAmount().toString());
		try {
			balance.setText(dao.getAccount(previousTransaction.getFrom().getAccountNumber()).getBalance().toString());
		} catch (AccountNotFoundException | ServerNotActiveException e2) {
			e2.printStackTrace();
		}
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
