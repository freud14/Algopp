package tpFinal.model.textArea;

import java.awt.Point;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * �coute le changement de position du curseur et de la s�lection
 * 
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 * @author http://www.lifl.fr/~secq/IUT/SWING/TP_Swing3.html
 */
public class CursorListener implements CaretListener {

	private boolean isSelected = false;
	
	private Point selection; // X = d�but de la s�lection, Y = fin de la s�lection 

	/**
	 * G�re les �v�nements de changement du curseur (changement de position/s�lection)
	 */
	public void caretUpdate(CaretEvent e) {
		//Obtient l'emplacement dans le texte.
		int dot = e.getDot();
		int mark = e.getMark();
		
		//Aucune s�lection si dot == mark
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
	 * La fonction retourne s'il y a une s�lection ou non
	 * @return isSelected
	 */
	public boolean isSelected(){
		return this.isSelected;
	}
	
	/**
	 * La fonction retourne l'index de s�lection
	 * @return Retourne l'index de s�lection
	 */
	public Point getSelection(){
		return this.selection;
	}	
}
