package calcoola;

import java.util.*;

    // Calculation stages:
    // 0. Scans for user input
    // 1. Checks for input's validity using regular expression
    // 2. Parses the given input into Evaluable list
    // 3. Transfers the list into postfix representation
    // 4. Evaluates the new list and updates the variable table

 public class Calculator {

    private static HashMap<String,Integer> map;
     private static Parser parser;
     private static Evaluator evaluator;
     private static Scanner scanner;
     private static BNF bnf;


    static void printVars() {
        System.out.println("Current Variables:");
        StringBuilder table = new StringBuilder();
        table.append("[ ");
        for (Map.Entry<String,Integer> set : map.entrySet()) {
            table.append(set.getKey());
            table.append(" = ");
            table.append(set.getValue());
            table.append(" ; ");
        }
        int len = table.length();
        if (len > 2) { // removes last " ; "
            table.delete(len-3,len);
        }
        table.append(" ]");
        System.out.println(table);
    }

    public static void main(String[] args) {
        map = new HashMap<>();
        bnf = new BNF();
        parser = new Parser(map,bnf);
        evaluator = new Evaluator();
        scanner = new Scanner(System.in);
        String welcome = "Welcome to Calcoola!\n" +
                "A text based assignment expression calculator.\n\n"
                + "The syntax is a subset of Java numeric expressions and operators:\n"
                + "- You can use (positive/negative) numbers and variables as Operands.\n"
                + "- You can use ++, --, +, -, *, /, % and ^^ (Power of 2) as Operators.\n"
                + "Input Constraints:\n- Whitespace is required before/after ++ and -- with respect to their position (i.e X++ ++X)\n"
                + "- Whitespace is required before positive/negative (+/-) Operands.\n"
                + "At any stage, you can:\n"
                + "- Press p to print the computed variables\n"
                + "- Press r to clear the computation history\n"
                + "- Press q to exit\n\n"
                + "Please insert a series of assignment expressions:\n";
        System.out.println(welcome);
        String input;
        while (scanner.hasNextLine()) {
             input = scanner.nextLine();
             switch (input) {
                 case "":
                     break;
                 case "q":
                     System.out.println("Exiting...");
                     scanner.close();
                     System.exit(0);
                     break;
                 case "p":
                     printVars();
                     break;
                 case "r":
                     map.clear();
                     System.out.println("Computation history is now cleared");
                     break;
                 default:
                     try {
                         evaluator.eval(parser.parse(input));
                         printVars();
                     } catch (CalcException e) {
                         System.out.println(e.getMessage());
                     }
                     break;
             }
        }
    }
}
