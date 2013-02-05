package applet;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import shared.Command;

public class Applet extends JApplet
{
	private static final long serialVersionUID = 3170574749472554461L; // make eclipse be quiet

	private JPanel interaction;

	public JTextArea errors;

	private SimulationView simulation;

	private Timer drawer;

	/**
	 * Setup the GUI.
	 */
	public void init()
	{
		setLayout(new BorderLayout());

		simulation = new SimulationView(this);
		simulation.setPreferredSize(new Dimension(800, 300));

		add(simulation, BorderLayout.CENTER);

		drawer = new Timer(100, new DrawerTimer());
		drawer.start();

		interaction = new JPanel();

		// add title
		JLabel title = new JLabel("Simulation Applet", JLabel.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 14));
		interaction.add(title);

		JButton addCarButton = new JButton("Add car");
		interaction.add(addCarButton);
		addCarButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				interactWithServlet(Command.ADD_CAR);
			}
		});

		add(interaction, BorderLayout.SOUTH);

		errors = new JTextArea();
		errors.setEditable(false);
		add(errors, BorderLayout.NORTH);
	}

	private class DrawerTimer implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			simulation.repaint();
		}
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

	/**
	 * Send the inputField data to the servlet and show the result in the outputField.
	 */

	public ArrayList<Object> interactWithServlet(Command c, Object... input)
	{
		ArrayList<Object> results = new ArrayList<Object>();

		try
		{
			URLConnection con = getServletConnection();
			OutputStream outstream = con.getOutputStream(); // error location
			ObjectOutputStream oos = new ObjectOutputStream(outstream);

			oos.writeObject(c);
			oos.flush();

			for(Object o : input)
			{
				oos.writeObject(o);
				oos.flush();
			}

			oos.close();

			// receive result from servlet
			InputStream instr = con.getInputStream();
			ObjectInputStream inputFromServlet = new ObjectInputStream(instr);

			int size = (Integer) inputFromServlet.readObject();
			if(size < 0)
				throw new Command.CommandUnknownException(c + " is not a known command");

			for(int x = 0; x < size; x++)
				results.add(inputFromServlet.readObject());

			inputFromServlet.close();
			instr.close();
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
			errors.setText(e.toString());
		}

		return results;
	}
}
