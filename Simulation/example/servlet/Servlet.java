package example.servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;

import example.shared.Command;
import example.servlet.Simulation;

import shared.Communication;

public class Servlet extends servlet.Servlet
{
	private static final long serialVersionUID = 8303699064575138297L;

	protected Simulation simulation;

	protected int simulationSpeed;

	private final Thread simulationThread;

	public Servlet()
	{
		super();

		simulation = new Simulation();

		simulationSpeed = 50;

		LOGGER.fine("Created simulation");

		simulationThread = new Thread(new SimulationRunner(), "Simulation_Thread");
		simulationThread.start();

		LOGGER.fine("Created servlet and started simulation");
	}

	protected class SimulationRunner implements Runnable
	{
		public SimulationRunner()
		{
		}

		@Override
		public void run()
		{
			LOGGER.fine("Entered run");
			while(!Thread.currentThread().isInterrupted())
			{
				LOGGER.fine("Began a step-through of the simulation");
				simulation.run();
				LOGGER.fine("Completed a step-through of the simulation");
				try
				{
					Thread.sleep(simulationSpeed);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
					LOGGER.warning("Simulation sleep thread interrupted " + e.getMessage());
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	public void destroy()
	{
		super.destroy();

		try
		{
			simulationThread.join();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
			LOGGER.warning("Unable to end simulation thread " + e.getMessage());
		}
	}

	public void doCommand(ObjectInputStream inputFromApplet, ObjectOutputStream outputToApplet, Object command) throws IOException
	{
		if(!(command instanceof Command))
		{
			super.doCommand(inputFromApplet, outputToApplet, command);
		}

		Command c = (Command) command;
		switch(c)
		{
			case ADD_CAR:
			{
				LOGGER.log(Level.INFO, "adding cars");

				simulation.addCar();

				// return true to signify success
				outputToApplet.writeObject(new Integer("1"));
				outputToApplet.writeObject(true);

				break;
			}
			case GET_CARS:
			{
				LOGGER.log(Level.INFO, "getting cars");

				outputToApplet.writeObject(new Integer("1"));
				outputToApplet.writeObject(simulation.getCars());

				break;
			}
			default: // as in default response
			{
				LOGGER.log(Level.INFO, "command not found: " + c);

				// signify failure
				outputToApplet.writeObject(new Integer("-1"));
				outputToApplet.writeObject(Communication.COMMAND_UNKNOWN_ERROR);

				break;
			}
		}
	}
}
