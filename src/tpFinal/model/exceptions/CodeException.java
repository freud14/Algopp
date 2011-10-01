package tpFinal.model.exceptions;

/**
 * Classe mère de nos exceptions.
 * Permet de catcher l'ensemble de celles-ci simplement en catchant
 * celle-ci. Elle n'est pas directement utilisable (abstract).
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public abstract class CodeException extends Exception {
	protected CodeException(String message) {
		super(message);
	}
}
