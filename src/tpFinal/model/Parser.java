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
 * Cette classe gère toutes les déclarations, affections et concaténation de variables.
 * Elle prend également en charge les conditions et les expressions mathématiques. 
 * 
 * Toutes les méthodes de cette classe sont statiques afin d'éviter de créer des objets
 * afin d'utiliser que des méthodes utilitaires.
 * 
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class Parser {

	/**
	 * Exécute une condition "Si" afin de savoir si la condition est valide.  Cette méthode
	 * vérifie que la condition est valide.  Cette méthode prend également en charge
	 * les opérations arithmétiques ainsi que les variables.
	 * 
	 * Bogue Connu: Un condition du type (bob = 3+(5+6 ET 4-2) = martin) serait acceptée.
	 * 
	 * @param condition La condition en entier
	 * @param environment L'environnement du programme lors de l'éxécution de la condition.
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

		//Validation des parenthèses
		if (!Parser.isValidParenthesis(condition)) {
			throw new MissingParenthesisException();
		}

		//Les autres erreurs seront trouvé par ma regex
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

		//Évaluation de la condition		
		valid = Parser.postfixeEvalCondition(Parser.infixeToPostfix(Parser.evalCondition(condition, environment)));
		
		return valid;
	}

	/**
	 * Méthode qui remplace les conditions par des vrais ou faux.
	 * @param condition		La condition à évaluer
	 * @return				Vrai si la condition est valide, faux sinon
	 * @throws ArithmeticSyntaxException 
	 * @throws UnknownVariableException 
	 * @throws SyntaxException 
	 * @throws TypeMissMatchException 
	 */
	private static String evalCondition(String condition, Environment environment) throws SyntaxException, UnknownVariableException, ArithmeticSyntaxException, TypeMissMatchException {
		//On ne découpe pas la méthode afin de ne pas créer de fonctions 
		//ayant en paramètre un trop grand nombre de paramètre.
		int index = 0;
		char character;
		boolean inString = false;
		for (int i = 0; i < condition.length(); i++) {
			character = condition.charAt(i);
			//Évalue si on est dedans une string
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
					
					
					//On a trouvé le milieu d'une condition
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
							//On a attends une parenthèse que l'on a pas ouvert
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
					
					//On a dépassé
					if (index == -1) {
						index = 0;
					}
					
					startCondition = index + 1;
					leftSide = Parser.solveCondition(condition.substring(index, i).trim(), environment);
					
					//Réinitialise les variables
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
							//On a attends une parenthèse que l'on a pas ouvert
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
	 * Méthode qui résous un côté d'une condition et retourne la valeur à sa plus simple expression.
	 * @param condition		La condition a résoudre.
	 * @param environment	Le contenant des variables.
	 * @return La String à l'état plus simple
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
	 * @param line La line à remplacer
	 * @param environment L'environnement de variable
	 * @return La String avec les variables remplacées
	 * @throws UnknownVariableException
	 */
	private static String replaceVariable(String line, Environment environment) throws UnknownVariableException {
		boolean inString = false;
		
		for (int i = 0; i < line.length(); i++) {
			StringBuilder varName = new StringBuilder();
			//Évalue si on est dedans une string
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
				while (i < line.length() &&  //Recherche un début de mot afin de cerné une variable
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
					!varName.toString().equals(WordsBoolean.VRAI.getWord())) {  //Si un mot à été trouvé

				//System.out.println(varName.toString() + "   " + environment.getVariableByName(varName.toString()));
				Object varValue = environment.getVariableByName(varName.toString());

				//Replace l'index au début du mot
				i = i - varName.length();

				if (varValue == null) {
					throw new UnknownVariableException(varName.toString());
				}
				else if (varValue instanceof String) { //Une chaine de caractères
					line = line.substring(0, i) + "\"" + varValue.toString() + "\"" + line.substring(i + varName.toString().length());
					i += 2 + varValue.toString().length();
				}
				else if (varValue instanceof Boolean){ //Un booléen
					//Pour une réecriture en français
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
	 * Ajoute la variable à l'environnement.
	 * @param line	Le texte de la ligne pour déclarer la variable.
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
		//On vérifie la validité de la déclaration
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

		//On regarde le type et on ajoute la variable à l'environnement.
		String s = declaration.trim().toLowerCase();
		Object result = null;
		//Si c'est une chaine de caractère...
		if(s.startsWith(WordsDeclaration.CHAINE.getWord())) {
			result = Parser.stringConcat(afterEquals, env);
		}
		//Si c'est un entier
		else if(s.startsWith(WordsDeclaration.ENTIER.getWord())) {
			result = Parser.evalMathOperation(afterEquals, env);
		}
		//Si c'est un booléen
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
	 * Méthode lançant l'exception UseOfReservedWord si la chaîne passée 
	 * en paramètre est un mot réservée
	 * @param s Le mot à vérifier
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
	 * @param line	Le texte de la ligne pour modifié la variable.
	 * @param env	Le contenant des variables.
	 * @throws SyntaxException
	 * @throws UnknownVariableException
	 * @throws MissingParenthesisException
	 * @throws ArithmeticSyntaxException 
	 * @throws TypeMissMatchException 
	 */
	protected static void assignment(String line, Environment env) throws SyntaxException, UnknownVariableException, MissingParenthesisException, ArithmeticSyntaxException, TypeMissMatchException {
		//On vérifie la validité de l'assignement
		String assign = "[\\s]*[A-Za-z_][A-Za-z0-9_]*[\\s]*(<\\-).*";
		Pattern pattern = Pattern.compile(assign);  
		Matcher matcher = pattern.matcher(line);  
		if(!matcher.matches()){  
			throw new SyntaxException();
		}

		//On prend le nom et la valeur à affecter la variable
		String name = line.substring(0, line.indexOf("<-")).trim();
		String var = Parser.trimEquals(line);

		//Prend la variable pour connaître son type
		Object obj = env.getVariableByName(name);
		if(obj != null) {
			if(obj instanceof String) {
				//Si c'était une String, on appelle la fonction pour concaténer
				String concat = Parser.stringConcat(var, env);
				env.setVariableByName(name, concat);
			}
			else if(obj instanceof Integer) {
				//Si c'était un entier, on évalue l'expression mathématique
				Integer value = Parser.evalMathOperation(var, env);
				env.setVariableByName(name, value);
			}
			else if(obj instanceof Boolean) {
				//Si c'est un booléen, on regarde si c'est juste un booléen unique
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
	 * Donne tout le texte après le premier <- de la string en paramètre
	 * @param instruction La ligne d'assignation
	 * @return Le texte après le premier <- de la string en paramètre
	 */
	private static String trimEquals(String instruction) {
		String retour = instruction.substring(instruction.indexOf("<-") + 2);

		return retour;
	}

	/**
	 * Méthode retournant une chaîne représentant la concaténation de celle du pseudo-code
	 * 	sur la ligne donnée.
	 * @param line	Le texte de la ligne contenant une plusieurs chaîne de caractères
	 * 				en pseudo-code.
	 * @param env	Le contenant des variables.
	 * @return	La chaîne de caractères résultante de celle en pseudo-code
	 * @throws UnknownVariableException
	 * @throws SyntaxException
	 * @throws ArithmeticSyntaxException
	 */
	protected static String stringConcat(String line, Environment env) throws UnknownVariableException, SyntaxException, ArithmeticSyntaxException {
		String retour = "";
		//On valide la string
		if(Parser.validConcatSyntax(line)) {
			//On divise la String pour obtenir chacune des parties de la concaténation
			LinkedList<String> list = Parser.splitConcatString(line, env);
			StringBuilder stringFinal = new StringBuilder("");
			for(String s : list) {
				//Si c'est une chaine...
				if(s.toString().startsWith("\"")) {
					stringFinal.append(s.substring(1, s.length() - 1));
				}
				//Si c'est un booléen
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
	 * Méthode qui divise une chaine valide de concaténation. Le signe de concaténation
	 * est le point(.).
	 * @param s La chaine à concaténer
	 * @param env L'environnement contenant les variables actuelles
	 * @return Un tableau de string contenant les parties de la chaîne
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
			case '"': //Si c'est un guillemet, c'est un début ou de fin de string
				inString = !inString;
				temp.append(letter);
				if(!inString) {
					//Si c'est la fin d'une string
					list.add(temp.toString().trim());//On l'ajoute dans la liste
					temp = new StringBuilder("");
				}
				break;
			case '\\': //Si c'est un barre oblique arrière...
				if(inString) {
					//On affiche seulement le prochain caractère
					temp.append(word[j]);
					//On passe par dessus le prochain caractère.
					j++;
				}
				break;
			case '.': //Si c'est un point (signe de concaténation)
				if(inString) {//Si on est dans une string, on l'ajoute.
					temp.append(letter);
				}
				else if(inVariable) {
					//Sinon, on ajoute la variable à la liste
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
					//Si c'est le dernier caractère, on ajoute le dernier "temp" dans la liste
					list.add(temp.toString().trim());
				}
			}
		}
		
		return list;
	}

	/**
	 * Valide la syntaxe d'une concaténation.
	 * @param s La chaine de concaténation(s)
	 * @return Vrai si la chaîne de concaténation est valide; sinon faux.
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
			case '"': //Si c'est un guillemet, c'est un début ou de fin de string
				inString = !inString;
				if(!inString) {
					//Si on vient de sortir d'une string, on attend le signe des concaténations
					waitForPlus = true;
				}
				else if(inString && waitForPlus) {
					//Si c'est une ouverture de chaine et et qu'on attendait une chaine, il y a erreur.
					error = true;
				}
				break;
			case '\\': //Si c'est un barre oblique arrière...
				if(inString) {
					//Si on est dans une chaine, on passe au prochain caractère.
					i++;
				}
				else {
					//Si on n'est pas dans une chaine, il y a erreur.
					error = true;
				}
				break;
			case '.': //Si c'est un point (signe de concaténation)
				if(!inString && waitForPlus) {
					//Si on n'est pas dans une chaine, on n'attend
					//plus pour le signe de concaténation et on n'est 
					//plus dans une variable
					waitForPlus = false;
					inVariable = false;
				}
				else if(!inString && !waitForPlus) {
					//Si on est pas dans une chaine et qu'on
					//attend pas pour un signe de concaténation
					error = true;
				}
				break;
			case ' ':
				if(inVariable) {
					//Si on était dans une variable, on attend pour un 
					//signe de concaténation et on n'est plus dans une
					//variable
					waitForPlus = true;
					inVariable = false;
				}
				break;
			default:
				if(!inString && !waitForPlus) {
					//Si on était pas dans une chaine et qu'on attendait pas
					//pour un signe de concaténation, on est alors dans une 
					//variable et on attend un signe de concaténation.
					waitForPlus = true;
					inVariable = true;
				}
				else if(!inString && !inVariable && waitForPlus) {
					//Si on est dans une string ou dans une variable et qu'on attend pour un
					//signe de concaténation, on est dans une expression mathématique
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
	 * Algorithme qui transforme un calcul ou une condition de forme infixée vers une forme 
	 * postfixée.  Cet algorithme prend en compte les parenthèses et prend pour acquis que 
	 * la chaine est valide.  La chaine doit contenir uniquement des opérateurs et des
	 * nombres entiers.
	 * 
	 * La forme postfixée sépare les différents opérateurs et les nombre par une virgule.
	 * La forme postfixée peut commencer par une virgule.
	 * 
	 * Source: http://scriptasylum.com/tutorials/infix_postfix/infix_postfix.html
	 * @param infix		La chaine sous forme infixée.
	 * @return			Une chaine sous form postfixée
	 */
	private static String infixeToPostfix(String infix) {

		Stack<Character> stack = new Stack<Character>();
		StringBuilder postfix = new StringBuilder();


		//On enlève les espaces
		infix = infix.replaceAll(" ", "");
		//Remplacement de tout les signes
		infix = infix.replaceAll("--", "+");
		infix = infix.replaceAll("\\+-", "-");
		infix = infix.replaceAll("\\*-", "*-1*");

		//Vérifie qu'il n'y a pas d'erreur du genre 5/+5
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

//				//Ajoute un zéro pour bien gère la négation en début de déclaration
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
					stack.pop(); //Pop de la parenthèse ouvrante
				}
				break;
			default: //un caractère autre
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
	 * Fonction qui retourne la priorité d'un opérateur afin
	 * de pouvoir bien exécuter la priorité d'opération.
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
	 * Algorithme qui évalue une condition sous forme postfixée.
	 * Cet algorithme prend en compte que la chaine est valide et que
	 * tous les booléens et opérateurs sont séparés d'une virgule.
	 * 
	 * Source: http://scriptasylum.com/tutorials/infix_postfix/infix_postfix.html
	 * @param postfix		La chaine sous la forme postfixée
	 * @return				Le résultat sous la forme d'une chaine.
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
	 * Algorithme qui évalue une équation mathématique sous forme postfixée.
	 * Cet algorithme prend en compte que la chaine est valide et que
	 * tous les nombres et opérateurs sont séparés d'une virgule.
	 *  
	 * Source: http://scriptasylum.com/tutorials/infix_postfix/infix_postfix.html
	 * @param postfix		La chaine sous la forme postfixée
	 * @return				Le résultat sous la forme d'une chaine.
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
	 * Méthode qui évalue une opération  mathématique simple ou compliquée.
	 *
	 * @param operation						Le calcul à exécuter.
	 * @return								La réponse au calcul
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
	 * Algorithme qui valide si une expression est une expression mathématique valide à
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
		{ 	{2,2,2,2,2,2,2,2,2,2,-1,1,-1,-1,-1,0,-1}, 		//état 0
				{2,2,2,2,2,2,2,2,2,2,-1,1,-1,-1,-1,0,-1}, 		//état 1
				{2,2,2,2,2,2,2,2,2,2,4,4,4,4,4,-1,3}, 			//état 2
				{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,4,1,4,4,4,-1,3}, //état 3
				{2,2,2,2,2,2,2,2,2,2,-1,1,-1,-1,-1,0,-1}  		//état 4
		};
		//L'état
		int state = 0;
		//Compteur pour les parenthèses
		int cpt = 0;
		//Index du curseur
		int i = 0;
		//Position d'une lettre dans line
		int position = 0;
		//Le charactere de la chaîne
		char character;

		do {
			character = operation.charAt(i);
			//Balancer les parenthèses
			if (character == '(') {
				cpt++;
			}
			else if (character == ')') {
				cpt--;
			}

			if (cpt >= 0) {
				position = line.indexOf(character);
				if (position >= 0) { //C'est un caractère valide
					//On passe au prochaine état
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
				state == 0 ||	//État non terminal
				state == 1||
				state == 4 ||
				position == -1) {
			valid = false;
		}

		return valid;
	}

	/*
	 * Méthode qui valide si le nombre de parenthèses ouvrantes est égale au nombre
	 * de parenthèses fermantes.
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
