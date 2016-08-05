package il.org.spartan.protolog;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import il.org.spartan.*;
import il.org.spartan.protolog.Term.*;

interface Unifier {
	static @Nullable Iterable<Replacement> mostGeneral(final Term... ts) {
		return mostGeneral(new Terms(ts));
	}

	static @Nullable Iterable<Replacement> mostGeneral(final Terms ts) {
		return mostGeneralx(ts, new LinkedHashSet<Replacement>());
	}

	static @Nullable Iterable<Replacement> mostGeneralx(final Terms ts, final LinkedHashSet<Replacement> $) {
		while (ts.size() >= 2) {
			final Term t1 = Utils.cantBeNull(ts.remove(0));
			final Term t2 = Utils.cantBeNull(ts.remove(0));
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
