package il.org.spartan.protolog;

import org.eclipse.jdt.annotation.*;
import il.org.spartan.protolog.Term.Atom.*;

public class Replacement {
	public final @NonNull Variable variable;
	public final @NonNull Term term;

	public Replacement(final Variable variable, final Term term) {
		this.variable = variable;
		this.term = term;
	}

	@Override
	public String toString() {
		return "[" + variable + "->" + term + "]";
	}

	public boolean legal() {
		return !term.hasVariable(variable);
	}
}