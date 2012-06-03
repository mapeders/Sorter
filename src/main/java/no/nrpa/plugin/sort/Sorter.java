package no.nrpa.plugin.sort;

import ij.plugin.DICOM;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.StringTokenizer;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class Sorter extends Observable
{
  private static Logger logger = Logger.getLogger(Sorter.class);
  private int numOfFiles;
  private int current;
  private String currentFile;
  private String sourceDir;
  private String targetDir;
  private List metaTags;

  public Sorter(String source, String target, List meta)
  {
    this.numOfFiles = 0;
    this.current = 0;
    this.currentFile = null;
    this.sourceDir = null;
    this.targetDir = null;
    this.metaTags = null;
    this.sourceDir = source;
    this.targetDir = target;
    this.metaTags = meta;
  }

  public String getCurrentFile() {
    return this.currentFile;
  }

  public void setCurrentFile(String currentFile) {
    this.currentFile = currentFile;
  }

  public void sort() {
    Collection coll = FileUtils.listFiles(new File(this.sourceDir), null, true);
    Iterator it = coll.iterator();
    setNumOfFiles(coll.size());

    while (it.hasNext()) {
      File o = (File)it.next();
      String filename = o.getPath();
      setCurrentFile(filename);
      setCurrent(++this.current);
      File file = new File(filename);
      DICOM dicom = new DICOM();
      Map metaData = new HashMap();
      try {
        dicom.open(file.getAbsolutePath());
        String a = dicom.getProperty("Info").toString();
        for (StringTokenizer tokens = new StringTokenizer(a, 
          System.getProperty("line.separator")); 
          tokens.hasMoreTokens(); )
        {
          String temp = tokens.nextToken();
          if (temp.matches("^.{4},.{4}.*")) {
            HeaderLine line = new HeaderLine(temp);
            logger.debug("read tag : " + line.getKey());
            metaData.put(line.getKey(), line);
          }
        }

        HeaderLine patientName = (HeaderLine)metaData.get("0010,0010");
        File patientDir = new File(this.targetDir + File.separator + 
          patientName.getValue());
        if (!patientDir.exists()) {
          logger.debug(patientDir.getAbsolutePath());
          patientDir.mkdir();
        }
        String newFileName = File.separator + 
          getFilePrefix(metaData, this.metaTags) + ".dcm";
        File targetFile = new File(patientDir + File.separator + 
          newFileName);
        logger.debug("copying target file " + 
          targetFile.getAbsolutePath());
        FileUtils.copyFile(file, targetFile);
      } catch (Exception ex) {
        logger.error("file " + filename + " is not an image", ex);
      }
    }
  }

  private String getFilePrefix(Map metaData, List list)
  {
    StringBuffer prefix = new StringBuffer();
    for (Iterator it = list.iterator(); it.hasNext(); ) {
      String code = it.next().toString().trim();
      HeaderLine node = (HeaderLine)metaData.get(code);
      if ((node == null) && (code.equals("0018,1153")))
        node = (HeaderLine)metaData.get("0018,1152");
      if ((node != null) && (node.getValue() != null)) {
        logger.debug("header code : " + code);
        if (prefix.length() == 0)
          prefix.append(node.getValue());
        else {
          prefix.append("_" + node.getValue());
        }
      }
    }
    HeaderLine aquisitionDate = (HeaderLine)metaData.get("0008,0032");
    String t = aquisitionDate.getValue();
    int i = t.indexOf(".");
    if (i == -1)
      prefix.append("_" + t);
    else {
      prefix.append("_" + t.substring(0, i));
    }

    HeaderLine proc = (HeaderLine)metaData.get("0008,0068");
    prefix.append("_" + proc.getValue());
    return prefix.toString();
  }

  public int getCurrent() {
    return this.current;
  }

  public void setCurrent(int current) {
    this.current = current;
    setChanged();
    notifyObservers();
    clearChanged();
  }

  public int getNumOfFiles() {
    return this.numOfFiles;
  }

  public void setNumOfFiles(int numOfFiles) {
    this.numOfFiles = numOfFiles;
    setChanged();
    notifyObservers();
    clearChanged();
  }
}