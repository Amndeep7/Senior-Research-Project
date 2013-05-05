package example.shared;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Car implements Serializable
{
	private static final long serialVersionUID = -8819358195585385588L;

	private int xPos, yPos, width, height, speed;

	private double angle;

	public Car(int x, int y, int w, int h, int s, double a)
	{
		setXPos(x);
		setYPos(y);
		setWidth(w);
		setHeight(h);
		setSpeed(s);
		angle = a;
	}

	// getters and setters
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

	public int getSpeed()
	{
		return speed;
	}

	public void setSpeed(int speed)
	{
		this.speed = speed;
	}

	public double getFacing()
	{
		return angle;
	}

	public void setFacing(double facing)
	{
		this.angle = facing;
	}

	public void draw(Graphics2D g, BufferedImage i)
	{
		g.drawImage(i, getXPos(), getYPos(), getXPos() + getWidth(), getYPos() + getHeight(), 0, 0, i.getWidth(), i.getHeight(), null);
	}

	public void move()
	{
		xPos += Math.cos(angle);
		yPos += Math.sin(angle);
	}

	public String toString()
	{
		return "Car:" + getXPos() + "/" + getYPos();
	}
}
