package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lors d'une fin de boucle (FinTantQue) 
 *  qui ne finie pas un segment conditionnel boucl�.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class WhileEndWhileException extends CodeException {
	public WhileEndWhileException() {
		super("FinTantQue attendu/inattendu");
	}
}
