package raycaster.fractals;

import java.util.concurrent.Callable;

/**
 * Class that defines thread jobs.
 * @author Dario Vidas
 */
public class ThreadTask implements Callable<Void> {

	private double reMin;
	private double reMax;
	private double imMin;
	private double imMax;
	private int width;
	private int height;
	private int ymin;
	private int ymax;
	private ComplexRootedPolynomial rootedPolynomial;
	private int maxIter;
	private double convTreshold;
	private double rootTreshold;
	private short[] data;

	/**
	 * Constructor.
	 * @param reMin real min
	 * @param reMax real max
	 * @param imMin imaginary min
	 * @param imMax imaginary max
	 * @param width plane width
	 * @param height plane height
	 * @param ymin min y for drawing
	 * @param ymax max y for drawing
	 * @param rootedPolynomial rooted polynomial
	 * @param maxIter iteration limit
	 * @param convTreshold treshold limit, |zn - zn1|
	 * @param rootTreshold treshold for finding closest root
	 * @param data data for drawing the plane
	 */
	public ThreadTask(double reMin, double reMax, double imMin, double imMax, int width, int height,
			int ymin, int ymax, ComplexRootedPolynomial rootedPolynomial, int maxIter, double convTreshold,
			double rootTreshold, short[] data) {
		super();
		this.reMin = reMin;
		this.reMax = reMax;
		this.imMin = imMin;
		this.imMax = imMax;
		this.width = width;
		this.height = height;
		this.ymin = ymin;
		this.ymax = ymax;
		this.rootedPolynomial = rootedPolynomial;
		this.maxIter = maxIter;
		this.convTreshold = convTreshold;
		this.rootTreshold = rootTreshold;
		this.data = data;
	}

	@Override
	public Void call() {
		NewtonFractals.calculate(reMin, reMax, imMin, imMax, width, height, ymin, ymax, rootedPolynomial,
				maxIter, convTreshold, rootTreshold, data);
		return null;
	}

}
