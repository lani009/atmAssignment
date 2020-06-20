package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import form.Enum.BankType;

/**
 * 계좌 생성하기 위한 테스트 클래스.
 */
public class CreateAccount {
    static String address = "lanihome.iptime.org";
    static Connection[] conn = new Connection[BankType.values().length]; // 은행의 개수만큼 Connection공간 할당

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        Class.forName("org.mariadb.jdbc.Driver");
        System.out.println("은행 계정 생성 프로그램");
        System.out.println("사용방법\n\"아이디 비밀번호 은행타입\"을 입력합니다");
        System.out.println("은행타입\n1. MDCBank\n2. KAKAOBank\n3. NHBank\n4. AJOUBank");
        System.out.println("예: lani 1234 3\n위의 글을 입력하면 id: lani, pw: 1234, 은행타입: NHBank로 입력됩니다.");
        PreparedStatement pstmt;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            String str = br.readLine();
            String input[] = str.split(" ");
            pstmt = getConnection(BankType.parseBank(Integer.parseInt(input[2]))).prepareStatement("INSERT INTO customer VALUES(?, PASSWORD(?))");
            pstmt.setString(1, input[0]);
            pstmt.setString(2, input[1]);
            pstmt.executeUpdate();
            pstmt.close();

            pstmt = getConnection(BankType.valueOf(input[2])).prepareStatement("INSERT INTO accounts VALUES(?, ?, ?)");
            pstmt.setString(1, input[0]);
            pstmt.setString(2, getRandAccountNumber());
            pstmt.setBigDecimal(3, BigDecimal.ZERO);
        }

    }

    static String getRandAccountNumber() {
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        sb.append(rand.nextInt(9)+1);
        sb.append(rand.nextInt(10));
        sb.append(rand.nextInt(10));
        sb.append(rand.nextInt(10));
        sb.append("-");
        sb.append(rand.nextInt(10));
        sb.append(rand.nextInt(10));
        sb.append(rand.nextInt(10));
        sb.append("-");
        sb.append(rand.nextInt(10));
        sb.append(rand.nextInt(10));
        sb.append(rand.nextInt(10));
        sb.append(rand.nextInt(10));
        return sb.toString();
    }

    private static String getSqlAddress(BankType bankType) {
        return String.format("jdbc:mariadb://%s:3306/%s?"
        + "useUnicode=true&characterEncoding=utf8", address, bankType.toString());
    }

    private synchronized static Connection getConnection(BankType bankType) throws SQLException {
        if (conn[bankType.toInt() - 1] == null) {
            // 만약 해당 은행에 대한 Connection이 열려있지 않다면, Connnection을 생성해준다.
            conn[bankType.toInt() - 1] = DriverManager.getConnection(getSqlAddress(bankType), "lani", "1234");
            return conn[bankType.toInt() - 1];
        } else {
            return conn[bankType.toInt() - 1];
        }
    }
}