package raycaster.fractals;

/**
 * Immutable class representing complex numbers.
 * @author Dario Vidas
 */
public class Complex {

	public static final Complex ZERO = new Complex(0, 0);
	public static final Complex ONE = new Complex(1, 0);
	public static final Complex ONE_NEG = new Complex(-1, 0);
	public static final Complex IM = new Complex(0, 1);
	public static final Complex IM_NEG = new Complex(0, -1);

	private double real;
	private double imag;

	/**
	 * Constructs complex number 0 + 0i.
	 */
	public Complex() {
		this(0, 0);
	}

	/**
	 * Constructs complex number from given arguments.
	 * @param re real part
	 * @param im imaginary part
	 */
	public Complex(double re, double im) {
		super();
		this.real = re;
		this.imag = im;
	}

	/**
	 * Calculates module of complex number.
	 * @return module
	 */
	public double module() {
		return Math.sqrt(real * real + imag * imag);
	}

	/**
	 * Multiplies this complex with given.
	 * @param c complex number
	 * @return new complex number
	 */
	public Complex multiply(Complex c) {
		final double tempRe = this.real * c.real - this.imag * c.imag;
		final double tempIm = this.imag * c.real + this.real * c.imag;
		return new Complex(tempRe, tempIm);
	}

	/**
	 * Divides this complex with given complex.
	 * @param c complex number
	 * @return new complex number
	 */
	public Complex divide(Complex c) {
		final double moduleSqr = c.real * c.real + c.imag * c.imag;
		final double tempRe = this.real * c.real + this.imag * c.imag;
		final double tempIm = this.imag * c.real - this.real * c.imag;
		return new Complex(tempRe / moduleSqr, tempIm / moduleSqr);
	}

	/**
	 * Adds given complex to this.
	 * @param c complex number
	 * @return new complex number
	 */
	public Complex add(Complex c) {
		return new Complex(this.real + c.real, this.imag + c.imag);
	}

	/**
	 * Subtracts given complex from this.
	 * @param c complex number
	 * @return new complex number
	 */
	public Complex sub(Complex c) {
		return new Complex(this.real - c.real, this.imag - c.imag);
	}

	/**
	 * Negates this complex number.
	 * @return new complex number
	 */
	public Complex negate() {
		return new Complex(-this.real, -this.imag);
	}

	@Override
	public String toString() {
		final char operator = this.imag >= 0 ? '+' : '-';
		return String.format("%.2f%ci%.2f", this.real, operator, Math.abs(this.imag));
	}

	/**
	 * Checks if complex number is equal to zero.
	 * @return <code>true</code> if it is, <code>false</code> otherwise
	 */
	public boolean isZero() {
		return this.real == 0 && this.imag == 0;
	}

	@Override
	public boolean equals(Object anObject) {
		if (!(anObject instanceof Complex)) {
			return false;
		}
		Complex temp = (Complex) anObject;
		return (Math.abs(this.real - temp.real) <= 1e-9) && (Math.abs(this.imag - temp.imag) <= 1e-9);
	}
	
	@Override
	public int hashCode() {
		return Double.valueOf(real).hashCode() + Double.valueOf(imag).hashCode();
	}

}
