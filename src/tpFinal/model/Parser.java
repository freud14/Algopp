package tpFinal.model;

import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tpFinal.model.exceptions.codeExceptions.ArithmeticSyntaxException;
import tpFinal.model.exceptions.codeExceptions.MissingParenthesisException;
import tpFinal.model.exceptions.codeExceptions.SyntaxException;
import tpFinal.model.exceptions.codeExceptions.TypeMissMatchException;
import tpFinal.model.exceptions.codeExceptions.UnknownVariableException;
import tpFinal.model.exceptions.codeExceptions.UseOfReservedWord;
import tpFinal.model.exceptions.codeExceptions.VariableRedefinitionException;
import tpFinal.model.prohibitedWords.WordsBoolean;
import tpFinal.model.prohibitedWords.WordsCondition;
import tpFinal.model.prohibitedWords.WordsDeclaration;
import tpFinal.model.prohibitedWords.WordsInOut;
import tpFinal.model.prohibitedWords.WordsIntraCondition;
import tpFinal.model.prohibitedWords.WordsLoop;

/**
 * Cette classe g�re toutes les d�clarations, affections et concat�nation de variables.
 * Elle prend �galement en charge les conditions et les expressions math�matiques. 
 * 
 * Toutes les m�thodes de cette classe sont statiques afin d'�viter de cr�er des objets
 * afin d'utiliser que des m�thodes utilitaires.
 * 
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class Parser {

	/**
	 * Ex�cute une condition "Si" afin de savoir si la condition est valide.  Cette m�thode
	 * v�rifie que la condition est valide.  Cette m�thode prend �galement en charge
	 * les op�rations arithm�tiques ainsi que les variables.
	 * 
	 * Bogue Connu: Un condition du type (bob = 3+(5+6 ET 4-2) = martin) serait accept�e.
	 * 
	 * @param condition La condition en entier
	 * @param environment L'environnement du programme lors de l'�x�cution de la condition.
	 * @return Retourne vrai si la condition est valide sinon faux.
	 * @throws MissingParenthesisException
	 * @throws SyntaxException
	 * @throws UnknownVariableException
	 * @throws ArithmeticSyntaxException
	 * @throws TypeMissMatchException
	 */
	protected static boolean condition(String condition, Environment environment) throws MissingParenthesisException, SyntaxException, UnknownVariableException, ArithmeticSyntaxException, TypeMissMatchException {

		boolean valid = false;

		condition = condition.trim().toLowerCase();

		//Validation des parenth�ses
		if (!Parser.isValidParenthesis(condition)) {
			throw new MissingParenthesisException();
		}

		//Les autres erreurs seront trouv� par ma regex
		String regVariable = "[\\s\\(]*([\"].*[\"]|(\\-)?[\\-\\s]*[a-z0-9_]+([\\s\\)]*[\\+\\-\\*/%\\.][\\-\\s]*[\\s\\(]*[a-z0-9_]+)*)[\\s\\)]*";
		String regex = "[\\s\\(]*"+regVariable+"(<>|<=|>=|=|<|>)"+regVariable+"[\\s\\)]*(( )*(et|ou)( )*[\\s\\(]*"+regVariable+"(<>|<=|>=|=|<|>)"+regVariable+"[\\s\\)]*)*";
		Pattern pattern = Pattern.compile(regex);  
		Matcher matcher = pattern.matcher(condition);  
		if(!matcher.matches()){  
			throw new SyntaxException();
		}

		//Remplace les variables par des valeurs
		condition = Parser.replaceVariable(condition, environment);

		//Remplace les et/ou par &/|
		boolean inString = false;
		for (int i = 0; i < condition.length(); i++) {
			if (condition.charAt(i) == '"') {
				inString = !inString;
			}

			if (!inString &&
					i > 0 &&
					i + 2 < condition.length() &&
					condition.charAt(i) == 'e' &&
					(condition.charAt(i - 1) == ')' ||
							condition.charAt(i - 1) == ' ') &&
							condition.charAt(i + 1) == 't' &&
							(condition.charAt(i + 2) == ' ' ||
									condition.charAt(i + 2) == '(')) {
				condition = condition.substring(0, i) + "&" + condition.substring(i + 2);
			}
			else if (!inString &&
					i > 0 &&
					i + 2 < condition.length() &&
					condition.charAt(i) == 'o' &&
					(condition.charAt(i - 1) == ')' ||
							condition.charAt(i - 1) == ' ') &&
							condition.charAt(i + 1) == 'u' &&
							(condition.charAt(i + 2) == ' ' ||
									condition.charAt(i + 2) == '('))  {
				condition = condition.substring(0, i) + "|" + condition.substring(i + 2);
			}
		}

		//�valuation de la condition		
		valid = Parser.postfixeEvalCondition(Parser.infixeToPostfix(Parser.evalCondition(condition, environment)));
		
		return valid;
	}

	/**
	 * M�thode qui remplace les conditions par des vrais ou faux.
	 * @param condition		La condition � �valuer
	 * @return				Vrai si la condition est valide, faux sinon
	 * @throws ArithmeticSyntaxException 
	 * @throws UnknownVariableException 
	 * @throws SyntaxException 
	 * @throws TypeMissMatchException 
	 */
	private static String evalCondition(String condition, Environment environment) throws SyntaxException, UnknownVariableException, ArithmeticSyntaxException, TypeMissMatchException {
		//On ne d�coupe pas la m�thode afin de ne pas cr�er de fonctions 
		//ayant en param�tre un trop grand nombre de param�tre.
		int index = 0;
		char character;
		boolean inString = false;
		for (int i = 0; i < condition.length(); i++) {
			character = condition.charAt(i);
			//�value si on est dedans une string
			if (character == '"') {
				inString = !inString;
			}
			
			if (!inString) {

				if (character == '=' ||
						character == '<' ||
						character == '>') {
					
					String operator;
					if ((condition.charAt(i + 1) == '=' ||
							condition.charAt(i + 1) == '>')) {
						operator = condition.substring(i, i + 2);
					}
					else {
						operator = String.valueOf(character);
					}
					
					int startCondition = 0;
					int endCondition = 0;
					String leftSide = "";
					String rightSide = "";
					
					
					//On a trouv� le milieu d'une condition
					int cpt = 0;
					boolean end = false;
					
					//On commence par la gauche
					index = i;
					index--;
					while ( index >= 0 && !end) {
						if (condition.charAt(index) == ')') {
							cpt++;
						}
						else if (condition.charAt(index) == '(') {
							//On a attends une parenth�se que l'on a pas ouvert
							if (cpt == 0) { 
								end = true;
								index++;
							}
							else {
								cpt--;
							}
						}
						
						if (condition.charAt(index) == '&' ||
								condition.charAt(index) == '|') {
							end = true;
							index++;
						}
						
						if (!end) {
							index--;
						}
					}
					
					//On a d�pass�
					if (index == -1) {
						index = 0;
					}
					
					startCondition = index + 1;
					leftSide = Parser.solveCondition(condition.substring(index, i).trim(), environment);
					
					//R�initialise les variables
					end = false;

					cpt = 0;
					
					if (condition.charAt(i + 1) == '=' ||
							condition.charAt(i + 1) == '<' ||
							condition.charAt(i + 1) == '>') {
						i++;
					}
					
					 index = i;
					//Ensuite la droite
					index++;
					while ( index < condition.length() && !end) {
						
						if (condition.charAt(index) == '(') {
							cpt++;
						}
						else if (condition.charAt(index) == ')') {
							//On a attends une parenth�se que l'on a pas ouvert
							if (cpt == 0) { 
								end = true;
							}
							else {
								cpt--;
							}
						}
						if (condition.charAt(index) == '&' ||
								condition.charAt(index) == '|') {
							end = true;
						}
						if (!end) {
							index++;
						}
					}
					
					endCondition = index;   
					rightSide = Parser.solveCondition(condition.substring(i + 1 , index).trim(), environment);
					
					if (operator.equals("=")) {
						if (rightSide.equals(leftSide)) {
							//Vrai
							condition = condition.substring(0, startCondition - 1) + WordsBoolean.VRAI.getWord() + condition.substring(endCondition);
							}
						else {
							//faux
							condition = condition.substring(0, startCondition - 1) + WordsBoolean.FAUX.getWord() + condition.substring(endCondition);
						}
					}
					else if (operator.equals(">=")) {
						try {
							int left = Integer.parseInt(leftSide);
							int right = Integer.parseInt(rightSide);
							
							if (left >= right) {
								//Vrai
								condition = condition.substring(0, startCondition - 1) + WordsBoolean.VRAI.getWord() + condition.substring(endCondition);
								}
							else {
								//faux
								condition = condition.substring(0, startCondition - 1) + WordsBoolean.FAUX.getWord() + condition.substring(endCondition);
							}
						}
						catch (NumberFormatException e){
							throw new TypeMissMatchException();
						}

					}
					else if (operator.equals("<=")) {
						try {
							int left = Integer.parseInt(leftSide);
							int right = Integer.parseInt(rightSide);
							
							if (left <= right) {
								//Vrai
								condition = condition.substring(0, startCondition - 1) + WordsBoolean.VRAI.getWord() + condition.substring(endCondition);
								}
							else {
								//faux
								condition = condition.substring(0, startCondition - 1) + WordsBoolean.FAUX.getWord() + condition.substring(endCondition);
							}
						}
						catch (NumberFormatException e){
							throw new TypeMissMatchException();
						}
					}
					else if (operator.equals("<>")) {
						if (!rightSide.equals(leftSide)) {
							//Vrai
							condition = condition.substring(0, startCondition - 1) + WordsBoolean.VRAI.getWord() + condition.substring(endCondition);
							}
						else {
							//faux
							condition = condition.substring(0, startCondition - 1) + WordsBoolean.FAUX.getWord() + condition.substring(endCondition);
						}
						
					}
					else if (operator.equals("<")) {
						try {
							int left = Integer.parseInt(leftSide);
							int right = Integer.parseInt(rightSide);
							
							if (left < right) {
								//Vrai
								condition = condition.substring(0, startCondition - 1) + WordsBoolean.VRAI.getWord() + condition.substring(endCondition);
								}
							else {
								//faux
								condition = condition.substring(0, startCondition - 1) + WordsBoolean.FAUX.getWord() + condition.substring(endCondition);
							}
						}
						catch (NumberFormatException e){
							throw new TypeMissMatchException();
						}
					}
					else if (operator.equals(">")) {
						try {
							int left = Integer.parseInt(leftSide);
							int right = Integer.parseInt(rightSide);
							
							if (left > right) {
								//Vrai
								condition = condition.substring(0, startCondition - 1) + WordsBoolean.VRAI.getWord() + condition.substring(endCondition);
								}
							else {
								//faux
								condition = condition.substring(0, startCondition - 1) + WordsBoolean.FAUX.getWord() + condition.substring(endCondition);
							}
						}
						catch (NumberFormatException e){
							throw new TypeMissMatchException();
						}
					}

				}
			}
					
		}
		
		return condition;
	}


	/**
	 * M�thode qui r�sous un c�t� d'une condition et retourne la valeur � sa plus simple expression.
	 * @param condition		La condition a r�soudre.
	 * @param environment	Le contenant des variables.
	 * @return La String � l'�tat plus simple
	 * @throws SyntaxException 
	 * @throws ArithmeticSyntaxException 
	 * @throws UnknownVariableException 
	 * @throws TypeMissMatchException 
	 * @throws SyntaxException 
	 */
	private static String solveCondition(String condition, Environment environment) throws UnknownVariableException, ArithmeticSyntaxException, TypeMissMatchException, SyntaxException {
		String tmp = "";

		if (condition.contains("\"") ||
				condition.contains(".")) {
			//C'est une string
			if (!Parser.validConcatSyntax(condition)) {
				throw new SyntaxException();
			}
			else {
				tmp = Parser.stringConcat(condition, environment);
			}
		}
		else if (condition.contains(WordsBoolean.VRAI.getWord()) ||
				condition.contains(WordsBoolean.FAUX.getWord())) {
			//C'est un boolean
			tmp = condition;
		}
		else if (Parser.isValidMathExpression(condition)){
			//C'est un int
			tmp = String.valueOf(Parser.postfixeEvalMath(Parser.infixeToPostfix(condition)));
		}
		else
		{
			//Junk
			throw new TypeMissMatchException();
		}

		return tmp;
	}

	/**
	 * Remplace les variables contenues dans une condition pour leur vrai valeur.
	 * @param line La line � remplacer
	 * @param environment L'environnement de variable
	 * @return La String avec les variables remplac�es
	 * @throws UnknownVariableException
	 */
	private static String replaceVariable(String line, Environment environment) throws UnknownVariableException {
		boolean inString = false;
		
		for (int i = 0; i < line.length(); i++) {
			StringBuilder varName = new StringBuilder();
			//�value si on est dedans une string
			if (line.charAt(i) == '"') {
				inString = !inString;
			}

			if (!inString &&
					(Character.isLetter(line.charAt(i)) ||
					line.charAt(i) == '_') &&
					((i > 0 &&
					!Character.isLetter(line.charAt(i - 1)) &&
					!Character.isDigit(line.charAt(i - 1))) ||
					i == 0)) {
				while (i < line.length() &&  //Recherche un d�but de mot afin de cern� une variable
						(Character.isLetter(line.charAt(i)) ||
								Character.isDigit(line.charAt(i)) ||
								line.charAt(i) == '_')) {



					varName = varName.append(line.charAt(i));
					i++;
				}	
			}

			if (!varName.toString().equals("") && 
					!varName.toString().equals("et") && 
					!varName.toString().equals("ou") &&
					!varName.toString().equals(WordsBoolean.FAUX.getWord()) &&
					!varName.toString().equals(WordsBoolean.VRAI.getWord())) {  //Si un mot � �t� trouv�

				//System.out.println(varName.toString() + "   " + environment.getVariableByName(varName.toString()));
				Object varValue = environment.getVariableByName(varName.toString());

				//Replace l'index au d�but du mot
				i = i - varName.length();

				if (varValue == null) {
					throw new UnknownVariableException(varName.toString());
				}
				else if (varValue instanceof String) { //Une chaine de caract�res
					line = line.substring(0, i) + "\"" + varValue.toString() + "\"" + line.substring(i + varName.toString().length());
					i += 2 + varValue.toString().length();
				}
				else if (varValue instanceof Boolean){ //Un bool�en
					//Pour une r�ecriture en fran�ais
					if (String.valueOf(varValue).equals("true")) {
						line = line.substring(0, i) + WordsBoolean.VRAI.getWord() + line.substring(i + varName.toString().length());
						line = line.replace(varName.toString(), WordsBoolean.VRAI.getWord());
						i += WordsBoolean.VRAI.getWord().length();
					}
					else {
						line = line.substring(0, i) + WordsBoolean.FAUX.getWord() + line.substring(i + varName.toString().length());
						i += WordsBoolean.FAUX.getWord().length();
					}
				}
				else {		//Un integer
					line = line.substring(0, i) + String.valueOf(varValue) + line.substring(i + varName.toString().length());
					i += String.valueOf(varValue).length();
				}
			}
		}
		return line;
	}

	/**
	 * Ajoute la variable � l'environnement.
	 * @param line	Le texte de la ligne pour d�clarer la variable.
	 * @param env	Le contenant des variables.
	 * @throws SyntaxException
	 * @throws UnknownVariableException
	 * @throws VariableRedefinitionException
	 * @throws MissingParenthesisException
	 * @throws ArithmeticSyntaxException 
	 * @throws UseOfReservedWord 
	 * @throws TypeMissMatchException 
	 */
	protected static void declaration(String declaration, Environment env) throws SyntaxException, UnknownVariableException, VariableRedefinitionException, MissingParenthesisException, ArithmeticSyntaxException, UseOfReservedWord, TypeMissMatchException {
		//On v�rifie la validit� de la d�claration
		String assign = "[\\s]*[A-Za-z_][A-Za-z0-9_]*[\\s]*(<\\-).*";
		String regex = "(" + WordsDeclaration.CHAINE.getWord() + 
		"|" + WordsDeclaration.ENTIER.getWord() + 
		"|" + WordsDeclaration.BOOLEEN.getWord() +
		")" + assign;
		Pattern pattern = Pattern.compile(regex);  
		Matcher matcher = pattern.matcher(declaration.toLowerCase());  
		if(!matcher.matches()){  
			throw new SyntaxException();
		}

		//On prend le nom de la variable et sa valeur en String
		String afterEquals = Parser.trimEquals(declaration);
		int n = declaration.indexOf(" ") + 1;
		String name = declaration.substring(n, declaration.indexOf("<-", n)).trim();

		Parser.reactToRestrictedWords(name);

		//On regarde le type et on ajoute la variable � l'environnement.
		String s = declaration.trim().toLowerCase();
		Object result = null;
		//Si c'est une chaine de caract�re...
		if(s.startsWith(WordsDeclaration.CHAINE.getWord())) {
			result = Parser.stringConcat(afterEquals, env);
		}
		//Si c'est un entier
		else if(s.startsWith(WordsDeclaration.ENTIER.getWord())) {
			result = Parser.evalMathOperation(afterEquals, env);
		}
		//Si c'est un bool�en
		else if(s.startsWith(WordsDeclaration.BOOLEEN.getWord())) {
			String bool = afterEquals.trim();
			bool = Parser.replaceVariable(bool, env);
			if(bool.equals(WordsBoolean.VRAI.getWord())) {
				result = new Boolean(true);
			}
			else if(bool.equals(WordsBoolean.FAUX.getWord())) {
				result = new Boolean(false);
			}
			else {
				result = Parser.condition(afterEquals, env);
			}
		}

		if(!env.addVariable(name, result)) {
			throw new VariableRedefinitionException(name);
		}
	}

	/**
	 * M�thode lan�ant l'exception UseOfReservedWord si la cha�ne pass�e 
	 * en param�tre est un mot r�serv�e
	 * @param s Le mot � v�rifier
	 * @throws UseOfReservedWord
	 */
	private static void reactToRestrictedWords(String s) throws UseOfReservedWord {
		for(WordsBoolean w : WordsBoolean.values()) {
			if(s.equals(w.getWord())) {
				throw new UseOfReservedWord();
			}
		}
		for(WordsCondition w : WordsCondition.values()) {
			if(s.equals(w.getWord())) {
				throw new UseOfReservedWord();
			}
		}
		for(WordsDeclaration w : WordsDeclaration.values()) {
			if(s.equals(w.getWord())) {
				throw new UseOfReservedWord();
			}
		}
		for(WordsInOut w : WordsInOut.values()) {
			if(s.equals(w.getWord())) {
				throw new UseOfReservedWord();
			}
		}
		for(WordsIntraCondition w : WordsIntraCondition.values()) {
			if(s.equals(w.getWord())) {
				throw new UseOfReservedWord();
			}
		}
		for(WordsLoop w : WordsLoop.values()) {
			if(s.equals(w.getWord())) {
				throw new UseOfReservedWord();
			}
		}
	}

	/**
	 * Modifie la valeur d'une variable.
	 * @param line	Le texte de la ligne pour modifi� la variable.
	 * @param env	Le contenant des variables.
	 * @throws SyntaxException
	 * @throws UnknownVariableException
	 * @throws MissingParenthesisException
	 * @throws ArithmeticSyntaxException 
	 * @throws TypeMissMatchException 
	 */
	protected static void assignment(String line, Environment env) throws SyntaxException, UnknownVariableException, MissingParenthesisException, ArithmeticSyntaxException, TypeMissMatchException {
		//On v�rifie la validit� de l'assignement
		String assign = "[\\s]*[A-Za-z_][A-Za-z0-9_]*[\\s]*(<\\-).*";
		Pattern pattern = Pattern.compile(assign);  
		Matcher matcher = pattern.matcher(line);  
		if(!matcher.matches()){  
			throw new SyntaxException();
		}

		//On prend le nom et la valeur � affecter la variable
		String name = line.substring(0, line.indexOf("<-")).trim();
		String var = Parser.trimEquals(line);

		//Prend la variable pour conna�tre son type
		Object obj = env.getVariableByName(name);
		if(obj != null) {
			if(obj instanceof String) {
				//Si c'�tait une String, on appelle la fonction pour concat�ner
				String concat = Parser.stringConcat(var, env);
				env.setVariableByName(name, concat);
			}
			else if(obj instanceof Integer) {
				//Si c'�tait un entier, on �value l'expression math�matique
				Integer value = Parser.evalMathOperation(var, env);
				env.setVariableByName(name, value);
			}
			else if(obj instanceof Boolean) {
				//Si c'est un bool�en, on regarde si c'est juste un bool�en unique
				//ou une condition.
				String bool = var.trim();
				bool = Parser.replaceVariable(bool, env);
				Boolean result = null;
				if(bool.equals(WordsBoolean.VRAI.getWord())) {
					result = new Boolean(true);
				}
				else if(bool.equals(WordsBoolean.FAUX.getWord())) {
					result = new Boolean(false);
				}
				else {
					result = Parser.condition(bool, env);
				}
				env.setVariableByName(name, result);
			}
		}
		else {
			//Si la variable n'existe pas.
			throw new UnknownVariableException(name);
		}
	}

	/**
	 * Donne tout le texte apr�s le premier <- de la string en param�tre
	 * @param instruction La ligne d'assignation
	 * @return Le texte apr�s le premier <- de la string en param�tre
	 */
	private static String trimEquals(String instruction) {
		String retour = instruction.substring(instruction.indexOf("<-") + 2);

		return retour;
	}

	/**
	 * M�thode retournant une cha�ne repr�sentant la concat�nation de celle du pseudo-code
	 * 	sur la ligne donn�e.
	 * @param line	Le texte de la ligne contenant une plusieurs cha�ne de caract�res
	 * 				en pseudo-code.
	 * @param env	Le contenant des variables.
	 * @return	La cha�ne de caract�res r�sultante de celle en pseudo-code
	 * @throws UnknownVariableException
	 * @throws SyntaxException
	 * @throws ArithmeticSyntaxException
	 */
	protected static String stringConcat(String line, Environment env) throws UnknownVariableException, SyntaxException, ArithmeticSyntaxException {
		String retour = "";
		//On valide la string
		if(Parser.validConcatSyntax(line)) {
			//On divise la String pour obtenir chacune des parties de la concat�nation
			LinkedList<String> list = Parser.splitConcatString(line, env);
			StringBuilder stringFinal = new StringBuilder("");
			for(String s : list) {
				//Si c'est une chaine...
				if(s.toString().startsWith("\"")) {
					stringFinal.append(s.substring(1, s.length() - 1));
				}
				//Si c'est un bool�en
				else if(s.toLowerCase().equals(WordsBoolean.VRAI.getWord()) || s.toLowerCase().equals(WordsBoolean.FAUX.getWord())) {
					stringFinal.append(s.toLowerCase());
				}
				else { //if(Parser.isValidMathExpression(s)) {
					stringFinal.append(Parser.evalMathOperation(s, env));
				}
			}
			retour = stringFinal.toString();
		}
		else {
			throw new SyntaxException();
		}
		
		return retour;
	}

	/**
	 * M�thode qui divise une chaine valide de concat�nation. Le signe de concat�nation
	 * est le point(.).
	 * @param s La chaine � concat�ner
	 * @param env L'environnement contenant les variables actuelles
	 * @return Un tableau de string contenant les parties de la cha�ne
	 * @throws UnknownVariableException
	 */
	private static LinkedList<String> splitConcatString(String s, Environment env) throws UnknownVariableException {
		//On remplace les variables dans la string
		char word[] = Parser.replaceVariable(s, env).toCharArray();
		boolean inString = false;
		boolean inVariable = false;
		LinkedList<String> list = new LinkedList<String>();
		StringBuilder temp;
		int j = 0;
		temp = new StringBuilder("");
		while(j < word.length) {
			char letter = word[j];
			j++;

			switch(letter) {
			case '"': //Si c'est un guillemet, c'est un d�but ou de fin de string
				inString = !inString;
				temp.append(letter);
				if(!inString) {
					//Si c'est la fin d'une string
					list.add(temp.toString().trim());//On l'ajoute dans la liste
					temp = new StringBuilder("");
				}
				break;
			case '\\': //Si c'est un barre oblique arri�re...
				if(inString) {
					//On affiche seulement le prochain caract�re
					temp.append(word[j]);
					//On passe par dessus le prochain caract�re.
					j++;
				}
				break;
			case '.': //Si c'est un point (signe de concat�nation)
				if(inString) {//Si on est dans une string, on l'ajoute.
					temp.append(letter);
				}
				else if(inVariable) {
					//Sinon, on ajoute la variable � la liste
					list.add(temp.toString().trim());
					temp = new StringBuilder("");
					inVariable = false;
				}
				break;
			case ' ':
				temp.append(letter);
				break;
			default:
				//Si c'est une lettre
				if(!inString) {
					//Si on est pas dans une string, on est dans une variable
					inVariable = true;
				}
				temp.append(letter);
				if(j == word.length) {
					//Si c'est le dernier caract�re, on ajoute le dernier "temp" dans la liste
					list.add(temp.toString().trim());
				}
			}
		}
		
		return list;
	}

	/**
	 * Valide la syntaxe d'une concat�nation.
	 * @param s La chaine de concat�nation(s)
	 * @return Vrai si la cha�ne de concat�nation est valide; sinon faux.
	 */
	private static boolean validConcatSyntax(String s) {
		boolean inString = false;
		boolean waitForPlus = false;
		boolean error = false;
		boolean inVariable = false;
		char word[] = s.toCharArray();
		int i = 0;
		while(i < word.length && !error) {
			char letter = word[i];
			i++;

			switch(letter) {
			case '"': //Si c'est un guillemet, c'est un d�but ou de fin de string
				inString = !inString;
				if(!inString) {
					//Si on vient de sortir d'une string, on attend le signe des concat�nations
					waitForPlus = true;
				}
				else if(inString && waitForPlus) {
					//Si c'est une ouverture de chaine et et qu'on attendait une chaine, il y a erreur.
					error = true;
				}
				break;
			case '\\': //Si c'est un barre oblique arri�re...
				if(inString) {
					//Si on est dans une chaine, on passe au prochain caract�re.
					i++;
				}
				else {
					//Si on n'est pas dans une chaine, il y a erreur.
					error = true;
				}
				break;
			case '.': //Si c'est un point (signe de concat�nation)
				if(!inString && waitForPlus) {
					//Si on n'est pas dans une chaine, on n'attend
					//plus pour le signe de concat�nation et on n'est 
					//plus dans une variable
					waitForPlus = false;
					inVariable = false;
				}
				else if(!inString && !waitForPlus) {
					//Si on est pas dans une chaine et qu'on
					//attend pas pour un signe de concat�nation
					error = true;
				}
				break;
			case ' ':
				if(inVariable) {
					//Si on �tait dans une variable, on attend pour un 
					//signe de concat�nation et on n'est plus dans une
					//variable
					waitForPlus = true;
					inVariable = false;
				}
				break;
			default:
				if(!inString && !waitForPlus) {
					//Si on �tait pas dans une chaine et qu'on attendait pas
					//pour un signe de concat�nation, on est alors dans une 
					//variable et on attend un signe de concat�nation.
					waitForPlus = true;
					inVariable = true;
				}
				else if(!inString && !inVariable && waitForPlus) {
					//Si on est dans une string ou dans une variable et qu'on attend pour un
					//signe de concat�nation, on est dans une expression math�matique
					switch(letter) {
					case '(':
					case ')':
					case '+':
					case '-':
					case '*':
					case '/':
						break;
					default:
						if(letter < '0' || letter > '9') {
							error = true;
						}
					}

				}
			}
		}

		boolean retour = true;
		//Si on est encore dans une chaine ou qu'il y a une erreur, on retourne false.
		if(inString == true || error == true) {
			retour = false;
		}
		return retour;
	}

	/**
	 * Algorithme qui transforme un calcul ou une condition de forme infix�e vers une forme 
	 * postfix�e.  Cet algorithme prend en compte les parenth�ses et prend pour acquis que 
	 * la chaine est valide.  La chaine doit contenir uniquement des op�rateurs et des
	 * nombres entiers.
	 * 
	 * La forme postfix�e s�pare les diff�rents op�rateurs et les nombre par une virgule.
	 * La forme postfix�e peut commencer par une virgule.
	 * 
	 * Source: http://scriptasylum.com/tutorials/infix_postfix/infix_postfix.html
	 * @param infix		La chaine sous forme infix�e.
	 * @return			Une chaine sous form postfix�e
	 */
	private static String infixeToPostfix(String infix) {

		Stack<Character> stack = new Stack<Character>();
		StringBuilder postfix = new StringBuilder();


		//On enl�ve les espaces
		infix = infix.replaceAll(" ", "");
		//Remplacement de tout les signes
		infix = infix.replaceAll("--", "+");
		infix = infix.replaceAll("\\+-", "-");
		infix = infix.replaceAll("\\*-", "*-1*");

		//V�rifie qu'il n'y a pas d'erreur du genre 5/+5
		//A cause de la transformation de 5/--5 -> 5/+5
		for (int i = 0; i < infix.length(); i++) {
			if ((i == 0 &&
					infix.charAt(i) == '+') ||
					(i > 0 &&
					infix.charAt(i) == '+' &&
					!Character.isDigit(infix.charAt(i - 1))) &&
					infix.charAt(i - 1) != ')') {
				infix = infix.substring(0, i) + infix.substring(i + 1);
				i--;
			}
		}

//				//Ajoute un z�ro pour bien g�re la n�gation en d�but de d�claration
//				postfix = postfix.append("0,");

		char character;
		int priorityChar = 0;
		int priorityLast = 0;
		boolean isWordFinished = false;

		for (int i = 0; i < infix.length(); i++) {
			character = (char) infix.charAt(i);
			switch (character) {
			case '-':
			case '+':
			case '*':
			case '/':
			case '%':
			case '&':
			case '|':
				if (i > 0 &&
						character == '-' &&
						!Character.isDigit(infix.charAt(i - 1)) && 
						infix.charAt(i - 1) != ')' &&
						infix.charAt(i - 1) != '-' ||
						(i == 0)) {
					postfix = postfix.append(',');
					postfix = postfix.append(character);
					isWordFinished = false;
				}
				else {
					isWordFinished = true;
					if (stack.isEmpty()) {
						stack.push(character);
					}
					else {
						priorityChar = Parser.getPrority(character);
						priorityLast = Parser.getPrority(stack.peek());

						if (priorityLast < priorityChar) { //Si le dernier est moins prioritaire
							stack.push(character);
						}
						else {
							while (!stack.isEmpty() && Parser.getPrority(stack.peek()) >= priorityChar && stack.peek() != '(') {
								postfix = postfix.append(',');
								postfix = postfix.append(stack.pop());
							}
							stack.push(character);
						}
					}
				}
				break;
			case '(':
				isWordFinished = true;
				stack.push(character);
				break;
			case ')':
				isWordFinished = true;
				while (!stack.isEmpty() && stack.peek() != '(') {
					postfix = postfix.append(',');
					postfix = postfix.append(stack.pop());
				}
				if (!stack.isEmpty()) {
					stack.pop(); //Pop de la parenth�se ouvrante
				}
				break;
			default: //un caract�re autre
				if (isWordFinished) {
					postfix = postfix.append(',');
					isWordFinished = false;
				}
				postfix = postfix.append(character);
			}
		}

		while (!stack.isEmpty()) {
			postfix = postfix.append(',');
			postfix = postfix.append(stack.pop());	
		}

		return postfix.toString();
	}

	/*
	 * Fonction qui retourne la priorit� d'un op�rateur afin
	 * de pouvoir bien ex�cuter la priorit� d'op�ration.
	 */
	private static int getPrority (char operator) {
		int tmp;

		switch (operator) {
		case '-':
		case '+':
			tmp = 0;
			break;
		case '*':
		case '/':
		case '%':
			tmp = 1;
			break;
		case '|':
			tmp = 2;
			break;
		case '&':
			tmp = 3;
			break;
		default:
			tmp = -1;
			break;
		}
		return tmp;
	}


	/**
	 * Algorithme qui �value une condition sous forme postfix�e.
	 * Cet algorithme prend en compte que la chaine est valide et que
	 * tous les bool�ens et op�rateurs sont s�par�s d'une virgule.
	 * 
	 * Source: http://scriptasylum.com/tutorials/infix_postfix/infix_postfix.html
	 * @param postfix		La chaine sous la forme postfix�e
	 * @return				Le r�sultat sous la forme d'une chaine.
	 * @see	postfixeEvalMath
	 * @see infixeToPostfix
	 */
	private static Boolean postfixeEvalCondition(String postfix) {

		Stack<Boolean> stack = new Stack<Boolean>();

		boolean tmp;
		String[] tabPostfix = postfix.split(",");

		for (int i = 0; i < tabPostfix.length; i++) {
			if (!tabPostfix[i].equals("")) {
				if (tabPostfix[i].equals("&")) {
					tmp = stack.pop();
					stack.push(stack.pop() && tmp);
				}
				else if(tabPostfix[i].equals("|")) {
					tmp = stack.pop();
					stack.push(stack.pop() || tmp);
				}
				else {
					if (tabPostfix[i].equals(WordsBoolean.FAUX.getWord())) {
						tmp = false;
					}
					else {
						tmp = true;
					}

					stack.push(tmp);
				}
			}
		}

		return stack.pop();
	}

	/**
	 * Algorithme qui �value une �quation math�matique sous forme postfix�e.
	 * Cet algorithme prend en compte que la chaine est valide et que
	 * tous les nombres et op�rateurs sont s�par�s d'une virgule.
	 *  
	 * Source: http://scriptasylum.com/tutorials/infix_postfix/infix_postfix.html
	 * @param postfix		La chaine sous la forme postfix�e
	 * @return				Le r�sultat sous la forme d'une chaine.
	 * @see	postfixeEvalCondition
	 * @see infixeToPostfixe
	 */
	private static Integer postfixeEvalMath(String postfix) throws ArithmeticSyntaxException {

		Stack<Integer> stack = new Stack<Integer>();

		int tmp;
		String[] tabPostfix = postfix.split(",");

		for (int i = 0; i < tabPostfix.length; i++) {
			if (!tabPostfix[i].equals("")) {
				if (tabPostfix[i].equals("+")) {
					tmp = stack.pop();
					stack.push(stack.pop() + tmp);
				}
				else if(tabPostfix[i].equals("-")) {
					tmp = stack.pop();
					stack.push(stack.pop() - tmp);
				}
				else if(tabPostfix[i].equals("*")) {
					tmp = stack.pop();
					stack.push(stack.pop() * tmp);
				}
				else if(tabPostfix[i].equals("/")) {
					tmp = stack.pop();
					if (tmp != 0) {
						stack.push(stack.pop() / tmp);
					}
					else {
						throw new ArithmeticSyntaxException();
					}
				}
				else if(tabPostfix[i].equals("%")) {
					tmp = stack.pop();
					stack.push(stack.pop() % tmp);
				}
				else {
					try {
						stack.push(Integer.parseInt(tabPostfix[i]));	
					}
					catch (NumberFormatException e) {
						throw new ArithmeticSyntaxException();
					}
					
				}
			}
		}

		return stack.pop();
	}

	/**
	 * M�thode qui �value une op�ration  math�matique simple ou compliqu�e.
	 *
	 * @param operation						Le calcul � ex�cuter.
	 * @return								La r�ponse au calcul
	 * @throws ArithmeticSyntaxException 	Si la syntaxe de l'expression est invalide
	 * @throws UnknownVariableException 
	 */
	private static int evalMathOperation(String operation, Environment env) throws ArithmeticSyntaxException, UnknownVariableException {
		operation = Parser.replaceVariable(operation, env);
		if (!Parser.isValidMathExpression(operation)) {
			throw new ArithmeticSyntaxException();
		}

		return Parser.postfixeEvalMath(Parser.infixeToPostfix(operation));
	}

	/**
	 * Algorithme qui valide si une expression est une expression math�matique valide �
	 * l'aide d'un automate.
	 * 
	 * Source : http://recursivite.developpez.com/?page=page_8#LVII-C-4
	 * @param operation						La string du calcul
	 * @return								Vrai si l'expression est valide, faux si invalide
	 */
	private static boolean isValidMathExpression(String operation)  {
		boolean valid = true;
		operation = operation.replaceAll(" ", "");

		String line = "0123456789+-*%/()";
		//Algorithme de validation par automate :D
		int [][] automate =
			/* 	 0 1 2 3 4 5 6 7 8 9 + - * % / ( ) */
		{ 	{2,2,2,2,2,2,2,2,2,2,-1,1,-1,-1,-1,0,-1}, 		//�tat 0
				{2,2,2,2,2,2,2,2,2,2,-1,1,-1,-1,-1,0,-1}, 		//�tat 1
				{2,2,2,2,2,2,2,2,2,2,4,4,4,4,4,-1,3}, 			//�tat 2
				{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,4,1,4,4,4,-1,3}, //�tat 3
				{2,2,2,2,2,2,2,2,2,2,-1,1,-1,-1,-1,0,-1}  		//�tat 4
		};
		//L'�tat
		int state = 0;
		//Compteur pour les parenth�ses
		int cpt = 0;
		//Index du curseur
		int i = 0;
		//Position d'une lettre dans line
		int position = 0;
		//Le charactere de la cha�ne
		char character;

		do {
			character = operation.charAt(i);
			//Balancer les parenth�ses
			if (character == '(') {
				cpt++;
			}
			else if (character == ')') {
				cpt--;
			}

			if (cpt >= 0) {
				position = line.indexOf(character);
				if (position >= 0) { //C'est un caract�re valide
					//On passe au prochaine �tat
					state = automate[state][position];	
				}
			}
			i++;
		} while (i < operation.length() &&
				position != -1 &&
				cpt >= 0 &&
				state >= 0);

		if (cpt != 0) {
			valid = false;
		}

		if (state == -1 ||
				state == 0 ||	//�tat non terminal
				state == 1||
				state == 4 ||
				position == -1) {
			valid = false;
		}

		return valid;
	}

	/*
	 * M�thode qui valide si le nombre de parenth�ses ouvrantes est �gale au nombre
	 * de parenth�ses fermantes.
	 */
	private static boolean isValidParenthesis(String operation) {

		boolean valid = true;
		int cpt = 0;
		for (int i = operation.length() - 1; i >= 0; i--) {
			switch (operation.charAt(i)) {
			case '(':
				cpt++;
				break;
			case ')':
				cpt--;
				break;			
			}
		}

		if (cpt != 0) {
			valid = false;
		}

		return valid;
	}

	/**
	 * Main du parser permettant de le tester en l'isolant.
	 * Le code en commentaire est un exemple de son utilisation pour tester.
	 */
