package raycaster.fractals;

/**
 * Class that hold method for calculating Newton fractals.
 * @author Dario Vidas
 */
public class NewtonFractals {

	/**
	 * Calculates array for drawing the plane using Newton fractals.
	 * @param remin real min
	 * @param remax real max
	 * @param immin imaginary min
	 * @param immax imaginary max
	 * @param width plane width
	 * @param height plane height
	 * @param ymin min y for drawing
	 * @param ymax max y for drawing
	 * @param polynomial rooted polynomial
	 * @param limit iteration limit
	 * @param convergenceTreshold treshold limit, |zn - zn1|
	 * @param rootTreshold treshold for finding closest root
	 * @param data data for drawing the plane
	 */
	public static void calculate(double remin, double remax, double immin, double immax, int width,
			int height, int ymin, int ymax, ComplexRootedPolynomial polynomial, int limit,
			double convergenceTreshold, double rootTreshold, short[] data) {

		int offset = ymin * width;
		ComplexPolynomial derivative = polynomial.toComplexPolynom().derive();

		//using the given algorithm
		for (int y = ymin; y <= ymax; y++) {
			for (int x = 0; x < width; x++) {

				final double cre = x * (remax - remin) / (width - 1) + remin;
				final double cim = (height - 1 - y) * (immax - immin) / (height - 1) + immin;
				Complex zn = new Complex(cre, cim);
				short iteration = 0;

				Complex zn1;
				double module;
				do {	//algorithm says this
					Complex numerator = polynomial.apply(zn);
					Complex denominator = derivative.apply(zn);
					Complex fraction = numerator.divide(denominator);
					zn1 = zn.sub(fraction);

					module = zn.sub(zn1).module();
					iteration++;
					zn = zn1;
				} while (iteration < limit && module > convergenceTreshold);

				int index = polynomial.indexOfClosestRootFor(zn1, rootTreshold);
				data[offset] = (short) (index + 1);
				offset++;
			}
		}
	}
}
