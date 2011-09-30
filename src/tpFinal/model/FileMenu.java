package tpFinal.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import tpFinal.form.AppFrame;

/**
 * Classe qui fournis des m�thodes pour la gestion du document pr�sent.
 * 
 * Toutes les m�thodes de cette classe sont statiques afin d'�viter de cr�er des objets
 * afin d'utiliser que des m�thodes utilitaires.
 * 
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class FileMenu {

	private static final String MESSAGE_ERROR_OPEN = "Impossible d'ouvrir le fichier sp�cifi�.";
	private static final String EXIT_SAVE = "Voulez-vous enregistrer le document?";
	
	private static boolean alreadySave = false; //Est vrai lorsque le fichier est d�j� enregistr� quelque part.
	private static boolean modified = false; //Est vrai lorsque le fichier a �t� modifi�.
	private static File currentFile = null; //Contient le fichier pr�sent s'il a d�j� �t� enregistr�.

	/**
	 * Cette m�thode ouvre un nouveau document.
	 * @return Retourne faux si la personne chosi de ne pas ouvrir un nouveau document (Annuler)
	 */
	public static boolean newDocument() {
		boolean retour = true;
		if(FileMenu.modified) {
			if(!AppFrame.getInstance().getAppText().getText().equals("")) {
				int choix = JOptionPane.showConfirmDialog(AppFrame.getInstance(), 
				FileMenu.EXIT_SAVE);

				if(choix == JOptionPane.YES_OPTION) {
					FileMenu.saveDocument();
				}

				if(choix != JOptionPane.CANCEL_OPTION) {
					AppFrame.getInstance().getAppText().setText("");
					FileMenu.alreadySave = false;
					FileMenu.currentFile = null;
					FileMenu.setUnmodifiedDocument();
				}
				else {
					retour = false;
				}
			}
		}
		else {
			AppFrame.getInstance().getAppText().setText("");
			FileMenu.alreadySave = false;
			FileMenu.currentFile = null;
			
			FileMenu.setUnmodifiedDocument();
		}
		return retour;
	}

	/**
	 * Cette m�thode permet d'ouvrir un document sur le disque.
	 */
	public static void openDocument() {
		JFileChooser file = new JFileChooser();
		if(file.showOpenDialog(AppFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
			File f = file.getSelectedFile();
			if(f.canRead()) {
				FileMenu.currentFile = f;

				FileMenu.alreadySave = true;
				AppFrame.getInstance().getAppText().setText(FileMenu.readFile(FileMenu.currentFile));
				FileMenu.setUnmodifiedDocument();
			}
			else {
				JOptionPane.showMessageDialog(AppFrame.getInstance(), FileMenu.MESSAGE_ERROR_OPEN);
				FileMenu.openDocument();
			}
		}
	}

	/**
	 * Cette m�thode enregistre un document d�j� enregistr�. Elle 
	 * appelle la m�thode <code>saveDocumentAs</code> si le document
	 * n'a jamais �t� enregist�.
	 * @see FileMenu#saveDocumentAs()
	 */
	public static void saveDocument() {
		if(FileMenu.alreadySave) {
			String code = AppFrame.getInstance().getAppText().getText();
			FileMenu.writeFile(FileMenu.currentFile, code);
			FileMenu.setUnmodifiedDocument();
		}
		else {
			FileMenu.saveDocumentAs();
		}
	}

	/**
	 * Ouvre une fen�tre de dialogue qui permet d'enregistrer le document pr�sent.
	 */
	public static void saveDocumentAs() {
		JFileChooser file = new JFileChooser();
		if(file.showSaveDialog(AppFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
			FileMenu.currentFile  = file.getSelectedFile();

			FileMenu.alreadySave = true;
			FileMenu.setUnmodifiedDocument();
			FileMenu.writeFile(FileMenu.currentFile, AppFrame.getInstance().getAppText().getText());
		}
	}

	/**
	 * Modifie l'�tat du document comme �tant modifi�.
	 * @see FileMenu#setUnmodifiedDocument()
	 */
	public static void setModifiedDocument() {
		FileMenu.modified = true;
		if(FileMenu.currentFile != null) {
			AppFrame.getInstance().setTitle(FileMenu.currentFile.getName() + "* - "+ AppFrame.DEFAULT_TITLE);
		}
		else {
			AppFrame.getInstance().setTitle("Nouveau document* - "+ AppFrame.DEFAULT_TITLE);
		}
	}

	/**
	 * Modifie l'�tat du document comme �tant non-modifi�.
	 * @see FileMenu#setModifiedDocument()
	 */
	private static void setUnmodifiedDocument() {
		FileMenu.modified = false;
		if(FileMenu.currentFile != null) {
			AppFrame.getInstance().setTitle(FileMenu.currentFile.getName() + " - "+ AppFrame.DEFAULT_TITLE);
		}
		else {
			AppFrame.getInstance().setTitle("Nouveau document - "+ AppFrame.DEFAULT_TITLE);
		}
	}

	/**
	 * Retourne vrai si le document pr�sent a �t� modifi�
	 * @return Retourne vrai si le document pr�sent a �t� modifi�
	 */
	public static boolean isModified() {
		return FileMenu.modified;
	}
	
	/**
	 * Lit le fichier sp�cifi� en param�tre
	 * @param f Le fichier
	 * @return Le fichier complet en String
	 */
	private static String readFile(File f) {
		StringBuilder retour = new StringBuilder();
		if(f != null) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(f));

				while(in.ready()) {
					retour.append("\r\n");
					retour.append(in.readLine());
				}
				retour.delete(0, 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retour.toString();

	}

	/**
	 * �crit le texte dans le fichier sp�cifi� en param�tre
	 * @param f Le fichier
	 * @param texte Le texte � �crire dans le fichier
	 */
	private static void writeFile(File f, String texte) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			out.write(texte); 
			out.close(); 

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
