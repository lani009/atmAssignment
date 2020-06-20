package application;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.LoginException;

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
	int num=0;
	TransactionDAO dao;
	public void MDCBankAction(ActionEvent e) {
		num=1;
	}
	public void KAKAOBankAction(ActionEvent e) {
		num=2;
	}
	public void NHBankAction(ActionEvent e) {
		num=3;
	}
	public void AJOUBankAction(ActionEvent e) {
		num=4;
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		MDCBank.setOnAction(e -> {
			MDCBank.setOnAction(event->MDCBankActionAction(event));
            Bank.setText("MDCBank");
        });
		KAKAOBank.setOnAction(e -> {
			KAKAOBank.setOnAction(event->KAKAOBankActionAction(event));
            Bank.setText("KAKAOBank");
        });
		NHBank.setOnAction(e -> {
			MDCBank.setOnAction(event->NHBankActionAction(event));
            Bank.setText("NHBank");
        });
		AJOUBank.setOnAction(e -> {
			MDCBank.setOnAction(event->AJOUBankActionAction(event));
            Bank.setText("AJOUBank");
        });
		send.setOnAction(e -> {
			
        	dao.getInstance();
        
            Account transactionAccount=dao.getAccount("");
			
            try {
            	if(num==1) {
            		Account opponentAccount = dao.searchAccount(Account.getText(), BankType.MDCBank);
            	}
            	else if(num==2) {
            		Account opponentAccount = dao.searchAccount(Account.getText(), BankType.KAKAOBank);
            	}
            	else if(num==3) {
            		Account opponentAccount = dao.searchAccount(Account.getText(), BankType.NHBank);
            	}
            	else if(num==4) {
            		Account opponentAccount = dao.searchAccount(Account.getText(), BankType.AJOUBank);
            	}
                 /*                                                                                      
                Transaction transaction = new Transaction(TransactionType.TRANSFER, transactionAccount, opponentAccount,
                        BigInteger.valueOf(Integer.parseInt(Amount.getText())));

                dao.sendTransaction(transaction); // 거래를 처리
                */
            } catch (AccountNotFoundException e1) {
            	message.setText("Cannot find the account");
                // 상대방의 계좌를 못 찾았을 경우임.
            	// 추가로 잔고보다 송금하려는 금액이 많을 때 예외 처리
            } 
            Parent login;
            try {
                login = FXMLLoader.load(getClass().getResource("Sendsuccess.fxml"));
                Scene scene = new Scene(login);
        
                Stage primaryStage = (Stage)backtoMainmenu.getScene().getWindow(); // 현재 윈도우 가져오기
            
                primaryStage.setScene(scene);
            } catch (IOException e1) {
                e1.printStackTrace();
			}
		});
        
			
		backtoMainmenu.setOnAction(e -> {
            Parent login;
            try {
                login = FXMLLoader.load(getClass().getResource("Mainmenu.fxml"));
                Scene scene = new Scene(login);
        
                Stage primaryStage = (Stage)backtoMainmenu.getScene().getWindow(); // 현재 윈도우 가져오기
            
                primaryStage.setScene(scene);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
		
	
		}
}

