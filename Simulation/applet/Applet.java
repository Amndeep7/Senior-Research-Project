package applet;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Simple demonstration for an Applet <-> Servlet communication.
 */
public class Applet extends JApplet
{
	private static final long serialVersionUID = 3170574749472554461L; // make eclipse be quiet

	private JPanel panel;

	private JTextField inputField;
	private JTextField outputField;
	private JTextArea exceptionArea;

	/**
	 * Setup the GUI.
	 */
	public void init()
	{
		panel = new JPanel();
		inputField = new JTextField();
		outputField = new JTextField();
		exceptionArea = new JTextArea();

		// set new layout
		panel.setLayout(new GridBagLayout());

		// add title
		JLabel title = new JLabel("Simulation Applet", JLabel.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 14));
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		panel.add(title, c);

		// add input JLabel, field and send JButton
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.EAST;
		panel.add(new JLabel("Input:", JLabel.RIGHT), c);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		panel.add(inputField, c);
		JButton sendButton = new JButton("Send");
		c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(sendButton, c);
		sendButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onSendData();
			}
		});

		// add output JLabel and non-editable field
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.EAST;
		panel.add(new JLabel("Output:", JLabel.RIGHT), c);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		panel.add(outputField, c);
		JButton flipButton = new JButton("Flip");
		c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(flipButton, c);
		flipButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String text = outputField.getText();
				String newOut = "";
				for(String part : text.split(" "))
				{
					newOut += new StringBuffer(part).reverse().toString() + " ";
				}
				outputField.setText(newOut);
			}
		});
		outputField.setEditable(false);

		// add exception JLabel and non-editable text area
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.EAST;
		panel.add(new JLabel("Exception:", JLabel.RIGHT), c);
		c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		panel.add(exceptionArea, c);
		exceptionArea.setEditable(false);

		add(panel);
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
	private void onSendData()
	{
		try
		{
			// get input data for sending
			String input = inputField.getText();

			// send data to the servlet
			URLConnection con = getServletConnection();
			OutputStream outstream = con.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(outstream);
			oos.writeObject(input);
			oos.flush();
			oos.close();

			// receive result from servlet
			InputStream instr = con.getInputStream();
			ObjectInputStream inputFromServlet = new ObjectInputStream(instr);
			String result = (String) inputFromServlet.readObject();
			inputFromServlet.close();
			instr.close();

			// show result
			outputField.setText(result);

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			exceptionArea.setText(ex.toString());
		}
	}
}
