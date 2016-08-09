package il.org.spartan.protolog;

import il.org.spartan.*;

public class Symbol extends Wrapper<String> {
  private static String check(final String s) {
    if (s.isEmpty())
      throw new IllegalArgumentException();
    return s;
  }
  public Symbol(final String symbol) {
    super(check(symbol));
  }
  public Symbol(final Symbol other) {
    super(other.inner);
  }
  @Override public Symbol clone() throws CloneNotSupportedException {
    return (Symbol) Utils.cantBeNull(super.clone());
  }
}