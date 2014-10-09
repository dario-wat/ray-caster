package hr.fer.zemris.java.hw06.part1;

import java.util.concurrent.ThreadFactory;

/**
 * Class that represents daemon type threads.
 * @author Dario Vidas
 */
public class DaemonicThreadFactory implements ThreadFactory {

	@Override
	public Thread newThread(Runnable r) {
		Thread tempThread = new Thread(r);
		tempThread.setDaemon(true);
		return tempThread;
	}

}
