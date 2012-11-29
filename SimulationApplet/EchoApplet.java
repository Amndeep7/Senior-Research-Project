import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

/**
 * Simple demonstration for an Applet <-> Servlet communication.
 */
public class EchoApplet extends Applet {
	private static final long serialVersionUID = 3170574749472554461L; // make eclipse be quiet
	
	private TextField inputField = new TextField();
	private TextField outputField = new TextField();
	private TextArea exceptionArea = new TextArea();

	/**
	 * Setup the GUI.
	 */
	public void init() {
		// set new layout
		setLayout(new GridBagLayout());

		// add title		
		Label title = new Label("Echo Applet", Label.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 14));
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		add(title, c);

		// add input label, field and send button
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.EAST;
		add(new Label("Input:", Label.RIGHT), c);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		add(inputField, c);
		Button sendButton = new Button("Send");
		c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		add(sendButton, c);
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSendData();
			}
		});

		// add output label and non-editable field
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.EAST;
		add(new Label("Output:", Label.RIGHT), c);
		c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		add(outputField, c);
		outputField.setEditable(false);

		// add exception label and non-editable textarea
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.EAST;
		add(new Label("Exception:", Label.RIGHT), c);
		c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		add(exceptionArea, c);
		exceptionArea.setEditable(false);
	}

	/**
	 * Get a connection to the servlet.
	 */
	private URLConnection getServletConnection()
		throws MalformedURLException, IOException {

		// Open a connection to the servlet
		URL urlServlet = new URL(getCodeBase(), "echo");
		URLConnection con = urlServlet.openConnection();

		// Configuration
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		con.setRequestProperty(
			"Content-Type",
			"application/x-java-serialized-object");

		// and return
		return con;
	}

	/**
	 * Send the inputField data to the servlet and show the result in the outputField.
	 */
	private void onSendData() {
		try {
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

		} catch (Exception ex) {
			ex.printStackTrace();
			exceptionArea.setText(ex.toString());
		}
	}
}
