package servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shared.Command;
import shared.Communication;

public class Servlet extends HttpServlet
{
	private static final long serialVersionUID = 6938369357587229915L;

	protected Logger LOGGER;

	public Servlet()
	{
		super();

		LOGGER = Logger.getLogger(Servlet.class.getName());
		LOGGER.setLevel(Level.ALL);
		LOGGER.addHandler(new ConsoleHandler());

		LOGGER.fine("Created servlet.Servlet and loggers");
	}

	private void closeInput(ObjectInputStream inputFromApplet)
	{
		try
		{
			inputFromApplet.close();
		}
		catch(IOException e)
		{
			LOGGER.warning("Unable to close input stream " + e.getMessage());
		}
	}

	private void closeOutput(ObjectOutputStream outputToApplet)
	{
		try
		{
			outputToApplet.flush();
			outputToApplet.close();
		}
		catch(IOException e)
		{
			LOGGER.warning("Unable to close output stream " + e.getMessage());
		}
	}

	protected void createConnection(String name)
	{
	}

	protected void closeConnection(String name)
	{
	}

	protected void doCommand(ObjectInputStream inputFromApplet, ObjectOutputStream outputToApplet, Object command, String name) throws IOException
	{
		if(!(command instanceof Command))
		{
			LOGGER.log(Level.SEVERE, "Did not receive shared.Command in doCommand from servlet.Servlet");
			return;
		}

		Command c = (Command) command;
		switch(c)
		{
			case CREATE_CONNECTION:
			{
				LOGGER.log(Level.FINE, "creating a connection with an applet");

				createConnection(name);

				LOGGER.log(Level.FINE, "created a connection with " + name);

				outputToApplet.writeObject(new Integer("1"));
				outputToApplet.writeObject(true);

				break;
			}
			case CLOSE_CONNECTION:
			{
				LOGGER.log(Level.FINE, "destroying a connection with an applet");

				closeConnection(name);

				LOGGER.log(Level.FINE, "destroyed a connection with " + name);

				outputToApplet.writeObject(new Integer("1"));
				outputToApplet.writeObject(true);

				break;
			}
			case LOG:
			{
				LOGGER.log(Level.INFO, "logging message from applet");

				Level level = null;
				try
				{
					level = (Level) inputFromApplet.readObject();
				}
				catch(ClassNotFoundException | IOException e)
				{
					LOGGER.warning("Problem with reading in the logging level from " + name + " " + e.getMessage());

					// signify failure
					outputToApplet.writeObject(new Integer("-1"));
					outputToApplet.writeObject(Communication.MISSING_ARGUMENT_ERROR.toString() + ": Attempt to read logging level failed");

					break;
				}

				String message = null;
				try
				{
					message = (String) inputFromApplet.readObject();
				}
				catch(ClassNotFoundException | IOException e)
				{
					e.printStackTrace();
					LOGGER.warning("Problem with reading in the message from " + name + " " + e.getMessage());

					// signify failure
					outputToApplet.writeObject(new Integer("-1"));
					outputToApplet.writeObject(Communication.MISSING_ARGUMENT_ERROR.toString() + ": Attempt to read logging message failed");

					break;
				}

				LOGGER.log(level, message);

				// return true to signify success
				outputToApplet.writeObject(new Integer("1"));
				outputToApplet.writeObject(true);

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

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		response.setContentType("application/x-java-serialized-object");
		ObjectInputStream inputFromApplet = null;
		ObjectOutputStream outputToApplet = null;
		try
		{
			inputFromApplet = new ObjectInputStream(request.getInputStream());
			LOGGER.fine("created input streams");

			outputToApplet = new ObjectOutputStream(response.getOutputStream());
			LOGGER.fine("created output streams");

			Object c = inputFromApplet.readObject();
			LOGGER.fine("Received " + c + " command");

			String name = (String) inputFromApplet.readObject();
			LOGGER.fine("Received from " + name);

			doCommand(inputFromApplet, outputToApplet, c, name);
		}
		catch(IOException | ClassNotFoundException e)
		{
			LOGGER.warning(e.getMessage());
		}
		finally
		{
			closeInput(inputFromApplet);
			closeOutput(outputToApplet);
		}
	}
}
