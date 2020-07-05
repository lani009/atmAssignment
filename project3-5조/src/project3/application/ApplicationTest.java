package project3.application;

import javax.security.auth.login.LoginException;

import project3.form.TransactionDAO;
import project3.form.Enum.BankType;

public class ApplicationTest {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        try {
            TransactionDAO dao = TransactionDAO.login("asdf", "asdf", BankType.MDCBank);
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}