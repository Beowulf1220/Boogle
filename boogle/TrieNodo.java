package boogle;

public class TrieNodo {
	
	private boolean fin;
	private TrieNodo nodo[];
	
	//Constructor vacío
	public TrieNodo() {
		nodo = new TrieNodo[26];
		for(int i = 0; i < 26; i++) nodo[i] = null;
		fin = false;
	}
	
	//Métodos get y set para las variables
	public TrieNodo getNodo() {
		return this;
	}
	
	public TrieNodo getNodo(int index) {
		return nodo[index-97];
	}
	
	public void setNodo(char letra) {
		nodo[letra-97] = new TrieNodo();
	}
	
	public boolean isFin() {
		return fin;
	}
	
	public void setFin(boolean b) {
		fin = b;
	}
}