package controller;

import model.Model;
import model.ModelImpl;

/**
 * Class designed to start the program.
 *
 * @author
 *
 */

public final class Application {

	private Application() {
	}

	/**
	 *
	 * @param args
	 *            Arguments passed to the main method
	 */
	public static void main(final String[] args) {
		// application starter
		final Model m = new ModelImpl();
		final Controller c = new ControllerImpl(m);
		// things to implement
		// final View v = new ViewImpl(c);
		// c.setView(v);
		// v.startView();
	}

}
