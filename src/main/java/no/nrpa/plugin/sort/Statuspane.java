package no.nrpa.plugin.sort;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Statuspane extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;
	private int current;
	private int max;
	private JProgressBar progressBar;
	private JLabel fileLabel;

	public Statuspane(ActionListener act) {
		this.current = 0;
		this.max = 0;
		this.progressBar = null;
		this.fileLabel = null;
		setLayout(new BorderLayout());
		JPanel north = new JPanel();
		this.fileLabel = new JLabel();
		north.add(this.fileLabel);
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(10, 3));
		this.progressBar = new JProgressBar(this.current);
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(this.progressBar);
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		JPanel south = new JPanel();
		JButton quit = new JButton("Quit");
		quit.addActionListener(act);
		south.add(quit);
		add(north, "North");
		add(center, "Center");
		add(south, "South");
	}

	public void update(Observable o, Object arg) {
		Sorter so = (Sorter) o;
		if (this.max == 0) {
			this.max = so.getNumOfFiles();
			this.progressBar.setMaximum(this.max);
		} else {
			this.progressBar.setValue(so.getCurrent());
			this.fileLabel.setText(so.getCurrentFile());
		}
		paintAll(getGraphics());
		setVisible(true);
	}
}