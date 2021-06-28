package calcoola;

 public class BNF {
    //    BNF =:
    //<trm> =:: <var> <equ> <exp>
    //<equ> =:: [<pre>] '='
    //<exp> =:: <opr> (<op> <opr>)*
    //<opr> =:: [<s_pre>] <par> | <s_opr>
    //<s_opr> =:: (<s_pre>)* <space> <s_op> <var> | (<s_pre>)* <var> <s_op> <space>
    //<par> =:: <num> | <var>
    //<op> =:: '+' | '-' | '/' | '*' | '%' | '^^'
    //<s_op> =:: '++' | '--'
    //<pre> =:: '-' | '+'
    //<s_pre> =:: <space> ['-' | '+']
    //<num> =:: <dig>+
    //<dig> =:: '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' | '0'
    //<var> =:: <chr> [<chr> | <dig>]*
    //<chr> =:: 'a' | 'b' | 'c' | 'd' | 'e' | 'f' | 'g' | 'h' | 'i' | 'j' | 'k' | 'l' | 'm' | 'n' | 'o' | 'p' | 'q' | 'r' | 's' | 't' | 'u' | 'v' | 'w' | 'x' | 'y' | 'z' |
    //            'A' | 'B' | 'C' | 'D' | 'E' | 'F' | 'G' | 'H' | 'I' | 'J' | 'K' | 'L' | 'M' | 'N' | 'O' | 'P' | 'Q' | 'R' | 'S' | 'T' | 'U' | 'V' | 'W' | 'X' | 'Y' | 'Z'


     private String spc = "[\\s]*";
     private String req_spc = "[\\s]+";
     private String num = "[\\d]+";
     private String pre = "[-+]?";
     private String s_pre = "(" + req_spc + "[-+]?" + ")*";
     private String var = "[a-zA-z][\\w]*";
     private String par = "(" + num + "|" + var + ")";
     private String equ = pre + "=";
     private String s_op = "(\\+\\+|--)";
     private String s_opr =  "(" + s_pre + req_spc + s_op + spc + var + "|" + s_pre + spc + var + spc + s_op + req_spc + ")";
     private String pow_opr = s_pre + spc + par + spc + "\\^\\^";
     private String opr = "(" + s_pre + spc + par + "|" + s_opr + "|"+  pow_opr + ")";
     private String op = "[-+/*%]";
     private String exp = opr + spc + "(" + op + spc + opr + spc + ")*";
    private String trm = spc + var + spc + equ + spc + exp + spc;

    public BNF() {
        // default, using above trm
    }
    BNF(String trm) {
        this.trm = trm;
    }
    String getBNF() {
        return this.trm;
    }

    void setBNF(String trm) {
        this.trm = trm;
    }

}
