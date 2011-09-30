package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lorsqu'on effectue des op�rations sur deux types 
 *  diff�rents incompatibles.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class TypeMissMatchException extends CodeException {
	public TypeMissMatchException() {
		super("Les types ne sont pas correspondants");
	}
}
