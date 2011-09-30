package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lorsqu'on tente de red�finir une variable.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class VariableRedefinitionException extends CodeException {
	public VariableRedefinitionException(String varName) {
		super("Variable '" + varName + "' red�finie");
	}
}
