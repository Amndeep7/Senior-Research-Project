package example.servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import example.shared.Command;
import example.servlet.Simulation;

import shared.Communication;

public class ExampleServlet extends servlet.Servlet {
	private static final long serialVersionUID = 8303699064575138297L;

	private int simulationSpeed;

	private Map<String, Simulation> simulations;
	private Map<String, SimulationThread> simulationThreads;

	public ExampleServlet() {
		super();

		simulationSpeed = 100;

		simulations = new HashMap<String, Simulation>();
		simulationThreads = new HashMap<String, SimulationThread>();

		LOGGER.fine("Created example servlet");
	}

	public void destroy() {
		super.destroy();
		LOGGER.fine("Trying to destroy servlet");
		for (String name : simulationThreads.keySet()) {
			closeConnection(name);
		}
	}

	protected void createConnection(String name) {
		super.createConnection(name);

		Simulation simulation = new Simulation();
		SimulationThread simulationThread = new SimulationThread(name);

		simulations.put(name, simulation);
		simulationThreads.put(name, simulationThread);

		simulationThread.start();
	}

	protected void closeConnection(String name) {
		super.closeConnection(name);

		try {
			LOGGER.fine("Trying to close " + name);
			simulationThreads.get(name).setRunState(false);
			simulationThreads.get(name).join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			LOGGER.warning("Unable to end simulation thread " + name + " "
					+ e.getMessage());
			Thread.currentThread().interrupt();
		}

		LOGGER.fine("Simulation thread size before = "
				+ simulationThreads.size() + " Simulation size before = "
				+ simulations.size());

		simulationThreads.remove(name);
		simulations.remove(name);

		LOGGER.fine("Simulation thread size after = "
				+ simulationThreads.size() + " Simulation size after = "
				+ simulations.size());
	}

	protected void doCommand(ObjectInputStream inputFromApplet,
			ObjectOutputStream outputToApplet, Object command, String name)
			throws IOException {
		if (!(command instanceof Command)) {
			super.doCommand(inputFromApplet, outputToApplet, command, name);
		}

		Command c = (Command) command;
		switch (c) {
		case ADD_BOID: {
			LOGGER.log(Level.INFO, "adding boids");

			simulations.get(name).addBoid();

			// return true to signify success
			outputToApplet.writeObject(new Integer("1"));
			outputToApplet.writeObject(true);

			break;
		}
		case GET_BOIDS: {
			LOGGER.log(Level.INFO, "getting boids");

			outputToApplet.writeObject(new Integer("1"));
			outputToApplet.writeObject(simulations.get(name).getBoids());
			
			// return true to signify success
			outputToApplet.writeObject(new Integer("1"));
			outputToApplet.writeObject(true);

			break;
		}
		case REMOVE_BOID: {
			LOGGER.log(Level.INFO, "removing boids");

			Integer index = null;
			try {
				index = (Integer) inputFromApplet.readObject();
				simulations.get(name).removeBoid(index);
				

				// return true to signify success
				outputToApplet.writeObject(new Integer("1"));
				outputToApplet.writeObject(true);

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				LOGGER.warning("Problem with getting index for removing boid "
						+ e.getMessage());

				// signify failure
				outputToApplet.writeObject(new Integer("-1"));
				outputToApplet.writeObject(Communication.MISSING_ARGUMENT_ERROR
						.toString()
						+ ": Attempt to read logging message failed");
			}

			break;
		}
		default: // as in default response
		{
			LOGGER.log(Level.SEVERE,
					Communication.COMMAND_UNKNOWN_ERROR.toString()
							+ " in example " + c);

			// signify failure
			outputToApplet.writeObject(new Integer("-1"));
			outputToApplet.writeObject(Communication.COMMAND_UNKNOWN_ERROR);

			break;
		}
		}
	}

	protected class SimulationThread extends Thread {
		private String name;
		private boolean shouldRun;

		public SimulationThread(String n) {
			name = n;
			shouldRun = true;
		}

		public void setRunState(boolean b) {
			shouldRun = b;
		}

		@Override
		public void run() {
			LOGGER.fine("Entered run for " + name);
			while (shouldRun) {
				LOGGER.fine("Began a step-through of the simulation for "
						+ name);
				simulations.get(name).run();
				LOGGER.fine("Completed a step-through of the simulation for "
						+ name);
				try {
					LOGGER.fine("Now sleeping " + name);
					Thread.sleep(simulationSpeed);
				} catch (InterruptedException e) {
					LOGGER.fine("Trying to interrupt");
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}
