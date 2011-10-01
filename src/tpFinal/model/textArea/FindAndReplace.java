package tpFinal.model.textArea;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.JOptionPane;

import tpFinal.form.AppFrame;


/**
 * 	Classe avec les fonctions de recherches et de remplacements
 * 
 * Toutes les méthodes de cette classe sont statiques afin d'éviter de créer des objets
 * afin d'utiliser que des méthodes utilitaires.
 * 
 *  @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class FindAndReplace {

	/**
	 * Enlève les retours de chariot (\r) pour éviter les problèmes
	 *  de position de le AppText.
	 * @param text	La chaîne à corriger
	 * @return		La chaîne corrigée
	 */
	public static String removeCariageReturn(String text){
		return text.replaceAll("\r", "");
	}

	/**
	 * Sélectionne le mot recherché à la position recherché
	 * @param searched 		Expression recherché
	 * @param index		 	Index du terme recherché
	 */
	public static void find(String searched, int index){
		int i = 1;
		try{
			Pattern pattern = Pattern.compile(searched);
			Matcher matcher = pattern.matcher(
					FindAndReplace.removeCariageReturn(AppFrame.getInstance().getAppText().getText()));
			if (matcher.find())
			{
				AppFrame.getInstance().getAppText().select(matcher.start(), matcher.end());
				System.out.println(matcher.start());
				System.out.println(matcher.end());
				while (matcher.find() && i < index){
					AppFrame.getInstance().getAppText().select(matcher.start(), matcher.end());
					i++;
					System.out.println(matcher.start());
					System.out.println(matcher.end());
				}
			}
			else {
				noResult();
			}
		} catch(PatternSyntaxException pse){
			JOptionPane.showMessageDialog(AppFrame.getInstance(), "Recherche invalide");
		}
	}

	/**
	 * Affiche un MessageDialog pour dire que le terme n'est pas trouvable
	 */
	public static void noResult(){
		JOptionPane.showMessageDialog(AppFrame.getInstance(), "Impossible de trouver le terme recherché");
	}

	/**
	 * Fonction qui cherche le terme et le remplace
	 * @param searched		Terme recherché	
	 * @param newText		Terme à modifié
	 */
	public static  void remplaceFind(String searched, String newText){
		try{
			Pattern pattern = Pattern.compile(searched);
			Matcher matcher = pattern.matcher(
					FindAndReplace.removeCariageReturn(AppFrame.getInstance().getAppText().getText()));
			if (matcher.find()) {
				if (! AppFrame.getInstance().getAppText().getCaretListener().isSelected()){
					AppFrame.getInstance().getAppText().select(matcher.start(), matcher.end());
				}
				else if (AppFrame.getInstance().getAppText().getCaretListener().isSelected() &&
						AppFrame.getInstance().getAppText().getSelectedText().equals(searched)) {
					AppFrame.getInstance().getAppText().replaceSelection(newText);
				}
			}
			else {
				noResult();
			}

		} catch(PatternSyntaxException pse){
			JOptionPane.showMessageDialog(AppFrame.getInstance(), "Recherche invalide");
		}
	}

	/**
	 * Remplace tous les termes trouvés par le nouveau terme
	 * @param searched		Terme recherché
	 * @param newText		Terme à modifié
	 */
	public static void remplaceAll(String searched, String newText) {
		int i = 0;
		
		//Parcours le texte pour calculer la quantité de fois que le terme est présent
		try{
			Pattern pattern = Pattern.compile(searched);
			Matcher matcher = pattern.matcher(
					FindAndReplace.removeCariageReturn(AppFrame.getInstance().getAppText().getText()));
			while(matcher.find()) {
				i++;
			}
		}catch(PatternSyntaxException pse){
			JOptionPane.showMessageDialog(AppFrame.getInstance(), "Recherche invalide");
		}
		
		//Effectue la modification et remets à jour le matcher à chaque boucle pour éviter les problèmes si le mot
		// à remplacé n'est pas de la même longueur que le nouveau mot
		for (int j = 0; j < i; j++){
			try{
				Pattern pattern = Pattern.compile(searched);
				Matcher matcher = pattern.matcher(
						FindAndReplace.removeCariageReturn(AppFrame.getInstance().getAppText().getText()));
				while(matcher.find()) {
					AppFrame.getInstance().getAppText().select(matcher.start(), matcher.end());
					if (AppFrame.getInstance().getAppText().getCaretListener().isSelected() &&
							AppFrame.getInstance().getAppText().getSelectedText().equals(searched)) {
						AppFrame.getInstance().getAppText().replaceSelection(newText);
					}
				}
			} catch(PatternSyntaxException pse){
				JOptionPane.showMessageDialog(AppFrame.getInstance(), "Recherche invalide");
			}
		}
	}	
}