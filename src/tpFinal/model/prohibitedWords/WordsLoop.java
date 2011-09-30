package tpFinal.model.prohibitedWords;

/**
 * Enum des mots clés liés au segment conditionnel répété.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public enum WordsLoop implements Words {
	TANTQUE("tantque"),
	FINTANTQUE("fintantque");
	
	private String word;
	
	private WordsLoop(String word) {
		this.word = word;
	}

	public String getWord() {
		return this.word;
	}
}
