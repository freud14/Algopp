package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lorsqu'on tente de redéfinir une variable.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class VariableRedefinitionException extends CodeException {
	public VariableRedefinitionException(String varName) {
		super("Variable '" + varName + "' redéfinie");
	}
}
