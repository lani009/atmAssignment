package application;

import java.math.BigInteger;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.LoginException;

import form.Account;
import form.Transaction;
import form.TransactionDAO;
import form.Enum.BankType;
import form.Enum.TransactionType;

public class Example {
    public static void main(String[] args) {
        String id = "asdf";
        String pw = "asdf";

        TransactionDAO dao;
        try {
            dao = TransactionDAO.login(id, pw); // 로그인하고 인스턴스 받아올 수 있음.
            
            Account[] myAccounts = dao.getAccountList();    // 나의 계좌 목록
            Account transactionAccount = myAccounts[2]; // 내 계좌중 2번째 계좌
            // 또는
            transactionAccount = dao.getAccount("111-1111-111");    // 내 계좌중 111-1111-111 계좌번호를 가진 계좌 객체를 받아올 수 있음

            try {
                Account opponentAccount = dao.searchAccount("222-111-2222", BankType.MDCBank);  // 222-111-2222 계좌번호를 가지고 MDCBank 은행의 계좌를 받을 수 있음.
                                                                                                // 상대방의 계좌를 찾을 때 쓰는 메소드임.
                
                // 111-1111-111이 222-111-2222에게 1000000을 송금
                Transaction transaction = new Transaction(TransactionType.TRANSFER, transactionAccount, opponentAccount, BigInteger.valueOf(1000000));
                
            } catch (AccountNotFoundException e) {
                // 상대방의 계좌를 못 찾았을 경우임.
            }
            

        } catch (LoginException e) {
            // 로그인을 실패한 경우임.
    }
}