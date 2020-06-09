package form;

import java.io.Serializable;
import java.math.BigInteger;

import form.Enum.TransactionType;

public class Transaction implements Serializable {
    /**
     * 시리얼 버전
     */
    private static final long serialVersionUID = -5900619565822534171L;
    private TransactionType transactionType;
    private Account from, to;
    private BigInteger amount;
    

    public Transaction(TransactionType transactionType, Account from, Account to, BigInteger amount) {
        this.transactionType = transactionType;
        this.from = from;
        this.to = to;
        if(amount.compareTo(BigInteger.valueOf(0)) == -1) {
            throw new IllegalArgumentException("Amount must be bigger than 0. Check Amount arg");
        }
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public Account getFrom() {
        return this.from;
    }

    public Account getTo() {
        return this.to;
    }

    public BigInteger getAmount() {
        return this.amount;
    }
    
    
}