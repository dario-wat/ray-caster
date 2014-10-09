package hr.fer.zemris.java.hw06.part1;

import hr.fer.zemris.java.tecaj_06.fractals.FractalViewer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for generating and drawing Newton fractals.
 * @author Dario Vidas
 */
public class Newton {

	/**
	 * Main program. Reads roots and calls method for drawing plane.
	 * @param args no arguments
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

		Complex[] roots = null;
		try {
			roots = loadRoots();
		} catch (IOException | IllegalArgumentException e) {
			System.out.println(e.getLocalizedMessage());
			System.exit(0);
		}

		ComplexRootedPolynomial rootedPolynomial = new ComplexRootedPolynomial(roots);
		System.out.println("Image of fractal will appear shortly. Thank you.");
		FractalViewer.show(new Generate(rootedPolynomial));
	}

	/**
	 * Reads complex numbers from stdin, parses them if possible and returns array of
	 * complex.
	 * @return array of read complex numbers
	 * @throws IOException error while reading
	 */
	private static Complex[] loadRoots() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

		List<Complex> rootList = new ArrayList<>();
		int i = 0;
		while (true) {
			i++;
			System.out.print("Root " + i + "> ");

			//reading lines
			String line = reader.readLine();
			if (line == null || line.equals("done")) {	//reason to exit
				break;
			}

			//converting strings into complex if possible and saving
			Complex root = getComplex(line);
			if (root == null) {
				System.out.println("Incorrect input: " + line);
				i--;
				continue;
			}
			rootList.add(root);
		}

		if (rootList.size() < 2) {
			throw new IllegalArgumentException("Expected at least two roots!");
		}

		reader.close();

		//list to array, casting problems
		Complex[] array = new Complex[rootList.size()];
		for (int j = 0; j < array.length; j++) {
			array[j] = rootList.get(j);
		}
		return array;
	}

	/**
	 * Tries to parse string to complex number. If string can be parsed to complex
	 * returns complex, else returns null.
	 * @param line string
	 * @return complex number
	 */
	private static Complex getComplex(String line) {
		String[] splitted = line.split("[ ]+");
		if (splitted.length != 1 && splitted.length != 3 || line.isEmpty()) {
			return null;
		}

		if (splitted.length == 1) {		//checking when only 1 component is given
			if (splitted[0].charAt(0) == 'i') {
				if (splitted[0].length() == 1) {
					return new Complex(0, 1);
				}
				Double component = getDouble(splitted[0].substring(1));
				if (component != null) {
					return new Complex(0, component);
				}
			} else {
				Double component = getDouble(splitted[0]);
				if (component != null) {
					return new Complex(component, 0);
				}
			}
			return null;
		}

		//checking when both components are given
		if (splitted[2].charAt(0) != 'i') {
			return null;
		}

		Double real = getDouble(splitted[0]);
		Double imag = null;
		if (splitted[2].length() == 1) {
			imag = (double) 1;
		} else {
			imag = getDouble(splitted[2].substring(1));
		}

		if (real == null || imag == null) {
			return null;
		}

		if (splitted[1].length() == 1) {
			if (splitted[1].charAt(0) == '-') {
				imag *= -1;
			} else if (splitted[1].charAt(0) != '+') {
				return null;
			}
			return new Complex(real, imag);
		}
		return null;
	}

	/**
	 * Tries parsing string to double. If string can be parsed to double returns
	 * double value, else returns null.
	 * @param string string
	 * @return double value
	 */
	private static Double getDouble(String string) {
		double number;
		try {
			number = Double.parseDouble(string);
		} catch (NumberFormatException e) {
			return null;
		}
		return number;
	}
}
