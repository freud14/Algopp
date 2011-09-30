package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lors d'une opération arithmétique invalide.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class ArithmeticSyntaxException extends CodeException {
	public ArithmeticSyntaxException() {
		super("Opération arithmétique invalide");
	}
}
