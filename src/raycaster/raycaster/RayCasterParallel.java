package raycaster.raycaster;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.tecaj_06.rays.IRayTracerProducer;
import hr.fer.zemris.java.tecaj_06.rays.IRayTracerResultObserver;
import hr.fer.zemris.java.tecaj_06.rays.Point3D;
import hr.fer.zemris.java.tecaj_06.rays.Ray;
import hr.fer.zemris.java.tecaj_06.rays.RayTracerViewer;
import hr.fer.zemris.java.tecaj_06.rays.Scene;

/**
 * Class for RayCaster paraller running.
 * @author Dario Vidas
 */
public class RayCasterParallel {

	/**
	 * Main program. Starts graphics.
	 * @param args no arguments
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Crates tracer producer that runs computation in parallel.
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

				//starting multithread
				ForkJoinPool pool = new ForkJoinPool();

				pool.invoke(new ThreadTask(eye, horizontal, vertical, width, height, 0, height - 1, xAxis,
						yAxis, screenCorner, scene, red, green, blue));

				pool.shutdown();

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}

	/**
	 * Class that defines task for thread.
	 * @author Dario Vidas
	 */
	static class ThreadTask extends RecursiveAction {

		private static final long serialVersionUID = 1L;
		private Point3D eye;
		private double horizontal;
		private double vertical;
		private int width;
		private int height;
		private int ymin;
		private int ymax;
		private Point3D xAxis;
		private Point3D yAxis;
		private Point3D screenCorner;
		private Scene scene;
		private short[] red;
		private short[] green;
		private short[] blue;
		private static final int LIMIT = 32;

		/**
		 * Constructor for task.
		 * @param eye eye point
		 * @param horizontal horizontal size
		 * @param vertical vertical size
		 * @param width width of screen
		 * @param height height of screen
		 * @param ymin starting y
		 * @param ymax ending y
		 * @param xAxis x axis vector
		 * @param yAxis y axis vector
		 * @param screenCorner screen corner point
		 * @param scene scene
		 * @param red color red array
		 * @param green color green array
		 * @param blue color blue array
		 */
		public ThreadTask(Point3D eye, double horizontal, double vertical, int width, int height, int ymin,
				int ymax, Point3D xAxis, Point3D yAxis, Point3D screenCorner, Scene scene, short[] red,
				short[] green, short[] blue) {
			super();
			this.eye = eye;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.width = width;
			this.height = height;
			this.ymin = ymin;
			this.ymax = ymax;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.screenCorner = screenCorner;
			this.scene = scene;
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

		@Override
		protected void compute() {
			int rowCount = ymax - ymin + 1;
			if (rowCount < LIMIT) {
				parallelTracer(eye, horizontal, vertical, width, height, ymin, ymax, xAxis, yAxis,
						screenCorner, scene, red, green, blue);
			} else {
				invokeAll(new ThreadTask(eye, horizontal, vertical, width, height, ymin, (ymin + ymax) / 2,
							xAxis, yAxis, screenCorner, scene, red, green, blue),
						new ThreadTask(eye, horizontal,	vertical, width, height, (ymin + ymax) / 2 + 1, ymax,
							xAxis, yAxis, screenCorner, scene, red, green, blue)
				);
			}
		}

	}

	/**
	 * Method for parallel tracer running.
	 * @param eye eye point
	 * @param horizontal horizontal size
	 * @param vertical vertical size
	 * @param width width of screen
	 * @param height height of screen
	 * @param ymin starting y
	 * @param ymax ending y
	 * @param xAxis x axis vector
	 * @param yAxis y axis vector
	 * @param screenCorner screen corner point
	 * @param scene scene
	 * @param red color red array
	 * @param green color green array
	 * @param blue color blue array
	 */
	private static void parallelTracer(Point3D eye, double horizontal, double vertical, int width,
			int height, int ymin, int ymax, Point3D xAxis, Point3D yAxis, Point3D screenCorner, Scene scene,
			short[] red, short[] green, short[] blue) {

		short[] rgb = new short[3];
		int offset = ymin * width;
		for (int y = ymin; y <= ymax; y++) {
			for (int x = 0; x < width; x++) {

				Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply(horizontal * x / (width - 1.0)))
						.sub(yAxis.scalarMultiply(vertical * y / (height - 1.0)));

				Ray ray = Ray.fromPoints(eye, screenPoint);

				RayCaster.tracer(scene, ray, rgb);

				red[offset] = rgb[0] > 255 ? 255 : rgb[0];
				green[offset] = rgb[1] > 255 ? 255 : rgb[1];
				blue[offset] = rgb[2] > 255 ? 255 : rgb[2];

				offset++;
			}
		}
	}
}
