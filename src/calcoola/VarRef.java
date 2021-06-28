package calcoola;

import java.util.HashMap;

 class VarRef extends Var {
    VarRef(String val, HashMap<String,Integer> map,boolean neg) {
        super(val,"VarRef",map,neg);
    }

    @Override
    int eval() {
        int ans = this.getMap().get(this.getVal());
        if (this.getNeg()) {
            ans *= -1;
        }
        return ans;
    }
}
