package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import form.TransactionDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainmenuController implements Initializable {
	@FXML
	Label welcome;
	@FXML
	Label Account;
	@FXML
	Button deposit;
	@FXML
	Button withdraw;
	@FXML
	Button send;
	@FXML
	Button T;
	@FXML
	Button account;
	@FXML
	Button logout;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/*
		 * 계좌 아이디를 불러와서 출력
		 */
		/*
		 * dao=getlogin(); welcome.setText(dao);
		 */
		/*
		 * 계좌 번호를 출력
		 */

		/*
		 * Account.setText(dao);
		 *
		 * 계좌 선택 화면으로 돌아가기
		 */
		account.setOnAction(e -> {
			Parent login;
			try {
				login = FXMLLoader.load(getClass().getResource("Account.fxml"));
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
				login = FXMLLoader.load(getClass().getResource("deposit.fxml"));
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
				login = FXMLLoader.load(getClass().getResource("withdraw.fxml"));
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
				login = FXMLLoader.load(getClass().getResource("Send.fxml"));
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
				login = FXMLLoader.load(getClass().getResource("Record.fxml"));
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
				login = FXMLLoader.load(getClass().getResource("login.fxml"));
				Scene scene = new Scene(login);

				Stage primaryStage = (Stage) logout.getScene().getWindow(); // 현재 윈도우 가져오기

				primaryStage.setScene(scene);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

	}

}
