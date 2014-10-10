package raycaster.fractals;

/**
 * Immutable class for creating and using complex polynomials constructed with
 * coefficients.
 * @author Dario Vidas
 */
public class ComplexPolynomial {

	private Complex[] factors;

	/**
	 * Constructs polynomial from given factors (coefficients) where the first
	 * element has exponent 0, next 1, etc (array's index represents exponent).
	 * @param factors factors
	 */
	public ComplexPolynomial(Complex... factors) {
		super();
		this.factors = factors.clone();
	}

	/**
	 * Returns order of polynomial.
	 * @return order
	 */
	public short order() {
		return (short) (factors.length - 1);
	}

	/**
	 * Multiplies this polynomial with given.
	 * @param p polynomial
	 * @return new polynomial
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		final short orderThis = this.order();
		final short orderP = p.order();

		Complex[] nPoly = zeros(orderThis + orderP + 1);
		for (int i = orderThis; i >= 0; i--) {
			for (int j = orderP; j >= 0; j--) {
				nPoly[i + j] = nPoly[i + j].add(factors[i].multiply(p.factors[j]));
			}
		}
		return new ComplexPolynomial(nPoly);
	}

	/**
	 * Helper method, creates polynomial with zeros.
	 * @param size size
	 * @return polynomial
	 */
	private Complex[] zeros(int size) {
		Complex[] zeroPolynomial = new Complex[size];
		for (int i = 0; i < size; i++) {
			zeroPolynomial[i] = Complex.ZERO;
		}
		return zeroPolynomial;
	}

	/**
	 * Computes first derivative of this polynomial.
	 * @return derivative
	 */
	public ComplexPolynomial derive() {
		final short derLength = (short) (factors.length - 1);
		Complex[] derivative = zeros(derLength);
		for (int i = 1; i < derLength + 1; i++) {
			derivative[i - 1] = factors[i].multiply(new Complex(i, 0));
		}
		return new ComplexPolynomial(derivative);
	}

	/**
	 * Calculates polynomial value at given point.
	 * @param z point
	 * @return result
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ZERO;
		for (int i = 0; i < factors.length; i++) {
			result = result.add(factors[i].multiply(powerOf(z, i)));
		}
		return result;
	}

	/**
	 * Helper method that calculates complex to the power of.
	 * @param z complex number
	 * @param power power
	 * @return new complex number
	 */
	private static Complex powerOf(Complex z, int power) {
		Complex result = Complex.ONE;
		for (int i = 0; i < power; i++) {
			result = result.multiply(z);
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder build = new StringBuilder();
		build.append('(').append(factors[factors.length - 1].toString()).append(")z^").append(this.order());

		for (int i = factors.length - 2; i >= 0; i--) {
			if (factors[i].isZero()) {
				continue;
			}
			build.append(" + (").append(factors[i].toString()).append(")z^").append(i);
		}
		return build.toString();
	}

}
