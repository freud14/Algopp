package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lorsque qu'il manque une parenth�se.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class MissingParenthesisException extends CodeException {
	public MissingParenthesisException() {
		super("Parenth�se attendue");
	}
}
