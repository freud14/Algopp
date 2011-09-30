package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lorsqu'il y a une erreur de syntax.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class SyntaxException extends CodeException {
	public SyntaxException() {
		super("Erreur de syntaxe");
	}
}
