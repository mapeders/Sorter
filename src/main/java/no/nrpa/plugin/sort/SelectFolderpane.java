package no.nrpa.plugin.sort;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SelectFolderpane extends JPanel implements ActionListener {
	static final long serialVersionUID = 1L;
	private static final String TARGETFOLDER = "Select target folder";
	private static final String SOURCEFOLDER = "Select source folder";
	private String targetDirectory;
	private String rootDirectory;
	JLabel targetDirLabel;
	JLabel rootDirLabel;
	private Checkbox anodeTargetMaterial;
	private Checkbox filterMaterial;
	private Checkbox kVp;
	private Checkbox exposureInUAS;
	private Checkbox bodyPartThickness;
	private Checkbox compressionForce;

	public SelectFolderpane(ActionListener act) {
		this.targetDirectory = null;
		this.rootDirectory = null;
		this.targetDirLabel = new JLabel("Select target folder");
		this.rootDirLabel = new JLabel("Select source folder");
		this.anodeTargetMaterial = null;
		this.filterMaterial = null;
		this.kVp = null;
		this.exposureInUAS = null;
		this.bodyPartThickness = null;
		this.compressionForce = null;
		setLayout(new BorderLayout());
		JPanel north = new JPanel();
		north.setLayout(new GridLayout(4, 2));
		JButton selectTargetFolderButton = new JButton("Target");
		selectTargetFolderButton.addActionListener(this);
		JButton selectRootFolderButton = new JButton("Source");
		selectRootFolderButton.addActionListener(this);
		north.add(new JLabel(""));
		north.add(new JLabel(""));
		north.add(this.targetDirLabel);
		north.add(selectTargetFolderButton);
		north.add(this.rootDirLabel);
		north.add(selectRootFolderButton);
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(12, 2));
		this.anodeTargetMaterial = new Checkbox("Anode Target Material", true);
		this.filterMaterial = new Checkbox("Filter Material", true);
		this.kVp = new Checkbox("kVp", true);
		this.exposureInUAS = new Checkbox("Exposure in uAs", true);
		this.bodyPartThickness = new Checkbox("Bodypart thickness", true);
		this.compressionForce = new Checkbox("Compression force", true);
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(this.anodeTargetMaterial);
		center.add(new JLabel(""));
		center.add(this.filterMaterial);
		center.add(new JLabel(""));
		center.add(this.kVp);
		center.add(new JLabel(""));
		center.add(this.exposureInUAS);
		center.add(new JLabel(""));
		center.add(this.bodyPartThickness);
		center.add(new JLabel(""));
		center.add(this.compressionForce);
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		center.add(new JLabel(""));
		JPanel south = new JPanel();
		JButton execute = new JButton("Execute");
		execute.addActionListener(act);
		JButton quit = new JButton("Quit");
		quit.addActionListener(act);
		south.add(execute);
		south.add(quit);
		add(center, "Center");
		add(north, "North");
		add(south, "South");
	}

	public List getMetatags() {
		LinkedList list = new LinkedList();
		if (getAnodeTargetMaterialState())
			list.add("0018,1191");
		if (getFilterMaterialState())
			list.add("0018,7050");
		if (getKVpState())
			list.add("0018,0060");
		if (getExposureInUASState())
			list.add("0018,1153");
		if (getBodyPartThicknessState())
			list.add("0018,11A0");
		if (getCompressionForceState())
			list.add("0018,11A2");
		return list;
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Target")) {
			setTargetDirectory(selectDirectory());
			this.targetDirLabel.setText(getTargetDirectory());
		}
		if (action.equals("Source")) {
			setRootDirectory(selectDirectory());
			this.rootDirLabel.setText(getRootDirectory());
		}
		paintAll(getGraphics());
		setVisible(true);
	}

	private String selectDirectory() {
		String directory = null;
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select directory");
		chooser.setFileSelectionMode(1);
		chooser.setApproveButtonText("Select");
		if (chooser.showOpenDialog(null) == 0) {
			File file = chooser.getSelectedFile();
			directory = file.getPath();
			if (!directory.endsWith(File.separator))
				directory = directory + File.separator;
		}
		return directory;
	}

	public String getRootDirectory() {
		return this.rootDirectory;
	}

	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public String getTargetDirectory() {
		return this.targetDirectory;
	}

	public void setTargetDirectory(String targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

	public boolean getAnodeTargetMaterialState() {
		return this.anodeTargetMaterial.getState();
	}

	public boolean getExposureInUASState() {
		return this.exposureInUAS.getState();
	}

	public boolean getFilterMaterialState() {
		return this.filterMaterial.getState();
	}

	public boolean getKVpState() {
		return this.kVp.getState();
	}

	public boolean getBodyPartThicknessState() {
		return this.bodyPartThickness.getState();
	}

	public boolean getCompressionForceState() {
		return this.compressionForce.getState();
	}
}