package server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

import form.Transaction;
import server.Enum.RequsetType;

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
        // TODO database연결
    }

    @Override
    public void run() {
        while(true) {
            RequsetType requsetType = client.getPhase();
            switch (requsetType) {
                case TRANSACTION:
                    processTransaction();
                    break;
            
                default:
                    break;
            }
        }

    }

    /**
     * Transaction을 처리하기 위한 메소드
     */
    private void processTransaction() {
        try {
            String serialized = client.recv();  // 직렬화된 String

            byte[] serializedMember = Base64.getDecoder().decode(serialized);   // Base64 -> 바이트 배열로 변환
            try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedMember);
                    ObjectInputStream ois = new ObjectInputStream(bais)) {

                Object objectMember = ois.readObject();
                Transaction transaction = (Transaction) objectMember;   // 역직렬화하여 저장



            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}