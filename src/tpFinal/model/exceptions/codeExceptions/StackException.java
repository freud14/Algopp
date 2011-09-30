package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lorsque la pile d'exécution à la fin du programme
 *  n'est pas vide.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class StackException extends CodeException {
	public StackException() {
		super("Mauvaise utilisation des conditions et/ou des boucles");
	}
}
