package example.servlet;

import java.util.ArrayList;

import example.shared.Car;

public class Simulation
{
	ArrayList<Car> cars;

	public Simulation()
	{
		cars = new ArrayList<Car>();
		addCar();
	}

	public ArrayList<Car> getCars()
	{
		return cars;
	}

	public void addCar()
	{
		cars.add(new Car(50, 50, 100, 100, 10, 0));
	}

	public void removeCar(int index)
	{
		if(0 <= index && index < cars.size())
		{
			cars.remove(index);
		}
	}

	public void run()
	{
		for(Car c : cars)
		{
			c.move();
		}
	}
}
