package il.org.spartan.protolog;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.spartan.Beginning;

import il.org.spartan.*;

public class Rule {
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return "" + head + Beginning.with(" :- ").separate(body).by(", ") + ".";
	}

	public Rule(final Term.Compound head, final List<Term.Compound> body) {
		this.head = head;
		this.body = body;
	}

	public Rule(final Term.Compound head, final Term.Compound... body) {
		this.head = head;
		this.body = Utils.cantBeNull(Arrays.asList(body));
	}

	public final Term.@NonNull Compound head;
	public final @NonNull List<Term.Compound> body;
}
