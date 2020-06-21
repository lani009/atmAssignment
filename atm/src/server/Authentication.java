package server;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import form.Enum.BankType;

public class Authentication {
    private String id;
    private String pw;
    private String address;
    private Connection conn[] = new Connection[BankType.values().length];

    private Authentication() {
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
    }

    private String getSqlAddress(BankType bankType) {
        return String.format("jdbc:mariadb://%s:3306/%s?"
        + "useUnicode=true&characterEncoding=utf8", this.address, bankType.toString());
    }

    private synchronized Connection getConnection(BankType bankType) throws SQLException {
        if (conn[bankType.toInt() - 1] == null) {
            // 만약 해당 은행에 대한 Connection이 열려있지 않다면, Connnection을 생성해준다.
            conn[bankType.toInt() - 1] = DriverManager.getConnection(getSqlAddress(bankType), this.id, this.pw);
            return conn[bankType.toInt() - 1];
        } else {
            return conn[bankType.toInt() - 1];
        }
    }

    /**
     * 유저 로그인 로그인 성공일 경우 true를 리턴한다.
     * 
     * @param userId
     * @param userPw
     * @param bankType
     * @return 로그인 성공 여부
     */
    public boolean login(String userId, String userPw, String bankType) {
        try {
            PreparedStatement pstmt = getConnection(BankType.valueOf(bankType)).prepareStatement(
                    "SELECT id, password FROM customer WHERE id=? AND password=PASSWORD(?)"
            );
            pstmt.setString(1, userId);
            pstmt.setString(2, userPw);

            // 쿼리 실행
            ResultSet rs = pstmt.executeQuery();
            rs.first(); // 처음 행
            if (rs.getString("id").equals(userId)) {
                return true;
            } else {
                return false;
            }
        } catch (SQLDataException e) {
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }

    private static class LazyHolder {
        // 싱글톤 패턴 구현
        public static final Authentication INSTANCE = new Authentication();
    }

    /**
     * Singleton-pattern. 인스턴스 리턴
     * 
     * @return instance
     */
    public static Authentication getInstance() {
        return LazyHolder.INSTANCE;
    }

}