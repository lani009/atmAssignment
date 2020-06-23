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

	private int num = 0;

	TransactionDAO dao = TransactionDAO.getInstance();;

	public void MDCBankAction(ActionEvent e) {
		num = 1;
	}

	public void KAKAOBankAction(ActionEvent e) {
		num = 2;
	}

	public void NHBankAction(ActionEvent e) {
		num = 3;
	}

	public void AJOUBankAction(ActionEvent e) {
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
            detectString(Account, oldVal, newVal);
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

	/**
     * 텍스트 필드에 non-int 타입의 값이 입력되는 것을 방지한다.
     * @param textField 감지할 TextField
     * @param oldVal oldVal
     * @param newVal newVal
     */
    private void detectString(TextField textField, String oldVal, String newVal) {

        if(newVal.length() == 0) return;    /* 아무것도 입력된 것이 없을 경우, 메소드 종료 */

		char[] charArray = newVal.toCharArray();
		if (charArray.length > 13) {
			// 13자리 넘지 못하게 함.
			textField.setText(oldVal);
		}
        for (char i : charArray) {
            if (!('0' <= i && i <= '9')) {
                // 정수 아닌 값이 입력되었을 경우, 전의 값으로 되돌아 감.
                textField.setText(oldVal);
            }
        }
    }
}
