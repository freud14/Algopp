package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lorsque la pile d'ex�cution � la fin du programme
 *  n'est pas vide.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class StackException extends CodeException {
	public StackException() {
		super("Mauvaise utilisation des conditions et/ou des boucles");
	}
}
