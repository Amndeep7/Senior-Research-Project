package example.shared;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

public class Boid implements Serializable {
	private static final long serialVersionUID = -8819358195585385588L;

	String name;

	private int width, height;

	private double xPos, yPos, speed, angle;

	private ArrayList<Boid> neighbors;

	boolean displayNeighbors;

	public Boid(String n, double x, double y, int w, int h, double s, double a) {
		name = n;
		setXPos(x);
		setYPos(y);
		setWidth(w);
		setHeight(h);
		setSpeed(s);
		angle = a;

		neighbors = new ArrayList<Boid>();
		displayNeighbors = false;
	}

	// getters and setters
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public double getXPos() {
		return xPos;
	}

	public void setXPos(double xPos) {
		this.xPos = xPos;
	}

	public double getYPos() {
		return yPos;
	}

	public void setYPos(double yPos) {
		this.yPos = yPos;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getFacing() {
		return angle;
	}

	public void setFacing(double facing) {
		this.angle = facing;
	}

	public ArrayList<Boid> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(ArrayList<Boid> nbs) {
		this.neighbors = nbs;
	}

	public boolean getDisplayNeighbors() {
		return displayNeighbors;
	}

	public void setDisplayNeighbors(boolean dn) {
		displayNeighbors = dn;
	}

	public void draw(Graphics2D g) {
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];
		xPoints[0] = (int) Math.rint(xPos + width * Math.cos(getFacing()));
		yPoints[0] = (int) Math.rint(yPos + height * Math.sin(getFacing()));
		xPoints[1] = (int) Math.rint(xPos + width * Math.cos(getFacing() + 3 * Math.PI / 4));
		yPoints[1] = (int) Math.rint(yPos + height * Math.sin(getFacing() + 3 * Math.PI / 4));
		xPoints[2] = (int) Math.rint(xPos + width * Math.cos(getFacing() + 5 * Math.PI / 4));
		yPoints[2] = (int) Math.rint(yPos + height * Math.sin(getFacing() + 5 * Math.PI / 4));

		g.setColor(Color.MAGENTA);
		g.fillPolygon(xPoints, yPoints, 3);
		g.setColor(Color.ORANGE);
		g.setFont(new Font("Times New Roman", Font.BOLD, 30));
		g.drawString(name, (int) getXPos(), (int) getYPos());

		if (displayNeighbors) {
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.WHITE);
			for (Boid neighbor : neighbors) {
				g.drawLine((int) xPos, (int) yPos, (int) neighbor.xPos, (int) neighbor.yPos);
			}
		}
	}

	public void move() {
		xPos += Math.cos(angle) * getSpeed();
		yPos += Math.sin(angle) * getSpeed();
	}

	public String toString() {
		return "Boid:" + getXPos() + "|" + getYPos();
	}
}
