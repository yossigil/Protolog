package il.org.spartan.protolog;

import il.org.spartan.*;
import il.org.spartan.misc.*;

public class Symbol extends Wrapper<String> {
	public Symbol(final String symbol) {
		super(check(symbol));
	}

	public Symbol(final Symbol other) {
		super(other.t);
	}

	private static String check(final String s) {
		if (s.isEmpty())
			throw new IllegalArgumentException();
		return s;
	}

	@Override
	public Symbol clone() throws CloneNotSupportedException {
		return (Symbol) Utils.cantBeNull(super.clone());
	}
}