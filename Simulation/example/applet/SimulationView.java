package example.applet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import example.shared.Boid;
import example.shared.Command;
import example.shared.Constants;

public class SimulationView extends JPanel
{
	private static final long serialVersionUID = -8869054371150649099L;

	private int framex = Constants.FRAMEX, framey = Constants.FRAMEY;
	private BufferedImage myImage;
	private Graphics2D myBuffer;

	private ExampleApplet applet;

	public SimulationView(ExampleApplet a)
	{
		applet = a;

		myImage = new BufferedImage(framex, framey, BufferedImage.TYPE_INT_ARGB);
		myBuffer = myImage.createGraphics();

		myBuffer.setColor(Color.black);
		myBuffer.fillRect(0, 0, framex, framey);
	}

	@SuppressWarnings("unchecked")
	public void paintComponent(Graphics g)
	{
		myBuffer.setColor(Color.green);
		myBuffer.fillRect(0, 0, framex, framey);

		myBuffer.setColor(Color.gray);
		for(int x = 0; x < framex; x+=Constants.ROAD_VERTICAL_SEPARATION){
			myBuffer.fillRect(x, 0, 2, framey);
		}
		for(int y = 0; y < framey; y+=Constants.ROAD_HORIZONTAL_SEPARATION){
			myBuffer.fillRect(0, y, framex, 2);
		}

		ArrayList<Boid> boids = new ArrayList<Boid>();
		ArrayList<Object> boidsAsObjects = (ArrayList<Object>) applet.interactWithServlet(Command.GET_BOIDS).get(0);

		for(Object boid : boidsAsObjects)
			boids.add((Boid) boid);

		for(Boid b : boids)
		{
			b.draw(myBuffer);
		}

		g.drawImage(myImage, 0, 0, getWidth(), getHeight(), 0, 0, framex, framey, null);
	}
}
