package project3.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import project3.form.TransactionDAO;

public class MainmenuController implements Initializable {
	@FXML
	Label welcome;
	@FXML
	Label accountLabel;
	@FXML
	Button deposit;
	@FXML
	Button withdraw;
	@FXML
	Button send;
	@FXML
	Button T;	// Transaction Button
	@FXML
	Button account;	// back to account select window
	@FXML
	Button logout;

	private TransactionDAO dao = TransactionDAO.getInstance();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		welcome.setText(dao.getUserId() + "님 환영합니다!");
		String accountNumber = dao.getSelectedDashedAccount();
		accountLabel.setText(accountNumber);

		account.setOnAction(e -> {
			Parent login;
			try {
				login = FXMLLoader.load(getClass().getResource("fxml/AccountChoose.fxml"));
				Scene scene = new Scene(login);

				Stage primaryStage = (Stage) account.getScene().getWindow(); // 현재 윈도우 가져오기

				primaryStage.setScene(scene);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		// 예금 화면으로 전환하기

		deposit.setOnAction(e -> {
			Parent login;
			try {
				login = FXMLLoader.load(getClass().getResource("fxml/deposit.fxml"));
				Scene scene = new Scene(login);

				Stage primaryStage = (Stage) deposit.getScene().getWindow(); // 현재 윈도우 가져오기

				primaryStage.setScene(scene);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		// 출금 화면으로 전환하기

		withdraw.setOnAction(e -> {
			Parent login;
			try {
				login = FXMLLoader.load(getClass().getResource("fxml/withdraw.fxml"));
				Scene scene = new Scene(login);

				Stage primaryStage = (Stage) withdraw.getScene().getWindow(); // 현재 윈도우 가져오기

				primaryStage.setScene(scene);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		/*
		 * 송금 화면으로 전환하기
		 */

		send.setOnAction(e -> {
			Parent login;
			try {
				login = FXMLLoader.load(getClass().getResource("fxml/Send.fxml"));
				Scene scene = new Scene(login);

				Stage primaryStage = (Stage) send.getScene().getWindow(); // 현재 윈도우 가져오기

				primaryStage.setScene(scene);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		/*
		 * 거래 내역 화면으로 전환하기
		 */
		T.setOnAction(e -> {
			Parent login;
			try {
				login = FXMLLoader.load(getClass().getResource("fxml/Record.fxml"));
				Scene scene = new Scene(login);

				Stage primaryStage = (Stage) T.getScene().getWindow(); // 현재 윈도우 가져오기

				primaryStage.setScene(scene);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		/*
		 * 로그아웃, 로그인 화면으로 전환하기
		 */
		logout.setOnAction(e -> {
			try {
				TransactionDAO.logout();
			} catch (IOException e2) {
				e2.printStackTrace();
			}

			Parent login;
			try {
				login = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));
				Scene scene = new Scene(login);

				Stage primaryStage = (Stage) logout.getScene().getWindow(); // 현재 윈도우 가져오기

				primaryStage.setScene(scene);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

	}

}
