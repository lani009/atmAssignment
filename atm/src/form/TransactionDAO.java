package form;

import java.io.IOException;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.LoginException;

import form.Enum.BankType;

/**
 * 서버와 통신하기 위한 클래스이다. 싱글톤 패턴으로 디자인되어 있으므로, login메소드를 먼저 호출해야 인스턴스를 얻을 수 있다.
 */
public class TransactionDAO {
    private static TransactionDAO instance = null;
    private static ClientSocket socket = null;

    private TransactionDAO() {
    }

    /**
     * 로그인 메소드 로그인 성공시 객체를 리턴한다.
     * 
     * @param id
     * @param pw
     * @param 은행종류
     * @return
     * @throws LoginException
     */
    public synchronized static TransactionDAO login(String id, String pw, BankType bankType) throws LoginException {
        if (instance != null) {
            throw new IllegalStateException(
                    "You did not logged out. Must have called logout() method before calling login() method.");
        }
        // 로그인 성공시 객체 리턴
        ClientSocket loginSocket = new ClientSocket();
        if (loginSocket.login(id, pw, bankType)) {
            socket = loginSocket;
            return instance;
        } else {
            throw new LoginException("Client Login Failed");
        }
    }

    /**
     * DAO 인스턴스 리턴. 로그인되어 있을 시에만 사용가능하다.
     * 
     * @return TransactionDAO instance
     */
    public static TransactionDAO getInstance() {
        if (instance == null) {
            throw new IllegalStateException("You Must be loggedin before calling getInstance() method.");
        }
        return instance;
    }

    /**
     * 로그아웃. 객체를 null로 초기화함
     * @throws IOException
     */
    public static void logout() throws IOException {
        if (instance == null) {
            throw new IllegalStateException("You Must be loggedin before calling logout() method.");
        }
        instance = null;
        socket.close();
    }

    /**
     * 거래 내역을 배열 형태로 리턴
     * @return 거래_내역
     */
    public Transaction[] getTransactionList() {
        return null;
    }

    /**
     * 계좌 목록을 받음
     * @return 계좌_목록
     */
    public Account[] getAccountList() {
        return null;
    }

    /**
     * index번 째 계좌를 받아올 수 있음.
     * @param index
     * @return 계좌_객체
     */
    public Account getAccount(int index) {
        return getAccountList()[index];
    }

    /**
     * 계좌를 받아올 수 있음.
     * @param accountNumber 계좌번호
     * @return 계좌_객체
     * @throws AccountNotFoundException
     */
    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        Account[] myAccounts = getAccountList();
        for (Account account : myAccounts) {
            if(account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        throw new AccountNotFoundException("Wrong Account. Cannot find account.");
    }

    /**
     * 거래를 요청하는 메소드.
     * @param transaction
     */
    public void sendTransaction(Transaction transaction) {

    }

    /**
     * 계좌 정보를 찾아서 Account 객체를 리턴한다.
     * 계좌 정보를 찾지 못할 경우 AccountNotFoundException throw
     * @param accountNumber 계좌번호
     * @param banktype 은행종류
     * @return Account 계좌
     * @throws AccountNotFoundException
     */
    public Account searchAccount(String accountNumber, BankType banktype) throws AccountNotFoundException {
        // TODO
        throw new AccountNotFoundException(String.format("Cannot Find %s %s Account", accountNumber, banktype));
        // return null;
    }

}