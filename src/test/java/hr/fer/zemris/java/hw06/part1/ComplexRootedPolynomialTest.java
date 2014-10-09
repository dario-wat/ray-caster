package hr.fer.zemris.java.hw06.part1;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexRootedPolynomialTest {

	@Test
	public void testApply() {
		ComplexRootedPolynomial p = new ComplexRootedPolynomial(new Complex(1, 0), new Complex(-1, 0),
				new Complex(0, 1), new Complex(0, -1));
		assertEquals("Apply is incorrect", new Complex(15, 0), p.apply(new Complex(2, 0)));
	}

	@Test
	public void testToComplexPolynom() {
		ComplexRootedPolynomial p = new ComplexRootedPolynomial(new Complex(2, -1), new Complex(-4, 1));
		ComplexPolynomial cp = p.toComplexPolynom();
		assertEquals("toComplexPolynom is incorrect", "(1,00+i0,00)z^2 + (2,00+i0,00)z^1 + (-7,00+i6,00)z^0",
				cp.toString());
	}
	
	@Test
	public void testIndexOfClosestRootInsideTreshold() {
		ComplexRootedPolynomial p = new ComplexRootedPolynomial(new Complex(1, 0), new Complex(-1, 0),
				new Complex(0, 1), new Complex(0, -1));
		int index = p.indexOfClosestRootFor(new Complex(0.995, 0), 0.1);
		assertEquals("indexOfClosestRoot is incorrect", 0, index);
	}
	
	@Test
	public void testIndexOfClosestRootOutsideTreshold() {
		ComplexRootedPolynomial p = new ComplexRootedPolynomial(new Complex(2, -1), new Complex(-4, 1));
		int index = p.indexOfClosestRootFor(new Complex(6, 1), 0.1);
		assertEquals("indexOfClosestRoot is incorrect", -1, index);
	}

	@Test
	public void testToString() {
		ComplexRootedPolynomial p = new ComplexRootedPolynomial(new Complex(2, -1), new Complex(-4, 1));
		assertEquals("toString does not work as expected", "(z-(2,00-i1,00)) (z-(-4,00+i1,00)) ", p.toString());
	}
}
