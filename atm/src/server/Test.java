package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import form.Enum.BankType;

public class Test {
    static String address = "lanihome.iptime.org";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // Authentication a = Authentication.getInstance();
        // System.out.println(a.login("asdf", "asdf", "MDCBank"));
        Class.forName("org.mariadb.jdbc.Driver");
        Connection conn = DriverManager.getConnection(getSqlAddress(BankType.MDCBank), "lani", "1234");
        Statement stmt = conn.createStatement();
        ResultSet s= stmt.executeQuery("SELECT * FROM history");
        s.last();
        System.out.println(s.getRow());
    }

    private static String getSqlAddress(BankType bankType) {
        return String.format("jdbc:mariadb://%s:3306/%s?"
        + "useUnicode=true&characterEncoding=utf8", address, bankType.toString());
    }
}