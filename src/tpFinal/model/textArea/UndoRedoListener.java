package tpFinal.model.textArea;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import tpFinal.form.AppFrame;

/**
 * Classe qui écoute les éditions que l'on peut annuler
 * 
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 * @author http://www.lifl.fr/~secq/IUT/SWING/TP_Swing3.html
 */
public class UndoRedoListener implements UndoableEditListener {

	/**
	 * Méthode qui reçoit un événement d'édition annulable en paramètre,
	 * et mets à jour les variables avec ce paramètre.
	 * 
	 * @param e - Événement d'édition annulable
	 */
	public void undoableEditHappened(UndoableEditEvent e) {
		//Retiens l'édition et mets à jour les menus.
		AppFrame.getInstance().getAppText().getUndo().addEdit(e.getEdit());
		AppFrame.getInstance().getAppText().getUndoAction().updateUndoState();
		AppFrame.getInstance().getAppText().getRedoAction().updateRedoState();
	}
}

