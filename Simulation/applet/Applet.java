package applet;

import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.swing.JApplet;

public class Applet extends JApplet
{
	private static final long serialVersionUID = 3170574749472554461L;

	protected Dimension size;

	protected String error;

	public void init()
	{
		size = getSize();

		error = "";
	}

	public void setError(String message)
	{
		error = message;
	}

	/**
	 * Get a connection to the servlet.
	 */
	private URLConnection getServletConnection() throws MalformedURLException, IOException
	{

		// Open a connection to the servlet
		URL urlServlet = new URL(getCodeBase(), "simulation");
		URLConnection con = urlServlet.openConnection();

		// Configuration
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		con.setRequestProperty("Content-Type", "application/x-java-serialized-object");

		// and return
		return con;
	}

	public ArrayList<Object> interactWithServlet(Object command, Object... input)
	{
		ArrayList<Object> results = new ArrayList<Object>();

		try
		{
			URLConnection con = getServletConnection();

			ObjectOutputStream outputToServlet = new ObjectOutputStream(con.getOutputStream());

			outputToServlet.writeObject(command);
			outputToServlet.flush();

			for(Object o : input)
			{
				outputToServlet.writeObject(o);
				outputToServlet.flush();
			}

			outputToServlet.close();

			ObjectInputStream inputFromServlet = new ObjectInputStream(con.getInputStream());

			int size = (Integer) inputFromServlet.readObject();
			if(size < 0)
			{
				error = inputFromServlet.readObject().toString();
			}
			else
			{
				for(int x = 0; x < size; x++)
				{
					results.add(inputFromServlet.readObject());
				}
			}

			inputFromServlet.close();
		}
		catch(IOException | ClassNotFoundException e)
		{
			setError(e.getMessage());
		}

		return results;
	}
}
