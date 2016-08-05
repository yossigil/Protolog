package org.spartan.protolog;

import java.util.LinkedHashSet;

import org.eclipse.jdt.annotation.Nullable;
import org.spartan.__;
import org.spartan.protolog.Term.Terms;

interface Unifier {
  static @Nullable Iterable<Replacement> mostGeneral(final Term... ts) {
    return mostGeneral(new Terms(ts));
  }
  static @Nullable Iterable<Replacement> mostGeneral(final Terms ts) {
    return mostGeneralx(ts, new LinkedHashSet<Replacement>());
  }
  static @Nullable Iterable<Replacement> mostGeneralx(final Terms ts, final LinkedHashSet<Replacement> $) {
    while (ts.size() >= 2) {
      final Term t1 = __.cantBeNull(ts.remove(0));
      final Term t2 = __.cantBeNull(ts.remove(0));
      if (t1.equals(t2)) {
        if (!t1.hasVariables())
          ts.add(t1);
        continue;
      }
      final Replacement r = t1.firstReplacement(t2);
      if (r == null)
        return null;
      ts.add(t1.replace(r));
      ts.add(t2.replace(r));
      $.add(r);
    }
    return $;
  }
}
