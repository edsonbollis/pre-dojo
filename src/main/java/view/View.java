package view;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import entities.User;
import services.Service;

@Component("view")
public class View extends JFrame {

	@Autowired
	Service service;

	@Value("${app.label.menu}")
	String labelMenu;
	
	@Value("${app.label.item}")
	String labelItem;
	
	@Value("${app.title.text}")
	String titleText;
	
	String AWARD = "award";

	private Container container;
	private JMenuBar menubar;;
	private JMenu archive;
	private JTextArea text;
	private Action open;

	/**
	 * Criação da tela de visualização.
	 */
	public void createView() {
		//Inicialização do componente
		this.text = new JTextArea();
		this.menubar = new JMenuBar();
		this.archive = new JMenu(labelMenu);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.container = this.getContentPane();
		
		//Creação da classe abstrata que gera uma ação na tela
		this.open = new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				  	JFileChooser chooser = new JFileChooser();
			        java.awt.Component parent = null;
			        int returnVal = chooser.showOpenDialog(parent);
			        if(returnVal == JFileChooser.APPROVE_OPTION) {
			        	showLog(chooser.getSelectedFile().getPath());
			            }               
			        }
				
		};

		createMenu();
		generateGUI(this.container);
	}

	/**
	 * Cria o menu da aplicação.
	 */
	private void createMenu() {
		JMenuItem item = new JMenuItem(this.open);
		this.archive.add(item);
		this.menubar.add(this.archive);
		this.setJMenuBar(this.menubar);
		item.setLabel(labelItem);
		
	}


	/**
	 * @param container: Container
	 * 
	 * Gera os elementos da interface gráfica com o usuário.
	 * 
	 */
	private void generateGUI(Container container) {
		container.add(new JScrollPane(this.text));
		this.text.setEditable(false);
		this.setVisible(true);
		this.setSize(640, 480);
		Font  defaultFont = new Font("ArialBlack", Font.PLAIN, 16);
		this.text.setFont(defaultFont);
	}
	
	
	/**
	 * @param path: String
	 * 
	 * Gera o texto que será mostrado na interface.
	 * 
	 */
	private void showLog(String path){
		
		Collection<User> collection = service.generateRank(new File(path));
		StringBuilder strBuilder = new StringBuilder("\n        " + titleText + "\n ");
		
		boolean first = true;
		for (User user : collection) {
			strBuilder.append("         " + user.getName());
			strBuilder.append(" \t  ");
			strBuilder.append(service.getKilledForUser(user) == null?0:service.getKilledForUser(user).size());
			strBuilder.append(" \t  ");
			strBuilder.append(service.getUserDeaths(user) == null?0:service.getUserDeaths(user).size());
			strBuilder.append(" \t  ");
			strBuilder.append(!first || service.predilectWeapon(user) == null?" - ":service.predilectWeapon(user).getName());
			strBuilder.append(" \t \t ");
			strBuilder.append(service.userDeath(user) && !service.numberDeathsInTime(user)?" - \t ":AWARD);
			strBuilder.append(" \n ");
			
			
			first = false;
			
		}
		
		text.setText(strBuilder.toString());
		
		
	}
	
}
