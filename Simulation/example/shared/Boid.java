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

	public void move() {
		xPos += Math.cos(angle) * getSpeed();
		yPos += Math.sin(angle) * getSpeed();
	}

	public boolean inDiscomfortRadius(Boid b) {
		if (Constants.RADIUS_OF_DISCOMFORT * Constants.RADIUS_OF_DISCOMFORT > (xPos - b.xPos) * (xPos - b.xPos) + (yPos - b.yPos) * (yPos - b.yPos)) {
			return true;
		} else {
			return false;
		}
	}

	public void adjustSpeed() {
		if (neighbors.size() != 0) {
			boolean foundDiscomfortableNeighbors = false;
			double aveSpeed = 0;

			for (Boid b : neighbors) {
				if (!foundDiscomfortableNeighbors && inDiscomfortRadius(b)) {
					foundDiscomfortableNeighbors = true;
				}
				aveSpeed += b.getSpeed();
			}
			aveSpeed /= neighbors.size();

			double delta = 0.40 * (speed - aveSpeed);

			speed = foundDiscomfortableNeighbors ? speed + delta : speed - delta;
		}

		speed += (Math.random() < 0.6 ? -1 : 1) * Math.random() * 0.15;

		if (speed < 0) {
			speed *= -1;
			angle += Math.PI;
		}
		
		if(speed < Constants.MINIMUM_SPEED){
			speed = Constants.MINIMUM_SPEED;
		}
		else if(speed > Constants.MAXIMUM_SPEED){
			speed = Constants.MAXIMUM_SPEED;
		}
	}

	public void adjustAngle() {
		if (neighbors.size() != 0) {
			boolean foundDiscomfortableNeighbors = false;
			double aveAngle = 0;

			for (Boid b : neighbors) {
				if (!foundDiscomfortableNeighbors && inDiscomfortRadius(b)) {
					foundDiscomfortableNeighbors = true;
				}
				aveAngle += b.getFacing();
			}
			aveAngle /= neighbors.size();

			double delta = 0.9*(angle - aveAngle);

			angle = foundDiscomfortableNeighbors ? angle + delta : angle - delta;
		}

		angle += (Math.random() < 0.5 ? -1 : 1) * Math.random() * Math.PI / 3;
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

		g.setStroke(new BasicStroke(5f));
		if (displayNeighbors) {
			for (Boid neighbor : neighbors) {
				if (inDiscomfortRadius(neighbor)) {
					g.setColor(Color.RED);
				} else {
					g.setColor(Color.WHITE);
				}
				g.drawLine((int) xPos, (int) yPos, (int) neighbor.xPos, (int) neighbor.yPos);
			}
		}
	}

	public String toString() {
		return "Boid:" + getXPos() + "|" + getYPos();
	}
}
