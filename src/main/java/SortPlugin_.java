import ij.plugin.PlugIn;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SortPlugin_ implements PlugIn {
	private static final String EXT_LIBS_DIR = "ext.libs.dir";
	private static final String ENTRY_POINT_CLASS = "plugin.entry.point.class";
	private static final String ENTRY_POINT_METHOD = "plugin.entry.point.method";
	private ResourceBundle properties = null;
	private static Logger logger = null;

	static {
		try {
			FileHandler handler = new FileHandler("SortPlugin.log", 100000, 2,
					true);
			logger = Logger.getLogger("SortPlugin_", "SortPlugin");
			handler.setLevel(Level.FINEST);
			logger.addHandler(handler);
			logger.setLevel(Level.FINEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SortPlugin_() throws IOException {
		this.properties = ResourceBundle.getBundle("SortPlugin");
		if (this.properties == null)
			logger.log(Level.WARNING, "failed to load properties " + new Date());
		else
			logger.log(Level.FINEST, "loaded properties " + new Date());
	}

	public void addURL(URL u) throws IOException {
		URLClassLoader sysloader = (URLClassLoader) ClassLoader
				.getSystemClassLoader();
		Class sysclass = URLClassLoader.class;
		Class[] parameters = { URL.class };
		try {
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysloader, new Object[] { u });
			System.setProperty("java.class.path",
					System.getProperty("java.class.path") + u.toExternalForm());
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "failed to add url " + u, t);
			throw new IOException(
					"Error, could not add URL to system classloader");
		}
	}

	private void addExternalLibraries() throws IOException {
		File extf = new File(this.properties.getString("ext.libs.dir"));
		File[] files = extf.listFiles();
		int start = 0;
		for (int stop = files.length; start < stop; start++) {
			addURL(files[start].toURL());
			logger.log(Level.FINEST, "added library : " + files[start].toURL()
					+ " to classpath");
		}
	}

	public void run(String arg) {
		try {
			addExternalLibraries();
			getClass();
			Class entryPoint = Class.forName(this.properties
					.getString("plugin.entry.point.class"));
			Object startingPoint = entryPoint.newInstance();
			Method startingMethod = entryPoint.getDeclaredMethod(
					this.properties.getString("plugin.entry.point.method"),
					null);
			startingMethod.invoke(startingPoint, null);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "run method failed", e);
		}
	}

	public static void main(String[] args1) {
		try {
			SortPlugin_ p = new SortPlugin_();
			p.run("test");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}