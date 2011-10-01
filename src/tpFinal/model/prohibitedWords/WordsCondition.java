package tpFinal.model.prohibitedWords;

/**
 * Enum des mots clés liés au segment conditionnel non répété.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public enum WordsCondition implements Words {
	SI("si"),
	SINON("sinon"),
	SINON_SI("sinonsi"),
	FIN_SI("finsi");
	
	private String word;
	
	private WordsCondition(String word) {
		this.word = word;
	}

	public String getWord() {
		return this.word;
	}
}
