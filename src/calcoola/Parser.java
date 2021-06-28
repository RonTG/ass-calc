package calcoola;

import java.util.*;
import java.util.regex.*;

 public class Parser {
     private Pattern pt;
     private HashMap<String,Integer> map;
     private BNF bnf;
     private Matcher m;

     private static final int PRE_INC = 0;
     private static final int PRE_DEC = 1;
     private static final int NEG = 2;
     private static final int POST_INC = 3;
     private static final int POST_DEC = 4;
     private static final int POW = 5;





     public Parser(HashMap<String,Integer> map, BNF bnf) {
        this.bnf = bnf;
        this.pt = Pattern.compile(bnf.getBNF());
        this.map = map;
        this.m = this.pt.matcher("");
    }




     void setMap(HashMap<String,Integer> map) {
          this.map = map;
     }

     void setPattern(Pattern p){
         this.pt = p;
     }

     void setMatcher(Matcher m) {
        this.m = m;
     }

     Matcher getMatcher(){
        return this.m;
     }

     void setBNF(BNF b) {
         this.bnf = b;
     }

     public HashMap<String,Integer> getMap() {
        return this.map;
     }

     Pattern getPattern(){
        return this.pt;
     }

     BNF getBNF() {
        return this.bnf;
     }

     // Returns 1 if s is a number or 0 if s is a variable
    int VarOrNum(String s) {
        try {
            Integer.parseInt(s);
            return 1;
        }
         catch (NumberFormatException e) {
            return 0;
        }
    }

    boolean varExists(String prev) throws CalcException {
            return (this.map.get(prev) != null);

    }

    void buildOperand(StringBuilder prev,LinkedList<Evaluable> result, boolean[] flags) throws CalcException {
        String s_prev = prev.toString();
        int type = VarOrNum(s_prev);
        boolean neg = flags[NEG];
        if (type == 1) {
            result.addLast(new Num(s_prev,neg));
            prev.delete(0,prev.length());
            flags[NEG] = false;
        }
        else {
            if (varExists(s_prev)) {

                result.addLast(new VarRef(s_prev, map,neg));
                prev.delete(0,prev.length());
                flags[NEG] = false;
            }
            else {
                throw new CalcException("Variable: '" + s_prev + "' " + "does not exists - please initialize before using it");
            }

        }
    }

    void buildOperator(char chr,LinkedList<Evaluable> result) {
        result.addLast(new Op(chr));
    }

    void buildsOp(StringBuilder prev,boolean order,boolean pos,LinkedList<Evaluable> result,boolean[] flags) throws CalcException{
        boolean neg = flags[NEG];
        String val = prev.toString();
        if (varExists(val)) {
            result.addLast(new sOp(val, order, pos, map,neg));
            prev.delete(0,prev.length());
            flags[NEG] = false;
        }
        else {
            throw new CalcException("Variable: '" + val + "' " + "does not exists - please initialize before using it");
        }

    }

     void buildPow(StringBuilder prev,LinkedList<Evaluable> result,boolean[] flags) throws CalcException{
        boolean neg = flags[NEG];
        String val = prev.toString();
         if (varExists(val)) {
             result.addLast(new Pow(val, map,neg));
             prev.delete(0,prev.length());
             flags[NEG] = false;
         }
         else {
             throw new CalcException("Variable: '" + val + "' " + "does not exists - please initialize before using it");
         }
     }

     // Counts the amount of minuses encountered so far
     void handleMinus(boolean[] flags) {
        if (flags[NEG]) {
            flags[NEG] = false;
        }
        else {
            flags[NEG] = true;
        }
     }

    // Check flags for special operators
     // Building an operand if needed and returns true, or false when not needed
    boolean checkFlags(StringBuilder prev, LinkedList<Evaluable> result, boolean[] flags) throws CalcException {

        if (flags[PRE_INC]) { // Builds previous ++X
            buildsOp(prev,true,true,result,flags);
            flags[PRE_INC] = false;
            return true;
        } else if (flags[PRE_DEC]) { // Builds previous --X
            buildsOp(prev,true,false,result,flags);
            flags[PRE_DEC] = false;
            return true;
        }
        else if (flags[POST_INC]) { // Builds previous X++
            buildsOp(prev,false,true,result,flags);
            flags[POST_INC] = false;
            return true;
        }
        else if(flags[POST_DEC]) { // Builds previous X--
            buildsOp(prev,false,false,result,flags);
            flags[POST_DEC] = false;
            return true;
        }
        else if (flags[POW]) { // Builds pow
            buildPow(prev,result,flags);
            flags[POW] = false;
            return true;
        }
        return false;
    }



     void handleOp(StringBuilder prev,char chr,boolean[] flags,LinkedList<Evaluable> result) throws CalcException {

        if (prev.length() != 0 ) { // Op case
            if (!checkFlags(prev,result,flags)) {
                buildOperand(prev,result,flags);
            }
            buildOperator(chr,result);
        }
        else {
            if (chr == '-') { // Negative case
                handleMinus(flags);
            }
        }
    }

         void handleSOP(StringBuilder prev,boolean pos,boolean[] flags) throws CalcException {

        if (prev.length() != 0) {
            if (pos) {
                flags[POST_INC] = true;
            }
            else {
                flags[POST_DEC] = true;
            }
        }
        else {
            if (pos) {
                flags[PRE_INC] = true;
            }
            else {
                flags[PRE_DEC] = true;
            }
        }
    }

    void handlePow(boolean[] flags) {
        flags[POW] = true;
    }

     LinkedList<Evaluable> parseExp(String left,char[] exp) throws CalcException {
         Evaluable varDecl = new VarDecl(left, map, false);
         LinkedList<Evaluable> result = new LinkedList<>();
         boolean[] flags = new boolean[6];
         for (int i = 0; i < flags.length; i++) {
             flags[i] = false;
         }
         char chr;
         // With prev we collect the operand to build, and restart it after building
         StringBuilder prev = new StringBuilder();
         for (int i = 0; i < exp.length; i++) {
             chr = exp[i];
             if (chr != ' ') {// Skips whitespaces
                 // When encountering an operator we need to build the previous operand
                 if (chr == '+' || chr == '-' || chr == '*' || chr == '/' || chr == '%' || chr == '^') {
                     switch (chr) {
                         case '^':
                             if (i < exp.length - 1 && exp[i + 1] == '^') {
                                 i++;
                                 handlePow(flags);
                             }
                             break;
                         case '+':
                             if (i < exp.length - 1 && exp[i + 1] == '+') {
                                 i++;
                                 handleSOP(prev, true, flags);
                             } else {
                                 handleOp(prev, chr, flags, result);
                             }
                             break;
                         case '-':
                             if (i < exp.length - 1 && exp[i + 1] == '-') {
                                 i++;
                                 handleSOP(prev, false, flags);
                             } else {
                                 handleOp(prev, chr, flags, result);
                             }
                             break;
                         default:
                             handleOp(prev,chr,flags,result);
                             break;
                     }
                 } else { // Still collecting operand
                     prev.append(chr);
                 }
             }

         }
         if (prev.length() != 0) { // No operator found at the end and we still have an operand to build
             if (!checkFlags(prev, result, flags)) {
                 buildOperand(prev, result, flags);
             }
         }
         result.addFirst(varDecl); // Fixes the list as required for evaluation

         return result;
     }




     // Splits term by assignment operator
     // Sets both parts inside res array
     void splitNormalizeTerm(String input, String[] res) {
         int equ_index = input.indexOf("=");
         String right = input.substring(equ_index+1);
         String left;
         // Normalize +=/-=
         char char_at = input.charAt(equ_index-1);
         if (char_at == '+' || char_at == '-') {
             left = input.substring(0,equ_index-1).trim();
             right = left + " " + char_at + " " + right;
         }
         else {
             left = input.substring(0,equ_index).trim();
         }
         res[0] = left;
         res[1] = right;
     }

     // Checks for validity by BNF pattern
     boolean isValid(String input) {
         // Fix input to support ++/-- with no space required at the end of an input (for the validity check)
         String fixed = input + " ";
         m.reset(fixed);
         return m.matches();
     }

     // return a list of infix order Evaluables
    public LinkedList<Evaluable> parse(String input) throws CalcException {
        // Validity Check
        if (isValid(input)) {

            String[] termSplited = new String[2];
            splitNormalizeTerm(input,termSplited);

            String left = termSplited[0];
            char[] right = termSplited[1].toCharArray();

            return parseExp(left,right);
        }
        else {
            throw new CalcException("Input is not valid");
        }

    }
}
