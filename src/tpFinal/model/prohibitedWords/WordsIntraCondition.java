package tpFinal.model.prohibitedWords;

/**
 * Enum des mots clés liés aux opérateurs booléen.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public enum WordsIntraCondition implements Words {
	ET("et"),
	OU("ou");
	
	private String word;
	
	private WordsIntraCondition(String word) {
		this.word = word;
	}

	public String getWord() {
		return this.word;
	}
}
