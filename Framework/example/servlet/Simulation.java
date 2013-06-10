/*
 * Copyright (c) 2012 Amndeep Singh Mann <Amndeep.dev@gmail.com> Please read License.txt for full license information.
 */

package example.servlet;

import java.util.ArrayList;

import example.shared.Boid;
import example.shared.Constants;

public class Simulation {
	private int framex = Constants.FRAMEX, framey = Constants.FRAMEY;

	private ArrayList<Boid> boids;

	private boolean displayNeighbors;

	public Simulation() {
		boids = new ArrayList<Boid>();
		addBoid("" + boids.size(), 500, 500, 25, 25, 10, 0);
		for (int z = 0; z < 75; z++) {
			addBoid();
		}

		displayNeighbors = false;
	}

	public ArrayList<Boid> getBoids() {
		for (Boid b : boids) {
			b.setDisplayNeighbors(displayNeighbors);
		}

		return boids;
	}

	public boolean getDisplayNeighbors() {
		return displayNeighbors;
	}

	public void setDisplayNeighbors(boolean b) {
		displayNeighbors = b;
	}

	public void addBoid() {
		addBoid("" + boids.size(), Math.random() * framex, Math.random() * framey, 25, 25, 5 + Math.random() * 15, Math.random() * 2 * Math.PI);
	}

	public void addBoid(String n, double x, double y, int w, int h, double s, double a) {
		Boid b = new Boid(n, x, y, w, h, s, a);
		applyGrid(b);
		changeAngle(b);
		boids.add(b);
	}

	public void removeBoid(int index) {
		if (0 <= index && index < boids.size()) {
			boids.remove(index);
		}
	}

	public void applyWrapAround(Boid b) {
		if (b.getXPos() < 0) {
			b.setXPos(b.getXPos() + framex);
		} else if (b.getXPos() >= framex) {
			b.setXPos(b.getXPos() - framex);
		}

		if (b.getYPos() < 0) {
			b.setYPos(b.getYPos() + framey);
		} else if (b.getYPos() >= framey) {
			b.setYPos(b.getYPos() - framey);
		}
	}

	public void applyGrid(Boid b) {
		double[] position = gridPosition(b);

		if (position[1] > Constants.ROAD_VERTICAL_SEPARATION / 2.0) {
			position[1] = Constants.ROAD_VERTICAL_SEPARATION;
		} else {
			position[1] = 0;
		}
		b.setXPos(position[1] + position[0] * Constants.ROAD_VERTICAL_SEPARATION);

		if (position[3] > Constants.ROAD_HORIZONTAL_SEPARATION / 2.0) {
			position[3] = Constants.ROAD_HORIZONTAL_SEPARATION;
		} else {
			position[3] = 0;
		}
		b.setYPos(position[3] + position[2] * Constants.ROAD_HORIZONTAL_SEPARATION);
	}

	public void changeAngle(Boid b) {
		double angle = b.getFacing();
		while (angle < 0) {
			angle += 2 * Math.PI;
		}
		double quotient = (int) (angle / (Math.PI / 2.0));
		angle %= Math.PI / 2.0;
		if (angle > (Math.PI / 2.0) / 2.0) {
			angle = Math.PI / 2.0;
		} else {
			angle = 0;
		}
		angle += quotient * (Math.PI / 2.0);
		b.setFacing(angle);
	}

	public double[] gridPosition(Boid b) {
		double quotientX = (int) (b.getXPos() / Constants.ROAD_VERTICAL_SEPARATION);
		double remainderX = b.getXPos() % (double) Constants.ROAD_VERTICAL_SEPARATION;
		double quotientY = (int) (b.getYPos() / (double) Constants.ROAD_HORIZONTAL_SEPARATION);
		double remainderY = b.getYPos() % (double) Constants.ROAD_HORIZONTAL_SEPARATION;

		return new double[] { quotientX, remainderX, quotientY, remainderY };
	}

	public boolean closeEnoughToCorner(Boid b, double leeway) {
		double[] position = gridPosition(b);
		return position[1] < leeway && position[3] < leeway;
	}

	public void run() {
		for (Boid b : boids) {
			b.move();
			applyWrapAround(b);

			if (closeEnoughToCorner(b, 5)) {
				applyGrid(b);

				b.adjustSpeed();
				b.adjustAngle();

				changeAngle(b);
			}
		}
		for (Boid b : boids) {
			b.setNeighbors(new ArrayList<Boid>());
		}
		for (int x = 0; x < boids.size(); x++) {
			for (int y = x + 1; y < boids.size(); y++) {
				if (Constants.RADIUS_OF_INFLUENCE * Constants.RADIUS_OF_INFLUENCE > (boids.get(x).getXPos() - boids.get(y).getXPos())
						* (boids.get(x).getXPos() - boids.get(y).getXPos()) + (boids.get(x).getYPos() - boids.get(y).getYPos())
						* (boids.get(x).getYPos() - boids.get(y).getYPos())) {
					boids.get(x).getNeighbors().add(boids.get(y));
					boids.get(y).getNeighbors().add(boids.get(x));
				}
			}
		}
	}
}
