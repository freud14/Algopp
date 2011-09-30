package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lors de l'utilisation d'une variable inconnue.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class UnknownVariableException extends CodeException {
	public UnknownVariableException(String varName) {
		super("Variable '" + varName + "' inconnue");
	}
}
