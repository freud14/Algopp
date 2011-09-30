package tpFinal;

import javax.swing.UIManager;

import tpFinal.form.AppFrame;

/**
 * Main de l'application. Point d'entr�e du programme.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class MainApp {

	/**
	 * @param args	Les arguments entr�s pour le programme (non utilis�).
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
