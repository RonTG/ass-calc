package calcoola;

abstract class Opr extends Evaluable {
    private boolean neg;

    Opr(String val,String type, boolean neg) {
        super(val,type);
        this.neg = neg;
    }

    abstract int eval();

    boolean getNeg() {
        return this.neg;
    }

    void setNeg(boolean neg) {
        this.neg = neg;
    }
}
