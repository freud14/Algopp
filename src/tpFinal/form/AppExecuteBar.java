package tpFinal.form;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * Contient les zones de textes qui font office de zone d'ex�cution
 * et de zone d'erreur. Elle contient des m�thodes qui permet de 
 * vider les zones de textes, d'ajouter du texte et de changer d'onglet 
 * automatiquement.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class AppExecuteBar extends JTabbedPane {

	private JTextArea displayZone = new JTextArea();
	private JTextArea error = new JTextArea();

	private JScrollPane scrollDisplay = new JScrollPane(this.displayZone);
	private JScrollPane scrollError = new JScrollPane(this.error);

	/**
	 * Initialise les zones de texte.
	 */
	public AppExecuteBar() {
		super();

		this.displayZone.setBackground(Color.BLACK);
		this.displayZone.setForeground(Color.WHITE);

		this.error.setForeground(Color.RED);

		this.displayZone.setEditable(false);
		this.displayZone.setAutoscrolls(true);
		this.error.setEditable(false);
		this.error.setAutoscrolls(true);

		this.addTab("Zone d'ex�cution", this.scrollDisplay);
		this.addTab("Erreur d'ex�cution", this.scrollError);
	}

	/**
	 * Ajoute une nouvelle ligne � la zone d'ex�cution.
	 * @param line La ligne � ajouter sans retour � la ligne.
	 */
	public void displayNewLine(String line) {
		this.displayZone.append(line + "\r\n");
		this.displayZone.setCaretPosition(this.displayZone.getDocument().getLength()); 
		this.gotoDisplayTab();
	}

	/**
	 * Ajoute une nouvelle ligne � la zone d'erreur.
	 * @param lineNb La ligne � ajouter sans retour � la ligne.
	 */
	public void displayNewError(String error, int lineNb) {
		this.error.append("\r\n" +  error + " � la ligne num�ro " + lineNb + ".");
		this.error.setCaretPosition(this.error.getDocument().getLength());
		this.gotoErrorTab();
	}

	/**
	 * Permet de basculer vers l'onglet de la zone d'erreur.
	 */
	public void gotoErrorTab() {
		this.setSelectedComponent(this.scrollError);
	}

	/**
	 * Permet de basculer vers l'onglet de la zone d'ex�cution.
	 */
	public void gotoDisplayTab() {
		this.setSelectedComponent(this.scrollDisplay);
	}

	/**
	 * Vide la zone d'ex�cution.
	 */
	public void clearDisplayZone() {
		this.displayZone.setText("");
	}

	/**
	 * Vide la zone d'erreur.
	 */
	public void clearErrorZone() {
		this.error.setText("");
	}

}
