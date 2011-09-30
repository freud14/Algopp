package tpFinal.model.textArea;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.undo.CannotRedoException;

import tpFinal.form.AppFrame;

/**
 *  Classe qui s'occupe du "Refaire" ("Redo")
 * 
 *  @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 *  @author http://www.lifl.fr/~secq/IUT/SWING/TP_Swing3.html
 */
public class RedoAction extends AbstractAction {
	
	/**
	 * Constructeur de RedoAction.
	 */
	public RedoAction() {
		super("Rétablir");
		setEnabled(false);
	}
	
	/**
	 * Lors d'un appel, la méthode essaie de refaire la dernière action, et si impossible,
	 * affichage d'un message d'erreur. Changement du statut des Undo et Redo.
	 */
	public void doRedo(){
		try {
			AppFrame.getInstance().getAppText().getUndo().redo();
		} catch (CannotRedoException ex) {
		}
		AppFrame.getInstance().getAppText().getRedoAction().updateRedoState();
		AppFrame.getInstance().getAppText().getUndoAction().updateUndoState();
	}

	/**
	 * Mets à jour l'état du Redo avec le dernier événement possible à refaire.
	 */
	protected void updateRedoState() {
		if (AppFrame.getInstance().getAppText().getUndo().canRedo()) {
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}

	/**
	 * Appelle la méthode doRedo()
	 */
	public void actionPerformed(ActionEvent e) {
		doRedo();
	}
} 
