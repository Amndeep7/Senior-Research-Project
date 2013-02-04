package applet;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
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
import javax.swing.JTextField;
import javax.swing.Timer;

import shared.Command;

/**
 * Simple demonstration for an Applet <-> Servlet communication.
 */
public class Applet extends JApplet
{
	private static final long serialVersionUID = 3170574749472554461L; // make eclipse be quiet

	private JPanel interaction;

	public JTextArea errors;

	private SimulationView simulation;

	private Timer drawer;

	public int postmethodcall = 0;
	public int repaintmethodcall = 0;
	public int paintcompmethodcall = 0;
	public int initmethodcall = 0;

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
		simulation.repaint();
		repaintmethodcall += 1;

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
				// onSendData();
			}
		});

		add(interaction, BorderLayout.SOUTH);

		errors = new JTextArea();
		errors.setEditable(false);
		add(errors, BorderLayout.NORTH);


		initmethodcall += 1;
		errors.setText("init called: " + initmethodcall);
	}

	private class DrawerTimer implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			repaintmethodcall += 1;
			//errors.setText("1 Post method call: " + postmethodcall + " Repaint method call: " + repaintmethodcall + " Paint component method call: " + paintcompmethodcall);
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
		postmethodcall += 1;
		//errors.setText("2 Post method call: " + postmethodcall + " Repaint method call: " + repaintmethodcall + " Paint component method call: " + paintcompmethodcall);

		ArrayList<Object> results = new ArrayList<Object>();

		try
		{
			URLConnection con = getServletConnection();
			OutputStream outstream = con.getOutputStream(); // error location
			ObjectOutputStream oos = new ObjectOutputStream(outstream);

			oos.writeObject(c);
			oos.flush();

			//errors.setText("Does input exist? " + input.length);

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
			//errors.setText("Value of size is " + size);
			if(size < 0)
				throw new Command.CommandUnknownException(c + " is not a known command");

			for(int x = 0; x < size; x++)
				results.add(inputFromServlet.readObject());

			//errors.setText(postmethodcall + " Do results exist? " + results.get(0));

			inputFromServlet.close();

			//errors.setText(postmethodcall + " Can I leave?");

			instr.close();

			//errors.setText(postmethodcall + " You're sure?");
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
			errors.setText(e.toString());
		}

		//errors.setText(postmethodcall + " You're not pulling my leg are you?");

		return results;
	}
}
