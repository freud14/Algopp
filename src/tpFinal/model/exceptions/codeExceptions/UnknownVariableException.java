package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lors de l'utilisation d'une variable inconnue.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class UnknownVariableException extends CodeException {
	public UnknownVariableException(String varName) {
		super("Variable '" + varName + "' inconnue");
	}
}
