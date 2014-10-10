package hr.fer.zemris.java.hw06.part1;

import hr.fer.zemris.java.tecaj_06.fractals.IFractalProducer;
import hr.fer.zemris.java.tecaj_06.fractals.IFractalResultObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Class implements method which produces data for drawing plane.
 * @author Dario Vidas
 */
public class Generate implements IFractalProducer {

	private ComplexRootedPolynomial rootedPolynomial;
	private ExecutorService service;
	private static final int DIVIDED = 8;

	/**
	 * Constructor. Generates service for running threads.
	 * @param rootedPolynomial polynomial
	 */
	public Generate(ComplexRootedPolynomial rootedPolynomial) {
		super();
		this.rootedPolynomial = rootedPolynomial;
		this.service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
				new DaemonicThreadFactory());	//daemon threads so it dies when graphics closes
	}

	@Override
	public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
			long requestNo, IFractalResultObserver observer) {

		final int iterations = 16 * 16;
		final double convTreshold = 1e-3;
		final double rootTreshold = 1e-3;
		short[] data = new short[width * height];
		ComplexPolynomial polynomial = rootedPolynomial.toComplexPolynom();

		//filling list with tasks
		List<ThreadTask> tasks = new ArrayList<>();
		int singleHeight = height / (DIVIDED * Runtime.getRuntime().availableProcessors()) + 1;
		int ymin = 0;
		while (true) {
			int ymax = Math.min(ymin + singleHeight - 1, height - 1);
			tasks.add(new ThreadTask(reMin, reMax, imMin, imMax, width, height, ymin, ymax, rootedPolynomial,
					iterations, convTreshold, rootTreshold, data));
			ymin = ymax + 1;
			if (ymin >= height) {
				break;
			}
		}

		//starts all tasks
		List<Future<Void>> results = null;
		try {
			results = service.invokeAll(tasks);
		} catch (InterruptedException e) {
			System.out.println("Task was interrupted! Exiting...");
			return;
		}

		//waits for all tasks to finish
		for (Future<Void> result : results) {
			try {
				result.get();
			} catch (InterruptedException | ExecutionException e) {
				System.out.println("Error while executing! Exiting...");
				return;
			}
		}

		observer.acceptResult(data, (short) (polynomial.order() + 1), requestNo);
	}
}
