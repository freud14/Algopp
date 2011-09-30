package tpFinal.form;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import tpFinal.model.textArea.FindAndReplace;

/**
 * Cette classe affiche une boîte de dialogue 
 * pour rechercher ou remplacer un terme.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class AppFindAndReplace extends JDialog implements ActionListener, KeyListener {
	private final static String DEFAULT_TITLE = "Rechercher/Remplacer"; //Titre par défaut de l'application
	private final static String DEFAULT_LBL_SEARCH = "Recherche:";
	private final static String DEFAULT_LBL_REPLACE = "Remplacer par:";
	private final static String DEFAULT_ACT_SEARCH = "Search";
	private final static String DEFAULT_ACT_REPLACE= "Replace";
	private final static String DEFAULT_BTN_NEXT = "Suivant"; //Label du bouton Suivant
	private final static String DEFAULT_BTN_REPLACE = "Remplacer"; //Label du bouton Remplacer
	private final static String DEFAULT_BTN_REPLACEALL = "Remplacer tout"; //Label du bouton Remplacer tout
	private final static String DEFAULT_BTN_CLOSE = "Fermer"; //Label du bouton Fermer

	private JTextField txtSearch;
	private JTextField txtReplace;

	//Indique l'index du mot rechercher
	private int index;

	/**
	 * Crée la fenêtre AppFindAndReplace
	 */
	public AppFindAndReplace() {
		super();
		this.setTitle(AppFindAndReplace.DEFAULT_TITLE);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
		this.index = 0;

		//----------------------------------------
		//Création du panelGauche et de ses objets
		//----------------------------------------
		JPanel panelGauche = new JPanel();
		panelGauche.setLayout(new GridLayout(2, 2, 5, 5));

		JLabel lblSearch = new JLabel(AppFindAndReplace.DEFAULT_LBL_SEARCH);
		this.txtReplace = new JTextField();
		this.txtReplace.setActionCommand(AppFindAndReplace.DEFAULT_ACT_SEARCH);
		this.txtReplace.addActionListener(this);
		this.txtReplace.addKeyListener(this);
		JLabel lblReplace = new JLabel(AppFindAndReplace.DEFAULT_LBL_REPLACE);
		this.txtSearch = new JTextField();
		this.txtSearch.setActionCommand(AppFindAndReplace.DEFAULT_ACT_REPLACE);
		this.txtSearch.addActionListener(this);
		this.txtSearch.addKeyListener(this);

		panelGauche.add(lblSearch);
		panelGauche.add(this.txtSearch);
		panelGauche.add(lblReplace);
		panelGauche.add(this.txtReplace);
		this.add(panelGauche);

		//----------------------------------------
		//Création du panelDroite et de ses objets
		//----------------------------------------
		JPanel panelDroite = new JPanel();
		panelDroite.setLayout(new GridLayout(4, 1, 5, 5));

		this.addButton(AppFindAndReplace.DEFAULT_BTN_NEXT, AppFindAndReplace.DEFAULT_BTN_NEXT, panelDroite);
		this.addButton(AppFindAndReplace.DEFAULT_BTN_REPLACE, AppFindAndReplace.DEFAULT_BTN_REPLACE, panelDroite);
		this.addButton(AppFindAndReplace.DEFAULT_BTN_REPLACEALL, AppFindAndReplace.DEFAULT_BTN_REPLACEALL, panelDroite);
		this.addButton(AppFindAndReplace.DEFAULT_BTN_CLOSE, AppFindAndReplace.DEFAULT_BTN_CLOSE, panelDroite);

		this.add(panelDroite);
		this.setModal(true);

		this.pack();

		//Centre la fenêtre sur l'AppFrame 
		this.setLocationRelativeTo(AppFrame.getInstance());
	}

	/**
	 * 	Ajoute un bouton 
	 * @param name
	 * @param actCommand
	 * @param panel
	 */
	private void addButton(String name, String actCommand, JPanel panel){
		JButton newButton = new JButton(name);
		newButton.setActionCommand(actCommand);
		newButton.addActionListener(this);
		newButton.addKeyListener(this);
		panel.add(newButton);
	}

	/**
	 * Gère les cliques de tous les boutons de la fenêtre
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == AppFindAndReplace.DEFAULT_BTN_NEXT){
			this.index++;
			FindAndReplace.find(this.txtSearch.getText(), this.index);
		}
		else if (e.getActionCommand() == AppFindAndReplace.DEFAULT_BTN_REPLACE){
			FindAndReplace.remplaceFind(this.txtSearch.getText(), this.txtReplace.getText());
		}
		else if (e.getActionCommand() == AppFindAndReplace.DEFAULT_BTN_REPLACEALL){
			FindAndReplace.remplaceAll(this.txtSearch.getText(), this.txtReplace.getText());
		}
		else if (e.getActionCommand() == AppFindAndReplace.DEFAULT_BTN_CLOSE){
			this.dispose();
		}
		else if (e.getActionCommand() == AppFindAndReplace.DEFAULT_ACT_REPLACE){
			this.index = 0;
		}
		else if (e.getActionCommand() == AppFindAndReplace.DEFAULT_ACT_SEARCH){
			this.index = 0;
		}
	}

	/**
	 * Écoute les événements des événements du clavier
	 * Quitter la fenêtre lorsque ESCAPE est pressé
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			this.dispose();
		}
	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}
}
