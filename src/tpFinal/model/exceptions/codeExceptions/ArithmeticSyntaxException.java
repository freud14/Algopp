package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lors d'une op�ration arithm�tique invalide.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class ArithmeticSyntaxException extends CodeException {
	public ArithmeticSyntaxException() {
		super("Op�ration arithm�tique invalide");
	}
}
