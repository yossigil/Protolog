/*
 * @file : ParserTest.java
 * @date : 15 August 2021
 */
package il.org.spartan.protolog;

import org.junit.jupiter.api.Test;

/**
 * ParserTest - this class contains 17 test cases for Parse class.
 *
 * The following things are checked:
 *  - existence of static parsing function
 *  - the parsing function creates a new term each time
 *  - returning null on null-string
 *  - returning null on empty-string
 *  - returning correct value for all one-letter constants
 *  - returning correct value for multiple-letter constants
 *  - returning correct value for the number zero
 *  - returning correct value for all positive numbers up to three-letter
 *  - returning correct value for multiple-letter positive numbers
 *  - returning correct value for all negative numbers up to three-letter
 *  - returning correct value for multiple-letter negative numbers
 *  - returning correct value for all one-letter variables
 *  - returning correct value for multiple-letter variables
 *  - returning correct value for compounds whose children are all non-compound terms
 *  - returning correct value for compounds whose children are all compounds
 *  - returning correct value for  compounds whose children are compounds and non-compound terms
 *  - processing spaces correctly for compounds
 */
public class ParserTest {

    /* ---------------------------------------- Testing General Cases: ---------------------------------------- */

    /**
     * This test checks that that static parsing function (Parser.parse()) exists.
     */
    @SuppressWarnings("unused")
    @Test
    public void testParserExists() {
        Term a = Parser.parse("a");
    }

    /**
     * This test checks that that static parsing function creates a new term each time.
     */
    @Test
    public void testCloning() {
        final Term result1 = Parser.parse("a");
        final Term result2 = Parser.parse("a");
        assert (result1 != result2);
    }

    /**
     * This test checks that the parser returns null if a null-string is given.
     */
    @Test
    public void testNullString() {
        final Term result = Parser.parse(null);
        assert (result == null);
    }

    /**
     * This test checks that the parser returns null if an empty-string is given.
     */
    @Test
    public void testEmptyString() {
        final Term result = Parser.parse("");
        assert (result == null);
    }


    /* ---------------------------------------- Testing String Literals: ---------------------------------------- */

    /**
     * This test checks that the parser parses correctly one-letter constants (checking all options).
     */
    @Test
    public void testShortConstants() {
        for (char letter = 'a'; letter <= 'z'; letter++) {
            Term result = Parser.parse(Character.toString(letter));
            assert (result instanceof Term.Atom.Constant);
            assert ((new Term.Atom.Constant(Character.toString(letter))).equals(result));
        }
    }

    /**
     * This test checks that the parser parses correctly multiple-letter constants.
     */
    @Test
    public void testLongConstants() {
        final String[] cases = new String[]{"hi", "test", "divide", "pArEnT", "rELATION"};
        for (String str : cases) {
            Term result = Parser.parse(str);
            assert (result instanceof Term.Atom.Constant);
            assert ((new Term.Atom.Constant(str)).equals(result));
        }
    }


    /* ---------------------------------------- Testing Number Literals: ---------------------------------------- */

    /**
     * This test checks that the parser parses correctly the number zero (checking all options).
     */
    @Test
    public void testZeroNumber() {
        final Term correct_result = new Term.Atom.Num(0);

        // checking "0" :
        final Term result1 = Parser.parse("0");
        assert (result1 instanceof Term.Atom.Num);
        assert (correct_result.equals(result1));

        // checking "-0" :
        final Term result2 = Parser.parse("-0");
        assert (result2 instanceof Term.Atom.Num);
        assert (correct_result.equals(result2));
    }

    /**
     * This test checks that the parser parses correctly up to three-letter positive numbers (checking all options).
     */
    @Test
    public void testShortPositiveNumbers() {
        for (int num = 1; num <= 999; num++) {
            Term result = Parser.parse(Integer.toString(num));
            assert (result instanceof Term.Atom.Num);
            assert ((new Term.Atom.Num(num)).equals(result));
        }
    }

    /**
     * This test checks that the parser parses correctly multiple-letter positive numbers.
     */
    @Test
    public void testLongPositiveNumbers() {
        final int[] cases = new int[]{1000, 100001, Integer.MAX_VALUE};
        for (int num : cases) {
            Term result = Parser.parse(Integer.toString(num));
            assert (result instanceof Term.Atom.Num);
            assert ((new Term.Atom.Num(num)).equals(result));
        }
    }

    /**
     * This test checks that the parser parses correctly up to three-letter negative numbers (checking all options).
     */
    @Test
    public void testShortNegativeNumbers() {
        for (int num = -1; num >= -999; num--) {
            Term result = Parser.parse(Integer.toString(num));
            assert (result instanceof Term.Atom.Num);
            assert ((new Term.Atom.Num(num)).equals(result));
        }
    }

    /**
     * This test checks that the parser parses correctly multiple-letter negative numbers.
     */
    @Test
    public void testLongNegativeNumbers() {
        final int[] cases = new int[]{-1000, -100001, Integer.MIN_VALUE};
        for (int num : cases) {
            Term result = Parser.parse(Integer.toString(num));
            assert (result instanceof Term.Atom.Num);
            assert ((new Term.Atom.Num(num)).equals(result));
        }
    }


    /* ---------------------------------------- Testing Variables: ---------------------------------------- */

