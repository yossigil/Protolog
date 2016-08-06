package il.org.spartan.protolog;

import static il.org.spartan.azzert.*;
import java.util.*;
import org.junit.*;
import org.junit.runners.*;
import il.org.spartan.*;
import il.org.spartan.iterables.*;
import il.org.spartan.protolog.Term.*;
import il.org.spartan.protolog.Term.Atom.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "nls", }) //
public class TermTest {
  final Constant c = new Constant("c");
  final Constant c1 = new Constant("c");
  final Compound e = new Compound("e");
  final Rule e2empty = new Rule(e);
  final Symbol f = new Symbol("f");
  final Variable Y = new Variable("Y");
  final Variable X = new Variable("X");
  final Variable X_clone = new Variable("X");
  final Variable Z = new Variable("Z");
  final Compound f0 = new Compound(f, Z);
  final Compound f1 = new Compound(f, X, Y, Z);
  final Compound f2 = new Compound(f, c, Y, c);
  final Compound f3 = new Compound(f, c, Z, c);
  final Compound f4 = new Compound(f, X, Y);
  final Compound f5 = new Compound(f, f4, X);
  final Compound f6 = new Compound(f, Y, Z);
  final Compound f7 = new Compound(f, Y, f4);
  final Num n = new Num(13);
  final Num n13 = new Num(13);
  final Compound px = new Compound("p", X, c, n);
  final Compound pxy = new Compound("p", Y, X, n);
  final Compound pxyz = new Compound("p", Y, X, Z);
  final Compound py = new Compound("p", Y, c, n);
  final Rule r2 = new Rule(e, px, e, py);
  final Replacement Rx = new Replacement(X, px);
  final Snippet s = new Snippet("a[i]");
  final Compound p = new Compound("p", X, s, c, n);
  final Compound q = new Compound("q", p, X, s, c, n);
  final Rule r1 = new Rule(e, p);
  final Term[] ts = { //
      c, c1, e, n, n13, px, py, pxy, pxyz, p, q, s, X, Y, Z, //
      f0, f1, f2, f3, f4, f5, f6, f7//
  };
  @Test public void c() throws CloneNotSupportedException {
    azzert.aye(c.isAtom());
    azzert.nay(c.isVariable());
    azzert.nay(c.hasVariables());
    azzert.nay(c.hasVariable(X));
    azzert.nay(c.hasVariable(X_clone));
    azzert.nay(c.hasVariable(Y));
    azzert.aye(c.freeVariables().isEmpty());
    azzert.that(c.clone(), is(c));
    azzert.that("c", is(c + ""));
    azzert.that(null, is(c));
  }
  @Test public void ce() {
    azzert.isNull(c.firstReplacement(e));
    azzert.isNull(e.firstReplacement(c));
    azzert.isNull(Unifier.mostGeneral(c, e));
    azzert.isNull(Unifier.mostGeneral(e, c));
  }
  @Test public void cX() {
    azzert.that("" + c.firstReplacement(X), is("[X->c]"));
  }
  @Test public void e() throws CloneNotSupportedException {
    azzert.nay(e.isAtom());
    azzert.nay(e.isVariable());
    azzert.nay(e.hasVariables());
    azzert.nay(e.hasVariable(X));
    azzert.nay(e.hasVariable(X_clone));
    azzert.nay(e.hasVariable(Y));
    azzert.that(e.freeVariables().size(), is(0));
    azzert.that(e.clone(), is(e));
    azzert.that(e + "", is("e"));
    azzert.that(null, is(e));
  }
  @Test public void e2empty() {
    azzert.that(e2empty + "", is("e."));
    azzert.that(null, is(e2empty));
  }
  @Test public void f4f5() {
    azzert.that("" + f4, is("f(X,Y)"));
    azzert.that("" + f5, is("f(f(X,Y),X)"));
    final Replacement r = f4.firstReplacement(f5);
    azzert.that("" + r, is("[Y->X]"));
    final Term f4r = f4.replace(r);
    azzert.that("" + f4r, is("f(X,X)"));
    final Term f5r = f5.replace(r);
    azzert.that("" + f5r, is("f(f(X,X),X)"));
    azzert.isNull(f4r.firstReplacement(f5r));
    azzert.isNull(f5r.firstReplacement(f4r));
    azzert.isNull(Unifier.mostGeneral(f4r, f5r));
    azzert.isNull(Unifier.mostGeneral(f5r, f4r));
    azzert.isNull(f5r.unify(f4r));
  }
  @Test public void f4f6() {
    azzert.that("" + f4, is("f(X,Y)"));
    azzert.that("" + f6, is("f(Y,Z)"));
    final Replacement r = f4.firstReplacement(f6);
    azzert.that("" + r, is("[X->Y]"));
    final Term f4r = f4.replace(r);
    azzert.that("" + f4r, is("f(Y,Y)"));
    final Term f6r = f6.replace(r);
    azzert.that("" + f6r, is("f(Y,Z)"));
    final Replacement r2nd = f4r.firstReplacement(f6r);
    azzert.that("" + r2nd, is("[Y->Z]"));
    final Term f4rr = f4r.replace(r2nd);
    azzert.that("" + f4rr, is("f(Z,Z)"));
    final Term f6rr = f6r.replace(r2nd);
    azzert.that("" + f6rr, is("f(Z,Z)"));
    azzert.that(f4rr, is(f6rr));
    azzert.that(f4.replace(Unifier.mostGeneral(f4, f6)), is(f6.replace(Unifier.mostGeneral(f4, f6))));
  }
  @Test public void firstReplacement() {
    azzert.isNull(p.firstReplacement(e));
    azzert.isNull(p.firstReplacement(c));
    azzert.isNull(p.firstReplacement(n));
    azzert.isNull(p.firstReplacement(p));
    azzert.isNull(p.firstReplacement(px));
    azzert.isNull(p.firstReplacement(py));
    azzert.isNull(p.firstReplacement(q));
    azzert.isNull(p.firstReplacement(s));
    azzert.isNull(p.firstReplacement(X));
    azzert.that("" + py.firstReplacement(X), is("[X->p(Y,c,13)]"));
    azzert.notNull(py.firstReplacement(px));
    azzert.that("" + py.firstReplacement(px), is("[Y->X]"));
    azzert.that("" + px.firstReplacement(py), is("[X->Y]"));
  }
  @Test public void firstReplacementAllOptions() {
    for (final Term t1 : ts)
      for (final Term t2 : ts) {
        final Replacement r1st = t1.firstReplacement(t2);
        final String message = "Unifying t1: " + t1 + " t2: " + t2 + //
            " 1st=" + r1st + // ;
            (r1st == null ? "" : "\n\t t1/r=" + t1.replace(r1st) + " t2/r=" + t2.replace(r1st)) + //
            "\n";
        System.out.println(message);
        if (t1.equals(t2)) {
          azzert.isNull(message, r1st);
          azzert.aye(message, iterables.isEmpty(Unifier.mostGeneral(t1, t2)));
          continue;
        }
        if (r1st == null) {
          azzert.isNull(message, Unifier.mostGeneral(t1, t2));
          continue;
        }
        final Iterable<Replacement> rs = Unifier.mostGeneral(t1, t2);
        if (rs != null) {
          azzert.that(message, t2.replace(rs), is(t1.replace(rs)));
          azzert.that(message, t1.replace(rs), is(t2.replace(rs)));
          continue;
        }
        for (Term t1_ = t1, t2_ = t2;;) {
          final Replacement r = t1_.firstReplacement(t2_);
          if (r == null) {
            azzert.isNull(message, Unifier.mostGeneral(t1_, t2_));
            azzert.isNull(message, Unifier.mostGeneral(t2_, t1_));
            break;
          }
          t1_ = t1_.replace(r);
          t2_ = t2_.replace(r);
        }
      }
  }
  @Test public void firstReplacementVariable() {
    azzert.notNull(X.firstReplacement(c));
    azzert.notNull(X.firstReplacement(s));
    azzert.notNull(X.firstReplacement(n));
    azzert.notNull(X.firstReplacement(e));
    azzert.isNull(X.firstReplacement(p));
    azzert.isNull(X.firstReplacement(q));
    azzert.isNull(X.firstReplacement(px));
    azzert.notNull(X.firstReplacement(py));
    azzert.that("" + X.firstReplacement(py), is("[X->p(Y,c,13)]"));
  }
  @Test public void firstReplacementWorks() {
    for (final Term t1 : ts)
      for (final Term t2 : ts) {
        final Replacement r = t1.firstReplacement(t2);
        if (r == null)
          continue;
        azzert.nay(t1.equals(t1.replace(r)) && t2.equals(t2.replace(r)));
      }
  }
  @SuppressWarnings({ "static-method" }) //
  @Test(expected = IllegalArgumentException.class) //
  public void illegalSymbol() {
    new Symbol("");
  }
  @Test(expected = UnsupportedOperationException.class) //
  public void iterateOverEmpty_c() {
    c.iterator().next();
  }
  @Test(expected = UnsupportedOperationException.class) //
  public void iterateOverEmpty_n() {
    n.iterator().next();
  }
  @Test(expected = UnsupportedOperationException.class) //
  public void iterateOverEmpty_s() {
    s.iterator().next();
  }
  @Test public void mostGeneralUnifier() {
    for (final Term t1 : ts)
      for (final Term t2 : ts) {
        System.out.println(t1);
        System.out.println(t2);
        final Iterable<Replacement> rs = Unifier.mostGeneral(t1, t2);
        System.out.println(rs);
        if (rs == null)
          continue;
        azzert.that("Cannot unify " + t1 + " with " + t2 + "\n\t" + // "
            "using " + iterables.count(rs) + " rules: " + rs + "\n\t" + //
            "first replacement is=" + t1.firstReplacement(t2) + "\n\t" + //
            "", t2.replace(rs), is(t1.replace(rs)));
      }
  }
  @Test public void n() throws CloneNotSupportedException {
    azzert.aye(n.isAtom());
    azzert.nay(n.isVariable());
    azzert.nay(n.hasVariables());
    azzert.nay(n.hasVariable(X));
    azzert.nay(n.hasVariable(X_clone));
    azzert.nay(n.hasVariable(Y));
    azzert.aye(n.freeVariables().isEmpty());
    azzert.that(n.clone(), is(n));
    azzert.that(n13, is(n));
    azzert.that("13", is(n + ""));
    azzert.that(null, is(n));
  }
  @Test public void noReplacementForEquals() {
    for (final Term t1 : ts)
      for (final Term t2 : ts)
        if (t1.equals(t2))
          azzert.isNull(t1.firstReplacement(t2));
  }
  @Test public void notEquals() throws CloneNotSupportedException {
    for (final Term t : ts) {
      azzert.that(t, is(t));
      azzert.that("t:" + t + "t.clone():" + t.clone() + "are not equal", t.clone(), is(t));
    }
    for (final Term t1 : ts)
      for (final Term t2 : ts)
        if (t1 == t2)
          azzert.that(t2, is(t1));
  }
  @Test public void p() throws CloneNotSupportedException {
    azzert.nay(p.isAtom());
    azzert.nay(p.isVariable());
    azzert.aye(p.hasVariables());
    azzert.aye(p.hasVariable(X));
    azzert.aye(p.hasVariable(X_clone));
    azzert.nay(p.hasVariable(Y));
    azzert.that(1, is(p.freeVariables().size()));
    azzert.that(p.clone(), is(p));
    azzert.that(p + "", is("p(X,\"a[i]\",c,13)"));
    azzert.that(null, is(p));
  }
  @Test public void px() throws CloneNotSupportedException {
    azzert.nay(px.isAtom());
    azzert.nay(px.isVariable());
    azzert.aye(px.hasVariables());
    azzert.aye(px.hasVariable(X));
    azzert.aye(px.hasVariable(X_clone));
    azzert.nay(px.hasVariable(Y));
    azzert.that(1, is(px.freeVariables().size()));
    azzert.that(px.clone(), is(px));
    azzert.that(p.clone(), is(px));
    azzert.that(p, is(px));
    azzert.that(px + "", is("p(X,c,13)"));
    azzert.that(null, is(px));
  }
  @Test public void py() throws CloneNotSupportedException {
    azzert.nay(py.isAtom());
    azzert.nay(py.isVariable());
    azzert.aye(py.hasVariables());
    azzert.nay(py.hasVariable(X));
    azzert.nay(py.hasVariable(X_clone));
    azzert.aye(py.hasVariable(Y));
    azzert.that(1, is(py.freeVariables().size()));
    azzert.that(py + "", is("p(Y,c,13)"));
    azzert.that(py.clone(), is(py));
    azzert.that(p, is(py));
    azzert.that(p.clone(), is(py));
    azzert.that(null, is(py));
  }
  @Test public void q() throws CloneNotSupportedException {
    azzert.nay(q.isAtom());
    azzert.nay(q.isVariable());
    azzert.aye(q.hasVariables());
    azzert.aye(q.hasVariable(X));
    azzert.aye(q.hasVariable(X_clone));
    azzert.nay(q.hasVariable(Y));
    azzert.that(1, is(q.freeVariables().size()));
    azzert.that(q.clone(), is(q));
    azzert.that(q + "", is("q(p(X,\"a[i]\",c,13),X,\"a[i]\",c,13)"));
    azzert.that(null, is(q));
  }
  @Test public void r1() {
    azzert.that(null, is(r1));
    azzert.that(r1 + "", is("e :- p(X,\"a[i]\",c,13)."));
  }
  @Test public void r2() {
    azzert.that(null, is(r2));
    azzert.that(r2 + "", is("e :- p(X,c,13), e, p(Y,c,13)."));
  }
  @Test public void Rx() {
    azzert.that(null, is(Rx));
    azzert.that(Rx + "", is("[X->p(X,c,13)]"));
    azzert.nay(Rx.legal());
    azzert.aye(new Replacement(Y, p).legal());
  }
  @Test public void s() throws CloneNotSupportedException {
    azzert.aye(s.isAtom());
    azzert.nay(s.isVariable());
    azzert.nay(s.hasVariables());
    azzert.nay(s.hasVariable(X));
    azzert.nay(s.hasVariable(X_clone));
    azzert.nay(s.hasVariable(Y));
    azzert.aye(s.freeVariables().isEmpty());
    azzert.that(s.clone(), is(s));
    azzert.that("\"a[i]\"", is(s + ""));
    azzert.that(null, is(s));
    azzert.that(s, is(s));
  }
  @Test public void t() throws CloneNotSupportedException {
    final Term t = new Term() {
      @Override public Term clone() throws CloneNotSupportedException {
        return (Term) super.clone();
      }
      @Override public boolean hasVariable(final Variable v) {
        return false;
      }
      @Override public boolean isAtom() {
        return false;
      }
      @Override public Iterator<Term> iterator() {
        return Term.empty;
      }
    };
    azzert.that(null, is(t));
    azzert.that(t, is(t));
    azzert.nay(t.isAtom());
    azzert.nay(t.isVariable());
    azzert.nay(t.hasVariables());
    azzert.nay(t.hasVariable(X));
    azzert.nay(t.hasVariable(X_clone));
    azzert.nay(t.hasVariable(Y));
    azzert.that(X_clone, is(t));
    azzert.that(t.clone(), is(t));
  }
  @Test public void X() throws CloneNotSupportedException {
    azzert.aye(X.isAtom());
    azzert.aye(X.isVariable());
    azzert.aye(X.hasVariables());
    azzert.aye(X.hasVariable(X));
    azzert.aye(X.hasVariable(X_clone));
    azzert.nay(X.hasVariable(Y));
    azzert.that(X_clone, is(X));
    azzert.that(X.clone(), is(X));
    azzert.that("X", is("" + X));
    azzert.that(null, is(X));
    azzert.that(X, is(X));
  }
  @Test public void Xc() {
    azzert.that("" + X.firstReplacement(c), is("[X->c]"));
  }
}
