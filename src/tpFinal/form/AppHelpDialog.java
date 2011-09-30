package tpFinal.form;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * Cette classe est une fenêtre de dialogue qui une aide pour le pseudo-code.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 */
public class AppHelpDialog extends JDialog {

	private static final String DEFAULT_TITLE = "Aide";
	private static final String AIDE = "<h1 style=\"text-align:center;\">Aide</h1><p>Cet outil est un outil de développement d’algorithme par l’utilisation d’un pseudo-code en français Il est présentement en état de prototype et ne devrait pas servir dans un cadre de production. Pour connaître la licence et les crédits de l'outil, voir la fenêtre « À propos » dans le menu « ? ».</p><h2>Fonctionnement</h2><h3>Déclarations et affectations de variables</h3><p>La déclaration d’une variable doit obligatoirement se faire au début du pseudo-code avant tout autre instruction. On ne peut pas déclarer une variable autre part dans le code. Les types de variable reconnu dans l’outil sont le type Booléen, Entier et Chaine. Par contre, on peut affecter une variable partout dans le code sauf dans les conditions. L’opérateur d’affection est &#60;-. Les valeurs booléennes sont « Vrai » et « Faux ». Les chaines de caractère sont délimités par des guillemets anglais : « \" ». Voici quelques exemples d’initialisation de variable et d’affectation:</p><pre>\tEntier int &#60;- 23\r\n\tEntier int2 &#60;- 1\r\n\tint &#60;- 3\r\n\tChaine string &#60;- \"machainedecaractère\"\r\n\tBooleen bool &#60;- Vrai</pre><h3>Calculs mathématiques</h3><p>Certaines opérations arithmétiques sont prises en charge par cet outil : la somme avec l’opérateur « + », la soustraction avec l’opérateur « - », la multiplication avec l’opérateur « * », la division avec l’opérateur « / » et le modulos avec l'opérateur « % ». Les parenthèses sont également prises en compte. Ces opérations ne fonctionnent évidemment que sur des variables de type entières. Voici quelques exemples de calculs arithmétiques :</p><pre>\tint &#60;- int + 2\r\n\tint &#60;- (int2 +4)\r\n\tint2 &#60;- int</pre><h3>Conditions</h3><p>Les conditions sont prises en charge par cet outil. Le mot clé de comparaison est « Si » et se termine par le mot clé « FinSi ». Le mot clé « SinonSi » existe également. Il y a aussi les mots clés « Ou » et « Et » qui servent à créer des conditions multiples. Les opérateurs de comparaison pris en charge sont « = », « <= », « >= », « <> », « < » et « > ». Il est aussi possible de comparer une variable avec une opération arithmétique. Voici un exemple de comparaison :</p><pre>\tSi((var = 4 Et string = \"machainedecaractère\") Ou int2 = int + 6)\r\n\t\tbool &#60;- Faux\r\n\tSinonSi(int = 2)\r\n\t\tbool &#60;- Vrai\r\n\tSinon\r\n\t\tvar &#60;- 2\r\n\tFinSi\r\n</pre><h3>Itération</h3><p>Les itérations conditionnelles sont prises en charge par cet outil.  Le mot clé est « TantQue » suivit d'une conditon (voir condition) et se termine par le mot clé « FinTantQue ».  Voici un exemple de comparaison : </p> <pre> \tEntier var &#60;- 0\r\n\tTantQue (var &#60; 4)\r\n\t\tvar &#60;- var + 1\r\n\tFinTantQue\r\n</pre><h3>Opérations d'entrées-sorties</h3><p>Il est possible de demander du texte à l'utilisation avec le mot clé « Lire » et d’afficher du texte à l’utilisateur avec le mot clé « Afficher ». En voici un exemple :</p><pre>\tChaine texte &#60;- \"Salut\"\r\n\tLire texte\r\n\tAfficher texte</pre><h3>Exemples</h3><p>Voici un exemple qui comporte tout ce que supporte notre application :</p><pre>\tEntier age &#60;- 0\r\n\tChaine affiche &#60;- \"\"\r\n\tLire age\r\n\tSi(age &#60; 25)\r\n\t\taffiche &#60;- \"Vous avez moins de 25 ans\"\r\n\tSinonSi(age &#60; 30)\r\n\t\taffiche &#60;- \"Vous avez moins de 30 ans\"\r\n\tSinon\r\n\t\taffiche &#60;- \"Vous plus de 30 ans\"\r\n\tFinSi\r\n\tAfficher affiche</pre>";
	
	private static final Dimension DEFAULT_DIMENSION = new Dimension(700, 600);
	
	/**
	 * Crée l'interface de la fenêtre Aide
	 */
	public AppHelpDialog() {
		super();

		this.setTitle(AppHelpDialog.DEFAULT_TITLE);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setModal(true);
		
		JEditorPane texte = new JEditorPane();
		texte.setContentType("text/html");
		//texte.set
		texte.setText(AppHelpDialog.AIDE);
		texte.setEditable(false);
		texte.setCaretPosition(0);

		JScrollPane scroll = new JScrollPane(texte);

		this.add(scroll);

		this.setSize(AppHelpDialog.DEFAULT_DIMENSION);
		this.setLocationRelativeTo(AppFrame.getInstance());
	}
}
