package raycaster.rays;

/**
 * Class defines sphere scene object.
 * @author Dario Vidas
 */
public class Sphere extends GraphicalObject {

	private Point3D center;
	private double radius;
	private double kdr;
	private double kdg;
	private double kdb;
	private double krr;
	private double krg;
	private double krb;
	private double krn;

	/**
	 * Constructor for sphere.
	 * @param center center of sphere
	 * @param radius sphere radius
	 * @param kdr diffuse red
	 * @param kdg diffuse green
	 * @param kdb diffuse blue
	 * @param krr reflection red
	 * @param krg reflection green
	 * @param krb reflection blue
	 * @param krn reflection n
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg,
			double krb, double krn) {
		super();
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * hr.fer.zemris.java.tecaj_06.rays.GraphicalObject#findClosestRayIntersection
	 * (hr.fer.zemris.java.tecaj_06.rays.Ray) Used sphere equation (x-x0)^2 +
	 * (y-y0)^2 + (z-z0)^2 = R^2. Used parametric linear equation p = s + d*t where p
	 * is point vector (x,y,z), s is start vector, d is directional vector and t is
	 * parameter.
	 */
	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		//for shorter writing
		final Point3D s = ray.start;
		final Point3D d = ray.direction;

		//for shorter writing and faster execute
		final double xr = s.x - center.x;
		final double yr = s.y - center.y;
		final double zr = s.z - center.z;

		//coefficients for quadratic equation
		final double coefA = d.x * d.x + d.y * d.y + d.z * d.z;
		final double coefB = 2 * (d.x * xr + d.y * yr + d.z * zr);
		final double coefC = xr * xr + yr * yr + zr * zr - radius * radius;

		final double discriminant = coefB * coefB - 4 * coefA * coefC;
		if (discriminant < 0) {		//are roots real
			return null;
		}

		//calculating q for roots
		double discSqrt = Math.sqrt(coefB * coefB - 4 * coefA * coefC);
		double q;
		if (coefB < 0) {
			q = (-coefB + discSqrt) / 2;
		} else {
			q = (-coefB - discSqrt) / 2;
		}

		//calculating roots obviously
		double root0 = q / coefA;
		double root1 = coefC / q;
		if (root0 > root1) {	//making root0 smaller
			double temp = root0;
			root0 = root1;
			root1 = temp;
		}

		if (root1 < 0) {		//both are negative, missed
			return null;
		}

		boolean outer;
		double root;
		if (root0 < 0) {		//deciding which root to use
			root = root1;
			outer = false;
		} else {
			root = root0;
			outer = true;
		}

		//creating intersection points
		final Point3D closestInter = new Point3D(s.x + root * d.x, s.y + root * d.y, s.z + root * d.z);

		return new RayIntersection(closestInter, closestInter.sub(s).norm(), outer) {

			@Override
			public Point3D getNormal() {
				return getPoint().sub(center).normalize();
			}

			@Override
			public double getKrr() {
				return krr;
			}

			@Override
			public double getKrn() {
				return krn;
			}

			@Override
			public double getKrg() {
				return krg;
			}

			@Override
			public double getKrb() {
				return krb;
			}

			@Override
			public double getKdr() {
				return kdr;
			}

			@Override
			public double getKdg() {
				return kdg;
			}

			@Override
			public double getKdb() {
				return kdb;
			}
		};
	}
}
