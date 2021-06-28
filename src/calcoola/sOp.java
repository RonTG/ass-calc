package calcoola;

import java.util.HashMap;

 class sOp extends Var {
     private boolean order;
     private boolean pos;

    // + -> pos = true | - -> pos = false
     // pre inc/dec -> order = true | post inc/dec -> order = false
    sOp(String val, boolean order, boolean pos, HashMap<String, Integer> map,boolean neg) {
        super(val, "sOp",map,neg);
        this.order = order;
        this.pos = pos;
    }

    boolean getOrder() {
        return this.order;
    }

    void setOrder(boolean ord) {
        this.order = ord;
    }

    boolean getPos() {
        return this.pos;
    }

    void setPos(boolean pos) {
        this.pos = pos;
    }


    @Override
    int eval() {
        Integer value = this.getMap().get(this.getVal());
            int res = 0;

            if (pos && order) {
                res = ++value;
            } else if (!pos && order) {
                res = --value;
            } else if (pos) {
                res = value++;
            } else {
                res = value--;
            }
            this.getMap().put(this.getVal(), value);
            if (this.getNeg()) {
                res *= -1;
            }
            return res;

    }
}
