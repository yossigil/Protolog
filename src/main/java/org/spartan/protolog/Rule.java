package org.spartan.protolog;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.spartan.Beginning;
import org.spartan.__;

public class Rule {
  @SuppressWarnings("nls") @Override public String toString() {
    return "" + head + Beginning.with(" :- ").separate(body).by(", ") + ".";
  }
  public Rule(final Term.Compound head, final List<Term.Compound> body) {
    this.head = head;
    this.body = body;
  }
  public Rule(final Term.Compound head, final Term.Compound... body) {
    this.head = head;
    this.body = __.cantBeNull(Arrays.asList(body));
  }

  public final @NonNull Term.Compound head;
  public final @NonNull List<Term.Compound> body;
}
