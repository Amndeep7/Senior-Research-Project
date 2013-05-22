package example.servlet;

import java.util.ArrayList;

import example.shared.Boid;
import example.shared.Constants;

public class Simulation
{
	private int framex = Constants.FRAMEX, framey = Constants.FRAMEY;

	private ArrayList<Boid> boids;

	public Simulation()
	{
		boids = new ArrayList<Boid>();
		addBoid();
		for(int z = 0; z < 20; z++)
		{
			// addBoid("" + boids.size(), Math.random() * framex, Math.random() * framey, 100, 100, Math.random() * 5, Math.random() * 2 * Math.PI);
		}
		for(Boid b : boids){
			applyGrid(b);
		}
	}

	public ArrayList<Boid> getBoids()
	{
		return boids;
	}

	public void addBoid()
	{
		addBoid("" + boids.size(), 500, 500, 100, 100, 10, 0);
	}

	public void addBoid(String n, double x, double y, int w, int h, double s, double a)
	{
		Boid b = new Boid(n, x, y, w, h, s, a);
		applyGrid(b);
		boids.add(b);
	}

	public void removeBoid(int index)
	{
		if(0 <= index && index < boids.size())
		{
			boids.remove(index);
		}
	}

	public void applyWrapAround(Boid b)
	{
		if(b.getXPos() < 0)
		{
			b.setXPos(b.getXPos() + framex);
		}
		else if(b.getXPos() >= framex)
		{
			b.setXPos(b.getXPos() - framex);
		}

		if(b.getYPos() < 0)
		{
			b.setYPos(b.getYPos() + framey);
		}
		else if(b.getYPos() >= framey)
		{
			b.setYPos(b.getYPos() - framey);
		}
	}

	public void applyGrid(Boid b)
	{
		double[] position = gridPosition(b);

		if(position[1] > Constants.ROAD_VERTICAL_SEPARATION / 2.0)
		{
			position[1] = Math.abs(position[1] - Constants.ROAD_VERTICAL_SEPARATION);
		}
		b.setXPos(position[1] + position[0] * Constants.ROAD_VERTICAL_SEPARATION);

		if(position[3] > Constants.ROAD_HORIZONTAL_SEPARATION / 2.0)
		{
			position[3] = Math.abs(position[3] - Constants.ROAD_HORIZONTAL_SEPARATION);
		}
		b.setYPos(position[3] + position[2] * Constants.ROAD_HORIZONTAL_SEPARATION);
	}

	public double[] gridPosition(Boid b){
		double quotientX = (int) (b.getXPos() / Constants.ROAD_VERTICAL_SEPARATION);
		double remainderX = b.getXPos() % (double) Constants.ROAD_VERTICAL_SEPARATION;
		double quotientY = (int) (b.getYPos() / (double) Constants.ROAD_HORIZONTAL_SEPARATION);
		double remainderY = b.getYPos() % (double) Constants.ROAD_HORIZONTAL_SEPARATION;

		return new double[]{quotientX, remainderX, quotientY, remainderY};
	}

	public boolean closeEnoughToCorner(Boid b, double leeway){
		double[] position = gridPosition(b);
		return position[1] < leeway && position[3] < leeway;
	}

	public void run()
	{
		for(Boid b : boids)
		{
			b.move();
			applyWrapAround(b);

			if(closeEnoughToCorner(b, 5)){
				applyGrid(b);
				b.changeAngle();
			}
		}
	}
}
