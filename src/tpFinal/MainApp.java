package tpFinal;

import javax.swing.UIManager;

import tpFinal.form.AppFrame;

/**
 * Main de l'application. Point d'entrée du programme.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class MainApp {

	/**
	 * @param args	Les arguments entrés pour le programme (non utilisé).
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		//salut sa va?
		AppFrame.getInstance();
	}

}
