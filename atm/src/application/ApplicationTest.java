package application;

import javax.security.auth.login.LoginException;

import form.TransactionDAO;
import form.Enum.BankType;

public class ApplicationTest {
    public static void main(String[] args) {
        try {
            TransactionDAO dao = TransactionDAO.login("asdf", "asdf", BankType.MDCBank);
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}