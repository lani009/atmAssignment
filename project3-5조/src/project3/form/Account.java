package project3.form;

import java.io.Serializable;
import java.math.BigInteger;

import project3.form.Enum.BankType;

/**
 * <strong>계좌</strong>를 표현한 클래스이다. 계좌번호, 사용은행, 잔고를 담고 있으며, Serializable하다.
 * 
 * @author 정의철
 */
public class Account implements Serializable {
    private static final long serialVersionUID = 3835905242342972326L;  // 시리얼 버전
    private String accountNumber;
    private BankType bankType;
    private BigInteger balance;

    /**
     * 계좌 정보 초기화
     * @param accountNumber 계좌번호(하이픈 추가되지 않도록 주의)
     * @param bankType 은행 종류
     * @param balance 잔고
     */
    public Account (String accountNumber, BankType bankType, BigInteger balance) {
        if(accountNumber.length() > 14) {
            // 계좌번호는 14자리 제한. 넘는 경우 예외를 발생시킨다.
            throw new IllegalArgumentException("Check Account Number. Length Error");
        }
        this.accountNumber = accountNumber;
        this.bankType = bankType;
        this.balance = balance;
    }

    /**
     * 계좌번호 리턴
     * @return accountNumber 계좌번호
     */
    public String getAccountNumber() {
        return this.accountNumber;
    }

    /**
     * 하이픈이 추가된 계좌번호 리턴
     * @return accountNumber 하이픈 추가된 계좌번호
     */
    public String getDashedAccountNumber() {
        StringBuffer sBuffer = new StringBuffer(this.accountNumber);
        sBuffer.insert(4, '-');
        sBuffer.insert(8, '-');
        return sBuffer.toString();
    }

    /**
     * 은행 종류를 Enum타입으로 리턴
     * @return 은행_종류
     */
    public BankType getBankType() {
        return this.bankType;
    }

    /**
     * 은행의 잔고 리턴
     * @return 잔고
     */
    public BigInteger getBalance() {
        return this.balance;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", getAccountNumber(), getBankType(), getBalance());
    }
}