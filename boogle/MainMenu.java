package boogle;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

public class MainMenu extends JFrame{
	
	//Atributos
	JLabel image;
	JLabel background;
	
	JButton btn_play;
	JButton btn_scores;
	JButton btn_exit;
	
	HashWord scores;
	
	HashScore backup;
	String puntajes;
	
	//Constructor
	public MainMenu(){
		setUndecorated(true);
		setSize(200,300);
		setLocationRelativeTo(null);
		setVisible(true);
		setLayout(null);
		setResizable(false);
		getContentPane().setBackground(Color.BLACK);
		iniComponents();
		iniButtons();
		
		//Icono del programa
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.createImage("src/resources/icono.png");
		setIconImage(img);
		
		//Recuperar el archivo con los puntajes
		backup = null;
		puntajes = "No hay puntajes";
		
	    try {
	    	ObjectInputStream reader = new ObjectInputStream(new FileInputStream("registros.dat"));
		    backup =  (HashScore) reader.readObject();
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	//Método mapa los componentes de la ventana
	private void iniComponents(){
		Image logo = new ImageIcon("src/resources/boggle.png").getImage();
		image = new JLabel(new ImageIcon(logo.getScaledInstance(180, 160, Image.SCALE_SMOOTH)));
		
		Image back = new ImageIcon("src/resources/back.jpg").getImage();
		background = new JLabel(new ImageIcon(back.getScaledInstance(200, 300, Image.SCALE_SMOOTH)));
		
		btn_play = new JButton("Jugar");
		btn_scores = new JButton("Ver puntuación");
		btn_exit = new JButton("Salir");
		
		btn_play.setBackground(Color.BLACK);
		btn_scores.setBackground(Color.BLACK);
		btn_exit.setBackground(Color.BLACK);
		
		btn_play.setForeground(Color.GREEN);
		btn_scores.setForeground(Color.GREEN);
		btn_exit.setForeground(Color.GREEN);
		
		btn_play.setFocusable(false);
		btn_scores.setFocusable(false);
		btn_exit.setFocusable(false);
		
		image.setBounds(10,10,180,160);
		background.setBounds(0, 0, 200, 300);
		
		btn_play.setBounds(10,180,180,30);
		btn_scores.setBounds(10,220,180,30);
		btn_exit.setBounds(10,260,180,30);
		
		btn_play.setBorder(new RoundedBorder(40));
		btn_scores.setBorder(new RoundedBorder(40));
		btn_exit.setBorder(new RoundedBorder(40));
		
		add(image);
		add(btn_play);
		add(btn_scores);
		add(btn_exit);
		add(background);
		
		repaint();
	}
	
	//Aquí se agregan los métodos a los botones
	private void iniButtons(){
		
		btn_play.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String name;
				
				if(backup!=null) {
					do {
						name = JOptionPane.showInputDialog(null,"Ingresé su nickname:");
						if(name != null && backup.isHere(name)) JOptionPane.showMessageDialog(null, "El nombre ya existe\nIntenté con otro.","Nombre invalido",JOptionPane.WARNING_MESSAGE);
						if(name != null && name.length() > 16) JOptionPane.showMessageDialog(null, "El nombre es muy largo\ndebe tener menos de 16 caracteres.","Nombre demaciado largo",JOptionPane.ERROR_MESSAGE);
					}while(name != null && name.length() < 1 || name.length() > 16 || backup.isHere(name));
				}else {
					do {
						name = JOptionPane.showInputDialog("Ingresé su nickname:");
						if(name != null && name.length() > 16) JOptionPane.showMessageDialog(null, "El nombre es muy largo\ndebe tener menos de 16 caracteres.","Nombre demaciado largo",JOptionPane.ERROR_MESSAGE);
					}while(name != null && name.length() < 1 || name.length() > 16);
				}
				
				String difficulty = (String) JOptionPane.showInputDialog(null,"Seleccioné una dificultad:","Slección de dificultad",JOptionPane.QUESTION_MESSAGE,new ImageIcon("src/resources/pensar.png"),new String[] {"Fácil","Normal","Difícil"},"Normal");
				
				if(difficulty != null) {
					new BoogleGame(name,difficulty);
					dispose();
				}
			}
		});
		
		btn_scores.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			    
			    if(backup != null && !backup.isEmpty()) {
			    	puntajes = backup.getNames();
			    }
			    
				JOptionPane.showMessageDialog(null,puntajes,"Puntajes",JOptionPane.DEFAULT_OPTION,new ImageIcon("src/resources/trofeo.jpg"));
			}
		});
		
		btn_exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(null,"¿Seguro de que quieres salir?","Confirmación de cierre", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("src/resources/miloco.png"));
					if (option == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
			}
		});
	}
}