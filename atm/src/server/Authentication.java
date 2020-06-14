package server;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Authentication {
    private String id;
    private String pw;
    private String address;
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    private Authentication() {
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

            String jdbcDriver = "jdbc:mariadb://" + address + ":3306/MDCBank?"
                    + "useUnicode=true&characterEncoding=utf8";

            // 커넥션 생성
            conn = DriverManager.getConnection(jdbcDriver, id, pw);

            // Statement 생성
            stmt = conn.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 유저 로그인 로그인 성공일 경우 true를 리턴한다.
     * 
     * @param userId
     * @param userPw
     * @return
     */
    public boolean login(String userId, String userPw, String bankType) {
        try {
            // 쿼리 실행
            rs = stmt.executeQuery(String.format(
                    "SELECT id, password FROM customer WHERE id=\"%s\" AND password=password(\"%s\")", userId, userPw));
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
     * @return
     */
    public static Authentication getInstance() {
        return LazyHolder.INSTANCE;
    }

}