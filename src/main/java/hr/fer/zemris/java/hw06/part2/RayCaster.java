package hr.fer.zemris.java.hw06.part2;

import hr.fer.zemris.java.tecaj_06.rays.GraphicalObject;
import hr.fer.zemris.java.tecaj_06.rays.IRayTracerProducer;
import hr.fer.zemris.java.tecaj_06.rays.IRayTracerResultObserver;
import hr.fer.zemris.java.tecaj_06.rays.LightSource;
import hr.fer.zemris.java.tecaj_06.rays.Point3D;
import hr.fer.zemris.java.tecaj_06.rays.Ray;
import hr.fer.zemris.java.tecaj_06.rays.RayIntersection;
import hr.fer.zemris.java.tecaj_06.rays.RayTracerViewer;
import hr.fer.zemris.java.tecaj_06.rays.Scene;

/**
 * Class for running graphics.
 * @author Dario Vidas
 */
public class RayCaster {

	/**
	 * Main program for running graphics.
	 * @param args no arguments
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Method for creating tracer.
	 * @return tracer producer
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {

			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal,
					double vertical, int width, int height, long requestNo, IRayTracerResultObserver observer) {

				//initialize
				System.out.println("Započinjem izračune...");
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				//axis initialization
				Point3D vuv = viewUp.normalize();
				Point3D zAxis = view.sub(eye).normalize();
				Point3D yAxis = vuv.sub(zAxis.scalarMultiply(zAxis.scalarProduct(vuv))).normalize();
				Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();

				//top left corner initialization
				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2.0)).add(
						yAxis.scalarMultiply(vertical / 2.0));

				//scene initialization
				Scene scene = RayTracerViewer.createPredefinedScene();

				short[] rgb = new short[3];
				int offset = 0;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {

						Point3D screenPoint = screenCorner.add(
								xAxis.scalarMultiply(horizontal * x / (width - 1.0))).sub(
								yAxis.scalarMultiply(vertical * y / (height - 1.0)));

						Ray ray = Ray.fromPoints(eye, screenPoint);

						tracer(scene, ray, rgb);

						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];

						offset++;
					}
				}

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}

		};
	}

	/**
	 * Traces objects in scene and gets colors.
	 * @param scene scene
	 * @param ray ray
	 * @param rgb colors
	 */
	public static void tracer(Scene scene, Ray ray, short[] rgb) {
		RayIntersection closest = getClosestIntersection(scene, ray);
		if (closest == null) {
			rgb[0] = 0;
			rgb[1] = 0;
			rgb[2] = 0;
		} else {
			determineColor(scene, closest, ray, rgb);
		}
	}

	/**
	 * Method is called when ray intersects with object in scene. Method determines
	 * color.
	 * @param scene scene
	 * @param intersection intersection point with object in scene
	 * @param eye ray sent from eye
	 * @param rgb colors
	 */
	private static void determineColor(Scene scene, RayIntersection intersection, Ray eye, short[] rgb) {
		//ambient components
		rgb[0] = 15;
		rgb[1] = 15;
		rgb[2] = 15;

		final double distErr = 1e-9;

		for (LightSource source : scene.getLights()) {	//all light sources
			//creating light ray
			Ray lsRay = Ray.fromPoints(source.getPoint(), intersection.getPoint());

			RayIntersection closestInt = getClosestIntersection(scene, lsRay);

			if (closestInt != null) {
				//distance between light source and intersection of light source ray and closest object
				final double distLight = closestInt.getPoint().sub(source.getPoint()).norm();
				//distance between light source and intersection of eye ray and closest object
				final double distEyeInt = source.getPoint().sub(intersection.getPoint()).norm();

				if (Double.compare(distLight + distErr, distEyeInt) >= 0) {
					getComponents(lsRay, eye, closestInt, source, rgb);
				}
			}
		}
	}

	/**
	 * Method gets color component of light source on given intersection.
	 * @param light ray of light source
	 * @param eye ray of eye
	 * @param lsInter intersection of object and ray of light
	 * @param source light source
	 * @param rgb colors
	 */
	private static void getComponents(Ray light, Ray eye, RayIntersection lsInter, LightSource source,
			short[] rgb) {

		//diffuse component
		final Point3D lightVector = light.direction.negate().normalize();
		final Point3D normal = lsInter.getNormal().normalize();
		final double dif = Math.max(lightVector.scalarProduct(normal), 0);

		//reflection (mirror) component
		final Point3D reflected = normal.scalarMultiply(2.0 * lightVector.scalarProduct(normal))
				.sub(lightVector).normalize();		//page 23 in book
		final Point3D toEyeVector = eye.start.sub(lsInter.getPoint()).normalize();
		final double cos = toEyeVector.scalarProduct(reflected);
		double mir = 0;
		if (Double.compare(cos, 0) >= 0) {
			mir = Math.pow(cos, lsInter.getKrn());
		}

		rgb[0] += (short) ((dif * lsInter.getKdr() + mir * lsInter.getKrr()) * source.getR());
		rgb[1] += (short) ((dif * lsInter.getKdg() + mir * lsInter.getKrg()) * source.getG());
		rgb[2] += (short) ((dif * lsInter.getKdb() + mir * lsInter.getKrb()) * source.getB());
	}

	/**
	 * Runs through list of scene objects and gets intersection of closest one If
	 * there is no intersection returns null.
	 * @param scene scene
	 * @param ray ray
	 * @return intersection
	 */
	private static RayIntersection getClosestIntersection(Scene scene, Ray ray) {
		double minDistance = -1;
		RayIntersection closest = null;

		for (GraphicalObject object : scene.getObjects()) {
			RayIntersection lightInt = object.findClosestRayIntersection(ray);

			if (lightInt == null) {
				continue;
			}

			if (minDistance == -1) {
				minDistance = lightInt.getDistance();
				closest = lightInt;
				continue;
			}

			final double distance = lightInt.getDistance();
			if (distance < minDistance) {
				minDistance = distance;
				closest = lightInt;
			}
		}
		return closest;
	}
}
