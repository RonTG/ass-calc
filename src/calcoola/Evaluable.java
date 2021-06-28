package calcoola;

// Evaluable types:
// Num, Op, sOp, Pow, Var
public abstract class Evaluable {

    private String type;
    private String val;

    Evaluable(String val,String type) {
        this.val=val;
        this.type=type;
    }

    String getType() {
        return this.type;
    }

    public String getVal() {
        return this.val;
    }

    void setType(String type) {
        this.type= type;
    }

    void setVal(String val) {
        this.val= val;
    }


}
