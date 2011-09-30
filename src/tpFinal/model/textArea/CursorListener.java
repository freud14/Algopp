package tpFinal.model.textArea;

import java.awt.Point;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * Écoute le changement de position du curseur et de la sélection
 * 
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 * @author http://www.lifl.fr/~secq/IUT/SWING/TP_Swing3.html
 */
public class CursorListener implements CaretListener {

	private boolean isSelected = false;
	
	private Point selection; // X = début de la sélection, Y = fin de la sélection 

	/**
	 * Gère les événements de changement du curseur (changement de position/sélection)
	 */
	public void caretUpdate(CaretEvent e) {
		//Obtient l'emplacement dans le texte.
		int dot = e.getDot();
		int mark = e.getMark();
		
		//Aucune sélection si dot == mark
		if (dot == mark) {  
			this.isSelected = false;
			this.selection = new Point(mark, dot);
		} else if (dot < mark) {
			this.isSelected = true;
			this.selection = new Point(dot, mark);
		} else {
			this.isSelected = true;
			this.selection = new Point(mark, dot);
		}
	}

	/**
	 * La fonction retourne s'il y a une sélection ou non
	 * @return isSelected
	 */
	public boolean isSelected(){
		return this.isSelected;
	}
	
	/**
	 * La fonction retourne l'index de sélection
	 * @return Retourne l'index de sélection
	 */
	public Point getSelection(){
		return this.selection;
	}	
}
