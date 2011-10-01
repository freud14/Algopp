package tpFinal.model.textArea;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.undo.CannotUndoException;

import tpFinal.form.AppFrame;
import tpFinal.model.FileMenu;

/**
 *  Classe qui s'occupe du Annuler (undo)
 * 
 *  @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 *  @author http://www.lifl.fr/~secq/IUT/SWING/TP_Swing3.html
 */
public	class UndoAction extends AbstractAction {

	/**
	 * Constructeur de UndoAction.
	 */
	public UndoAction() {
		super("Annuler");
		setEnabled(false);
	}

	/**
	 * Appelle la méthode doUndo()
	 */
	public void actionPerformed(ActionEvent e) {
		doUndo();
	}

	/**
	 * Lors d'un appel, la méthode essaie d'annuler la dernière action exécutée, et si impossible,
	 * affichage d'un message d'erreur. Changement du statut des Undo et Redo.
	 */
	public void doUndo(){
		try {
			AppFrame.getInstance().getAppText().getUndo().undo();
		} catch (CannotUndoException ex) {
			System.out.println("Plus rien à annuler");
		}
		AppFrame.getInstance().getAppText().getUndoAction().updateUndoState();
		AppFrame.getInstance().getAppText().getRedoAction().updateRedoState();
	}

	/**
	 * Mets à jour l'état du Undo avec le dernier événement annulable.
	 */
	protected void updateUndoState() {
		if (AppFrame.getInstance().getAppText().getUndo().canUndo()) {
			setEnabled(true);

			//On mets à jour la modification du document.
			FileMenu.setModifiedDocument();

			//putValue(Action.NAME, AppText.getUndo().getUndoPresentationName());
		} else {
			setEnabled(false);
			//putValue(Action.NAME, "Undo");
		}
	}      
}