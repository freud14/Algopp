package tpFinal.form;

import java.awt.Font;

import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;

import tpFinal.model.textArea.CursorListener;
import tpFinal.model.textArea.RedoAction;
import tpFinal.model.textArea.UndoAction;
import tpFinal.model.textArea.UndoRedoListener;

/**
 * G�re la zone de contexte o� le pseudo-code est �crit et 
 * g�re les actions � Annuler � et � R�tablir �.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class AppText extends JTextPane {

	private StyledDocument doc = this.getStyledDocument();
	//les objets pour les actions de Undo et de Redo
	private UndoManager undo = new UndoManager();
	private UndoAction undoAction;
	private RedoAction redoAction;
	private CursorListener caretListener;

	/**
	 * Cr�e la zone de texte.
	 */
	protected AppText() {
		super();
		this.setDocument(this.doc);
		this.caretListener = new CursorListener();
		this.addCaretListener(this.caretListener);
		this.undoAction = new UndoAction();
		this.redoAction = new RedoAction();
		this.doc.addUndoableEditListener(new UndoRedoListener());	
		this.getInputMap().put(KeyStroke.getKeyStroke("ctrl H"), "none");
		this.setFont(new Font("Courier New", this.getFont().getStyle(), 16));
	}

	/**
	 * Simule la touche Delete du clavier
	 * S'il y a une s�lection, elle est supprim�
	 * S'il n'y a pas de s�lection, elle supprime le caract�re apr�s le curseur
	 */
	protected void delete(){
		//S'il n'y a pas de s�lection, on s�lectionne le caract�re apr�s le curseur
		if (!this.caretListener.isSelected()){
			this.select(this.caretListener.getSelection().x, this.caretListener.getSelection().x);
		}
		this.replaceSelection("");
	}

	/**
	 * Retourne le UndoAction
	 * @return Retourne le UndoAction
	 */
	public UndoAction getUndoAction() {
		return this.undoAction;
	}

	/**
	 * Retourne le RedoAction
	 * @return Retourne le RedoAction
	 */
	public RedoAction getRedoAction() {
		return this.redoAction;
	}

	/**
	 * Retourne le UndoManager
	 * @return Retourne le UndoManager
	 */
	public UndoManager getUndo() {
		return this.undo;
	}
	
	/**
	 * Retourne le CaretListener
	 * @return Retourne le CaretListener
	 */
	public CursorListener getCaretListener() {
		return this.caretListener;
	}
	
}
