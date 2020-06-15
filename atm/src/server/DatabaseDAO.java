package server;

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import form.Transaction;
import form.Enum.BankType;
import server.Enum.RequsetType;

/**
 * 데이터베이스와 직접적으로 통신하여 은행 업무를 처리한다.
 */
public class DatabaseDAO implements Runnable {
    private ClientConnectionSocket client;
    private String id, pw, address;
    private Connection[] conn = new Connection[BankType.values().length];   // 은행의 개수만큼 Connection공간 할당


    /**
     * client socket받아와서 역할 수행
     */
    public DatabaseDAO(ClientConnectionSocket client) {
        JSONParser parser = new JSONParser();

        // 데이터베이스에 대한 정보를 파싱해 온다.
        try (FileReader reader = new FileReader("./atm/src/server/DBProperties.json")) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            this.id = (String) jsonObject.get("id");
            this.pw = (String) jsonObject.get("pw");
            this.address = (String) jsonObject.get("address");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        

        try {
            // mariadb JDBC controller 동적으로 불러옴.
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        this.client = client;
    }

    @Override
    public void run() {
        while(true) {
            RequsetType requsetType = client.getPhase();
            switch (requsetType) {
                case TRANSACTION:
                    processTransaction();
                    break;

                case GETMYACCOUNTLIST:
                    sendAccountList();
            
                default:
                    break;
            }
        }

    }

    /**
     * 고객에게 계좌 목록을 전송한다..
     */
    private void sendAccountList() {
        try {
            Connection conn = getConnection(client.getUserBankType());
            PreparedStatement pstmt = conn.prepareStatement(
                "SELECT accountNumber, balance FROM accounts WHERE id = '?'"
            );
            pstmt.setString(1, client.getUserId());
            ResultSet rs = pstmt.executeQuery();
            // TODO Transaction 배열 형태로 리턴
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 은행 별 DBMS 접속 주소를 반환한다.
     */
    private String getSqlAddress(BankType bankType) {
        return String.format("jdbc:mariadb://%s:3306/%s?"
        + "useUnicode=true&characterEncoding=utf8", address, bankType.toString());
    }

    /**
     * 은행별로 알맞은 Connection을 반환한다.
     * 
     * @throws SQLException
     */
    private Connection getConnection(BankType bankType) throws SQLException {
        if(conn[bankType.toInt() - 1] == null) {
            // 만약 해당 은행에 대한 Connection이 열려있지 않다면, Connnection을 생성해준다.
            conn[bankType.toInt() - 1] = DriverManager.getConnection(getSqlAddress(bankType), id, pw);
            return conn[bankType.toInt() - 1];
        } else {
            return conn[bankType.toInt() - 1];
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

                BigInteger amount = transaction.getAmount();
                PreparedStatement pstmtTo, pstmtFrom;
                Connection fromConnection, toConnection;
                switch (transaction.getTransactionType()) {
                    case DEPOSIT:
                    // 입금하는 경우이다. ATM -> 은행계좌
                        toConnection = getConnection(transaction.getTo().getBankType());
// TODO 계좌거래 내역에 추가하는 DB코드
                        // 고객의 계좌 잔고를 업데이트 하는 Statement를 준비
                        pstmtTo = toConnection.prepareStatement(
                            "UPDATE accounts SET balance = balance + ? WHERE accountNumber = '?'"
                        );
                        pstmtTo.setString(1, amount.toString());
                        pstmtTo.setString(2, transaction.getTo().getAccountNumber());

                        // 쿼리 실행
                        pstmtTo.executeUpdate();
                        break;
                
                    case WITHDRAWL:
                    // 출금하는 경우이다. 은행계좌 -> ATM
                        fromConnection = getConnection(transaction.getFrom().getBankType());

                        // 고객의 계좌 잔고를 업데이트 하는 Statement를 준비
                        pstmtFrom = fromConnection.prepareStatement(
                            "UPDATE accounts SET balance = balance - ? WHERE accountNumber = '?'"
                        );
                        pstmtFrom.setString(1, amount.toString());
                        pstmtFrom.setString(2, transaction.getFrom().getAccountNumber());

                        // 쿼리 실행
                        pstmtFrom.executeUpdate();
                        break;

                    case TRANSFER:
                    // 계좌이체를 하는 상황 FROM -> TO
                        fromConnection = getConnection(transaction.getFrom().getBankType());
                        toConnection = getConnection(transaction.getTo().getBankType());

                        // 고객의 계좌 잔고를 업데이트 하는 Statement를 준비
                        pstmtFrom = fromConnection.prepareStatement(
                            "UPDATE accounts SET balance = balance - ? WHERE accountNumber = '?'"
                        );
                        pstmtFrom.setString(1, amount.toString());
                        pstmtFrom.setString(2, transaction.getFrom().getAccountNumber());
                        pstmtFrom.executeUpdate();  // 쿼리 실행

                        pstmtTo = toConnection.prepareStatement(
                            "UPDATE accounts SET balance = balance + ? WHERE accountNumber = '?'"
                        );
                        pstmtTo.setString(1, amount.toString());
                        pstmtTo.setString(2, transaction.getTo().getAccountNumber());
                        pstmtTo.executeUpdate();    // 쿼리 실행

                        break;
                }
                
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}