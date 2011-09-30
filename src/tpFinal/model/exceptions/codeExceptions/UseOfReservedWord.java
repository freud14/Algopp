package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lorsque le client utilise un mot r�serv� incorrectement.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class UseOfReservedWord extends CodeException {
	public UseOfReservedWord() {
		super("Mot r�serv� comme nom de variable");
	}
}
