package tpFinal.model.prohibitedWords;

/**
 * Enum des mots cl�s li�s aux entr�es-sorties.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
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
