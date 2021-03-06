package project3.form;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.Base64;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.LoginException;

import project3.form.Enum.BankType;

/**
 * 서버와 통신하기 위한 클래스이다. <strong>싱글톤 패턴</strong>으로 디자인되어 있으므로, login메소드를 먼저 호출해야 인스턴스를 얻을 수 있다.
 * login이후에는 getInstance() 메소드를 통해 인스턴스를 리턴받을 수 있다.
 * 
 * @author 정의철
 */
public class TransactionDAO {
    /**
     * 자기자신을 담고있는 static변수
     */
    private static TransactionDAO instance = null;
    /**
     * 서버와의 통신을 담당하는 클래스
     */
    private ClientSocket socket = null;
    /**
     * 계좌 선택에서 선택한 계좌번호
     */
    private String selectedAccount = null;
    /**
     * 고객의 거래 은행 종류
     */
    private BankType selectedBankType = null;
    /**
     * 고객이 바로 직전에 진행한 거래
     */
    private Transaction previousTransaction = null;
    /**
     * 고객 ID
     */
    private String userId;

    /**
     * 싱글톤패턴의 일부. 외부에서 인스턴스 생성하는 것을 방지.
     */
    private TransactionDAO() {
    }

    /**
     * 로그인 메소드. 로그인 성공시 TransactionDAO 객체를 리턴한다.
     * 
     * @param id 고객 아이디
     * @param pw 고객 비밀번호
     * @param bankType 은행 종류
     * @return TransactionDAO 객체
     * @throws LoginException 로그인 실패할 경우
     */
    public synchronized static TransactionDAO login(String id, String pw, BankType bankType) throws LoginException {
        if (instance != null) {
            throw new IllegalStateException(
                    "You did not logged out. Must have called logout() method before calling login() method.");
        }
        // 로그인 성공시 객체 리턴
        ClientSocket loginSocket = new ClientSocket();
        if (loginSocket.login(id, pw, bankType)) {
            instance = new TransactionDAO();
            instance.socket = loginSocket;
            instance.setSelectedBankType(bankType);
            instance.userId = id;
            return instance;
        } else {
            try {
                loginSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw new LoginException("Client Login Failed");
        }
    }

    /**
     * 유저가 로그인 한 상태인지, 아닌지 리턴
     * @return 로그인된 여부
     */
    public static boolean isLoggedIn() {
        if(instance == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 로그인 된 상태의 DAO 인스턴스를 리턴한다. 로그인되어 있을 시에만 사용가능하다.
     * 
     * @return TransactionDAO instance
     */
    public static TransactionDAO getInstance() {
        if (instance == null) {
            throw new IllegalStateException("You Must be logged in before calling getInstance() method.");
        }
        return instance;
    }

    /**
     * 인스턴스를 null로 초기화 하며, 서버와의 연결을 종료한다. 로그아웃 이후에 TransactionDAO의 메소드를 호출할 경우, 예외를 발생시키니
     * 주의.
     * 
     * @throws IOException 로그아웃 실패
     */
    public static void logout() throws IOException {
        if (instance == null) {
            throw new IllegalStateException("You Must be logged in before calling logout() method.");
        }
        instance.socket.close();
        instance.socket = null;
        instance = null;
    }

    /**
     * String 데이터를 받아서 Object타입으로 역직렬화 한다.
     * @param objString 직렬화된 데이터
     * @return objectMember 역직렬화된 객체
     */
    private Object getObject(String objString) {
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
     * 계좌선택 화면에서 고객이 선택한 계좌를 set할 때 사용.
     * @param account 계좌
     */
    public void setSelectedAccount(String account) {
        this.selectedAccount = account;
    }

    /**
     * 계좌선택 화면에서 고객이 선택한 계좌를 받아온다.
     * @return selectedAccount 선택된 계좌
     */
    public String getSelectedAccount() {
        return this.selectedAccount;
    }

    /**
     * 계좌선택 화면에서 고객이 선택한 계좌를 받아온다.
     * <strong>하이픈</strong>이 붙은 상태로 리턴한다.
     * @return selectedAccount 선택된 계좌(하이픈 추가)
     */
    public String getSelectedDashedAccount() {
        StringBuffer sBuffer = new StringBuffer(getSelectedAccount());
        sBuffer.insert(4, '-');
        sBuffer.insert(8, '-');
        return sBuffer.toString();
    }

    /**
     * 로그인 창에서 고객이 선택한 은행을 set할 때 사용.
     * @param bType 은행 종류
     */
    public void setSelectedBankType(BankType bType) {
        this.selectedBankType = bType;
    }

    /**
     * 로그인 창에서 고객이 선택한 은행을 받아온다.
     * @return bankType 은행타입
     */
    public BankType getSelectedBankType() {
        return this.selectedBankType;
    }

    /**
     * 고객이 마지막으로 전송한 거래를 저장.
     */
    private void setPreviousTransaction(Transaction transaction) {
        this.previousTransaction = transaction;
    }

    /**
     * 고객이 마지막으로 전송한 거래를 받아온다.
     * @return previousTransaction 바로 직전에 진행한 거래
     */
    public Transaction getPreviousTransaction() {
        return this.previousTransaction;
    }

    /**
     * 고객의 ID를 받아온다.
     * @return userID
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * 거래를 진행하면서, 고객의 비밀번호가 맞는지 틀린지를 bool타입으로 리턴
     * @param pw 비밀번호
     * @return 비밀번호 일치 여부
     */
    public boolean checkPassword(String pw) {
        try {
            socket.send("check password");
            socket.send(pw);
            String a = socket.recv();
            return a.equals("true");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 데이터베이스로 부터 나의 거래 내역을 리턴. 만약 거래 내역이 없다면, null을 리턴한다. <br><strong>서버랑 직접적으로 통신하는 메소드이므로, 너무 많이 호출하지 말것!</strong>
     * 
     * @return 거래 내역
     * @throws ServerNotActiveException 서버 연결 강제 종료되었을 시
     */
    @SuppressWarnings ("unchecked") // Serializable type unchecked 경고 무시
    public Transaction[] getTransactionList() throws ServerNotActiveException {
        try {
            socket.send("get my transaction list");
            socket.send(getSelectedAccount());
            String oString = socket.recv();
            if(oString.equals("no such data")) {
                return null;
            }
            Object objectMember = getObject(oString);  // 직렬화
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
     * 데이터베이스로 부터 나의 계좌 목록을 받음. &#10;<strong>서버랑 직접적으로 통신하는 메소드이므로, 너무 많이 호출하지 말것!</strong>
     * 
     * @return 계좌_목록
     * @throws ServerNotActiveException 서버 연결 강제 종료
     */
    public Account[] getAccountList() throws ServerNotActiveException {
        try {
            socket.send("get my account list");
            String serialized;  // 직렬화된 String이 저장될 변수

            ArrayList<Account> accountArrayList = new ArrayList<>();
            while(!(serialized = socket.recv()).equals("-1")) {

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
     * @param index 인덱스
     * @return 계좌_객체
     * @throws ServerNotActiveException 서버 연결 강제 종료
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
     * 서버에 거래를 요청한다.
     * 
     * @param transaction 거래
     */
    public void sendTransaction(Transaction transaction) {
        try {
            setPreviousTransaction(transaction);
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
     * 발생시킨다. <strong>서버랑 직접적으로 통신하는 메소드이므로, 너무 많이 호출하지 말것!</strong>
     * 
     * @param accountNumber 계좌번호
     * @param banktype      은행종류
     * @return Account 계좌
     * @throws AccountNotFoundException 찾고자 하는 계좌가 없을 경우
     * @throws ServerNotActiveException 서버 통신 종료
     */
    public Account searchAccount(String accountNumber, BankType banktype)
            throws AccountNotFoundException, ServerNotActiveException {
        if(accountNumber.equals("ATM")) {
            // 요구하는 계좌가 ATM일 경우, 임의의 ATM 객체 리턴.
            return new Account("ATM", banktype, BigInteger.ZERO);
        }
        socket.send("search account");
        socket.send(accountNumber);
        socket.send(banktype.name());

        try {
            String serialized = socket.recv();
            if (serialized.equals("not found")) {
                // 만약 서버로 부터 not found 를 응답받을 경우, 해당하는 계좌번호가 존재하지 않는 것임. 따라서 예외를 발생시킨다.
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