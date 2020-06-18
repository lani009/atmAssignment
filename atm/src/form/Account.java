package form;

import java.io.Serializable;
import java.math.BigInteger;

import form.Enum.BankType;

/**
 * "계좌"를 표현한 클래스이다. 계좌번호, 사용은행, 잔고를 담고 있으며, Serializable하다.
 */
public class Account implements Serializable {
    private static final long serialVersionUID = 3835905242342972326L;  // 시리얼 버전
    private String accountNumber;
    private BankType bankType;
    private BigInteger balance;

    /**
     * 계좌 정보 초기화
     * @param accountNumber
     * @param bankType
     * @param balance
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