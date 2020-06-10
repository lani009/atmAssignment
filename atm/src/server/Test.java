package server;

public class Test {
    public static void main(String[] args) {
        Authentication a = Authentication.getInstance();
        a.login("asdf", "asdf");
    }
}