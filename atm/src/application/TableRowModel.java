package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;

/**
 * 거래내역을 보여줄 때에 하나의 거래 정보를 담을 수 있는 클래스
 */
public class TableRowModel {
	/**
	 * 거래 종류 (예: 출금, 입금, 계좌이체)
	 */
	@FXML
	private StringProperty kind;
	/**
	 * 상대방 은행 타입
	 */
	@FXML
	private StringProperty BankType;
	/**
	 * 거래 상대방
	 */
	@FXML
	private StringProperty subject;
	/**
	 * 거래 금액
	 */
	@FXML
	private StringProperty amount;
	/**
	 * 거래 후 잔고
	 */
	@FXML
	private StringProperty balance;
	
	/**
	 * Table에서의 Row 데이터를 담음
	 * @param kind 거래 종류
	 * @param BankType 은행 종류
	 * @param subject 누구에게
	 * @param amount 얼마를
	 * @param balance 거래 후 잔고
	 */
	public TableRowModel(String kind,String BankType,String subject,String amount,String balance) {
		
		this.kind=new SimpleStringProperty(kind);
		this.BankType=new SimpleStringProperty(BankType);
		this.subject=new SimpleStringProperty(subject);
		this.amount=new SimpleStringProperty(amount);
		this.balance=new SimpleStringProperty(balance);
		
	}

	public StringProperty getKind() {
		return kind;
	}

	public void setKind(StringProperty kind) {
		this.kind = kind;
	}
	public StringProperty getBankType() {
		return BankType;
	}

	public void setBankType(StringProperty BankType) {
		this.BankType = BankType;
	}

	public StringProperty getSubject() {
		return subject;
	}

	public void setSubject(StringProperty subject) {
		this.subject = subject;
	}

	public StringProperty getAmount() {
		return amount;
	}

	public void setAmount(StringProperty amount) {
		this.amount = amount;
	}

	public StringProperty getBalance() {
		return balance;
	}

	public void setBalance(StringProperty balance) {
		this.balance = balance;
	}
}
