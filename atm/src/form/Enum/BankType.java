package form.Enum;

public enum BankType {
    MDCBank("MDCBank", 1),
    KAKAOBank("KAKAOBank", 2),
    NHBank("NHBank", 3),
    AJOUBank("AJOUBank", 4);

    private String str;
    private int num;

    private BankType(String str, int num) {
        this.str = str;
        this.num = num;
    }

    public static BankType parseBank(int a) {
        for (BankType val : values()) {
            if(val.num == a) return val;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.str;
    }

    public int toInt() {
        return this.num;
    }
}