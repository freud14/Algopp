package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lors d'une fin de boucle (FinTantQue) 
 *  qui ne finie pas un segment conditionnel bouclé.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class WhileEndWhileException extends CodeException {
	public WhileEndWhileException() {
		super("FinTantQue attendu/inattendu");
	}
}
