package shared;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Car
{
	private int xPos, yPos, width, height;
	private BufferedImage image;

	public Car(int x, int y, int w, int h, BufferedImage i)
	{
		setXPos(x);
		setYPos(y);
		setWidth(w);
		setHeight(h);
		image = i;
	}

	//getters and setters
	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getXPos()
	{
		return xPos;
	}

	public void setXPos(int xPos)
	{
		this.xPos = xPos;
	}

	public int getYPos()
	{
		return yPos;
	}

	public void setYPos(int yPos)
	{
		this.yPos = yPos;
	}

	public void draw(Graphics2D g)
	{
		g.drawImage(image, xPos, yPos, xPos+width, yPos + height, 0, 0, image.getWidth(), image.getHeight(), null);
	}
}
