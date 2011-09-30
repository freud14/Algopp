package tpFinal.model;

import java.util.Stack;

import javax.swing.JOptionPane;

import tpFinal.form.AppFrame;
import tpFinal.form.AppText;
import tpFinal.model.exceptions.CodeException;
import tpFinal.model.exceptions.codeExceptions.ArithmeticSyntaxException;
import tpFinal.model.exceptions.codeExceptions.ConditionCrossLoopException;
import tpFinal.model.exceptions.codeExceptions.IfEndIfException;
import tpFinal.model.exceptions.codeExceptions.MissingParenthesisException;
import tpFinal.model.exceptions.codeExceptions.ReadingException;
import tpFinal.model.exceptions.codeExceptions.StackException;
import tpFinal.model.exceptions.codeExceptions.SyntaxException;
import tpFinal.model.exceptions.codeExceptions.TypeMissMatchException;
import tpFinal.model.exceptions.codeExceptions.UnknownVariableException;
import tpFinal.model.exceptions.codeExceptions.UseOfReservedWord;
import tpFinal.model.exceptions.codeExceptions.VariableRedefinitionException;
import tpFinal.model.exceptions.codeExceptions.WhileEndWhileException;
import tpFinal.model.prohibitedWords.WordsCondition;
import tpFinal.model.prohibitedWords.WordsDeclaration;
import tpFinal.model.prohibitedWords.WordsInOut;
import tpFinal.model.prohibitedWords.WordsLoop;

