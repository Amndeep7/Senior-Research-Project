package example.applet;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import shared.Command;

public class Applet extends applet.Applet
{
	private static final long serialVersionUID = -4898572627107479083L;

	private JPanel interaction;

	private SimulationView simulation;

	private JTextArea errors;

	private Timer drawer;

	public void init()
	{
		super.init();

		setLayout(new BorderLayout());

		errors = new JTextArea();
		errors.setEditable(false);
		errors.setPreferredSize(new Dimension(size.width / 10, size.height));
		errors.setLineWrap(true);
		errors.setWrapStyleWord(true);
		add(errors, BorderLayout.WEST);

		simulation = new SimulationView(this);
		simulation.setPreferredSize(new Dimension(9 * size.width / 10, size.height));
		add(simulation, BorderLayout.CENTER);

		// add title
		JLabel title = new JLabel("Boids Simulation Applet", JLabel.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 14));
		add(title, BorderLayout.NORTH);

		interaction = new JPanel();
		interaction.add(new JButton("Add car"));
		((JButton) (interaction.getComponents()[interaction.getComponents().length - 1])).addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				interactWithServlet(Command.LOG, Level.INFO, "I want to add a car");
				interactWithServlet(example.shared.Command.ADD_CAR);
			}
		});
		interaction.add(new JButton("Remove car"));
		((JButton) (interaction.getComponents()[interaction.getComponents().length - 1])).addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				interactWithServlet(Command.LOG, Level.INFO, "I want to remove a car");
				interactWithServlet(example.shared.Command.REMOVE_CAR, new Integer(0));
			}
		});
		add(interaction, BorderLayout.SOUTH);

		drawer = new Timer(100, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				errors.setText(error);
				simulation.repaint();
				interactWithServlet(Command.LOG, Level.FINEST, "Repainted screen");
			}
		});
		drawer.start();
	}
}