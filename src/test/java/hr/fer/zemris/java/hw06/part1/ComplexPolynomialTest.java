package hr.fer.zemris.java.hw06.part1;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComplexPolynomialTest {

	@Test
	public void testOrder() {
		ComplexPolynomial p = new ComplexPolynomial(new Complex(2, 1), new Complex(), new Complex(1, 9));
		assertEquals("Order is incorrect", 2, p.order());
	}

	@Test
	public void testMultiply() {
		ComplexPolynomial p1 = new ComplexPolynomial(new Complex(1, 2), new Complex(3, -1));
		ComplexPolynomial p2 = new ComplexPolynomial(new Complex(1, 1), new Complex(-2, 2));
		assertEquals("multiply is incorrect", "(-4,00+i8,00)z^2 + (-2,00+i0,00)z^1 + (-1,00+i3,00)z^0", p1
				.multiply(p2).toString());
	}

	@Test
	public void testDerive() {
		ComplexPolynomial p = new ComplexPolynomial(new Complex(1, 2), new Complex(4, 2), new Complex(-2, 1));
		assertEquals("derive is incorrect", "(-4,00+i2,00)z^1 + (4,00+i2,00)z^0", p.derive().toString());
	}
	
	@Test
	public void testApply() {
		ComplexPolynomial p = new ComplexPolynomial(new Complex(1, 2), new Complex(4, 2), new Complex(-2, 1));
		Complex result = p.apply(new Complex(2, 0));
		assertEquals("apply is incorrect", new Complex(1, 10), result);
	}
}
