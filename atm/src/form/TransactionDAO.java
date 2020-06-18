package form;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.Base64;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.LoginException;

import form.Enum.BankType;

/**
 * 서버와 통신하기 위한 클래스이다. 싱글톤 패턴으로 디자인되어 있으므로, login메소드를 먼저 호출해야 인스턴스를 얻을 수 있다.
 * login이후에는 getInstance() 메소드를 통해 인스턴스를 리턴받을 수 있다.
 */
public class TransactionDAO {
    /**
     * 자기자신을 담고있는 static변수
     */
    private static TransactionDAO instance = null;
    /**
     * 서버와의 통신을 담당하는 클래스
     */
    private static ClientSocket socket = null;

    private TransactionDAO() {
    }

    /**
     * 로그인 메소드. 로그인 성공시 객체를 리턴한다.
     * 
     * @param id
     * @param pw
     * @param 은행종류
     * @return self
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
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw new LoginException("Client Login Failed");
        }
    }

    /**
     * DAO 인스턴스를 리턴한다. 로그인되어 있을 시에만 사용가능하다.
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
     * 인스턴스를 null로 초기화 하며, 서버와의 연결을 종료한다. 로그아웃 이후에 login이외의 메소드를 호출할 경우, 예외를 발생시키니
     * 주의.
     * 
     * @throws IOException
     */
    public static void logout() throws IOException {
        if (instance == null) {
            throw new IllegalStateException("You Must be loggedin before calling logout() method.");
        }
        instance = null;
        socket.close();
        socket = null;
    }

    public Object getObject(String objString) {
        byte[] serializedMember = Base64.getDecoder().decode(objString);   // Base64 -> 바이트 배열로 변환
        try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedMember);
                ObjectInputStream ois = new ObjectInputStream(bais)) {

            Object objectMember = ois.readObject(); // 역직렬화
            return objectMember;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 데이터베이스로 부터 나의 거래 내역을 리턴. 만약 거래 내역이 없다면, null을 리턴한다. &#10;<strong>서버랑 직접적으로 통신하는 메소드이므로, 너무 많이 호출하지
     * 말것!<strong/>
     * 
     * @return 거래_내역
     * @throws ServerNotActiveException
     */
    @SuppressWarnings ("unchecked") // Serializable type unchecked 경고 무시
    public Transaction[] getTransactionList() throws ServerNotActiveException {
        try {
            socket.send("get my transaction list");

            String oString = socket.recv();
            if(oString.equals("no such data")) {
                return null;
            }
            Object objectMember = getObject(oString);  // 직렬화된 String
            ArrayList<Transaction> list = (ArrayList<Transaction>) objectMember;   
            Transaction[] transactionList = new Transaction[list.size()];
            list.toArray(transactionList);
            return transactionList; // Transaction List 리턴

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 데이터베이스로 부터 나의 계좌 목록을 받음. &#10;<strong>서버랑 직접적으로 통신하는 메소드이므로, 너무 많이 호출하지
     * 말것!<strong/>
     * 
     * @return 계좌_목록
     * @throws ServerNotActiveException
     * @throws NumberFormatException
     */
    public Account[] getAccountList() throws NumberFormatException, ServerNotActiveException {
        try {
            socket.send("get my account list");
            String serialized;  // 직렬화된 String이 저장될 변수

            ArrayList<Account> accountArrayList = new ArrayList<>();
            while((serialized = socket.recv()) != "-1") {

                Object objectMember = getObject(serialized);
                accountArrayList.add((Account) objectMember);   // Array List에 저장
            }

            Account[] accountList = accountArrayList.toArray(new Account[accountArrayList.size()]); // Account 배열 형태로 변환한다.
            return accountList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 나의 계좌중, index번 째 계좌를 받아올 수 있음.
     * 
     * @param index
     * @return 계좌_객체
     * @throws ServerNotActiveException
     */
    public Account getAccount(int index) throws ServerNotActiveException {
        return getAccountList()[index];
    }

    /**
     * 나의 계좌중 특정한 String을 가지는 계좌를 받아올 수 있음.
     * 
     * @param accountNumber 계좌번호
     * @return 계좌_객체
     * @throws AccountNotFoundException
     * @throws ServerNotActiveException
     */
    public Account getAccount(String accountNumber)
            throws AccountNotFoundException, ServerNotActiveException {
        Account[] myAccounts = getAccountList();
        for (Account account : myAccounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        throw new AccountNotFoundException("Wrong Account. Cannot find account.");
    }

    /**
     * 거래를 요청하는 메소드.
     * 
     * @param transaction
     */
    public void sendTransaction(Transaction transaction) {
        try {
            socket.send("transaction"); // transaction 요청
            byte[] serialized;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(transaction);
                serialized = baos.toByteArray(); // transaction인스턴스 직렬화
            }
            socket.send(Base64.getEncoder().encodeToString(serialized)); // Base64로 인코딩 하여 전송
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 계좌 정보를 찾아서 Account 객체를 리턴한다. 계좌 정보를 찾지 못할 경우 AccountNotFoundException 예외를
     * 발생시킨다. &#10;<strong>서버랑 직접적으로 통신하는 메소드이므로, 너무 많이 호출하지 말것!<strong/>
     * 
     * @param accountNumber 계좌번호
     * @param banktype      은행종류
     * @return Account 계좌
     * @throws AccountNotFoundException
     * @throws ServerNotActiveException
     * @throws IOException
     */
    public Account searchAccount(String accountNumber, BankType banktype)
            throws AccountNotFoundException, ServerNotActiveException {
        try {
            socket.send("search account");
            socket.send(accountNumber);
            socket.send(banktype.name());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        try {
            String serialized = socket.recv();
            if (serialized.equals("not found")) {
                // 만약 서버로 부터 not found 를 응답받을 경우, 예외를 발생시킨다.
                throw new AccountNotFoundException(
                        String.format("Cannot Find %s %s Account", accountNumber, banktype.name()));
            }

            Object objectMember = getObject(serialized);
            Account account = (Account) objectMember;   // 역직렬화
            return account;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}