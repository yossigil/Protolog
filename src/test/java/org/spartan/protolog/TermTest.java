package org.spartan.protolog;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.runners.*;
import org.spartan.protolog.Term.*;
import org.spartan.protolog.Term.Atom.*;

import il.org.spartan.iterables.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "nls", }) //
public class TermTest {
	final Variable X = new Variable("X");
	final Variable X_clone = new Variable("X");
	final Variable Y = new Variable("Y");
	final Variable Z = new Variable("Z");
	final Snippet s = new Snippet("a[i]");
	final Constant c = new Constant("c");
	final Constant c1 = new Constant("c");
	final Num n = new Num(13);
	final Num n13 = new Num(13);
	final Compound px = new Compound("p", X, c, n);
	final Compound py = new Compound("p", Y, c, n);
	final Compound pxy = new Compound("p", Y, X, n);
	final Compound pxyz = new Compound("p", Y, X, Z);
	final Compound p = new Compound("p", X, s, c, n);
	final Compound q = new Compound("q", p, X, s, c, n);
	final Compound e = new Compound("e");
	final Rule e2empty = new Rule(e);
	final Rule r1 = new Rule(e, p);
	final Rule r2 = new Rule(e, px, e, py);
	final Replacement Rx = new Replacement(X, px);
	final Symbol f = new Symbol("f");
	final Compound f0 = new Compound(f, Z);
	final Compound f1 = new Compound(f, X, Y, Z);
	final Compound f2 = new Compound(f, c, Y, c);
	final Compound f3 = new Compound(f, c, Z, c);
	final Compound f4 = new Compound(f, X, Y);
	final Compound f5 = new Compound(f, f4, X);
	final Compound f6 = new Compound(f, Y, Z);
	final Compound f7 = new Compound(f, Y, f4);
	final Term[] ts = { //
			c, c1, e, n, n13, px, py, pxy, pxyz, p, q, s, X, Y, Z, //
			f0, f1, f2, f3, f4, f5, f6, f7//
	};

	@Test
	public void f4f5() {
		assertEquals("f(X,Y)", "" + f4);
		assertEquals("f(f(X,Y),X)", "" + f5);
		final Replacement r = f4.firstReplacement(f5);
		assertEquals("[Y->X]", "" + r);
		final Term f4r = f4.replace(r);
		assertEquals("f(X,X)", "" + f4r);
		final Term f5r = f5.replace(r);
		assertEquals("f(f(X,X),X)", "" + f5r);
		assertNull(f4r.firstReplacement(f5r));
		assertNull(f5r.firstReplacement(f4r));
		assertNull(Unifier.mostGeneral(f4r, f5r));
		assertNull(Unifier.mostGeneral(f5r, f4r));
		assertNull(f5r.unify(f4r));
	}

	@Test
	public void f4f6() {
		assertEquals("f(X,Y)", "" + f4);
		assertEquals("f(Y,Z)", "" + f6);
		final Replacement r = f4.firstReplacement(f6);
		assertEquals("[X->Y]", "" + r);
		final Term f4r = f4.replace(r);
		assertEquals("f(Y,Y)", "" + f4r);
		final Term f6r = f6.replace(r);
		assertEquals("f(Y,Z)", "" + f6r);
		final Replacement r2nd = f4r.firstReplacement(f6r);
		assertEquals("[Y->Z]", "" + r2nd);
		final Term f4rr = f4r.replace(r2nd);
		assertEquals("f(Z,Z)", "" + f4rr);
		final Term f6rr = f6r.replace(r2nd);
		assertEquals("f(Z,Z)", "" + f6rr);
		assertEquals(f6rr, f4rr);
		assertEquals(f6.replace(Unifier.mostGeneral(f4, f6)), f4.replace(Unifier.mostGeneral(f4, f6)));
	}

	@Test
	public void Xc() {
		assertEquals("[X->c]", "" + X.firstReplacement(c));
	}

