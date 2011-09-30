package tpFinal.model.prohibitedWords;

/**
 * Enum des mots cl�s li�s au segment conditionnel non r�p�t�.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
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
