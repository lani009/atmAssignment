package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import form.Account;
import form.Transaction;
import form.Enum.BankType;
import form.Enum.TransactionType;
import server.Enum.RequsetType;

/**
 * 데이터베이스와 직접적으로 통신하여 클라이언트의 요청을 수행한다.
 */
public class DatabaseDAO implements Runnable, Closeable {
    private ClientConnectionSocket client;
    private String id, pw, address;
    private Connection[] conn = new Connection[BankType.values().length]; // 은행의 개수만큼 Connection공간 할당

    /**
     * client socket 받아와서 필드 변수 초기화. DBProperties.json을 파싱하여 데이터베이스의 주소와 id, pw를
     * 가져온다.
     */
    public DatabaseDAO(ClientConnectionSocket client) {
        JSONParser parser = new JSONParser();

        // 데이터베이스에 대한 정보를 파싱해 온다.
        try (FileReader reader = new FileReader("./DBProperties.json")) {
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
        while (true) {
            RequsetType requsetType = client.getPhase();
            switch (requsetType) {
                case TRANSACTION:
                    System.out.println("Transaction requst");
                    processTransaction();
                    break;

                case GETMYACCOUNTLIST:
                    System.out.println("Get my Account List requst");
                    sendAccountList();
                    break;

                case GETMYTRANSACTIONLIST:
                    System.out.println("Get my Transaction List requst");
                    getMyTransactionList();
                    break;

                case SEARCHACCOUNT:
                    System.out.println("Search Account requst");
                    searchAccount();
                    break;

                case CHECKPASSWORD:
                    System.out.println("Check Password requst");
                    checkPassword();
                    break;

                case DISCONNECT:
                    try {
                        close();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
            }
        }

    }

    /**
     * 은행 별 DBMS 접속 주소를 반환한다.
     * 
     * @return database_dbms_address
     */
    private String getSqlAddress(BankType bankType) {
        return String.format("jdbc:mariadb://%s:3306/%s?useUnicode=true&characterEncoding=utf8", address,
                bankType.toString());
    }

    /**
     * 은행별로 알맞은 Database Connection을 반환한다.
     * 
     * @throws SQLException
     */
    private synchronized Connection getConnection(BankType bankType) throws SQLException {
        if (conn[bankType.toInt() - 1] == null) {
            // 만약 해당 은행에 대한 Connection이 열려있지 않다면, Connnection을 생성해준다.
            conn[bankType.toInt() - 1] = DriverManager.getConnection(getSqlAddress(bankType), id, pw);
            return conn[bankType.toInt() - 1];
        } else {
            return conn[bankType.toInt() - 1];
        }
    }

    /**
     * 계좌이체, 입금, 출금시 비밀번호가 맞는지 틀린지 검증
     */
    private void checkPassword() {
        try {
            Connection conn = getConnection(client.getUserBankType());
            try (PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT * FROM accounts WHERE id=? AND password=PASSWORD(?)")) {
                pstmt.setString(1, client.getUserId());
                pstmt.setString(2, client.recv());
                ResultSet rs = pstmt.executeQuery();
                rs.last();

                if(rs.getRow() == 0) {
                    client.send("false");
                } else {
                    client.send("true");
                }
                rs.close();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 계좌번호가 존재하는지 찾기 위한 메소드
     */
    private void searchAccount() {
        try {
            String account = client.recv();
            BankType bankType = BankType.valueOf(client.recv());

            Connection conn = getConnection(bankType);
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM accounts WHERE accountNumber = ?");
            pstmt.setString(1, account);
            ResultSet rs = pstmt.executeQuery();
            rs.last();
            if (rs.getRow() == 0) {
                // 일치하는 계좌가 없을 경우
                client.send("not found");
                rs.close();
                pstmt.close();
                return;
            }
            rs.first();
            Account accountObject = new Account(account, bankType, BigInteger.ZERO);

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(accountObject);
                // serialized -> 직렬화된 account 객체
                byte[] serialized = baos.toByteArray();
                client.send(Base64.getEncoder().encodeToString(serialized)); // 바이트 배열로 생성된 직렬화 데이터를 base64로 변환 후 전송
            } catch (IOException e) {
                e.printStackTrace();
            }
            rs.close();
            pstmt.close();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 고객의 거래 내역을 불러오기 위함.
     */
    private void getMyTransactionList() {
        try {
            String accountNumber = client.recv(); // 계좌번호 받아옴.
            Connection conn = getConnection(client.getUserBankType());
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM history WHERE origin=? OR destination=?");
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, accountNumber);

            ResultSet rs = pstmt.executeQuery();
            ArrayList<Transaction> list = new ArrayList<>(); // 거래 정보를 담을 리스트 선언
            rs.last();
            if (rs.getRow() == 0) {
                // transaction list가 없을 경우
                client.send("no such data");
                pstmt.close();
                rs.close();
                return;
            }
            rs.beforeFirst();

            // result set에 담긴 내용을 ArrayList에 담는다.
            while (rs.next()) {
                if (rs.getString(1).equals("ATM")) {
                    // 입금의 경우
                    list.add(new Transaction(TransactionType.DEPOSIT,
                            new Account("ATM", client.getUserBankType(), BigInteger.ZERO),
                            new Account(rs.getString(2), client.getUserBankType(), rs.getBigDecimal(6).toBigInteger()),
                            rs.getBigDecimal(5).toBigInteger()));

                } else if (rs.getString(2).equals("ATM")) {
                    // 출금의 경우
                    list.add(new Transaction(TransactionType.WITHDRAWL,
                            new Account(rs.getString(1), client.getUserBankType(), rs.getBigDecimal(6).toBigInteger()),
                            new Account("ATM", client.getUserBankType(), BigInteger.ZERO),
                            rs.getBigDecimal(5).toBigInteger()));
                } else if (rs.getString(1).equals(accountNumber)) {
                    // 계좌 이체의 경우 (나 -> 상대)
                    list.add(new Transaction(TransactionType.TRANSFER,
                            new Account(rs.getString(1), BankType.valueOf(rs.getString(3)),
                                    rs.getBigDecimal(6).toBigInteger()),
                            new Account(rs.getString(2), BankType.valueOf(rs.getString(4)), BigInteger.ZERO),
                            rs.getBigDecimal(5).toBigInteger()));

                } else {
                    // 계좌 이체의 경우 (상대 -> 나)
                    list.add(new Transaction(TransactionType.TRANSFER,
                            new Account(rs.getString(1), BankType.valueOf(rs.getString(3)), BigInteger.ZERO),
                            new Account(rs.getString(2), BankType.valueOf(rs.getString(4)),
                                    rs.getBigDecimal(6).toBigInteger()),
                            rs.getBigDecimal(5).toBigInteger()));
                }
            }
            // ArrayList 직렬화 후 클라이언트에게 전송한다.
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(list);
                // serialized -> 직렬화된 list 객체
                byte[] serialized = baos.toByteArray();
                client.send(Base64.getEncoder().encodeToString(serialized)); // 바이트 배열로 생성된 직렬화 데이터를 base64로 변환 후 전송
            } catch (IOException e) {
                e.printStackTrace();
            }
            rs.close();
            pstmt.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 고객에게 자신의 계좌 목록을 전송한다.
     */
    private void sendAccountList() {
        try {
            Connection conn = getConnection(client.getUserBankType());
            // 데이터베이스로 부터 고객의 계좌 정보만을 가져오기 위한 sql문
            PreparedStatement pstmt = conn
                    .prepareStatement("SELECT accountNumber, balance FROM accounts WHERE id=?");
            pstmt.setString(1, client.getUserId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // 쿼리 결과로 부터 계좌의 정보를 받아온다.
                String accountNumber = rs.getString("accountNumber");
                String balance = rs.getString("balance");

                // 계좌 객체를 생성한다.
                Account account = new Account(accountNumber, client.getUserBankType(),
                        BigInteger.valueOf(Long.parseLong(balance)));
                System.out.println(account);

                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                    oos.writeObject(account);
                    // serialized -> 직렬화된 account 객체
                    byte[] serialized = baos.toByteArray();
                    client.send(Base64.getEncoder().encodeToString(serialized)); // 바이트 배열로 생성된 직렬화 데이터를 base64로 변환
                    System.out.println(Base64.getEncoder().encodeToString(serialized));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            client.send("-1"); // 계좌 전송이 끝났음을 클라이언트에게 알린다.
            pstmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 클라이언트가 요청한 Transaction을 처리하기 위한 메소드
     */
    private synchronized void processTransaction() {
        try {
            String serialized = client.recv(); // 직렬화된 String

            byte[] serializedMember = Base64.getDecoder().decode(serialized); // Base64 -> 바이트 배열로 변환
            try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedMember);
                    ObjectInputStream ois = new ObjectInputStream(bais)) {

                Object objectMember = ois.readObject();
                Transaction transaction = (Transaction) objectMember; // 역직렬화하여 저장

                BigInteger amount = transaction.getAmount();
                PreparedStatement pstmtTo, pstmtFrom;
                Connection fromConnection, toConnection;
                switch (transaction.getTransactionType()) {
                    case DEPOSIT:
                        // 입금하는 경우이다. ATM -> 은행계좌
                        toConnection = getConnection(transaction.getTo().getBankType());

                        // 고객의 계좌 잔고를 업데이트 하는 Statement를 준비
                        pstmtTo = toConnection.prepareStatement(
                                "UPDATE accounts SET balance = balance + ? WHERE accountNumber = ?");
                        pstmtTo.setBigDecimal(1, new BigDecimal(amount));
                        pstmtTo.setString(2, transaction.getTo().getAccountNumber());

                        // 쿼리 실행
                        pstmtTo.executeUpdate();

                        // 거래내역에 추가
                        pstmtTo = toConnection.prepareStatement("INSERT INTO history VALUES (?, ?, ?, ?, ?, ?);");
                        pstmtTo.setString(1, transaction.getFrom().getAccountNumber());
                        pstmtTo.setString(2, transaction.getTo().getAccountNumber());
                        pstmtTo.setString(3, transaction.getFrom().getBankType().name());
                        pstmtTo.setString(4, transaction.getTo().getBankType().name());
                        pstmtTo.setBigDecimal(5, new BigDecimal(amount));
                        pstmtTo.setBigDecimal(6, new BigDecimal(transaction.getTo().getBalance().add(amount)));
                        pstmtTo.executeUpdate();

                        pstmtTo.close();
                        break;

                    case WITHDRAWL:
                        // 출금하는 경우이다. 은행계좌 -> ATM
                        fromConnection = getConnection(transaction.getFrom().getBankType());

                        // 고객의 계좌 잔고를 업데이트 하는 Statement를 준비
                        pstmtFrom = fromConnection
                                .prepareStatement("UPDATE accounts SET balance = balance - ? WHERE accountNumber = ?");
                        pstmtFrom.setBigDecimal(1, new BigDecimal(amount));
                        pstmtFrom.setString(2, transaction.getFrom().getAccountNumber());

                        // 쿼리 실행
                        pstmtFrom.executeUpdate();

                        // 거래내역에 추가
                        pstmtFrom = fromConnection.prepareStatement("INSERT INTO history VALUES (?, ?, ?, ?, ?, ?);");
                        pstmtFrom.setString(1, transaction.getFrom().getAccountNumber());
                        pstmtFrom.setString(2, transaction.getTo().getAccountNumber());
                        pstmtFrom.setString(3, transaction.getFrom().getBankType().name());
                        pstmtFrom.setString(4, transaction.getTo().getBankType().name());
                        pstmtFrom.setBigDecimal(5, new BigDecimal(amount.negate()));
                        pstmtFrom.setBigDecimal(6, new BigDecimal(transaction.getTo().getBalance().subtract(amount)));
                        pstmtFrom.executeUpdate();

                        pstmtFrom.close();
                        break;

                    case TRANSFER:
                        // 계좌이체를 하는 상황 FROM -> TO
                        fromConnection = getConnection(transaction.getFrom().getBankType());
                        toConnection = getConnection(transaction.getTo().getBankType());

                        // 고객의 계좌 잔고를 업데이트 하는 Statement를 준비
                        pstmtFrom = fromConnection
                                .prepareStatement("UPDATE accounts SET balance = balance - ? WHERE accountNumber = ?");
                        pstmtFrom.setBigDecimal(1, new BigDecimal(amount));
                        pstmtFrom.setString(2, transaction.getFrom().getAccountNumber());
                        pstmtFrom.executeUpdate(); // 쿼리 실행

                        pstmtTo = toConnection
                                .prepareStatement("UPDATE accounts SET balance = balance + ? WHERE accountNumber = ?");
                        pstmtTo.setBigDecimal(1, new BigDecimal(amount));
                        pstmtTo.setString(2, transaction.getTo().getAccountNumber());
                        pstmtTo.executeUpdate(); // 쿼리 실행

                        // 거래내역에 추가
                        pstmtFrom = fromConnection.prepareStatement("INSERT INTO history VALUES (?, ?, ?, ?, ?, ?);");
                        pstmtFrom.setString(1, transaction.getFrom().getAccountNumber());
                        pstmtFrom.setString(2, transaction.getTo().getAccountNumber());
                        pstmtFrom.setString(3, transaction.getFrom().getBankType().name());
                        pstmtFrom.setString(4, transaction.getTo().getBankType().name());
                        pstmtFrom.setBigDecimal(5, new BigDecimal(amount));
                        pstmtFrom.setBigDecimal(6, new BigDecimal(transaction.getFrom().getBalance().subtract(amount)));
                        pstmtFrom.executeUpdate();

                        // 거래내역에 추가
                        pstmtTo = toConnection.prepareStatement("INSERT INTO history VALUES (?, ?, ?, ?, ?);");
                        pstmtTo.setString(1, transaction.getFrom().getAccountNumber());
                        pstmtTo.setString(2, transaction.getTo().getAccountNumber());
                        pstmtTo.setString(3, transaction.getFrom().getBankType().name());
                        pstmtTo.setString(4, transaction.getTo().getBankType().name());
                        pstmtTo.setBigDecimal(5, new BigDecimal(amount));
                        pstmtTo.setBigDecimal(6, new BigDecimal(amount.add(transaction.getTo().getBalance())));
                        pstmtTo.executeUpdate();

                        pstmtFrom.close();
                        pstmtTo.close();

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

    @Override
    public void close() throws IOException {
        client.close();
        try {
            for (Connection connection : conn) {
                if(connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}