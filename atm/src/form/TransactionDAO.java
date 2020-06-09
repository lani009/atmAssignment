package form;

public class TransactionDAO {
    private static TransactionDAO instance = null;

    private TransactionDAO() { }

    public synchronized static TransactionDAO login() {
        if(instance != null) {
            throw new IllegalStateException("You did not logged out. Must have called logout() method before calling login() method.");
        }
        // TODO 로그인 로직
        instance = new TransactionDAO();
        return instance;
    }

    public static TransactionDAO getInstance() {
        if(instance == null) {
            throw new IllegalStateException("You Must login before calling getInstance() method.");
        }
        return instance;
    }

    public static void logout() {
        instance = null;
        // TODO 소켓 연결 초기화
    }

    public Transaction[] getTransactionList() {
        return null;
    }

    public Account[] getAccountList() {
        return null;
    }

    public void sendTransaction() {

    }

    
    
}