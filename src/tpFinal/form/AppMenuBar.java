package tpFinal.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import tpFinal.model.Executor;
import tpFinal.model.FileMenu;

/**
 * Crée un JMenuBar personnalisé pour l'application.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class AppMenuBar extends JMenuBar implements ActionListener {

	//Texte du menu « Fichier »
	private static final String DEFAULT_FILE = "Fichier";
	private static final String DEFAULT_NEW = "Nouveau";
	private static final String DEFAULT_OPEN = "Ouvrir...";
	private static final String DEFAULT_SAVE = "Enregistrer";
	private static final String DEFAULT_SAVE_AS = "Enregistrer sous...";
	private static final String DEFAULT_EXIT = "Quitter";

	//Texte du menu « Édition »
	private static final String DEFAULT_EDIT = "Édition";
	private static final String DEFAULT_UNDO = "Annuler";
	private static final String DEFAULT_REDO = "Rétablir";
	private static final String DEFAULT_CUT	= "Couper";
	private static final String DEFAULT_COPY = "Copier";
	private static final String DEFAULT_PASTE = "Coller";
	private static final String DEFAULT_DELETE = "Supprimer";
	private static final String DEFAULT_SELECT_ALL = "Sélectionner tout";
	private static final String DEFAULT_FIND = "Rechercher/Remplacer...";
	
	//Texte du menu « Exécuter »
	private static final String DEFAULT_RUN = "Code";
	private static final String DEFAULT_RUN_ITEM = "Exécuter code";

	//Texte du menu « ? »
	private static final String DEFAULT_OTHERS = "?";
	private static final String DEFAULT_HELP = "Afficher l'aide";
	private static final String DEFAULT_ABOUT = "À propos de Algo++";
	
	/**
	 * Crée le JMenuBar et ajoute les items des menus.
	 */
	public AppMenuBar() {
		super();

		this.initFileMenu();
		this.initEditionMenu();
		this.initRunMenu();
		this.initOthersMenu();
	}
	
	/**
	 * Initialise le menu Fichier
	 */
	private void initFileMenu() {
		//Menu Fichier
		JMenu file = new JMenu(AppMenuBar.DEFAULT_FILE);

		this.addMenuItem(AppMenuBar.DEFAULT_NEW, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), AppMenuBar.DEFAULT_NEW, file);
		this.addMenuItem(AppMenuBar.DEFAULT_OPEN, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK), AppMenuBar.DEFAULT_OPEN, file);
		this.addMenuItem(AppMenuBar.DEFAULT_SAVE, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), AppMenuBar.DEFAULT_SAVE, file);
		this.addMenuItem(AppMenuBar.DEFAULT_SAVE_AS, null, AppMenuBar.DEFAULT_SAVE_AS, file);
		file.addSeparator();
		this.addMenuItem(AppMenuBar.DEFAULT_EXIT, null, AppMenuBar.DEFAULT_EXIT, file);

		this.add(file);
	}

	/**
	 * Initialise le menu Édition
	 */
	private void initEditionMenu (){
		//Menu Édition
		JMenu edit = new JMenu(AppMenuBar.DEFAULT_EDIT);

		this.addMenuItem(AppMenuBar.DEFAULT_UNDO, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), AppMenuBar.DEFAULT_UNDO, edit);
		this.addMenuItem(AppMenuBar.DEFAULT_REDO, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), AppMenuBar.DEFAULT_REDO, edit);
		edit.addSeparator();
		this.addMenuItem(AppMenuBar.DEFAULT_CUT, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK), AppMenuBar.DEFAULT_CUT, edit);
		this.addMenuItem(AppMenuBar.DEFAULT_COPY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), AppMenuBar.DEFAULT_COPY, edit);
		this.addMenuItem(AppMenuBar.DEFAULT_PASTE, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), AppMenuBar.DEFAULT_PASTE, edit);
		this.addMenuItem(AppMenuBar.DEFAULT_DELETE, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), AppMenuBar.DEFAULT_DELETE, edit);
		edit.addSeparator();
		this.addMenuItem(AppMenuBar.DEFAULT_SELECT_ALL, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK), AppMenuBar.DEFAULT_SELECT_ALL, edit);
		this.addMenuItem(AppMenuBar.DEFAULT_FIND, KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), AppMenuBar.DEFAULT_FIND, edit);

		this.add(edit);	
	}
	
	/**
	 * Initialise le menu Exécuté
	 */
	private void initRunMenu (){
		//Menu Exécuter
		JMenu run = new JMenu(AppMenuBar.DEFAULT_RUN);

		this.addMenuItem(AppMenuBar.DEFAULT_RUN_ITEM, KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), AppMenuBar.DEFAULT_RUN_ITEM, run);

		this.add(run);
	}
	
	/**
	 * Initialise le menu ?
	 */
	private void initOthersMenu() {
		//Menu ?
		JMenu others = new JMenu(AppMenuBar.DEFAULT_OTHERS);

		this.addMenuItem(AppMenuBar.DEFAULT_HELP, null, AppMenuBar.DEFAULT_HELP, others);
		this.addMenuItem(AppMenuBar.DEFAULT_ABOUT, null, AppMenuBar.DEFAULT_ABOUT, others);
		
		this.add(others);
	}
	
	/**
	 * Initialise des MenuItem avec les paramètres
	 * @param name 			Nom du bouton
	 * @param key			Raccourcis clavier
	 * @param actCommand	ActionCommand
	 * @param parent		Parent (JMenu)
	 */
	private void addMenuItem(String name, KeyStroke key, String actCommand, JMenu parent) {
		JMenuItem newMenu = new JMenuItem(name);
		newMenu.setAccelerator(key);
		newMenu.setActionCommand(actCommand);
		newMenu.addActionListener(this);
		parent.add(newMenu);
	}

	/**
	 * Cette méthode est appelée lorsque l'on clique sur un des items des menus.
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(AppMenuBar.DEFAULT_NEW)) {
			FileMenu.newDocument();
		}
		else if(e.getActionCommand().equals(AppMenuBar.DEFAULT_OPEN)) {
			FileMenu.openDocument();
		}
		else if(e.getActionCommand().equals(AppMenuBar.DEFAULT_SAVE)) {
			FileMenu.saveDocument();
		}
		else if(e.getActionCommand().equals(AppMenuBar.DEFAULT_SAVE_AS)) {
			FileMenu.saveDocumentAs();
		}
		else if(e.getActionCommand().equals(AppMenuBar.DEFAULT_EXIT)) {
			AppFrame.getInstance().exitApp();
		}
		else if(e.getActionCommand().equals(AppMenuBar.DEFAULT_UNDO)) {
			AppFrame.getInstance().getAppText().getUndoAction().doUndo();
		}
		else if(e.getActionCommand().equals(AppMenuBar.DEFAULT_REDO)) {
			AppFrame.getInstance().getAppText().getRedoAction().doRedo();
		}
		else if(e.getActionCommand().equals(AppMenuBar.DEFAULT_CUT)) {
			AppFrame.getInstance().getAppText().cut();
		}
		else if(e.getActionCommand().equals(AppMenuBar.DEFAULT_COPY)) {
			AppFrame.getInstance().getAppText().copy();
		}
		else if(e.getActionCommand().equals(AppMenuBar.DEFAULT_PASTE)) {
			AppFrame.getInstance().getAppText().paste();
		}
		else if(e.getActionCommand().equals(AppMenuBar.DEFAULT_DELETE)) {
			AppFrame.getInstance().getAppText().delete();
		}
		else if(e.getActionCommand().equals(AppMenuBar.DEFAULT_SELECT_ALL)) {
			AppFrame.getInstance().getAppText().selectAll();
		}
		else if(e.getActionCommand().equals(AppMenuBar.DEFAULT_FIND)) {
			new AppFindAndReplace().setVisible(true);
		}
		else if(e.getActionCommand().equals(AppMenuBar.DEFAULT_RUN_ITEM)) {
			AppFrame.getInstance().getAppExecuteBar().gotoDisplayTab();
			new Executor(AppFrame.getInstance().getAppText()).execute();
		}
		else if(e.getActionCommand().equals(AppMenuBar.DEFAULT_HELP)) {
			new AppHelpDialog().setVisible(true);
		}
		else if(e.getActionCommand().equals(AppMenuBar.DEFAULT_ABOUT)) {
			new AppAboutDialog().setVisible(true);
		}
	}
}
