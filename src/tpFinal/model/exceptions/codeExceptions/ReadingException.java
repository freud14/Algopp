package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lorsqu'on essai de lire un bool�en.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class ReadingException extends CodeException {
	public ReadingException() {
		super("Lecture de bool�en impossible");
	}
}
