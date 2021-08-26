package boogle;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;

public class BoogleGame extends JFrame{
	
	//Constantes
	private final int FACIL = 0;
	private final int NORMAL = 1;
	private final int DIFICIL = 2;
	
		/*
	 * 
	 * 			<< Sobre las dificultades >>
	 * 
	 * Fácil:
	 * 	> Tienes 6 minutos
	 *  > Los bonus de tiempo valen el doble de la longitud de la palabra
	 *  > El tablero no cambia
	 *  
	 * Normal:
	 *  > Tienes 5 minutos
	 *  > Los bonus de tiempo valen la longitud de la palabra
	 *  > El tablero no cambia
	 *  
	 * Difícil:
	 *  > Tienes 3 minutos y 30 segundos
	 *  > Los bonus de tiempo valen la longitud de la palabra
	 *  > El tablero cambia cada minuto.
	 * 
	 	*/
	
	//Atributos
	private String player_name;
	public static String str_word;
	private int bien = 0;
	private int dificultad = 0;
	
	//Labels para la información
	private JLabel jl_palabra;
	private JLabel jl_aciertos;
	private JLabel jl_tiempo;
	private JLabel jl_timePlus;
	
	private JTextArea jta_historial;
	private JScrollPane scroll;
	
	//Botones
	private BoogleButton tablero[][];
	private JButton btn_confirmar;
	private JButton btn_borrar;
	private JButton info;
	private JButton btn_reiniciar;
	private JButton btn_salir;
	
	private JPanel panel;
	
	//El Timer acualizara la información de los JLable
	private Timer time;
	
	//variables para el timepo
	private byte minutos;
	private byte segundos;
	
	//Contador para el tiempo de el texto de "tiempo plus"
	private byte plusCounter;
	
	//Un trie es para el archivo .txt y el otro para las palabras que encontro el jugador
	private HashWord jugador;
	private TrieStandard archivo;
	
	//Constructor
	public BoogleGame(String player_name,String difficulty){
		iniValues(player_name);
		
		switch (difficulty) {
		case "Fácil":
			dificultad = FACIL;
			break;
		case "Normal":
			dificultad = NORMAL;
			break;
		case "Difícil":
			dificultad = DIFICIL;
			break;
		default:
			dificultad = 0;
			break;
		}
		
		//Inicializo el timepo
		if(dificultad == FACIL) {
			minutos = 6;
			segundos = 0;
		}else if(dificultad == NORMAL){
			minutos = 5;
			segundos = 0;
		}else {
			minutos = 3;
			segundos = 30;
		}
		
		setTitle("Boogle - Jugador: "+player_name+" - Dificultad: "+difficulty);
	}
	
