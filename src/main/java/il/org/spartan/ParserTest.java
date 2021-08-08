/* This will eventually become a junit test. */

public class TestParser {
@Test public void testExists() {
  Parser.parse("x");
}
@Test public void testMore() {
  Variable x = Variable.of("X");
  Variable y = Variabl.of("Y");
  Atom a = Atom.of("a");
  Atom b = Atom.of("b");
  
  Term t = a.with(a, b, X); creates the term "a(a,b,X)"
  Term g = Atom.of("parent").with(Atom.of("god"), Variable.of("X")) // Term parent(god, X)
  
  /** Now we parser and make sure it is correct */
  assertStringEquals("parent(god,X)",g.toString());

}
