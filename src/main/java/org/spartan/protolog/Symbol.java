package org.spartan.protolog;

import org.spartan.__;
import org.spartan.collections.Wrapper;

public class Symbol extends Wrapper<String> {
  public Symbol(final String symbol) {
    super(check(symbol));
  }
  public Symbol(final Symbol other) {
    super(other.inner);
  }
  private static String check(final String s) {
    if (s.isEmpty())
      throw new IllegalArgumentException();
    return s;
  }
  @Override public Symbol clone() throws CloneNotSupportedException {
    return (Symbol) __.cantBeNull(super.clone());
  }
}