package calcoola;

 public class Op extends Evaluable {
     private int pred;

    public Op(char val) {
        super(String.valueOf(val),"Op");
        switch (val) {
            case '+':
                this.pred = 0;
                break;
            case '-':
                this.pred = 0;
                break;
            case '*':
                this.pred = 1;
                break;
            case '/':
                this.pred = 1;
                break;
            case '%':
                this.pred = 1;
                break;
            default:
                break;

        }
    }

    int getPred() {
        return this.pred;
    }
}
