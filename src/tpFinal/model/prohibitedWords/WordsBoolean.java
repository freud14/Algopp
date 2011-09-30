package tpFinal.model.prohibitedWords;

/**
 * Enum des diff�rentes valeurs possibles d'un bool�en.
 * @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
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
