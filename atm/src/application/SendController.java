package application;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.rmi.server.ServerNotActiveException;
import java.util.ResourceBundle;

import javax.security.auth.login.AccountNotFoundException;

import form.Account;
import form.Transaction;
import form.TransactionDAO;
import form.Enum.BankType;
import form.Enum.TransactionType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SendController implements Initializable {
	@FXML
	TextField Account;
	@FXML
	TextField Amount;
	@FXML
	Button backtoMainmenu;
	@FXML
	Button MDCBank;
	@FXML
	Button KAKAOBank;
	@FXML
	Button NHBank;
	@FXML
	Button AJOUBank;
	@FXML
	Button send;
	@FXML
	Label message;
	@FXML
	Label Bank;
	@FXML
	TextField pw;
	@FXML
	Label selectedBankType;

	private int num = 0;

	TransactionDAO dao = TransactionDAO.getInstance();;

	public void MDCBankAction(ActionEvent e) {
		selectedBankType.setText("MDCBank selected");
		num = 1;
	}

	public void KAKAOBankAction(ActionEvent e) {
		selectedBankType.setText("KAKAOBank selected");
		num = 2;
	}

	public void NHBankAction(ActionEvent e) {
		selectedBankType.setText("NHBank selected");
		num = 3;
	}

	public void AJOUBankAction(ActionEvent e) {
		selectedBankType.setText("AJOUBank selected");
		num = 4;
	}

	private void gotoNextPage() {
		Parent login;
		try {
			login = FXMLLoader.load(getClass().getResource("fxml/Sendsuccess.fxml"));
			Scene scene = new Scene(login);

			Stage primaryStage = (Stage) backtoMainmenu.getScene().getWindow(); // 현재 윈도우 가져오기

			primaryStage.setScene(scene);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		MDCBank.setOnAction(e -> {
			MDCBankAction(e);
		});
		KAKAOBank.setOnAction(e -> {
			KAKAOBankAction(e);
			
		});
		NHBank.setOnAction(e -> {
			NHBankAction(e);
			
		});
		AJOUBank.setOnAction(e -> {
			AJOUBankAction(e);
			
		});
		send.setOnAction(e -> {
			try {
				Account transactionAccount = dao.getAccount(dao.getSelectedAccount());
				Account opponentAccount = dao.searchAccount(Account.getText(), BankType.parseBank(num));
				if(transactionAccount.getBalance().compareTo(BigInteger.valueOf(Long.parseLong(Amount.getText()))) == -1) {
					message.setText("The balance is lack");

					return;
				}
				if (dao.checkPassword(pw.getText())) {
					Transaction transaction = new Transaction(TransactionType.TRANSFER, transactionAccount,
							opponentAccount, BigInteger.valueOf(Integer.parseInt(Amount.getText())));

					dao.sendTransaction(transaction);
				}
				gotoNextPage();
			} catch (AccountNotFoundException | ServerNotActiveException e1) {
				message.setText("Cannot find the account");
				// 상대방의 계좌를 못 찾았을 경우임.
				// TODO 추가로 잔고보다 송금하려는 금액이 많을 때 예외 처리
			}

		});

		Account.textProperty().addListener((observable, oldVal, newVal) -> {
            InputUtil.detectString(Account, oldVal, newVal);
		});
		
		Amount.textProperty().addListener((observable, oldVal, newVal) -> {
            InputUtil.detectString(Amount, oldVal, newVal);
		});

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
