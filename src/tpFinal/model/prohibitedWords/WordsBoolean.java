package tpFinal.model.prohibitedWords;

/**
 * Enum des différentes valeurs possibles d'un booléen.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public enum WordsBoolean implements Words {
	FAUX("faux"),
	VRAI("vrai");
	
	private String word;
	
	private WordsBoolean(String word) {
		this.word = word;
	}

	public String getWord() {
		return this.word;
	}
	
}
