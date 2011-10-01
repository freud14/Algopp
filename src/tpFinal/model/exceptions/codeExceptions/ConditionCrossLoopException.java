package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lorsque les bornes d'une condition croise les bornes d'une boucles.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class ConditionCrossLoopException extends CodeException {
	public ConditionCrossLoopException() {
		super("Une condition enjambe une boucle TantQue");
	}
}
