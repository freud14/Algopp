package tpFinal.model.exceptions;

/**
 * Classe m�re de nos exceptions.
 * Permet de catcher l'ensemble de celles-ci simplement en catchant
 * celle-ci. Elle n'est pas directement utilisable (abstract).
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public abstract class CodeException extends Exception {
	protected CodeException(String message) {
		super(message);
	}
}
