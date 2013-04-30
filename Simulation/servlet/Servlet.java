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

	protected static Logger LOGGER;

	public Servlet()
	{
		super();

		LOGGER = Logger.getLogger(Servlet.class.getName());
		LOGGER.setLevel(Level.ALL);
		LOGGER.addHandler(new ConsoleHandler());

		LOGGER.fine("Created servlet.Servlet and loggers");
	}

	public void closeInput(ObjectInputStream ob)
	{
		try
		{
			ob.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			LOGGER.warning("Unable to close input stream " + e.getMessage());
		}
	}

	public void closeOutput(ObjectOutputStream outputToApplet)
	{
		try
		{
			outputToApplet.flush();
			outputToApplet.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			LOGGER.warning("Unable to close output stream " + e.getMessage());
		}
	}

	public void doCommand(ObjectInputStream inputFromApplet, ObjectOutputStream outputToApplet, Object command) throws IOException
	{
		if(!(command instanceof Command))
		{
			LOGGER.log(Level.SEVERE, "Did not receive shared.Command in doCommand from servlet.Servlet");
			return;
		}

		Command c = (Command) command;
		switch(c)
		{
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
					e.printStackTrace();
					LOGGER.warning("Problem with reading in the logging level from the applet " + e.getMessage());

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
					LOGGER.warning("Problem with reading in the message from the applet " + e.getMessage());

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

			Object c = inputFromApplet.readObject();
			LOGGER.fine("Received " + c + " command");

			outputToApplet = new ObjectOutputStream(response.getOutputStream());
			LOGGER.fine("created output streams");

			doCommand(inputFromApplet, outputToApplet, c);
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
			LOGGER.warning(e.getMessage());
		}
		finally
		{
			closeInput(inputFromApplet);
			closeOutput(outputToApplet);
		}
	}
}
