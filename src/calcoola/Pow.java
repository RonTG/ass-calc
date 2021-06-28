package calcoola;

import java.util.HashMap;

public class Pow extends Var{
    public Pow(String val,  HashMap<String, Integer> map, boolean neg) {
        super(val,"Pow",map,neg);
    }

    @Override
    int eval() {
        int ans = this.getMap().get(this.getVal());
        ans = ans * ans;
        if (this.getNeg()) {
            ans *= -1;
        }
        return ans;
    }
}
