package tpFinal.form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

/**
 * Cette classe affiche une boîte de dialogue 
 * présentant l’application, le nom des 
 * programmeurs et illustrée d’une image.
 * @author Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault
 * */
public class AppAboutDialog extends JDialog implements ActionListener {

	private final static String DEFAULT_TITLE = "À propos..."; //Titre par défaut de l'application
	private final static String DEFAULT_OK = "OK"; //Label du bouton OK
	private static final String ABOUT_ICON = "../ressources/about.gif"; //Chemin de l'image About
	
	//Texte de la fenêtre À propos...
	private static final String MESSAGE = "Cet outil de développement d'algorithme a été développé par Louis-Étienne Dorval, Alexis Légaré-Julien, Frédérik Paradis et Simon Perreault. Il est présentement en état de prototype et ne devrait pas servir dans un cadre de production. Pour connaître le fonctionnement de l'outil, voir l'aide dans le menu « ? ».\r\n\r\nCet outil est sous licence GNU GPL. Pour avoir des détails sur la licence, rendez vous sur le site gnu.org.";
	
	private static final Dimension DEFAULT_SIZE = new Dimension(400, 150); //Taille par défaut de la fenêtre

	/**
	 * Crée l'interface de la fenêtre À propos...
	 */
	public AppAboutDialog() {
		super();

		this.setTitle(AppAboutDialog.DEFAULT_TITLE);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setModal(true);

		//Création du Layout
		this.setLayout(new BorderLayout());

		//Label et image 
		JTextArea label = new JTextArea(AppAboutDialog.MESSAGE);
		label.setFont(new Font(label.getFont().getFamily(), label.getFont().getStyle(), 12));
		label.setLineWrap(true);
		label.setWrapStyleWord(true);
		label.setEditable(false);
		label.setColumns(30);
		JScrollPane scroll = new JScrollPane(label);
		scroll.setPreferredSize(AppAboutDialog.DEFAULT_SIZE);

		ImageIcon img = new ImageIcon(this.getClass().getResource(AppAboutDialog.ABOUT_ICON));
		JLabel lblImage = new JLabel(img);

		//Création du bouton OK
		JButton boutonOK = new JButton(AppAboutDialog.DEFAULT_OK);
		boutonOK.setActionCommand(AppAboutDialog.DEFAULT_OK);
		boutonOK.addActionListener(this);
		JPanel panelOK = new JPanel();
		panelOK.add(boutonOK);

		this.add(scroll, BorderLayout.NORTH);
		this.add(lblImage, BorderLayout.CENTER);
		this.add(panelOK, BorderLayout.SOUTH);

		this.pack();
		this.setLocationRelativeTo(AppFrame.getInstance());
	}

	/**
	 * Gère le clique sur le bouton OK
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(AppAboutDialog.DEFAULT_OK)) {
			this.dispose();
		}		
	}
}
