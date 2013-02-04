package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shared.Command;

/**
 * Simple demonstration for an Applet <-> Servlet communication.
 */
public class Servlet extends HttpServlet
{
	private static final long serialVersionUID = 6938369357587229915L; // make eclipse be quiet

	private static Logger LOGGER;

	private Simulation simulation;

	public Servlet()
	{
		super();

		LOGGER = Logger.getLogger(Servlet.class.getName());
		LOGGER.addHandler(new ConsoleHandler());


		simulation = new Simulation();

		LOGGER.log(Level.INFO, "exited constructor");
	}

	/**
	 * Get a String-object from the applet and send it back.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LOGGER.log(Level.INFO, "entered method");

		response.setContentType("application/x-java-serialized-object");

		InputStream in = request.getInputStream();
		ObjectInputStream inputFromApplet = new ObjectInputStream(in);

		LOGGER.log(Level.INFO, "created input streams");

		Command c = null;
		try
		{
			c = (Command) inputFromApplet.readObject();
		}
		catch(ClassNotFoundException e1)
		{
			e1.printStackTrace();
		}

		LOGGER.log(Level.INFO, "identified command as " + c);

		OutputStream outstr = response.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(outstr);

		LOGGER.log(Level.INFO, "created output streams");

		switch(c)
		{
			case GET_CARS:
			{
				LOGGER.log(Level.INFO, "getting cars");

				oos.writeObject(new Integer("1"));
				oos.writeObject(simulation.getCars());

				oos.flush();
				oos.close();

				break;
			}
			case ADD_CAR:
			{
				LOGGER.log(Level.INFO, "adding cars");

				simulation.addCar();
				// return true to signify success
				oos.writeObject(new Integer("1"));
				oos.writeObject(true);

				oos.flush();
				oos.close();

				break;
			}
			default: // as in default response
			{
				LOGGER.log(Level.INFO, "command not found: " + c);

				// signify failure
				oos.writeObject(new Integer("-1"));

				oos.flush();
				oos.close();

				break;
			}
		}

		LOGGER.log(Level.INFO, "leaving method");
	}
}
