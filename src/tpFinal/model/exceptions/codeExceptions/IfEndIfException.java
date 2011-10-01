package tpFinal.model.exceptions.codeExceptions;

import tpFinal.model.exceptions.CodeException;

/**
 * Exception survenant lors d'une fin de condition (FinSi) 
 *  qui ne finie pas un segment conditionnel ou lors d'une condition
 *  alternative (Sinon/SinonSi) qui n'est psa dans un segment conditionnel.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class IfEndIfException extends CodeException {
	public IfEndIfException() {
		super("FinSi attendu/inattendu");
	}
}
