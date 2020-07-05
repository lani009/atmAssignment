package form.Enum;

/**
 * 은행의 목록
 * @author 정의철
 *
 */
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

    /**
     * int를 BankType로 파싱
     * @param n 순번
     * @return val 은행 종류
     */
    public static BankType parseBank(int n) {
        for (BankType val : values()) {
            if(val.num == n) return val;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.str;
    }

    /**
     * BankType를 int로 파싱
     * @return num 은행 순번
     */
    public int toInt() {
        return this.num;
    }
}