	private void iniValues(String player_name){
		
		this.player_name = player_name;
		jugador = new HashWord();
		
		//Se llena el trie con las palabras del archivo "palabras.txt"
		try { llenarDiccionario();}
		catch (IOException e) {
			JOptionPane.showInternalMessageDialog(null, "El Archivo \"src/resources/palabras.txt\" no ha sido encontrado","Error",JOptionPane.ERROR_MESSAGE);
		}
		
		//inicializo todos los compnentes de la ventana
		iniFrame();
		iniComponentes();
		iniBotones();
		
		plusCounter = 2;
		
		//El Timer ejecutara el método actualizar() cada 100 milisegundos
		time = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {actualizarTime();}
		});
		time.start();
	}

	//Método para incializar la ventan
	private void iniFrame(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,500);
		setLayout(null);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setBackground(Color.BLACK);
		
		//Icono del programa
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.createImage("src/resources/icono.png");
		setIconImage(img);
	}
	
	//Método para los componentes de la vetana
	private void iniComponentes() {
		
		//Instancias e inicialización de los componentes
		jl_aciertos = new JLabel("<< Puntos: 0 >>");
		jl_tiempo = new JLabel("tiempo: 0"+minutos+"´´"+segundos+"´");
		jl_timePlus = new JLabel();
		jl_palabra = new JLabel(" palabra:");
		
		jta_historial = new JTextArea(" Historial de palabras:\n");
		jta_historial.setEditable(false);
		scroll = new JScrollPane(jta_historial);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		btn_confirmar = new JButton("Revisar");
		btn_borrar = new JButton("borrar");
		btn_salir = new JButton("salir");
		info = new JButton("¿Cómo se juega?");
		btn_reiniciar = new JButton("reiniciar");
		tablero = new BoogleButton[8][8];
		
		str_word = "";
		panel = new JPanel(null);
		
		//Tamaño y posición de cada componente
		jl_aciertos.setBounds(440, 5, 100, 30);
		jl_tiempo.setBounds(440, 25, 100, 30);
		jl_timePlus.setBounds(526, 25, 50, 30);
		scroll.setBounds(420, 60, 150, 260);
		jta_historial.setBounds(420, 60, 150, 300);
		jl_palabra.setBounds(200,420,200,30);
		btn_confirmar.setBounds(5, 420, 80, 30);
		btn_borrar.setBounds(90,420,100,30);
		info.setBounds(420, 375, 150, 30);
		btn_reiniciar.setBounds(420,330,150,30);
		btn_salir.setBounds(420, 420, 150, 30);
		panel.setBounds(5, 5, 400, 400);
		
		//Quita el focus de los botones
		btn_confirmar.setFocusable(false);
		btn_borrar.setFocusable(false);
		info.setFocusable(false);
		btn_reiniciar.setFocusable(false);
		btn_salir.setFocusable(false);
		
		//Personalización de los colores de todos los componentes
		jl_aciertos.setForeground(Color.GREEN);
		jl_tiempo.setForeground(Color.WHITE);
		jl_timePlus.setForeground(Color.GREEN);
		
		jl_palabra.setForeground(Color.BLACK);
		jl_palabra.setBackground(Color.WHITE);
		jl_palabra.setOpaque(true);
		
		btn_confirmar.setBackground(Color.BLACK);
		btn_confirmar.setForeground(Color.GREEN);
		
		btn_borrar.setBackground(Color.BLACK);
		btn_borrar.setForeground(Color.RED);
		
		info.setBackground(Color.BLACK);
		info.setForeground(Color.GREEN);
		
		btn_reiniciar.setBackground(Color.BLACK);
		btn_reiniciar.setForeground(Color.GREEN);
		
		btn_salir.setBackground(Color.BLACK);
		btn_salir.setForeground(Color.GREEN);
		
		//Agregar los componentes al frame
		add(panel);
		add(jl_aciertos);
		add(jl_tiempo);
		add(jl_timePlus);
		add(scroll);
		add(jl_palabra);
		add(btn_confirmar);
		add(btn_borrar);
		add(info);
		add(btn_reiniciar);
		add(btn_salir);
		
		iniTablero();
		repaint();
	}
	
	//Instanciar los botones del tablero, sus valores y personalizarlos con color
	void iniTablero(){
		
		int letra;
		Font fuente = new Font("Arial", Font.BOLD, 16);
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				letra = letraGenerator();
				tablero[i][j] = new BoogleButton();
				tablero[i][j].setText(""+(char) letra);
				tablero[i][j].setFont(fuente);
				tablero[i][j].setSize(50,50);
				tablero[i][j].setOpaque(true);
				tablero[i][j].setBackground(Color.BLACK);
				tablero[i][j].setForeground(Color.YELLOW);
				tablero[i][j].setFocusable(false);
				panel.add(tablero[i][j]).setLocation(i*50, j*50);
			}
		}
	}
	
	//Para generar las letras del tablero
	private int letraGenerator() {
		int letra = (int)Math.floor(Math.random()*100);
		
		//probabilidad de las letras de mayor uso en el español
		if(letra <= 40) {
			letra = (int)Math.floor(Math.random()*(6));
			if(letra == 0) letra = 101; // E
			else if(letra == 1) letra = 97; //A
			else if(letra == 2) letra = 111; // O
			else if(letra == 3) letra = 115;
			else if(letra == 4) letra = 116;
			else letra = 114; //R
		}else {//Azar puro
			letra = (int)Math.floor(Math.random()*(122-97)+97);
		}
		return letra;
	}
	
	//Método para actualizar los valores de los JLabel
	void actualizar(){
		jl_aciertos.setText("<< Puntos: "+bien+" >>");
	}
	
	//Método para que el Timer actualicé los el JLabel con los valores del jl_tiempo
	void actualizarTime() {
		if(minutos > 0 || segundos > 0) {
			if(segundos > 0) segundos--;
			else {
				minutos--;
				segundos = 59;
			}
		}
		
		if(jl_timePlus.getText() != "") {
			plusCounter--;
			jl_tiempo.setForeground(Color.GREEN);
			if(plusCounter==0) {
				jl_tiempo.setForeground(Color.WHITE);
				jl_timePlus.setText("");
				plusCounter = 2;
			}
		}else {
			if(minutos == 0 && segundos <= 30 && segundos%2 == 0) jl_tiempo.setForeground(Color.RED);
			else jl_tiempo.setForeground(Color.WHITE);
		}
		
		if(segundos > 9) jl_tiempo.setText("tiempo: 0"+minutos+"´´"+segundos+"´");
		else jl_tiempo.setText("tiempo: 0"+minutos+"´´0"+segundos+"´");
		
		if(minutos == 0 && segundos == 0) {
			time.stop();
			save();
			dispose();
			despedida();
			new MainMenu();
		}else if(dificultad == DIFICIL && segundos == 0) {
			btn_borrar();
			
			//Para cambair las letras del tablero cada minuto
			for(int i = 0; i < 8; i++) {
				for(int j = 0; j < 8; j++) {
					tablero[i][j].setText(""+(char) letraGenerator());
				}
			}
		}
	}
	
	//Método para agregar el evento a todos los botones
	private void iniBotones() {
		
		//Evento del botón btn_salir
		btn_salir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				save();
				dispose();
				new MainMenu();
				despedida();
			}
		});
		
		//Evento para cada botón del tablero
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				tablero[i][j].addActionListener(new EventButton(tablero,i,j,jl_palabra));
			}
		}
		
		//Evento del botón btn_confirmar
		btn_confirmar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(archivo.existe(str_word)) {
					if(!jugador.isHere(str_word)) {
						jugador.put(str_word);
						jta_historial.append(" > "+str_word+" ("+str_word.length()+" pts)"+"\n");
						bien += str_word.length();
						if(dificultad == FACIL) {
							jl_timePlus.setText("(+"+str_word.length()*2+")");
							segundos += str_word.length()*2;
						}else {
							jl_timePlus.setText("(+"+str_word.length()+")");
							segundos += str_word.length();
						}
						jl_tiempo.setForeground(Color.GREEN);
						if(segundos > 9) jl_tiempo.setText("tiempo: "+minutos+"´´"+segundos+"´");
						else jl_tiempo.setText("tiempo: 0"+minutos+"´´0"+segundos+"´");
					}
				}
				btn_borrar();
				actualizar();
			}
		});
		
		//Evento del botón info
		info.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Forma palabras dando clic sobre las letras y preciona confirmar, las\n"
						+ "palabras que formen y tu puntaje estan al lado derecho de la mesa de juego."+
						"\nSolo tienes 5 minutos para enontrar todas las palabras que puedas.","Información",JOptionPane.PLAIN_MESSAGE);
			}
		});
		
		//Evento del botón btn_borrar
		btn_borrar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btn_borrar();
			}
		});
		
		//Evento del botón btn_reiniciar
		btn_reiniciar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				bien = 0;
				btn_borrar();
				
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						tablero[i][j].setText(""+(char) letraGenerator());
					}
				}
				
				jta_historial.setText(" historial de palabras:\n");
				
				actualizar();
				jugador.vaciar();
				jl_tiempo.setText("tiempo: 0"+minutos+"´´"+segundos+"´");
				jl_tiempo.setForeground(Color.WHITE);
				minutos = 5;
				segundos = 0;
			}
		});
	}
	
	//Método para btn_borrar la selección del usuario
	private void btn_borrar() {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				tablero[i][j].setEnabled(true);
				tablero[i][j].setSePuede(false);
				tablero[i][j].setSeleccionado(false);
				tablero[i][j].setBackground(Color.BLACK);
				tablero[i][j].setForeground(Color.YELLOW);
			}
		}
		str_word  = "";
		jl_palabra.setText(" Palabra: ");
	}
	
	//Método para llenar el trie con todas las palabras del archivo palabras.txt
	private void llenarDiccionario() throws IOException {
		
		archivo = new TrieStandard();
		String ruta = "src/resources/palabras.txt";
		
		try {
			FileReader fr = new FileReader(ruta);
			BufferedReader br = new BufferedReader(fr);
			
			String linea;
			
			while((linea = br.readLine()) != null) archivo.setPalabra(linea);
		 
		      fr.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showInternalMessageDialog(null, "El Archivo \""+ruta+"\" no ha sido encontrado","Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	//Para guardar antes de termianr el juego
	private void save() {
		HashScore backup = null;
		
		//Lectura
	    try {
	    	ObjectInputStream reader = new ObjectInputStream(new FileInputStream("registros.dat"));
		    backup =  (HashScore) reader.readObject();
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
	    if(backup==null) backup = new HashScore();
	    backup.put(player_name,bien);
	    
	    //Escritura
	    try {
	    	ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream("registros.dat"));
			writer.writeObject(backup);
			writer.close();
			//System.out.println("Fichero guardado");
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "Trone","Error de escritura",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//Para cuando termine el juego y monstremos el puntaje
	private void despedida() {
		
		String personaje;
		
		int azar = (int)Math.floor(Math.random()*2);
		
		if(azar == 1) personaje = "src/resources/marco.png";
		else personaje = "src/resources/eri.png";
		
		JOptionPane.showMessageDialog(null, "¡ Finalizaste el juego "+player_name+" !\nLograste "+bien+" puntos.","Game Over",JOptionPane.INFORMATION_MESSAGE,new ImageIcon(personaje));
	}
}