package applet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

import shared.Car;
import shared.Facing;

/**
 * Simple demonstration for an Applet <-> Servlet communication.
 */
public class Applet extends JApplet
{
	private static final long serialVersionUID = 3170574749472554461L; // make eclipse be quiet

	private JPanel panel;
	
	private JPanel map;
	private int framex = 1000, framey = 1000;
	private BufferedImage myImage;
	private Graphics2D myBuffer;
	
	private ArrayList<Car> cars;
	
	private Timer drawer;

	/**
	 * Setup the GUI.
	 */
	public void init()
	{
		cars = new ArrayList<Car>();
		cars.add(new Car(50, 50, 50, 50, 10, Facing.EAST, false));
		
		map = new JPanel();
		myImage = new BufferedImage(framex, framey, BufferedImage.TYPE_INT_ARGB);
		myBuffer = myImage.createGraphics();
		
		myBuffer.setColor(Color.black);
		myBuffer.fillRect(0, 0, framex, framey);
		add(map);
		
		drawer = new Timer(100, new DrawerTimer());
		drawer.start();
		
		panel = new JPanel();

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

		JButton addCarButton = new JButton("Add car");
		c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(addCarButton, c);
		addCarButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
				//onSendData();
			}
		});

		add(panel);
	}
	
	public void paintComponent(Graphics g)
	{
		myBuffer.fillRect(0, 0, framex, framey);
		
		for(Car c : cars)
		{
			c.draw(myBuffer, null);
		}
		
		g.drawImage(myImage, 0, 0, framex, framey, 0, 0, map.getWidth(), map.getHeight(), null);
	}
	
	public BufferedImage getCarPicture()
	{
		JOptionPane.showMessageDialog(null, "in get car picture");
		URL imageURL = getClass().getResource("/shared/resources/pictures/car.png");
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(imageURL);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.out.println("Failed to load image");
			System.exit(1);
		}
		
		return image;
	}
	
	private class DrawerTimer implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			map.repaint();
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
	/*private void onSendData()
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
	}*/
}
