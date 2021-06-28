package calcoola;

import java.util.HashMap;

abstract class Var extends Opr {

    private HashMap<String,Integer> map;

    Var(String val, String type, HashMap<String,Integer> map,boolean neg)
    {
        super(val,type,neg);
        this.map = map;
    }

    HashMap<String,Integer> getMap() {
        return this.map;
    }

    abstract int eval();
}
