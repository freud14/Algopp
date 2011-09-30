package tpFinal.model.prohibitedWords;

/**
 * Enum des mots cl�s li�s aux d�clarations de variables.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
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
