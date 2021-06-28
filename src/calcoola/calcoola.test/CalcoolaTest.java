package calcoola.calcoola.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import calcoola.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;

public class CalcoolaTest {
    private HashMap<String,Integer> map;
    private BNF bnf;
    private Parser parser;
    private Evaluator evaluator;

    @BeforeEach
    public void setUp() throws Exception {
        map = new HashMap<>();
        bnf = new BNF();
        parser = new Parser(map,bnf);
        evaluator = new Evaluator();
    }

    @Test
    public void testPrecedence() {
        try {
            assertEquals(7, (evaluator.eval(parser.parse("i=1+2*3"))), "Precedence should be given into account");
        } catch (CalcException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testOrder() {
        try {
            assertEquals(-4, (evaluator.eval(parser.parse("i=1-2-3"))), "Order should be given into account");
        } catch (CalcException e) {
            System.out.println(e.getMessage());

        }
    }

    @Test
    public void testPreInc() {
        try {
            evaluator.eval(parser.parse("i=1"));
            assertEquals(2, (evaluator.eval(parser.parse("j = ++i"))), "Pre inc is not working");
        } catch (CalcException e) {
            System.out.println(e.getMessage());

        }
    }

    @Test
    public void testPostInc() {
        try {
            evaluator.eval(parser.parse("i=1"));
            assertEquals(1, (evaluator.eval(parser.parse("j = i++"))), "post inc is not working");
        } catch (CalcException e) {
            System.out.println(e.getMessage());

        }
    }

    @Test
    public void testPostDec() {
        try {
            evaluator.eval(parser.parse("i=1"));
            assertEquals(1, (evaluator.eval(parser.parse("j = i++"))), "post inc is not working");
        } catch (CalcException e) {
            System.out.println(e.getMessage());

        }
    }

    @Test
    public void testPluses() {
        try {
            evaluator.eval(parser.parse("i=1"));
            evaluator.eval(parser.parse("j=1"));
            assertEquals(3, (evaluator.eval(parser.parse("k = j++ + ++i"))), "calculator should differ between special and normal operators");
        } catch (CalcException e) {
            System.out.println(e.getMessage());

        }
    }

    @Test
    public void testMinuses() {
        try {
            assertEquals(2, (evaluator.eval(parser.parse("k = - - 2"))), "calculator should differ between special and normal operators");
        } catch (CalcException e) {
            System.out.println(e.getMessage());

        }
    }

    @Test
    public void testMix() {
        try {
            assertEquals(-2, (evaluator.eval(parser.parse("k = - - + + - 2"))), "calculator should differ between special and normal operators");
        } catch (CalcException e) {
            System.out.println(e.getMessage());

        }
    }

    @Test
    public void testPosNeg() {
        try {
            assertEquals(5, (evaluator.eval(parser.parse("k = +2 - - 3"))), "calculator should recognize positive and negative numbers");
        } catch (CalcException e) {
            System.out.println(e.getMessage());

        }
    }

    @Test
    public void testMinusPP() {
        try {
            evaluator.eval(parser.parse("i = 1"));
            assertEquals(-1, (evaluator.eval(parser.parse("j = -i++"))), "negative post inc/dec error");
        } catch (CalcException e) {
            System.out.println(e.getMessage());

        }
    }

    @Test
    public void testNormalize() {
        try {
            evaluator.eval(parser.parse(("i = 1")));

            assertEquals(3, evaluator.eval(parser.parse("i += 2")), "normalize not working");
        } catch (CalcException e) {
            System.out.println(e.getMessage());

        }
    }

    @Test
    public void testValidityCheck() {
        try {
            evaluator.eval(parser.parse("k = 2++"));
            fail ("input is not valid - calculator should only be able to increment variables");
        } catch (CalcException e) {
        }
    }




    @Test
    public void testVarExists() {
        try {
            evaluator.eval(parser.parse("k = -j"));
            fail ("calculator should not accept un previously declared variables");
        } catch (CalcException e) {
        }
    }

    @Test
    public void testVarExistsAfterPreDec() {
        try {
            evaluator.eval(parser.parse("j = 2"));
            evaluator.eval(parser.parse("k = --j + m"));
            fail ("calculator should not accept un previously declared variables");
            assertEquals(2,parser.getMap().get("j"),"variable j must not be computed due to variabe not exists error of variable m");
        } catch (CalcException e) {
        }
    }

    @Test
    public void testVarExistsAfterNormalize() {
        try {
            evaluator.eval(parser.parse("j += 2"));

            fail ("calculator should not accept un previously declared variables");
        } catch (CalcException e) {
        }
    }

    @Test
    public void testWrongAssign() {
        try {
            evaluator.eval(parser.parse("2 = 2"));
            fail ("calculator should only be able to assign into variables");
        } catch (CalcException e) {
        }
    }

    @Test
    public void testEvalTerm() {
        LinkedList<Evaluable> test = new LinkedList<>();
        test.add(new VarDecl("i",map,false));
        test.add(new Num("1",false));
        test.add(new Num("2",false));
        test.add(new Num("3",false));
        test.add(new Op('*'));
        test.add(new Op('+'));

        assertEquals(7,evaluator.eval(test),"evaluate algorithm is not working");

    }

    @Test
    public void testPostFix() {
            LinkedList<Evaluable> test = new LinkedList<>();
            test.add(new Num("1",false));
            test.add(new Op('+'));
            test.add(new Num("2",false));
            test.add(new Op('*'));
            test.add(new Num("3",false));
            LinkedList<Evaluable> expected = new LinkedList<>();
            expected.add(new Num("1",false));
            expected.add(new Num("2",false));
            expected.add(new Num("3",false));
            expected.add(new Op('*'));
            expected.add(new Op('+'));

            test = evaluator.postFix(test);

            assertEquals(5, evaluator.postFix(test).size(), "length of postfix is not equal to infix");

        for (int i =0 ; i<5; i++) {
            assertEquals(expected.get(i).getVal(),test.get(i).getVal(),test.get(i).getVal() + " not equal " + expected.get(i).getVal());
        }
    }
}
