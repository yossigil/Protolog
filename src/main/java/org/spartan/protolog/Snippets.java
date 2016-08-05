package org.spartan.protolog;

import java.util.*;

import org.eclipse.jdt.annotation.*;
import org.spartan.protolog.Term.Atom.*;

import il.org.spartan.*;

public interface Snippets extends Set<Snippet> {
	default boolean add(final String s) {
		return add(new Snippet(s));
	}

	@SuppressWarnings("serial") //
	public static final Snippets EMPTY = new Implementation() {
		private final static String id = "EMPY SNIPPETS SET"; //$NON-NLS-1$

		@Override
		public boolean add(final String e) {
			throw new UnsupportedOperationException(id);
		}

		@Override
		public boolean remove(final @Nullable Object o) {
			throw new UnsupportedOperationException(id);
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException(id);
		}
	};

	default Snippets make(final Collection<Snippet> ss) {
		return new Implementation(ss);
	}

	default Snippets make(final String... ss) {
		return new Implementation(ss);
	}

	default Snippets cat(final Iterable<Snippet> ss) {
		final Snippets $ = make();
		for (final Snippet s1 : this)
			for (final Snippet s2 : ss)
				$.add(s1.toString() + s2.toString());
		return $;
	}

	default Snippets cap(final Collection<Snippet> ss) {
		final Snippets $ = make(this);
		$.addAll(ss);
		return $;
	}

	default Snippets cut(final Collection<Snippet> ss) {
		final Snippets $ = make(this);
		$.removeAll(ss);
		return $;
	}

	@SuppressWarnings("serial") //
	class Implementation extends LinkedHashSet<Snippet> implements Snippets {
		public Implementation(final String... ss) {
			for (final String s : ss)
				add(Utils.cantBeNull(s));
		}

		public Implementation(final Collection<Snippet> ss) {
			super(ss);
		}
	}
}