package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lorsqu'on essai de lire un booléen.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class ReadingException extends CodeException {
	public ReadingException() {
		super("Lecture de booléen impossible");
	}
}
