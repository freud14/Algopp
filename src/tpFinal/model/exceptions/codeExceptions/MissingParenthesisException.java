package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lorsque qu'il manque une parenthèse.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class MissingParenthesisException extends CodeException {
	public MissingParenthesisException() {
		super("Parenthèse attendue");
	}
}
