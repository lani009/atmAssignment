package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;

public class TableRowModel {
	@FXML
	private StringProperty num;
	@FXML
	private StringProperty kind;
	@FXML
	private StringProperty subject;
	@FXML
	private StringProperty amount;
	@FXML
	private StringProperty balance;
	
	public TableRowModel(String num,String kind,String subject,String amount,String balance) {
		this.num=new SimpleStringProperty(num);
		this.kind=new SimpleStringProperty(kind);
		this.subject=new SimpleStringProperty(subject);
		this.amount=new SimpleStringProperty(amount);
		this.balance=new SimpleStringProperty(balance);
		
	}
	public StringProperty getNum() {
		return num;
	}

	public void setNum(StringProperty num) {
		this.num = num;
	}

	public StringProperty getKind() {
		return kind;
	}

	public void setKind(StringProperty kind) {
		this.kind = kind;
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
