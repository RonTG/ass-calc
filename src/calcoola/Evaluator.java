package calcoola;

import java.util.*;

 public class Evaluator {


    int evaluateOp(String op,int left, int right) {
        switch (op) {
            case "+":
                return left + right;
            case "-":
                return left - right;
            case "*":
                return left * right;
            case "/":
                return left / right;
            case "%":
                return left % right;
        }
        return 0;
    }

    // Evaluates postfix representation
    public int evaluateTerm(LinkedList<Evaluable> term) {
        Stack stack = new Stack<>();
        ListIterator<Evaluable> listIter = term.listIterator(0);
        int right;
        int left;
        int res = 0;
        while (listIter.hasNext()) {
            Evaluable next = listIter.next();
            String op;
            if (next.getType().equals("Op")) {
                op = next.getVal();
                right = (int)stack.pop();
                left = (int)stack.pop();
                res = evaluateOp(op,left,right);
                stack.push(res);
            }
            else { // Operand
                stack.push(((Opr)next).eval());
            }
        }
        if (!stack.isEmpty()) {
            res = (int)stack.pop();
        }
        return res;
    }

    // Treating operators with respect to their precedence
     void postFixOp(Op op, LinkedList<Evaluable> postfix, Stack<Op> stack) {
         while (!stack.isEmpty()) {
             if (stack.peek().getPred() >= op.getPred()) {
                 postfix.addLast(stack.pop());
             }
             else {
                 break;
             }
         }
         stack.push(op);
     }

    // Transforms infix into postfix representation
     public LinkedList<Evaluable> postFix(LinkedList<Evaluable> infix) {
         LinkedList<Evaluable> postfix = new LinkedList<>();
         ListIterator<Evaluable> listIter = infix.listIterator(0);
         Stack<Op> stack = new Stack<>();
         Evaluable next;
         while (listIter.hasNext()) {
             next = listIter.next();
             String type = next.getType();
             if (type.equals("Op")) {
                 postFixOp((Op)next,postfix, stack);
             }
             else { // Operand
                 postfix.addLast(next);
             }
         }
         while (!stack.isEmpty()) {
             postfix.addLast(stack.pop());
         }
         return postfix;
     }


     // Evaluates the given infix list and updates variable map
    public int eval(LinkedList<Evaluable> term) {
        VarDecl varDecl = (VarDecl)term.removeFirst();
        LinkedList<Evaluable> postfix = postFix(term);
        int evaluated = evaluateTerm(postfix);
        varDecl.updateVal(evaluated);
        return evaluated;
    }
}