	@Test
	public void cX() {
		assertEquals("[X->c]", "" + c.firstReplacement(X));
	}

	@Test
	public void mostGeneralUnifier() {
		for (final Term t1 : ts)
			for (final Term t2 : ts) {
				System.out.println(t1);
				System.out.println(t2);
				final Iterable<Replacement> rs = Unifier.mostGeneral(t1, t2);
				System.out.println(rs);
				if (rs == null)
					continue;
				assertEquals(//
						"Cannot unify " + t1 + " with " + t2 + "\n\t" + // "
								"using " + Iterables.count(rs) + " rules: " + rs + "\n\t" + //
								"first replacement is=" + t1.firstReplacement(t2) + "\n\t" + //
								"", //
						t1.replace(rs), //
						t2.replace(rs) //
				);
			}
	}

	@Test
	public void notEquals() throws CloneNotSupportedException {
		for (final Term t : ts) {
			assertEquals(t, t);
			assertEquals("t:" + t + "t.clone():" + t.clone() + "are not equal", t, t.clone());
		}
		for (final Term t1 : ts)
			for (final Term t2 : ts)
				if (t1 == t2)
					assertEquals(t1, t2);
	}

	@Test
	public void noReplacementForEquals() {
		for (final Term t1 : ts)
			for (final Term t2 : ts)
				if (t1.equals(t2))
					assertNull(t1.firstReplacement(t2));
	}

	@Test
	public void firstReplacementWorks() {
		for (final Term t1 : ts)
			for (final Term t2 : ts) {
				final Replacement r = t1.firstReplacement(t2);
				if (r == null)
					continue;
				assertFalse(t1.equals(t1.replace(r)) && t2.equals(t2.replace(r)));
			}
	}

	@Test
	public void firstReplacementAllOptions() {
		for (final Term t1 : ts)
			for (final Term t2 : ts) {
				final Replacement r1st = t1.firstReplacement(t2);
				final String message = "Unifying t1: " + t1 + " t2: " + t2 + //
						" 1st=" + r1st + // ;
						(r1st == null ? "" : "\n\t t1/r=" + t1.replace(r1st) + " t2/r=" + t2.replace(r1st)) + //
						"\n";
				System.out.println(message);
				if (t1.equals(t2)) {
					assertNull(message, r1st);
					assertTrue(message, Iterables.isEmpty(Unifier.mostGeneral(t1, t2)));
					continue;
				}
				if (r1st == null) {
					assertNull(message, Unifier.mostGeneral(t1, t2));
					continue;
				}
				final Iterable<Replacement> rs = Unifier.mostGeneral(t1, t2);
				if (rs != null) {
					assertEquals(message, t1.replace(rs), t2.replace(rs));
					assertEquals(message, t2.replace(rs), t1.replace(rs));
					continue;
				}
				for (Term t1_ = t1, t2_ = t2;;) {
					final Replacement r = t1_.firstReplacement(t2_);
					if (r == null) {
						assertNull(message, Unifier.mostGeneral(t1_, t2_));
						assertNull(message, Unifier.mostGeneral(t2_, t1_));
						break;
					}
					t1_ = t1_.replace(r);
					t2_ = t2_.replace(r);
				}
			}
	}

	@Test
	public void ce() {
		assertNull(c.firstReplacement(e));
		assertNull(e.firstReplacement(c));
		assertNull(Unifier.mostGeneral(c, e));
		assertNull(Unifier.mostGeneral(e, c));
	}

	@Test
	public void c() throws CloneNotSupportedException {
		assertTrue(c.isAtom());
		assertFalse(c.isVariable());
		assertFalse(c.hasVariables());
		assertFalse(c.hasVariable(X));
		assertFalse(c.hasVariable(X_clone));
		assertFalse(c.hasVariable(Y));
		assertTrue(c.freeVariables().isEmpty());
		assertEquals(c, c.clone());
		assertEquals(c + "", "c");
		assertNotEquals(c, null);
	}

