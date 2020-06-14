package form.Enum;

public enum BankType {
    MDCBank("MDCBank"),
    KAKAOBank("KAKAOBank"),
    NHBank("NHBank");

    private String str;

    private BankType(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}