//	public static void main(String[] args) {
//
//		Environment env = new Environment();
//
//		try {
////			Parser.declaration("entier bob <- 5", env);
////			Parser.declaration("entier var2 <- var + 3", env);
////			Parser.declaration("Booleen bool <- vrai", env);
////			Parser.declaration("booleen bool2 <- faux", env);
////			Parser.declaration("Booleen bool3 <- bool", env);
////			Parser.declaration("Booleen bool4 <- (bool2 = bool)", env);
////			Parser.declaration("chaine s <- var. \"salut\" . var2", env);			
////			Parser.declaration("Chaine s2 <- s.\" sa va\".s", env);
////
////			Parser.assignment("var <- var + 2", env);
////			Parser.assignment("var2 <- (var + 3) / 5", env);
////			Parser.assignment("var <- var - 5", env);
////			Parser.assignment("s <- s . \"sssss\"", env);
////			Parser.assignment("s2 <- s .var2. \"sssss\" .s2", env);
////			Parser.assignment("bool <- faux", env);
////			Parser.assignment("bool2 <- (var = var2)", env);
//
//			//env.addVariable("_s", "salut");
//			//Parser.replaceVariable("_s . \"salut\"", env);
//
//			//env.addVariable("s", "salut");
//			//System.out.println(" s . \"sa va\" . s");
//			//System.out.println(Parser.replaceVariable(" s . \"sa va\" . s", env));
//			//System.out.println(" s . s . \"sa va\" ");
//			//System.out.println(Parser.replaceVariable(" s . s . \"sa va\" ", env));
//			//System.out.println(" s . s ");
//			//System.out.println(Parser.replaceVariable(" s . s ", env));
//
////			env.addVariable("bool", true);
////			System.out.println(Parser.stringConcat("\"salut\" . vrai. bool. faux", env));
////			System.out.println("result: " + condition("\"vrai\" = vrai", env));
////			
////			env.addVariable("___bob", 4);
////			
////			String line = "( \"44\" < \"45\")";
////
////			System.out.println("Line : " + Parser.condition(line, env));
//
//			
//			env.showEnvironment();
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//			env.showEnvironment();
//		}
//
//	}

}
