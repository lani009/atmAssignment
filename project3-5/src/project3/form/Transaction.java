package project3.form;

import java.io.Serializable;
import java.math.BigInteger;

import project3.form.Enum.TransactionType;

/**
 * <strong>거래</strong>를 표현한 클래스
 * @author 정의철
 *
 */
public class Transaction implements Serializable {
    /**
     * 시리얼 버전
     */
    private static final long serialVersionUID = -5900619565822534171L;
    private TransactionType transactionType;
    private Account from, to;
    private BigInteger amount;
    

    /**
     * 거래 객체를 생성
     * @param transactionType 거래 종류
     * @param from 누가
     * @param to 누구에게
     * @param amount 얼마나
     */
    public Transaction(TransactionType transactionType, Account from, Account to, BigInteger amount) {
        this.transactionType = transactionType;
        this.from = from;
        this.to = to;

        this.amount = amount;
    }

    /**
     * 거래 종류 리턴
     * @return 거래 종류
     */
    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    /**
     * 누가 보내는지 리턴
     * @return 누가 보내는지
     */
    public Account getFrom() {
        return this.from;
    }

    /**
     * 누구에게 보내는지 리턴
     * @return 누구에게 보내는지
     */
    public Account getTo() {
        return this.to;
    }

    /**
     * 얼마나 보내는지 리턴
     * @return 금액
     */
    public BigInteger getAmount() {
        return this.amount;
    }
    
    @Override
    public String toString() {
    	return String.format("Transaction 객체\nfrom: %s\nto: %s\namount: %s", getFrom(), getTo(), getAmount());
    }
}