	@Test
	public void e() throws CloneNotSupportedException {
		assertFalse(e.isAtom());
		assertFalse(e.isVariable());
		assertFalse(e.hasVariables());
		assertFalse(e.hasVariable(X));
		assertFalse(e.hasVariable(X_clone));
		assertFalse(e.hasVariable(Y));
		assertEquals(0, e.freeVariables().size());
		assertEquals(e, e.clone());
		assertEquals("e", e + "");
		assertNotEquals(e, null);
	}

	@Test
	public void firstReplacement() {
		assertNull(p.firstReplacement(e));
		assertNull(p.firstReplacement(c));
		assertNull(p.firstReplacement(n));
		assertNull(p.firstReplacement(p));
		assertNull(p.firstReplacement(px));
		assertNull(p.firstReplacement(py));
		assertNull(p.firstReplacement(q));
		assertNull(p.firstReplacement(s));
		assertNull(p.firstReplacement(X));
		assertEquals("[X->p(Y,c,13)]", "" + py.firstReplacement(X));
		assertNotNull(py.firstReplacement(px));
		assertEquals("[Y->X]", "" + py.firstReplacement(px));
		assertEquals("[X->Y]", "" + px.firstReplacement(py));
	}

	@Test
	public void firstReplacementVariable() {
		assertNotNull(X.firstReplacement(c));
		assertNotNull(X.firstReplacement(s));
		assertNotNull(X.firstReplacement(n));
		assertNotNull(X.firstReplacement(e));
		assertNull(X.firstReplacement(p));
		assertNull(X.firstReplacement(q));
		assertNull(X.firstReplacement(px));
		assertNotNull(X.firstReplacement(py));
		assertEquals("[X->p(Y,c,13)]", "" + X.firstReplacement(py));
	}

	@Test
	public void n() throws CloneNotSupportedException {
		assertTrue(n.isAtom());
		assertFalse(n.isVariable());
		assertFalse(n.hasVariables());
		assertFalse(n.hasVariable(X));
		assertFalse(n.hasVariable(X_clone));
		assertFalse(n.hasVariable(Y));
		assertTrue(n.freeVariables().isEmpty());
		assertEquals(n, n.clone());
		assertEquals(n, n13);
		assertEquals(n + "", "13");
		assertNotEquals(n, null);
	}

	@Test
	public void p() throws CloneNotSupportedException {
		assertFalse(p.isAtom());
		assertFalse(p.isVariable());
		assertTrue(p.hasVariables());
		assertTrue(p.hasVariable(X));
		assertTrue(p.hasVariable(X_clone));
		assertFalse(p.hasVariable(Y));
		assertEquals(p.freeVariables().size(), 1);
		assertEquals(p, p.clone());
		assertEquals("p(X,\"a[i]\",c,13)", p + "");
		assertNotEquals(p, null);
	}

	@Test
	public void px() throws CloneNotSupportedException {
		assertFalse(px.isAtom());
		assertFalse(px.isVariable());
		assertTrue(px.hasVariables());
		assertTrue(px.hasVariable(X));
		assertTrue(px.hasVariable(X_clone));
		assertFalse(px.hasVariable(Y));
		assertEquals(px.freeVariables().size(), 1);
		assertEquals(px, px.clone());
		assertNotEquals(px, p.clone());
		assertNotEquals(px, p);
		assertEquals("p(X,c,13)", px + "");
		assertNotEquals(px, null);
	}

	@Test
	public void py() throws CloneNotSupportedException {
		assertFalse(py.isAtom());
		assertFalse(py.isVariable());
		assertTrue(py.hasVariables());
		assertFalse(py.hasVariable(X));
		assertFalse(py.hasVariable(X_clone));
		assertTrue(py.hasVariable(Y));
		assertEquals(py.freeVariables().size(), 1);
		assertEquals("p(Y,c,13)", py + "");
		assertEquals(py, py.clone());
		assertNotEquals(py, p);
		assertNotEquals(py, p.clone());
		assertNotEquals(py, null);
	}

