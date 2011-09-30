package tpFinal.model.textArea;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.undo.CannotRedoException;

import tpFinal.form.AppFrame;

/**
 *  Classe qui s'occupe du "Refaire" ("Redo")
 * 
 *  @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 *  @author http://www.lifl.fr/~secq/IUT/SWING/TP_Swing3.html
 */
public class RedoAction extends AbstractAction {
	
	/**
	 * Constructeur de RedoAction.
	 */
	public RedoAction() {
		super("R�tablir");
		setEnabled(false);
	}
	
	/**
	 * Lors d'un appel, la m�thode essaie de refaire la derni�re action, et si impossible,
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
	 * Mets � jour l'�tat du Redo avec le dernier �v�nement possible � refaire.
	 */
	protected void updateRedoState() {
		if (AppFrame.getInstance().getAppText().getUndo().canRedo()) {
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}

	/**
	 * Appelle la m�thode doRedo()
	 */
	public void actionPerformed(ActionEvent e) {
		doRedo();
	}
} 
