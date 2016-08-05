package il.org.spartan.protolog;

import java.util.*;
import org.eclipse.jdt.annotation.*;
import il.org.spartan.*;
import il.org.spartan.misc.*;
import il.org.spartan.protolog.Term.Atom.*;

public interface Term extends Iterable<Term>, Cloneable {
  default Term clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException();
  }
  static @NonNull final Iterator<Term> empty = new Iterator<Term>() {
    @Override public boolean hasNext() {
      return false;
    }
    @Override public Term next() {
      throw new UnsupportedOperationException();
    }
  };
  boolean isAtom();
  @Override default Iterator<Term> iterator() {
    return empty;
  }
  default public @Nullable Replacement firstReplacement(final Term t) {
    return isVariable() ? ((Variable) this).variableReplacement(t) : //
        t.isVariable() ? t.firstReplacement(this) : //
            t instanceof Compound ? compositeReplacement((Compound) t) : //
                null;
  }
  default @Nullable Replacement compositeReplacement(final Compound c) {
    assert !equals(c);
    return null;
  }
  default Term replace(final Iterable<Replacement> rs) {
    Term $ = this;
    for (final Replacement r : Utils.cantBeNull(rs))
      $ = $.replace(Utils.cantBeNull(r));
    return $;
  }
  default Term replace(final Replacement r) {
    if (!hasVariable(Utils.cantBeNull(r.variable)))
      return this;
    final List<Term> a = new ArrayList<>();
    for (final Term t : this)
      a.add(t.replace(r));
    return new Compound((Symbol) this, a);
  }
  default boolean hasVariable(@SuppressWarnings("unused") final Variable v) {
    return false;
  }
  default boolean hasVariables() {
    if (isVariable())
      return true;
    for (final Term t : this)
      if (!t.hasVariables())
        return true;
    return false;
  }
  default boolean isVariable() {
    return false;
  }
  default @Nullable Iterable<Replacement> unify(final Term t) {
    return Unifier.mostGeneral(this, t);
  }
  default Set<il.org.spartan.protolog.Term.Atom.Variable> freeVariables() {
    final Set<Variable> $ = new LinkedHashSet<>();
    if (isVariable())
      $.add((Variable) this);
    else
      for (final Term t : this)
        $.addAll(t.freeVariables());
    return $;
  }

  class Terms extends ArrayList<Term> {
    private static final long serialVersionUID = -1395858331471910282L;
    public Terms(final Collection<? extends Term> c) {
      super(c);
    }
    public Terms(final Term... ts) {
      for (final Term t : ts)
        add(t);
    }
  }

  final class Compound extends Symbol implements Term {
    public String name() {
      return get();
    }
    public int arity() {
      return length();
    }
    public int length() {
      return ts.size();
    }
    public Term get(final int i) {
      return Utils.cantBeNull(ts.get(i));
    }
    @Override public Compound clone() throws CloneNotSupportedException {
      final List<Term> $ = new ArrayList<>();
      for (final Term t : ts)
        $.add(t.clone());
      return new Compound(this, $);
    }
    @Override public String toString() {
      return super.toString() + Beginning.with("(").separate(ts).byCommas().endingWith(")");
    }
    public final @NonNull Terms ts;
    @Override public final boolean equals(final Wrapper<?> w) {
      return super.equals(w) && equals((Compound) w);
    }
    public boolean equals(final Compound c) {
      if (ts.size() != c.ts.size())
        return false;
      for (int i = 0; i < ts.size(); ++i)
        if (!ts.get(i).equals(c.ts.get(i)))
          return false;
      return true;
    }
    @Override public @Nullable Replacement compositeReplacement(final Compound c) {
      if (ts.size() != c.ts.size() || !c.t.equals(t))
        return null;
      for (int i = 0; i < ts.size(); ++i) {
        final Replacement $ = ts.get(i).firstReplacement(Utils.cantBeNull(c.ts.get(i)));
        if ($ != null)
          return $;
      }
      return null;
    }
    public Compound(final Symbol symbol, final Term... ts) {
      this(symbol, Utils.cantBeNull(Arrays.asList(ts)));
    }
    public Compound(final String symbol, final Term... ts) {
      this(new Symbol(symbol), Utils.cantBeNull(Arrays.asList(ts)));
    }
    public Compound(final Symbol symbol, final List<Term> ts) {
      super(symbol);
      this.ts = new Terms(ts);
    }
    @Override public Iterator<Term> iterator() {
      return Utils.cantBeNull(ts.iterator());
    }
    @Override public boolean isAtom() {
      return false;
    }
    @Override public boolean hasVariable(final Variable v) {
      for (final Term t : this)
        if (t.hasVariable(v))
          return true;
      return false;
    }
  }

  public interface Atom extends Term {
    @Override default boolean isAtom() {
      return true;
    }

    class Constant extends Symbol implements Atom {
      public Constant(final String symbol) {
        super(symbol);
      }
      @Override public Constant clone() throws CloneNotSupportedException {
        return (Constant) Utils.cantBeNull(super.clone());
      }
    }

    class Variable extends Symbol implements Atom {
      public @Nullable Replacement variableReplacement(final Term t) {
        return t.hasVariable(this) ? null : new Replacement(this, t);
      }
      @Override public boolean isVariable() {
        return true;
      }
      @Override public Term replace(final Replacement r) {
        return !equals(Utils.cantBeNull(r.variable)) ? this : Utils.cantBeNull(r.term);
      }
      @Override public boolean hasVariable(final Variable v) {
        return equals(v);
      }
      public Variable(final String symbol) {
        super(symbol);
      }
      @Override public Variable clone() throws CloneNotSupportedException {
        return (Variable) Utils.cantBeNull(super.clone());
      }
    }

    class Num implements Atom {
      private static final String TO_STRING = ""; //$NON-NLS-1$
      public final int n;
      public Num(final int n) {
        this.n = n;
      }
      @Override public String toString() {
        return TO_STRING + n;
      }
      @Override public Atom clone() throws CloneNotSupportedException {
        return (Atom) Utils.cantBeNull(super.clone());
      }
      @Override public int hashCode() {
        return n;
      }
      @Override public boolean equals(final @Nullable Object o) {
        return o == this || o != null && getClass() == o.getClass() && n == ((Num) o).n;
      }
    }

    static class Snippet extends Wrapper<String> implements Atom {
      @SuppressWarnings("nls") @Override public String toString() {
        return "\"" + super.toString() + "\"";
      }
      public Snippet(final String text) {
        super(text);
      }
      @Override public Snippet clone() throws CloneNotSupportedException {
        return (Snippet) Utils.cantBeNull(super.clone());
      }
    }
  }
}
