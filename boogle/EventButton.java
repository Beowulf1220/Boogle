package boogle;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

public class EventButton implements ActionListener{

	private BoogleButton boton[][];
	int x;
	int y;
	
	JLabel palabra;
	
	//Constructor
	public EventButton(BoogleButton boton[][], int x, int y, JLabel palabra) {
		this.boton = boton;
		this.x = x;
		this.y = y;
		this.palabra = palabra;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		boton[x][y].setForeground(Color.BLACK);
		boton[x][y].setBackground(Color.WHITE);
		boton[x][y].setEnabled(false);
		boton[x][y].setSePuede(false);
		boton[x][y].setSeleccionado(true);
		
		BoogleGame.str_word += boton[x][y].getText();
		palabra.setText(" Palabra: "+BoogleGame.str_word);
		
		//Habilita los que están alrrededor de la letra seleccionada y los otros desabilitalos
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				boton[i][j].setSePuede(false);
				boton[i][j].setEnabled(false);
			}
		}
		
		//Todos los botones (8 botones) adyacentes al seleccionado
		if(x - 1 >= 0 && y - 1 >= 0) if(!boton[x-1][y-1].isSeleccionado()) boton[x-1][y-1].setSePuede(true);
		if(y - 1 >= 0) if(!boton[x][y-1].isSeleccionado()) boton[x][y-1].setSePuede(true);
		if(x + 1 < 8 && y - 1 >= 0) if(!boton[x+1][y-1].isSeleccionado()) boton[x+1][y-1].setSePuede(true);
		if(x - 1 >= 0) if(!boton[x-1][y].isSeleccionado()) boton[x-1][y].setSePuede(true);
		if(x + 1 < 8) if(!boton[x+1][y].isSeleccionado()) boton[x+1][y].setSePuede(true);
		if(x - 1 >= 0 && y + 1 < 8) if(!boton[x-1][y+1].isSeleccionado()) boton[x-1][y+1].setSePuede(true);
		if(y + 1 < 8) if(!boton[x][y+1].isSeleccionado()) boton[x][y+1].setSePuede(true);
		if(x + 1 < 8 && y + 1 < 8) if(!boton[x+1][y+1].isSeleccionado()) boton[x+1][y+1].setSePuede(true);
		
		//Habilita los que están alrrededor de la letra seleccionada y los otros desabilitalos
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(!boton[i][j].getSePuede()) boton[i][j].setEnabled(false);
				else boton[i][j].setEnabled(true);
			}
		}
	}
}