//Contenir erreur & d�couper le texte en section avec mots cl�s
//  Ex�cute le code en faisant des mini-parse et le divise pour le donner au parseur
/**
 * G�re les erreurs retourn�s par le parser et ex�cute le code en s'aidant du parser
 * 	(qui lui dit si une condition est vrai/faux) et g�re les conditions � l'aide d'une
 * 	pile (pour trouver les sinon/finsi).
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class Executor {
	
	private static final String READ_MSG = "Veuillez entrez la valeur ";
	private static final String READ_INTEGER = "de l'entier : ";
	private static final String READ_STRING = "de la cha�ne de caract�res : ";
	private static final String STR_AFFECTATION = "<-";
	
	private String[] txtLines;
	
	//Num�ro de ligne pour la lecture (utile en tant que propri�t� pour les boucles)
	private int lineNbToRead = 0;
	
	//profondeur des Si + TantQue
	private Stack<ReadMode> conditionalStack = new Stack<ReadMode>();
	
	//profondeur des TantQue
	private Stack<WhileNode> whileStack = new Stack<WhileNode>();
	
	//Doit �tre passer au Parser
	private Environment variables = new Environment();
	
	/**
	 * Cr�e un instance de Executor afin d'ex�cuter le texte entr� par l'utilisateur.
	 * @param txt	Le texte entr� par l'utilisateur.
	 */
	public Executor(AppText txt) {
		AppFrame.getInstance().getAppExecuteBar().clearErrorZone();
		AppFrame.getInstance().getAppExecuteBar().clearDisplayZone();
		
		//On s�pare dans un tableau en enlevant les espaces
		this.txtLines = txt.getText().toLowerCase().split("\n");
		
		//On enl�ve les espaces
		for (int i = 0; i < this.txtLines.length; i++) {
			this.txtLines[i] = this.txtLines[i].trim();
		}
	}
	
	/**
	 * Lance l'ex�cution du code entr� par le client.
	 */
	public void execute() {
		int lng = this.txtLines.length;

		this.conditionalStack.push(ReadMode.READ_TRUE);
		try {
		//Tant que l'ex�cution du programme n'est pas termin�e
		for (this.lineNbToRead = 0; this.lineNbToRead < lng; this.lineNbToRead++) {
			//Bool�en disant si une action a �t� effectu�e pour cette ligne
			//  (respect des normes du C�gep --> pas de 'continue')
			boolean isSomething = false;
			//Si la ligne n'est pas vide
			if (this.txtLines[this.lineNbToRead].isEmpty() == false) {
				//Type de lecture TRUE
				if (this.conditionalStack.peek().equals(ReadMode.READ_TRUE)) {
					//CHECK : D�claration
					if (true == (isSomething = this.isDeclaration()));
					//CHECK : IN-OUT
					else if (true == (isSomething = this.isInOut()));
					//CHECK : Affectation
					else if (isSomething = this.isAssignment());
				}
				//Type de lecture TRUE & FALSE
				if (isSomething == false && 
						this.conditionalStack.peek().equals(ReadMode.READ_BREAK) == false) {
					//CHECK : elseif
					if (true == (isSomething = this.isElseIf()));
					//CHECK : else
					else if (isSomething = this.isElse());
				}
				//Type de lecture TRUE & FALSE & BREAK
				if (isSomething == false) {
					//CHECK : if
					if (true == (isSomething = this.isIf()));
					//CHECK : endif
					else if (true == (isSomething = this.isEndIf()));
					//CHECK : while
					else if (true == (isSomething = this.isWhile()));
					//CHECK : endWhile
					else if (isSomething = this.isEndWhile());
				}
				//CHECK : Stack --> Aucun type de lecture (conditionalStack endommag�e)
				//Pas besoin de tester whileStack car au seul .pop() possible, 
				// on a d�j� test� qu'il serait valide dans sa m�thode.
				if (this.conditionalStack.isEmpty()) {
					throw new StackException();
				}
				//CHECK : Unknown --> Ligne qui ne fait rien && non vide
				else if (isSomething == false && this.conditionalStack.peek() == ReadMode.READ_TRUE) {
					throw new SyntaxException();
				}
			}
		}
		//�vite d'�crire une ~12aine de catch d'exceptions
		//De plus, on n'a pas d'exceptions throwable catch� ici autres que celles cr��s
		// cependant on peut quand m�me catcher celles qui peuvent survenir de Java.
		//Voir les "import" pour avoir un bon r�sum� de ceux-ci.
		} catch (CodeException e) { 
			this.showErrorAtLine(e.getMessage(), this.lineNbToRead);
		}
	}
	
	//=====D�BUT M�THODES DE D�COUPAGE
	
	//Dit si la ligne repr�sente une d�claration de variable.
	private boolean isDeclaration() throws SyntaxException, 
	UnknownVariableException, VariableRedefinitionException, MissingParenthesisException, 
	ArithmeticSyntaxException, UseOfReservedWord, TypeMissMatchException {
		boolean rtr = false;
		for (WordsDeclaration w : WordsDeclaration.values()) {
			if (this.txtLines[this.lineNbToRead].startsWith(w.getWord() + " ") ||
					this.txtLines[this.lineNbToRead].startsWith(w.getWord() + "(")) {
				Parser.declaration(this.txtLines[this.lineNbToRead], this.variables);
				rtr = true;
			}
		}
		return rtr;
	}

	//Dit si la ligne repr�sente une entr�e ou une sortie de variable.
	private boolean isInOut() throws SyntaxException, ArithmeticSyntaxException, 
	ReadingException, UnknownVariableException {
		boolean rtr = false;
		if (this.txtLines[this.lineNbToRead].startsWith(WordsInOut.AFFICHER.getWord() + " ") ||
				this.txtLines[this.lineNbToRead].startsWith(WordsInOut.AFFICHER.getWord() + "(")) {
			//Le parseur doit pouvoir dire si la variable existe et est un entier/String
			this.print(Parser.stringConcat(
					this.txtLines[this.lineNbToRead].substring(
							WordsInOut.AFFICHER.getWord().length()), this.variables));
			rtr = true;
		}
		else if (this.txtLines[this.lineNbToRead].startsWith(WordsInOut.LIRE.getWord() + " ")) {
			this.readValue(this.txtLines[this.lineNbToRead].substring(WordsInOut.LIRE.getWord().length()));
			rtr = true;
		}
		return rtr;
	}

	//Dit si la ligne repr�sente une affectation de variable.
	private boolean isAssignment() throws SyntaxException, UnknownVariableException, 
	MissingParenthesisException, ArithmeticSyntaxException, TypeMissMatchException {
		boolean rtr = false;
		if (this.txtLines[this.lineNbToRead].contains(Executor.STR_AFFECTATION) &&
				!(	this.txtLines[this.lineNbToRead].startsWith(WordsCondition.SI.getWord() + " ") ||
					this.txtLines[this.lineNbToRead].startsWith(WordsCondition.SI.getWord() + "(") ||
					this.txtLines[this.lineNbToRead].startsWith(WordsCondition.SINON_SI.getWord() + " ") ||
					this.txtLines[this.lineNbToRead].startsWith(WordsCondition.SINON_SI.getWord() + "(")) ) {
				Parser.assignment(
						this.txtLines[this.lineNbToRead],
						this.variables);
				rtr = true;
		}
		return rtr;
	}

	//Dit si la ligne repr�sente un d�but de segment conditionnel d�coulant d'une condition ("Si").
	private boolean isIf() throws MissingParenthesisException, SyntaxException, 
	UnknownVariableException, ArithmeticSyntaxException, TypeMissMatchException {
		boolean rtr = false;
		if (this.txtLines[this.lineNbToRead].startsWith(WordsCondition.SI.getWord() + " ") ||
			this.txtLines[this.lineNbToRead].startsWith(WordsCondition.SI.getWord() + "(") ) {
			if (this.whileStack.isEmpty() == false) {
				this.whileStack.peek().incAntiIf();
			}
			if (this.conditionalStack.peek().equals(ReadMode.READ_TRUE)) {
				this.conditionalStack.push(
						Parser.condition(this.txtLines[this.lineNbToRead].substring(
								WordsCondition.SI.getWord().length()), this.variables) ? 
								ReadMode.READ_TRUE : ReadMode.READ_FALSE);
			}
			else {
				this.conditionalStack.push(ReadMode.READ_BREAK);
			}
			rtr = true;
		}
		return rtr;
	}

	//Dit si la ligne repr�sente un d�but de segment conditionnel d�coulant d'au moins une condition fausse
	// en relan�ant une autre condition ("SinonSi").
	private boolean isElseIf() throws ConditionCrossLoopException, IfEndIfException, 
	MissingParenthesisException, SyntaxException, UnknownVariableException, 
	ArithmeticSyntaxException, TypeMissMatchException {
		boolean rtr = false;
		if (this.txtLines[this.lineNbToRead].startsWith(WordsCondition.SINON_SI.getWord() + " ") ||
			this.txtLines[this.lineNbToRead].startsWith(WordsCondition.SINON_SI.getWord() + "(") ) {
			if (this.whileStack.isEmpty() == false &&
					this.whileStack.peek().getAntiIf() == 0) {
				throw new ConditionCrossLoopException();
			}
			if (this.conditionalStack.isEmpty()) {
				throw new IfEndIfException();
			}
			else {
				switch (this.conditionalStack.peek()) {
				case READ_TRUE : 
					//On a lu du code vrai, on a pas � retester si on veut ex�cuter le reste
					this.conditionalStack.set(this.conditionalStack.size() - 1, ReadMode.READ_BREAK);
					break;
				case READ_FALSE : 
					this.conditionalStack.set(this.conditionalStack.size() - 1,
						Parser.condition(this.txtLines[this.lineNbToRead].substring(
								WordsCondition.SINON_SI.getWord().length()), this.variables) ? 
								ReadMode.READ_TRUE : ReadMode.READ_FALSE);
				}
			}
			rtr = true;
		}
		return rtr;
	}

	//Dit si la ligne repr�sente un d�but de segment conditionnel 
	// d�coulant d'au moins une condition fausse ("Sinon").
	private boolean isElse() throws ConditionCrossLoopException, IfEndIfException {
		boolean rtr = false;
		if (
				this.txtLines[this.lineNbToRead].startsWith(WordsCondition.SINON.getWord())) {
			if (this.whileStack.isEmpty() == false &&
					this.whileStack.peek().getAntiIf() == 0) {
				throw new ConditionCrossLoopException();
			}
			else if (this.conditionalStack.isEmpty()) {
				throw new IfEndIfException();
			}
			else {
				switch (this.conditionalStack.peek()) {
				case READ_TRUE : 
					//On a lu du code vrai, on a pas � retester si on veut ex�cuter le reste
					this.conditionalStack.set(this.conditionalStack.size() - 1, ReadMode.READ_BREAK);
					break;
				case READ_FALSE : 
					this.conditionalStack.set(this.conditionalStack.size() - 1, ReadMode.READ_TRUE);
				}
			}
			rtr = true;
		}
		return rtr;
	}

	//Dit si la ligne repr�sente une fin de segment conditionnel.
	private boolean isEndIf() throws ConditionCrossLoopException, IfEndIfException {
		boolean rtr = false;
		if (this.txtLines[this.lineNbToRead].startsWith(WordsCondition.FIN_SI.getWord())) {
			if (this.conditionalStack.isEmpty() == false) {
				this.conditionalStack.pop();
				if (this.whileStack.isEmpty() == false) {
					if (this.whileStack.peek().getAntiIf() == 0) {
						throw new ConditionCrossLoopException();
					}
					this.whileStack.peek().decAntiIf();
				}
			}
			else {
				throw new IfEndIfException();
			}
			rtr = true;
		}
		return rtr;
	}
	
	//Dit si la ligne repr�sente un d�but de segment conditionnel r�p�t� (une boucle; TantQue).
	private boolean isWhile() throws MissingParenthesisException, SyntaxException, 
	UnknownVariableException, ArithmeticSyntaxException, TypeMissMatchException {
		boolean rtr = false;
		if (this.txtLines[this.lineNbToRead].startsWith(WordsLoop.TANTQUE.getWord() + " ") ||
				this.txtLines[this.lineNbToRead].startsWith(WordsLoop.TANTQUE.getWord() + "(")) {
			//Si on est en mode de lecture READ_TRUE et que la condition du while est vrai
			if (this.conditionalStack.isEmpty() == false &&
					(this.conditionalStack.peek() == ReadMode.READ_TRUE) &&
					Parser.condition(this.txtLines[this.lineNbToRead].substring(
							WordsLoop.TANTQUE.getWord().length()), this.variables)) {
				this.conditionalStack.push(ReadMode.READ_TRUE);
				this.whileStack.push(new WhileNode(this.lineNbToRead, 
						this.txtLines[this.lineNbToRead].substring(
						WordsLoop.TANTQUE.getWord().length())));
			}
			else {
				this.conditionalStack.push(ReadMode.READ_BREAK);
				this.whileStack.push(new WhileNode(this.lineNbToRead, "1=2"));
			}
			rtr = true;
		}
		return rtr;
	}

	//Dit si la ligne repr�sente une fin de segment conditionnel r�p�t� (une fin de boucle; FinTantQue).
	private boolean isEndWhile() throws WhileEndWhileException, ConditionCrossLoopException, 
	MissingParenthesisException, SyntaxException, UnknownVariableException, ArithmeticSyntaxException, 
	TypeMissMatchException {
		boolean rtr = false;
		if (this.txtLines[this.lineNbToRead].startsWith(WordsLoop.FINTANTQUE.getWord())) {
			//Si on a pas rencontr� de d�but de boucle
			if (this.whileStack.isEmpty()) {
				throw new WhileEndWhileException();
			}
			//Un if a �t� commenc� mais pas fini
			else if (this.whileStack.peek().getAntiIf() != 0) {
				throw new ConditionCrossLoopException();
			}
			//Si on est en mode de lecture READ_TRUE, et que la condition est vrai
			// (les 2 premi�res conditions ne servent qu'� optimiser car un parsing est co�teux.
			else if (this.conditionalStack.isEmpty() == false &&
					this.conditionalStack.peek() == ReadMode.READ_TRUE &&
					Parser.condition(
					this.whileStack.peek().getCondition(), 
					this.variables)) {
				//On change le cours de la lecture (retour au while)
				this.lineNbToRead = this.whileStack.peek().getLineNumber();
			}
			else { //L'ex�cution intra-boucle est termin�e
				this.whileStack.pop();
				this.conditionalStack.pop();
			}
			rtr = true;
		}
		return rtr;
	}
	
	//=====FIN M�THODES DE D�COUPAGE
	
	//Affiche une valeur � l'utilisateur dans la fen�tre
	private void print(String toPrint) {
		AppFrame.getInstance().getAppExecuteBar().displayNewLine(toPrint);
	}
	
	//Permet de lire une valeur
	private void readValue(String varName) throws ReadingException, UnknownVariableException {
		varName = varName.trim();
		if (this.variables.getVariableByName(varName) != null) {
			if (this.variables.getVariableByName(varName) instanceof Boolean) {
				throw new ReadingException();
			}
			else if (this.variables.getVariableByName(varName) instanceof Integer) {
				String in = new Integer(
						Integer.parseInt(
								JOptionPane.showInputDialog(
								Executor.READ_MSG + Executor.READ_INTEGER))).toString();
				this.variables.setVariableByName(varName, in != null ? in : new Integer(0));
			}
			else { //On consid�re que c'est un String
				String in = JOptionPane.showInputDialog(
								Executor.READ_MSG + Executor.READ_STRING);
				this.variables.setVariableByName(varName, in != null ? in : new String(""));
			}
		}
		else {
			throw new UnknownVariableException(varName);
		}
	}
	
	//Affiche les erreurs
	private void showErrorAtLine(String error, int line) {
		AppFrame.getInstance().getAppExecuteBar().displayNewError(error, line + 1);
	}
	
	//=========FIN DES M�THODES APPARTENANT DIRECTEMENT � LA CLASSE EXECUTOR
	
	/**
	 * Classe repr�sentant l'objet utilis� pour g�rer les boucles.
	 */
	private class WhileNode {
		
		private int lineNb;
		private String condition;
		
		/**
		 * Pr�vient qu'un if/elseif/else/endif "enjambe" un TantQue.
		 * S'incr�mente lors d'un if,
		 * se d�cr�mente lors d'un endif,
		 * si == 0 lors d'un elseif/else, on throw l'exception ConditionCrossLoopException
		 */
		private int antiCrosser = 0;
		
		public WhileNode(int lineNumber, String cond) {
			this.lineNb = lineNumber;
			this.condition = cond;
		}

		public int getLineNumber() {
			return this.lineNb;
		}

		public String getCondition() {
			return this.condition;
		}

		public void incAntiIf() {
			this.antiCrosser++;
		}

		public void decAntiIf() {
			this.antiCrosser--;
		}

		public int getAntiIf() {
			return this.antiCrosser;
		}
	}

	/**
	 * Enum�ration repr�sentant les types de lecture possibles.
	 * Est utilis�e pour g�rer boucles & conditions.
	 */
	private enum ReadMode {
		//Types de lectures
		READ_TRUE, //On lit le code normalement
		READ_FALSE,//On lit le code sans ex�cuter
		READ_BREAK;//On lit le code jusqu'� la borne de fin du If/While ayant mis le mode de lecture en BREAK
	}

}
