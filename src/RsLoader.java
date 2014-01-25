import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFrame;

/**
 * @author Ahad Ahmad
 */
public class RsLoader extends JFrame implements AppletStub {

    public static final String PAGE_ADDRESS = "http://oldschool1.runescape.com/";
    private static final long serialVersionUID = -3450697819043722786L;
    private static final HashMap<String, String> parameters = new HashMap<String, String>();
    private static URL pageAddressUrl;

    public static void main(String[] args) throws Exception {
	pageAddressUrl = new URL(PAGE_ADDRESS);
	String pageSource = readPageSource(PAGE_ADDRESS);
	parseParameters(pageSource);
	String gamepackFileName = findGamepackFileName(pageSource);
	URLClassLoader classLoader = new URLClassLoader(new URL[] { new URL(
		PAGE_ADDRESS + gamepackFileName) });
	Applet game = (Applet) classLoader.loadClass("client").newInstance();
	RsLoader rs2Loader = new RsLoader();
	game.setStub(rs2Loader);
	game.init();
	game.start();
	rs2Loader.add(game);
	rs2Loader.setDefaultCloseOperation(EXIT_ON_CLOSE);
	rs2Loader.setSize(773, 530);
	rs2Loader.setTitle("Runescape Loader Meow");
	rs2Loader.setResizable(false);
	rs2Loader.setVisible(true);
    }

    private static String readPageSource(String pageAddress) throws IOException {
	String source = "";
	Scanner scanner = new Scanner(new URL(pageAddress).openStream());
	while (scanner.hasNextLine()) {
	    source += scanner.nextLine() + "\n";
	}
	return source;
    }

    private static void parseParameters(String pageSource) {
	String[] lines = pageSource.split("\n");
	String paramNameBeginning = "param name=";
	String valueBeginning = "value=";
	for (String line : lines) {
	    if (!line.contains(paramNameBeginning)) {
		continue;
	    }
	    int start = line.indexOf(paramNameBeginning)
		    + paramNameBeginning.length() + 1;
	    int end = line.indexOf('"', start);
	    String name = line.substring(start, end);
	    start = line.indexOf(valueBeginning) + valueBeginning.length() + 1;
	    end = line.indexOf('"', start);
	    String value = line.substring(start, end);
	    parameters.put(name, value);
	}
    }

    private static String findGamepackFileName(String pageSource) {
	String gamepackFileNameStart = "archive=";
	int gamepackFileNameStartIndex = pageSource
		.indexOf(gamepackFileNameStart);
	return pageSource.substring(gamepackFileNameStartIndex
		+ gamepackFileNameStart.length(),
		pageSource.indexOf('\'', gamepackFileNameStartIndex));
    }

    public URL getDocumentBase() {
	return pageAddressUrl;
    }

    public URL getCodeBase() {
	return pageAddressUrl;
    }

    public String getParameter(String name) {
	return parameters.get(name);
    }

    public AppletContext getAppletContext() {
	return null;
    }

    public void appletResize(int width, int height) {

    }
}