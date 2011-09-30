package tpFinal.model.prohibitedWords;

/**
 * Enum des mots clés liés aux déclarations de variables.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public enum WordsDeclaration implements Words {
	BOOLEEN("booleen"),
	ENTIER("entier"),
	CHAINE("chaine");
	
	private String word;
	
	private WordsDeclaration(String word) {
		this.word = word;
	}

	public String getWord() {
		return this.word;
	}
}
