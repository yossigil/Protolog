package il.org.spartan.protolog;

import java.util.*;
import org.eclipse.jdt.annotation.*;
import il.org.spartan.*;
import il.org.spartan.protolog.Term.Atom.*;

public interface Term extends Iterable<Term>, Cloneable {
  public interface Atom extends Term {
    class Constant extends Symbol implements Atom {
      public Constant(final String symbol) {
        super(symbol);
      }
      @Override public Constant clone() throws CloneNotSupportedException {
        return (Constant) Utils.cantBeNull(super.clone());
      }
    }

    class Num implements Atom {
      private static final String TO_STRING = ""; //$NON-NLS-1$
      public final int n;
      public Num(final int n) {
        this.n = n;
      }
      @Override public Atom clone() throws CloneNotSupportedException {
        return (Atom) Utils.cantBeNull(super.clone());
      }
      @Override public boolean equals(final @Nullable Object o) {
        return o == this || o != null && getClass() == o.getClass() && n == ((Num) o).n;
      }
      @Override public int hashCode() {
        return n;
      }
      @Override public String toString() {
        return TO_STRING + n;
      }
    }

    static class Snippet extends Wrapper<String> implements Atom {
      public Snippet(final String text) {
        super(text);
      }
      @Override public Snippet clone() throws CloneNotSupportedException {
        return (Snippet) Utils.cantBeNull(super.clone());
      }
      @SuppressWarnings("nls") @Override public String toString() {
        return "\"" + super.toString() + "\"";
      }
    }

    class Variable extends Symbol implements Atom {
      public Variable(final String symbol) {
        super(symbol);
      }
      @Override public Variable clone() throws CloneNotSupportedException {
        return (Variable) Utils.cantBeNull(super.clone());
      }
      @Override public boolean hasVariable(final Variable v) {
        return equals(v);
      }
      @Override public boolean isVariable() {
        return true;
      }
      @Override public Term replace(final Replacement r) {
        return !equals(Utils.cantBeNull(r.variable)) ? this : Utils.cantBeNull(r.term);
      }
      public @Nullable Replacement variableReplacement(final Term t) {
        return t.hasVariable(this) ? null : new Replacement(this, t);
      }
    }
    @Override default boolean isAtom() {
      return true;
    }
  }

  final class Compound extends Symbol implements Term {
    public final @NonNull Terms ts;
    public Compound(final String symbol, final Term... ts) {
      this(new Symbol(symbol), Utils.cantBeNull(as.list(ts)));
    }
    public Compound(final Symbol symbol, final List<Term> ts) {
      super(symbol);
      this.ts = new Terms(ts);
    }
    public Compound(final Symbol symbol, final Term... ts) {
      this(symbol, Utils.cantBeNull(as.list(ts)));
    }
    public int arity() {
      return length();
    }
    @Override public Compound clone() throws CloneNotSupportedException {
      final List<Term> $ = new ArrayList<>();
      for (final Term t : ts)
        $.add(t.clone());
      return new Compound(this, $);
    }
    @Override public @Nullable Replacement compositeReplacement(final Compound c) {
      if (ts.size() != c.ts.size() || !c.inner.equals(inner))
        return null;
      for (int ¢ = 0; ¢ < ts.size(); ++¢) {
        final Replacement $ = ts.get(¢).firstReplacement(Utils.cantBeNull(c.ts.get(¢)));
        if ($ != null)
          return $;
      }
      return null;
    }
    public boolean equals(final Compound c) {
      if (ts.size() != c.ts.size())
        return false;
      for (int ¢ = 0; ¢ < ts.size(); ++¢)
        if (!ts.get(¢).equals(c.ts.get(¢)))
          return false;
      return true;
    }
    @Override public final boolean equals(final Wrapper<?> w) {
      return super.equals(w) && equals((Compound) w);
    }
    public Term get(final int i) {
      return Utils.cantBeNull(ts.get(i));
    }
    @Override public boolean hasVariable(final Variable v) {
      for (final Term t : this)
        if (t.hasVariable(v))
          return true;
      return false;
    }
    @Override public boolean isAtom() {
      return false;
    }
    @Override public Iterator<Term> iterator() {
      return Utils.cantBeNull(ts.iterator());
    }
    public int length() {
      return ts.size();
    }
    public String name() {
      return get();
    }
    @Override public String toString() {
      return super.toString() + beginning.with("(").separate(ts).byCommas().endingWith(")");
    }
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
  static @NonNull final Iterator<Term> empty = new Iterator<Term>() {
    @Override public boolean hasNext() {
      return false;
    }
    @Override public Term next() {
      throw new UnsupportedOperationException();
    }
  };
  default Term clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException();
  }
  default @Nullable Replacement compositeReplacement(final Compound c) {
    assert !equals(c);
    return null;
  }
  default public @Nullable Replacement firstReplacement(final Term ¢) {
    return isVariable() ? ((Variable) this).variableReplacement(¢) : //
        ¢.isVariable() ? ¢.firstReplacement(this) : //
            ¢ instanceof Compound ? compositeReplacement((Compound) ¢) : //
                null;
  }
  default Set<Variable> freeVariables() {
    final Set<Variable> $ = new LinkedHashSet<>();
    if (isVariable())
      $.add((Variable) this);
    else
      for (final Term ¢ : this)
        $.addAll(¢.freeVariables());
    return $;
  }
  default boolean hasVariable(final Variable v) {
    return false;
  }
  default boolean hasVariables() {
    if (isVariable())
      return true;
    for (final Term ¢ : this)
      if (!¢.hasVariables())
        return true;
    return false;
  }
  boolean isAtom();
  default boolean isVariable() {
    return false;
  }
  @Override default Iterator<Term> iterator() {
    return empty;
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
    final List<Term> $ = new ArrayList<>();
    for (final Term ¢ : this)
      $.add(¢.replace(r));
    return new Compound((Symbol) this, $);
  }
  default @Nullable Iterable<Replacement> unify(final Term t) {
    return Unifier.mostGeneral(this, t);
  }
}
