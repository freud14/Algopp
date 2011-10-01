package tpFinal.model.prohibitedWords;

/**
 * Enum des mots clés liés aux entrées-sorties.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public enum WordsInOut implements Words {
	AFFICHER("afficher"),
	LIRE("lire");
	
	private String word;
	
	private WordsInOut(String word) {
		this.word = word;
	}

	public String getWord() {
		return this.word;
	}
}
