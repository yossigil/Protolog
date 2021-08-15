/*
 * @file : ParserTest.java
 * @date : 15 August 2021
 */
package il.org.spartan.protolog;

import il.org.spartan.*;
import org.junit.*;
import org.junit.jupiter.api.Test;

public class ParserTest {

  /**
   * This test checks that that static parsing function (Parser.parse()) exists.
   */
  @Test public void testParserExists() {
    Parser.parse("a");
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

  /**
   * This test checks that the parser parses correctly one-letter constants.
   */
  @Test public void testShortConstants() {
    //todo
  }

  /**
   * This test checks that the parser parses correctly multiple-letter constants.
   */
  @Test public void testLongConstants() {
    //todo
  }

  /**
   * This test checks that the parser parses correctly one-letter numbers.
   */
  @Test public void testShortNumbers() {
    //todo
  }

  /**
   * This test checks that the parser parses correctly multiple-letter numbers.
   */
  @Test public void testLongNumbers() {
    //todo
  }

  /**
   * This test checks that the parser parses correctly one-letter variables.
   */
  @Test public void testShortVariables() {
    //todo
  }

  /**
   * This test checks that the parser parses correctly multiple-letter variables.
   */
  @Test public void testLongVariables() {
    //todo
  }

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
