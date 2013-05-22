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

public class Servlet extends servlet.Servlet
{
	private static final long serialVersionUID = 8303699064575138297L;

	private int simulationSpeed;

	private Map<String, Simulation> simulations;
	private Map<String, Thread> simulationThreads;

	public Servlet()
	{
		super();

		simulationSpeed = 100;

		simulations = new HashMap<String, Simulation>();
		simulationThreads = new HashMap<String, Thread>();

		LOGGER.fine("Created example servlet");
	}

	public void destroy()
	{
		super.destroy();

		try
		{
			for(String name : simulationThreads.keySet())
			{
				simulationThreads.get(name).join();
			}
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
			LOGGER.warning("Unable to end simulation thread " + e.getMessage());
		}

		simulationThreads.clear();
		simulations.clear();
	}

	protected void createConnection(String name)
	{
		super.createConnection(name);

		Simulation simulation = new Simulation();
		Thread simulationThread = new Thread(new SimulationRunner(name), "Simulation_Thread_" + name);

		simulations.put(name, simulation);
		simulationThreads.put(name, simulationThread);

		simulationThread.start();
	}

	protected void closeConnection(String name)
	{
		super.closeConnection(name);

		try
		{
			simulationThreads.get(name).join();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
			LOGGER.warning("Unable to end simulation thread " + name + " " + e.getMessage());
		}

		simulationThreads.remove(name);
		simulations.remove(name);
	}

	protected void doCommand(ObjectInputStream inputFromApplet, ObjectOutputStream outputToApplet, Object command, String name) throws IOException
	{
		if(!(command instanceof Command))
		{
			super.doCommand(inputFromApplet, outputToApplet, command, name);
		}

		Command c = (Command) command;
		switch(c)
		{
			case ADD_BOID:
			{
				LOGGER.log(Level.INFO, "adding boids");

				simulations.get(name).addBoid();

				// return true to signify success
				outputToApplet.writeObject(new Integer("1"));
				outputToApplet.writeObject(true);

				break;
			}
			case GET_BOIDS:
			{
				LOGGER.log(Level.INFO, "getting boids");

				outputToApplet.writeObject(new Integer("1"));
				outputToApplet.writeObject(simulations.get(name).getBoids());

				break;
			}
			case REMOVE_BOID:
			{
				LOGGER.log(Level.INFO, "removing boids");

				Integer index = null;
				try
				{
					index = (Integer) inputFromApplet.readObject();
				}
				catch(ClassNotFoundException e)
				{
					e.printStackTrace();
					LOGGER.warning("Problem with getting index for removing boid " + e.getMessage());

					// signify failure
					outputToApplet.writeObject(new Integer("-1"));
					outputToApplet.writeObject(Communication.MISSING_ARGUMENT_ERROR.toString() + ": Attempt to read logging message failed");
				}

				simulations.get(name).removeBoid(index);

				break;
			}
			default: // as in default response
			{
				LOGGER.log(Level.SEVERE, Communication.COMMAND_UNKNOWN_ERROR.toString() + " in example " + c);

				// signify failure
				outputToApplet.writeObject(new Integer("-1"));
				outputToApplet.writeObject(Communication.COMMAND_UNKNOWN_ERROR);

				break;
			}
		}
	}

	protected class SimulationRunner implements Runnable
	{
		private String name;

		public SimulationRunner(String n)
		{
			name = n;
		}

		@Override
		public void run()
		{
			LOGGER.fine("Entered run for " + name);
			while(!Thread.currentThread().isInterrupted())
			{
				LOGGER.fine("Began a step-through of the simulation for " + name);
				simulations.get(name).run();
				LOGGER.fine("Completed a step-through of the simulation for " + name);
				try
				{
					Thread.sleep(simulationSpeed);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
					LOGGER.warning("Simulation sleep thread interrupted for " + name + " " + e.getMessage());
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}
