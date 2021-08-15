/*
 * @file : ParserTest.java
 * @date : 15 August 2021
 */
package il.org.spartan.protolog;

import org.junit.jupiter.api.Test;

public class ParserTest {


  /* ---------------------------------------- Testing General Cases: ---------------------------------------- */

  /**
   * This test checks that that static parsing function (Parser.parse()) exists.
   */
  @Test public void testParserExists() {
    Parser.parse("a");
  }

  /**
   * This test checks that that static parsing function creates a new term each time.
   */
  @Test public void testCloning() {
    Term result1 = Parser.parse("a");
    Term result2 = Parser.parse("a");
    assert(result1 != result2);
  }

  /**
   * This test checks that the parser returns null if a null-string is given.
   */
  @Test public void testNullString() {
    Term result = Parser.parse(null);
    assert(result == null);
  }

  /**
   * This test checks that the parser returns null if an empty-string is given.
   */
  @Test public void testEmptyString() {
    Term result = Parser.parse("");
    assert(result == null);
  }


  /* ---------------------------------------- Testing String Literals: ---------------------------------------- */

  /**
   * This test checks that the parser parses correctly one-letter constants (checking all options).
   */
  @Test public void testShortConstants() {
    for(char letter = 'a'; letter <= 'z'; letter++) {
      Term result = Parser.parse(Character.toString(letter));
      assert(result instanceof Term.Atom.Constant);
      assert((new Term.Atom.Constant(Character.toString(letter))).equals(result));
    }
  }

  /**
   * This test checks that the parser parses correctly multiple-letter constants.
   */
  @Test public void testLongConstants() {
    String[] cases = new String[]{"hi", "test", "divide", "pArEnT", "rELATION"};
    for(String str : cases) {
      Term result = Parser.parse(str);
      assert(result instanceof Term.Atom.Constant);
      assert((new Term.Atom.Constant(str)).equals(result));
    }
  }


  /* ---------------------------------------- Testing Number Literals: ---------------------------------------- */

  /**
   * This test checks that the parser parses correctly up to three-letter positive numbers (checking all options).
   */
  @Test public void testShortPositiveNumbers() {
    for(int num = 0; num <= 999; num++) {
      Term result = Parser.parse(Integer.toString(num));
      assert(result instanceof Term.Atom.Num);
      assert((new Term.Atom.Num(num)).equals(result));
    }
  }

  /**
   * This test checks that the parser parses correctly multiple-letter positive numbers.
   */
  @Test public void testLongPositiveNumbers() {
    int[] cases = new int[]{1000, 100001, Integer.MAX_VALUE};
    for(int num : cases) {
      Term result = Parser.parse(Integer.toString(num));
      assert(result instanceof Term.Atom.Num);
      assert((new Term.Atom.Num(num)).equals(result));
    }
  }

  /**
   * This test checks that the parser parses correctly up to three-letter negative numbers (checking all options).
   */
  @Test public void testShortNegativeNumbers() {
    for(int num = -1; num >= -999; num--) {
      Term result = Parser.parse(Integer.toString(num));
      assert(result instanceof Term.Atom.Num);
      assert((new Term.Atom.Num(num)).equals(result));
    }
  }

  /**
   * This test checks that the parser parses correctly multiple-letter negative numbers.
   */
  @Test public void testLongNegativeNumbers() {
    int[] cases = new int[]{-1000, -100001, Integer.MIN_VALUE};
    for(int num : cases) {
      Term result = Parser.parse(Integer.toString(num));
      assert(result instanceof Term.Atom.Num);
      assert((new Term.Atom.Num(num)).equals(result));
    }
  }


  /* ---------------------------------------- Testing Variables: ---------------------------------------- */

  /**
   * This test checks that the parser parses correctly one-letter variables (checking all options).
   */
  @Test public void testShortVariables() {
    for(char letter = 'A'; letter <= 'Z'; letter++) {
      Term result = Parser.parse(Character.toString(letter));
      assert(result instanceof Term.Atom.Variable);
      assert((new Term.Atom.Variable(Character.toString(letter))).equals(result));
    }
  }

  /**
   * This test checks that the parser parses correctly multiple-letter variables.
   */
  @Test public void testLongVariables() {
    String[] cases = new String[]{"Result", "TEST", "NuMbEr"};
    for(String str : cases) {
      Term result = Parser.parse(str);
      assert(result instanceof Term.Atom.Variable);
      assert((new Term.Atom.Variable(str)).equals(result));
    }
  }


  /* ---------------------------------------- Testing Compound Terms: ---------------------------------------- */

  /**
   * This test checks that the parser parses correctly compounds whose children are all non-compound terms.
   */
  @Test public void testShallowCompounds() {
    //todo
  }

  /**
   * This test checks that the parser parses correctly compounds whose children are all compounds.
   */
  @Test public void testDeepCompounds() {
    //todo
  }

  /**
   * This test checks that the parser parses correctly compounds whose children are compounds and non-compound terms.
   */
  @Test public void testMixedCompounds() {
    //todo
  }
}
