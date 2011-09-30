package tpFinal.form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import tpFinal.model.FileMenu;

/**
 * Joue le rôle de la fenêtre principale du programme.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class AppFrame extends JFrame implements WindowListener {

	public static final String DEFAULT_TITLE = "Algo++ - Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault";
	private static final Dimension DEFAULT_DIMENSION = new Dimension(800, 560);

	private static final String EXIT_SAVE = "Voulez-vous enregistrer le document?";
	
	private static AppFrame instance = null;

	private AppText code = new AppText();
	private AppExecuteBar appExecuteBar = new AppExecuteBar();

	/**
	 * Crée l'instance unique de notre AppFrame
	 * @return Retourne l'instance créée.
	 */
	public static AppFrame getInstance() {
		if(AppFrame.instance == null) {
			AppFrame.instance = new AppFrame();
		}
		return AppFrame.instance;
	}

	/**
	 * Crée notre AppFrame et place les différentes composantes du programme
	 */
	private AppFrame() {
		super("Nouveau document - " + AppFrame.DEFAULT_TITLE);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		this.addWindowListener(this);

		AppMenuBar menu = new AppMenuBar();
		this.setJMenuBar(menu);

		JScrollPane text = new JScrollPane(this.code);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, text, this.appExecuteBar);
		splitPane.setResizeWeight(0.75);

		this.add(splitPane, BorderLayout.CENTER);

		this.setSize(AppFrame.DEFAULT_DIMENSION);
	}

	/**
	 * Demande une confirmation pour quitter le programme.
	 */
	public void exitApp() {
		if(FileMenu.isModified()) {
			int choix = JOptionPane.showConfirmDialog(AppFrame.getInstance(), 
					AppFrame.EXIT_SAVE);

			if(choix == JOptionPane.YES_OPTION) {
				FileMenu.saveDocument();
			}
			
			if(choix != JOptionPane.CANCEL_OPTION) {
				System.exit(0);
			}
		}
		else {
			System.exit(0);
		}
	}

	/**
	 * Retourne la zone du pseudo-code.
	 * @return Retourne la zone du pseudo-code.
	 */
	public AppText getAppText() {
		return this.code;
	}

	/**
	 * Retourne la zone d'exécution.
	 * @return Retourne la zone d'exécution.
	 */
	public AppExecuteBar getAppExecuteBar() {
		return this.appExecuteBar;
	}

	//************************* WINDOW LISTENER *************************//

	public void windowActivated(WindowEvent arg0) {

	}

	public void windowClosed(WindowEvent arg0) {

	}

	public void windowClosing(WindowEvent arg0) {
		this.exitApp();

	}

	public void windowDeactivated(WindowEvent arg0) {

	}

	public void windowDeiconified(WindowEvent arg0) {

	}

	public void windowIconified(WindowEvent arg0) {

	}

	public void windowOpened(WindowEvent arg0) {

	}

	//*******************************************************************//
}
