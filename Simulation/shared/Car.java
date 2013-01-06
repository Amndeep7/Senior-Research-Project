package shared;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Car
{
	private int xPos, yPos, width, height, speed;
	private boolean image;
	
	private Facing facing;

	public Car(int x, int y, int w, int h, int s, Facing f, boolean i)
	{
		setXPos(x);
		setYPos(y);
		setWidth(w);
		setHeight(h);
		setSpeed(s);
		facing = f;
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

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Facing getFacing() {
		return facing;
	}

	public void setFacing(Facing facing) {
		this.facing = facing;
	}

	public void draw(Graphics2D g, BufferedImage i)
	{
		if(image)
		{
			g.fillRect(getXPos(), getYPos(), getWidth(), getHeight());
		}
		else
		{
			g.drawImage(i, xPos, yPos, xPos+width, yPos + height, 0, 0, i.getWidth(), i.getHeight(), null);
		}
	}
}
