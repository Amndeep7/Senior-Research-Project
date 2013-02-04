package applet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import shared.Car;
import shared.Command;

public class SimulationView extends JPanel
{
	private static final long serialVersionUID = -8869054371150649099L;

	private int framex = 1600, framey = 600;
	private BufferedImage myImage;
	private Graphics2D myBuffer;

	private Applet applet;

	private HashMap<String, BufferedImage> images;

	public SimulationView(Applet a)
	{
		applet = a;

		images = new HashMap<String, BufferedImage>();
		images.put("car", getCarPicture());

		myImage = new BufferedImage(framex, framey, BufferedImage.TYPE_INT_ARGB);
		myBuffer = myImage.createGraphics();

		myBuffer.setColor(Color.black);
		myBuffer.fillRect(0, 0, framex, framey);
	}

	public BufferedImage getCarPicture()
	{
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

	@SuppressWarnings("unchecked")
	public void paintComponent(Graphics g)
	{
		applet.paintcompmethodcall += 1;
		applet.errors.setText("3 Post method call: " + applet.postmethodcall + " Repaint method call: " + applet.repaintmethodcall + " Paint component method call: " + applet.paintcompmethodcall);

		myBuffer.setColor(Color.green);
		myBuffer.fillRect(0, 0, framex, framey);

		//applet.errors.setText("2 Hello?");

		ArrayList<Car> cars = new ArrayList<Car>();
		ArrayList<Object> cars2 = new ArrayList<Object>();

		//applet.errors.setText("3 Hello?");

		ArrayList<Object> objects = null;
		//applet.errors.setText("4 Hello?");
		objects = applet.interactWithServlet(Command.GET_CARS);
		//applet.errors.setText("5 Hello?");
		//applet.errors.setText(objects.toString());
		cars2 = (ArrayList<Object>) objects.get(0);

		for(Object o : cars2)
			cars.add((Car) o);
		//applet.errors.setText("It received the items");

		//applet.errors.setText("1 Hello?");

		myBuffer.setColor(Color.black);
		myBuffer.drawString("" + applet.getCodeBase(), 10, 10);

		for(Car c : cars)
		{
			c.draw(myBuffer, images.get("car"));
		}

		g.drawImage(myImage, 0, 0, getWidth(), getHeight(), 0, 0, framex, framey, null);
	}
}
