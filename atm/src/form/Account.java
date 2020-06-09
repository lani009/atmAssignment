package form;

import java.math.BigInteger;

import form.Enum.BankType;

/**
 * 계좌 정보를 담고 있는 객체이다.
 */
public class Account {
    private String accountNumber;
    private String customerId;
    private BankType bankType;
    // private BigInteger balance;

    /**
     * 계좌 정보 초기화
     * @param accountNumber
     * @param customerId
     * @param bankType
     */
    public Account (String accountNumber, String customerId, BankType bankType) {
        if(accountNumber.length() > 14 || customerId.length() > 14) {
            throw new IllegalArgumentException("Check Account Number, Customer Id. Length Error");
        }
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.bankType = bankType;
    }

    /**
     * 계좌번호 리턴
     * @return 계좌번호
     */
    public String getAccountNumber() {
        return this.accountNumber;
    }

    /**
     * 고객 아이디 리턴
     * @return 고객_아이디
     */
    public String getCustomerId() {
        return this.customerId;
    }

    /**
     * 은행 종류를 Enum타입으로 리턴
     * @return 은행_종류
     */
    public BankType getBankType() {
        return this.bankType;
    }
}