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

public class ExampleApplet extends applet.Applet
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
		interaction.add(new JButton("Add boid"));
		((JButton) (interaction.getComponents()[interaction.getComponents().length - 1])).addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				interactWithServlet(Command.LOG, Level.INFO, "I want to add a boid");
				interactWithServlet(example.shared.Command.ADD_BOID);
			}
		});
		interaction.add(new JButton("Remove boid"));
		((JButton) (interaction.getComponents()[interaction.getComponents().length - 1])).addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				interactWithServlet(Command.LOG, Level.INFO, "I want to remove a boid");
				interactWithServlet(example.shared.Command.REMOVE_BOID, new Integer(0));
			}
		});
		interaction.add(new JButton("Display neighbors"));
		((JButton) (interaction.getComponents()[interaction.getComponents().length - 1])).addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(((JButton) (interaction.getComponents()[interaction.getComponents().length - 1])).getText().equals("Display neighbors")){
					((JButton) (interaction.getComponents()[interaction.getComponents().length - 1])).setText("Do not display neighbors");
				}
				else{
					((JButton) (interaction.getComponents()[interaction.getComponents().length - 1])).setText("Display neighbors");
				}
				interactWithServlet(Command.LOG, Level.INFO, "I want to display neighbors");
				interactWithServlet(example.shared.Command.DISPLAY_NEIGHBORS);
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
	
	public void destroy(){
		drawer.stop();
		super.destroy();
	}
}
