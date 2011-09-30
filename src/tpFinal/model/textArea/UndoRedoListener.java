package tpFinal.model.textArea;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import tpFinal.form.AppFrame;

/**
 * Classe qui �coute les �ditions que l'on peut annuler
 * 
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 * @author http://www.lifl.fr/~secq/IUT/SWING/TP_Swing3.html
 */
public class UndoRedoListener implements UndoableEditListener {

	/**
	 * M�thode qui re�oit un �v�nement d'�dition annulable en param�tre,
	 * et mets � jour les variables avec ce param�tre.
	 * 
	 * @param e - �v�nement d'�dition annulable
	 */
	public void undoableEditHappened(UndoableEditEvent e) {
		//Retiens l'�dition et mets � jour les menus.
		AppFrame.getInstance().getAppText().getUndo().addEdit(e.getEdit());
		AppFrame.getInstance().getAppText().getUndoAction().updateUndoState();
		AppFrame.getInstance().getAppText().getRedoAction().updateRedoState();
	}
}

