package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lorsqu'il y a une erreur de syntax.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class SyntaxException extends CodeException {
	public SyntaxException() {
		super("Erreur de syntaxe");
	}
}
