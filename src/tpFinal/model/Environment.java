package tpFinal.model;

//import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Cette classe gère les variables déclarés dans le pseudo-code de l'utlisateur.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 * 
 */
public class Environment {

	//HashTable qui stocke les variables avec leur valeur
	private Hashtable<String, Object> variables = new Hashtable<String, Object>();
	
	/**
	 * Cette ajoute une nouvelle variable dans l'environnement.
	 * @param name Le nom de la nouvelle variable
	 * @param value L'objet représentant le valeur de la variable
	 * @return Retourne vrai si l'ajout s'est effectué; faux si elle existait déjà.
	 */
	protected boolean addVariable(String name, Object value) {
		boolean retour = false;
		if(!this.variables.containsKey(name)) {
			retour = true;
			this.variables.put(name, value);
		}
		
		return retour;
	}

	/**
	 * Renvoie l'objet représentant une variable.
	 * @param name Le nom de la variable
	 * @return Retourne l'objet représentant la variable.
	 */
	protected Object getVariableByName(String name) {
		return this.variables.get(name);
	}
	
	/**
	 * Modifie l'objet représentant une variable.
	 * @param name Le nom de la variable
	 * @param value L'objet réprésentant la variable
	 * @return Retourne vrai si la variable existait; faux sinon.
	 */
	protected boolean setVariableByName(String name, Object value) {
		boolean retour = false;
		if(this.variables.containsKey(name)) {
			retour = true;
			this.variables.put(name, value);
		}
		
		return retour;
	}
}
