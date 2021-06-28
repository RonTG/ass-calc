package calcoola;

import java.util.HashMap;

 public class VarDecl extends Var {

    public VarDecl(String val, HashMap<String, Integer> map,boolean neg) {
        super(val,"VarDecl",map,false);
    }

    @Override
    int eval() {
        return this.getMap().get(this.getVal());
    }

    void updateVal(int newVal) {
        this.getMap().put(this.getVal(),newVal);
    }
}
