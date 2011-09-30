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
 * Gère la zone de contexte où le pseudo-code est écrit et 
 * gère les actions « Annuler » et « Rétablir ».
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class AppText extends JTextPane {

	private StyledDocument doc = this.getStyledDocument();
	//les objets pour les actions de Undo et de Redo
	private UndoManager undo = new UndoManager();
	private UndoAction undoAction;
	private RedoAction redoAction;
	private CursorListener caretListener;

	/**
	 * Crée la zone de texte.
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
	 * S'il y a une sélection, elle est supprimé
	 * S'il n'y a pas de sélection, elle supprime le caractère après le curseur
	 */
	protected void delete(){
		//S'il n'y a pas de sélection, on sélectionne le caractère après le curseur
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