    /**
     * This test checks that the parser parses correctly one-letter variables (checking all options).
     */
    @Test
    public void testShortVariables() {
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            Term result = Parser.parse(Character.toString(letter));
            assert (result instanceof Term.Atom.Variable);
            assert ((new Term.Atom.Variable(Character.toString(letter))).equals(result));
        }
    }

    /**
     * This test checks that the parser parses correctly multiple-letter variables.
     */
    @Test
    public void testLongVariables() {
        final String[] cases = new String[]{"Result", "TEST", "NuMbEr"};
        for (String str : cases) {
            Term result = Parser.parse(str);
            assert (result instanceof Term.Atom.Variable);
            assert ((new Term.Atom.Variable(str)).equals(result));
        }
    }


    /* ---------------------------------------- Testing Compound Terms: ---------------------------------------- */

    /**
     * This test checks that the parser parses correctly compounds whose children are all non-compound terms.
     */
    @Test
    public void testShallowCompounds() {

        // checking "test(a)" :
        final String string1 = "isLetter(a)";
        final Term term1 = new Term.Compound("isLetter", new Term.Atom.Constant("a"));
        final Term result1 = Parser.parse(string1);
        assert (result1 instanceof Term.Compound);
        assert (term1.equals(result1));

        // checking "test(1,-0)" :
        final String string2 = "inverse(1,-0)";
        final Term term2 = new Term.Compound("inverse", new Term.Atom.Num(1), new Term.Atom.Num(0));
        final Term result2 = Parser.parse(string2);
        assert (result2 instanceof Term.Compound);
        assert (term2.equals(result2));

        // checking "test(-1,1,hi,a,X)" :
        final String string3 = "test(-1,1,hi,a,X)";
        final Term term3 = new Term.Compound("test", new Term.Atom.Num(-1), new Term.Atom.Num(1),
                new Term.Atom.Constant("hi"), new Term.Atom.Constant("a"),
                new Term.Atom.Variable("X"));
        final Term result3 = Parser.parse(string3);
        assert (result3 instanceof Term.Compound);
        assert (term3.equals(result3));
    }

    /**
     * This test checks that the parser parses correctly compounds whose children are all compounds.
     */
    @Test
    public void testDeepCompounds() {

        // checking "test(test(a))" :
        final String string1 = "test(test(a))";
        final Term term1 = new Term.Compound("test",
                new Term.Compound("test", new Term.Atom.Constant("a")));
        final Term result1 = Parser.parse(string1);
        assert (result1 instanceof Term.Compound);
        assert (term1.equals(result1));

        // checking "equal(testing(X),testing(1))" :
        final String string2 = "equal(testing(X),testing(1))";
        final Term term2 = new Term.Compound("equal",
                new Term.Compound("testing", new Term.Atom.Variable("X")),
                new Term.Compound("testing", new Term.Atom.Num(1)));
        final Term result2 = Parser.parse(string2);
        assert (result2 instanceof Term.Compound);
        assert (term2.equals(result2));
    }

    /**
     * This test checks that the parser parses correctly compounds whose children are compounds and non-compound terms.
     */
    @Test
    public void testMixedCompounds() {

        // checking "equal(X, test(a))" :
        final String string1 = "equal(X, test(a))";
        final Term term1 = new Term.Compound("equal",
                new Term.Atom.Variable("X"),
                new Term.Compound("test", new Term.Atom.Constant("a")));
        final Term result1 = Parser.parse(string1);
        assert (result1 instanceof Term.Compound);
        assert (term1.equals(result1));

        // checking "equal(testing(X, inverse(0, Y)),testing(1, Y))" :
        final String string2 = "equal(testing(X, inverse(0, Y)),testing(1, Y))";
        final Term term2 = new Term.Compound("equal",
                new Term.Compound("testing",
                        new Term.Atom.Variable("X"),
                        new Term.Compound("inverse",
                                new Term.Atom.Num(0),
                                new Term.Atom.Variable("Y"))),
                new Term.Compound("testing",
                        new Term.Atom.Num(1),
                        new Term.Atom.Variable("Y")));
        final Term result2 = Parser.parse(string2);
        assert (result2 instanceof Term.Compound);
        assert (term2.equals(result2));
    }

    /**
     * This test checks that the parser parses correctly compounds, ignoring spaces.
     */
    @Test
    public void testSpacingCompounds() {
        final Term correct_result = new Term.Compound("test",
                new Term.Atom.Constant("a"),
                new Term.Atom.Constant("b"),
                new Term.Atom.Constant("c"));

        // checking "test(a,b,c)" :
        final String string1 = "test(a,b,c)";
        final Term result1 = Parser.parse(string1);
        assert (result1 instanceof Term.Compound);
        assert (correct_result.equals(result1));

        // checking "test( a,b,c )" :
        final String string2 = "test( a,b,c )";
        final Term result2 = Parser.parse(string2);
        assert (result2 instanceof Term.Compound);
        assert (correct_result.equals(result2));

        // checking "test(a , b , c)" :
        final String string3 = "test(a , b , c)";
        final Term result3 = Parser.parse(string3);
        assert (result3 instanceof Term.Compound);
        assert (correct_result.equals(result3));

        // checking "test( a , b , c )" :
        final String string4 = "test( a , b , c )";
        final Term result4 = Parser.parse(string4);
        assert (result4 instanceof Term.Compound);
        assert (correct_result.equals(result4));
    }
}
