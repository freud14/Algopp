package tpFinal.model.prohibitedWords;

/**
 * Enum des mots cl�s li�s aux op�rateurs bool�en.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
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
