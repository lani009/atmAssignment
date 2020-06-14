package server;

public class Test {
    public static void main(String[] args) {
        Authentication a = Authentication.getInstance();
        System.out.println(a.login("asdf", "asdf", "MDCBank"));
    }
}