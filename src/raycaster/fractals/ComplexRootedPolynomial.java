package hr.fer.zemris.java.hw06.part1;

/**
 * Immutable class for complex polynomials constructed with roots.
 * @author Dario Vidas
 */
public class ComplexRootedPolynomial {

	private Complex[] roots;

	/**
	 * Constructs complex polynom from given roots.
	 * @param roots roots
	 */
	public ComplexRootedPolynomial(Complex... roots) {
		super();
		this.roots = roots.clone();
	}

	/**
	 * Applies given complex number to polynom and extracts result.
	 * @param z complex number
	 * @return result
	 */
	public Complex apply(Complex z) {
		Complex temp = Complex.ONE;
		for (int i = 0; i < roots.length; i++) {
			temp = temp.multiply(z.sub(roots[i]));
		}
		return temp;
	}

	/**
	 * Converts this polynomial from <code>ComplexRootedPolynomial</code> to
	 * <code>ComplexPolynomial</code>.
	 * @return polynomial
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial polynomial = new ComplexPolynomial(Complex.ONE);
		for (int i = 0; i < roots.length; i++) {
			polynomial = polynomial.multiply(new ComplexPolynomial(roots[i].negate(), Complex.ONE));
		}
		return polynomial;
	}

	@Override
	public String toString() {
		StringBuilder build = new StringBuilder();
		for (int i = 0; i < roots.length; i++) {
			build.append("(z-(").append(roots[i]).append(")) ");
		}
		return build.toString();
	}

	/**
	 * Finds index of closest root for given complex number that is withing treshold.
	 * If there is no such root, returns -1.
	 * @param z complex number
	 * @param treshold treshold
	 * @return index
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		int index = -1;
		double minDistance = treshold;
		for (int i = 0; i < roots.length; i++) {
			double distance = roots[i].sub(z).module();
			if (distance <= minDistance) {
				minDistance = distance;
				index = i;
			}
		}
		return index;
	}

}
