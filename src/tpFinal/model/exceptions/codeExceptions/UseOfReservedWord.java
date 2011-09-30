package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lorsque le client utilise un mot réservé incorrectement.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class UseOfReservedWord extends CodeException {
	public UseOfReservedWord() {
		super("Mot réservé comme nom de variable");
	}
}
