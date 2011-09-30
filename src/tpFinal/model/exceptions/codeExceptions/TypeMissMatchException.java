package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lorsqu'on effectue des opérations sur deux types 
 *  différents incompatibles.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class TypeMissMatchException extends CodeException {
	public TypeMissMatchException() {
		super("Les types ne sont pas correspondants");
	}
}
