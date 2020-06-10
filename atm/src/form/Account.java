package form;

import java.math.BigInteger;

import form.Enum.BankType;

/**
 * 계좌 정보를 담고 있는 객체이다.
 */
public class Account {
    private String accountNumber;
    private BankType bankType;
    private BigInteger balance;

    /**
     * 계좌 정보 초기화
     * @param accountNumber
     * @param customerId
     * @param bankType
     */
    public Account (String accountNumber, BankType bankType, BigInteger balance) {
        if(accountNumber.length() > 14) {
            throw new IllegalArgumentException("Check Account Number. Length Error");
        }
        this.accountNumber = accountNumber;
        this.bankType = bankType;
        this.balance = balance;
    }

    /**
     * 계좌번호 리턴
     * @return 계좌번호
     */
    public String getAccountNumber() {
        return this.accountNumber;
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
}