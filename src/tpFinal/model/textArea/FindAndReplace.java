package tpFinal.model.textArea;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.JOptionPane;

import tpFinal.form.AppFrame;


/**
 * 	Classe avec les fonctions de recherches et de remplacements
 * 
 * Toutes les m�thodes de cette classe sont statiques afin d'�viter de cr�er des objets
 * afin d'utiliser que des m�thodes utilitaires.
 * 
 *  @author Louis-�tienne Dorval, Alexis L�gar�-Julien, Fr�d�rik Paradis et Simon Perreault
 */
public class FindAndReplace {

	/**
	 * Enl�ve les retours de chariot (\r) pour �viter les probl�mes
	 *  de position de le AppText.
	 * @param text	La cha�ne � corriger
	 * @return		La cha�ne corrig�e
	 */
	public static String removeCariageReturn(String text){
		return text.replaceAll("\r", "");
	}

	/**
	 * S�lectionne le mot recherch� � la position recherch�
	 * @param searched 		Expression recherch�
	 * @param index		 	Index du terme recherch�
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
		JOptionPane.showMessageDialog(AppFrame.getInstance(), "Impossible de trouver le terme recherch�");
	}

	/**
	 * Fonction qui cherche le terme et le remplace
	 * @param searched		Terme recherch�	
	 * @param newText		Terme � modifi�
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
	 * Remplace tous les termes trouv�s par le nouveau terme
	 * @param searched		Terme recherch�
	 * @param newText		Terme � modifi�
	 */
	public static void remplaceAll(String searched, String newText) {
		int i = 0;
		
		//Parcours le texte pour calculer la quantit� de fois que le terme est pr�sent
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
		
		//Effectue la modification et remets � jour le matcher � chaque boucle pour �viter les probl�mes si le mot
		// � remplac� n'est pas de la m�me longueur que le nouveau mot
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