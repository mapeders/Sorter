package no.nrpa.plugin.sort;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class SortFrame extends JFrame
  implements ActionListener
{
  static final long serialVersionUID = 1L;
  private SelectFolderpane selectFolder;
  private Statuspane statusPane;
  private Sorter sorter;

  public SortFrame()
  {
    super("Sort");
  }

  public void run()
  {
    this.selectFolder = null;
    this.statusPane = null;
    this.sorter = null;
    try
    {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (Exception localException) {
    }
    this.selectFolder = new SelectFolderpane(this);
    setDefaultCloseOperation(2);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(400, 400));
    getContentPane().add(this.selectFolder, "Center");
    pack();
    setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("Execute")) {
      getContentPane().removeAll();
      this.sorter = new Sorter(this.selectFolder.getRootDirectory(), this.selectFolder.getTargetDirectory(), this.selectFolder.getMetatags());
      this.statusPane = new Statuspane(this);
      this.sorter.addObserver(this.statusPane);
      getContentPane().add(this.statusPane, "Center");
      paintAll(getGraphics());
      setVisible(true);
      this.sorter.sort();
      getContentPane().removeAll();
      run();
    }
    if (e.getActionCommand().equals("Cancel")) {
      getContentPane().removeAll();
      this.sorter.setCurrent(0);
      this.sorter.setCurrentFile("");
      this.sorter.setNumOfFiles(0);
      getContentPane().add(this.selectFolder, "Center");
      paintAll(getGraphics());
      setVisible(true);
    }
    if (e.getActionCommand().equals("Quit")) {
      setVisible(false);
      dispose();
    }
  }

  public static void main(String[] args) {
    new SortFrame().run();
  }
}