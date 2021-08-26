package boogle;

import javax.swing.JButton;

public class BoogleButton extends JButton{
	
	private boolean sePuede;
	private boolean seleccionado;
	
	public BoogleButton(){
		sePuede = false;
		seleccionado = false;
	}
	
	public void setSePuede(boolean b) {
		sePuede = b;
	}
	
	public boolean getSePuede() {
		return sePuede;
	}
	
	public void setSeleccionado(boolean b) {
		seleccionado = b;
	}
	
	public boolean isSeleccionado() {
		return seleccionado;
	}
}