package application;

import java.io.IOException;
import java.net.URL;
import java.rmi.server.ServerNotActiveException;
import java.util.ResourceBundle;

import javax.security.auth.login.AccountNotFoundException;

import form.Account;
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

	private TransactionDAO dao = TransactionDAO.getInstance();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		/*
		 * dao=getlogin(); welcome.setText(dao.id+"님 환영합니다!");//TODO id를 불러오는 메소드 필요
		 */
		String id = dao.getUserId(); // id 불러오는 메소드

		/*
		 * Account.setText(dao); //TODO 계좌 선택화면에서 계좌 번호를 불러오는 메소드 필요
		 *
		 * 계좌 선택 화면으로 돌아가기
		 */
		String accountNumber = dao.getSelectedAccount(); // 선택된 계좌 불러오는 메소드
		try {
			Account accountObject = dao.getAccount(accountNumber);	// 이거 처럼 활용 가능
		} catch (AccountNotFoundException | ServerNotActiveException e3) {
			e3.printStackTrace();
		} 


		
		account.setOnAction(e -> {
			Parent login;
			try {
				login = FXMLLoader.load(getClass().getResource("fxml/Account.fxml"));
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
				TransactionDAO.logout();//TODO 로그아웃 하기
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
