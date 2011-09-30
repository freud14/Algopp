package tpFinal.model.prohibitedWords;

/**
 * Enum des mots cl�s li�s au segment conditionnel r�p�t�.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
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
