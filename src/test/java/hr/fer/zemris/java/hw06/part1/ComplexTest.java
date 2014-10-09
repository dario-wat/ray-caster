package hr.fer.zemris.java.hw06.part1;

import junit.framework.Assert;

import org.junit.Test;

public class ComplexTest {

	@Test
	public void testModule() {
		Complex c = new Complex(1, -2);
		Assert.assertTrue("Module is incorrect", Math.abs(c.module() - Math.sqrt(5)) < 1e-9);
	}

	@Test
	public void testMultiply() {
		Complex c1 = new Complex(1, 3);
		Complex c2 = new Complex(-1, 1);
		Assert.assertEquals("Multiply is incorrect", new Complex(-4, -2), c1.multiply(c2));
	}

	@Test
	public void testDivide() {
		Complex c1 = new Complex(2, 3);
		Complex c2 = new Complex(4, -1);
		Assert.assertEquals("Divide is incorrect", new Complex(0.294117647058824, 0.823529411764706),
				c1.divide(c2));
	}
	
	@Test
	public void testAdd() {
		Complex c1 = new Complex(-2, 4);
		Complex c2 = new Complex(3, -1);
		Assert.assertEquals("Add is incorrect", new Complex(1, 3), c1.add(c2));
	}
	
	@Test
	public void testSub() {
		Complex c1 = new Complex(1, 9);
		Complex c2 = new Complex(-2, 5);
		Assert.assertEquals("Sub is incorrect", new Complex(3, 4), c1.sub(c2));
	}
	
	@Test
	public void testNegate() {
		Complex c = new Complex(-4, 2);
		Assert.assertEquals("Negate is incorrect", new Complex(4, -2), c.negate());
	}
	
	@Test
	public void testIsZero() {
		Complex c = new Complex();
		Assert.assertTrue(c.isZero());
	}
	
	@Test
	public void testToString() {
		Complex c = new Complex(-2, 4);
		Assert.assertEquals("toString does not work as expected", "-2,00+i4,00", c.toString());
	}

}