	@Test
	public void q() throws CloneNotSupportedException {
		assertFalse(q.isAtom());
		assertFalse(q.isVariable());
		assertTrue(q.hasVariables());
		assertTrue(q.hasVariable(X));
		assertTrue(q.hasVariable(X_clone));
		assertFalse(q.hasVariable(Y));
		assertEquals(q.freeVariables().size(), 1);
		assertEquals(q, q.clone());
		assertEquals("q(p(X,\"a[i]\",c,13),X,\"a[i]\",c,13)", q + "");
		assertNotEquals(q, null);
	}

	@Test
	public void e2empty() {
		assertEquals("e.", e2empty + "");
		assertNotEquals(e2empty, null);
	}

	@Test
	public void r1() {
		assertNotEquals(r1, null);
		assertEquals("e :- p(X,\"a[i]\",c,13).", r1 + "");
	}

	@SuppressWarnings({ "static-method" }) //
	@Test(expected = IllegalArgumentException.class) //
	public void illegalSymbol() {
		new Symbol("");
	}

	@Test(expected = UnsupportedOperationException.class) //
	public void iterateOverEmpty_n() {
		n.iterator().next();
	}

	@Test(expected = UnsupportedOperationException.class) //
	public void iterateOverEmpty_s() {
		s.iterator().next();
	}

	@Test(expected = UnsupportedOperationException.class) //
	public void iterateOverEmpty_c() {
		c.iterator().next();
	}

	@Test
	public void r2() {
		assertNotEquals(r2, null);
		assertEquals("e :- p(X,c,13), e, p(Y,c,13).", r2 + "");
	}

	@Test
	public void Rx() {
		assertNotEquals(Rx, null);
		assertEquals("[X->p(X,c,13)]", Rx + "");
		assertFalse(Rx.legal());
		assertTrue(new Replacement(Y, p).legal());
	}

	@Test
	public void s() throws CloneNotSupportedException {
		assertTrue(s.isAtom());
		assertFalse(s.isVariable());
		assertFalse(s.hasVariables());
		assertFalse(s.hasVariable(X));
		assertFalse(s.hasVariable(X_clone));
		assertFalse(s.hasVariable(Y));
		assertTrue(s.freeVariables().isEmpty());
		assertEquals(s, s.clone());
		assertEquals(s + "", "\"a[i]\"");
		assertNotEquals(s, null);
		assertEquals(s, s);
	}

	@Test
	public void t() throws CloneNotSupportedException {
		final Term t = new Term() {
			@Override
			public Term clone() throws CloneNotSupportedException {
				return (Term) super.clone();
			}

			@Override
			public boolean hasVariable(final Variable v) {
				return false;
			}

			@Override
			public boolean isAtom() {
				return false;
			}

			@Override
			public Iterator<Term> iterator() {
				return Term.empty;
			}
		};
		assertNotEquals(t, null);
		assertEquals(t, t);
		assertFalse(t.isAtom());
		assertFalse(t.isVariable());
		assertFalse(t.hasVariables());
		assertFalse(t.hasVariable(X));
		assertFalse(t.hasVariable(X_clone));
		assertFalse(t.hasVariable(Y));
		assertNotEquals(t, X_clone);
		assertNotEquals(t, t.clone());
	}

	@Test
	public void X() throws CloneNotSupportedException {
		assertTrue(X.isAtom());
		assertTrue(X.isVariable());
		assertTrue(X.hasVariables());
		assertTrue(X.hasVariable(X));
		assertTrue(X.hasVariable(X_clone));
		assertFalse(X.hasVariable(Y));
		assertEquals(X, X_clone);
		assertEquals(X, X.clone());
		assertEquals("" + X, "X");
		assertNotEquals(X, null);
		assertEquals(X, X);
	}
}
