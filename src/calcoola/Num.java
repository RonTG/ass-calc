package calcoola;

 public class Num extends Opr {

    public Num(String val,boolean neg) {
        super(val,"Num",neg);
    }

    @Override
    int eval() {
        int ans = Integer.parseInt(this.getVal());
        if (this.getNeg()) {
            ans *= -1;
        }
        return ans;
    }
}
