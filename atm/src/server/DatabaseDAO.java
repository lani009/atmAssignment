package server;

/**
 * 데이터베이스와 직접적으로 통신하여 은행 업무를 처리한다.
 */
public class DatabaseDAO implements Runnable {
    private ClientConnectionSocket client;

    /**
     * client socket받아와서 역할 수행
     */
    public DatabaseDAO(ClientConnectionSocket client) {
        this.client = client;
    }

    @Override
    public void run() {
        while(true) {
            
        }

    }



}